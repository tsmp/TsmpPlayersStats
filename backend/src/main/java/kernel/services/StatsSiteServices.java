package kernel.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import kernel.entity.*;
import kernel.repository.*;
import kernel.response.GameInfoResponse;
import kernel.response.PlayerInfoResponse;
import kernel.response.PlayersStatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Component
public class StatsSiteServices
{
    @Autowired
    private PlayersRepoJPA playersRepoJPA;

    @Autowired
    private NicknamesRepoJPA nicknamesRepoJpa;

    @Autowired
    private GamesRepoJPA gamesRepoJpa;

    @Autowired
    private ServerNamesRepoJPA serverNamesRepoJPA;

    @Autowired
    private HitRepoJpa hitRepoJpa;

    @Autowired
    private WeaponRepoJpa weaponRepoJpa;

    @Autowired
    private MapRepoJPA mapRepoJPA;

    private String GetPlayerNicknames(Integer playerId)
    {
        String namesStr = "";
        List<String> names = nicknamesRepoJpa.searchByPlayerId(playerId);

        for(String nickname: names)
        {
            if (!namesStr.isEmpty())
                namesStr += ", ";

            namesStr += nickname;
        }

        return namesStr;
    }

    public List<PlayersStatsResponse> getPlayers()
    {
        List<Player> players = playersRepoJPA.findByUIDNotNull();
        List<PlayersStatsResponse> res = new ArrayList<>();

        int counter = 1;

        for(Player player: players)
        {
            final Integer pid = player.getPlayerId();
            final Integer puid = player.getUID();

            PlayersStatsResponse response = new PlayersStatsResponse();
            response.setNicknames(GetPlayerNicknames(pid));

            final Integer fragsCnt = gamesRepoJpa.sumPlayerFrags(puid);
            response.setFrags(fragsCnt != null ? fragsCnt : 0);

            final Integer artsCnt = gamesRepoJpa.sumPlayerArts(puid);
            response.setArts(artsCnt != null ? artsCnt : 0);

            final Integer minInGame = gamesRepoJpa.sumPlayerMinutesIngame(puid);
            response.setHoursIngame(minInGame != null ? minInGame / 60 : 0);

            final Integer deaths = gamesRepoJpa.sumPlayerDeaths(puid);
            if(deaths != null && deaths != 0)
                response.setKd((float)response.getFrags() / deaths);

            response.setNum(counter);
            response.setId(pid);

            res.add(response);
            counter++;
        }

        return res;
    }

    public PlayerInfoResponse getPlayerStat(Integer playerId)
    {
        Optional<Player> pl = playersRepoJPA.findById(playerId);

        if(pl.isEmpty() || pl.get().getUID() == null)
            return null;

        final Integer puid = pl.get().getUID();
        List<Game> games = gamesRepoJpa.findByPlayerUID(puid);
        List<PlayerInfoResponse.GameStruct> gamess = new ArrayList<>();

        for (Game game : games)
        {
            PlayerInfoResponse.GameStruct gameStruct = new PlayerInfoResponse.GameStruct();

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            gameStruct.setDate(formatter.format(game.getGameDate()));

            gameStruct.setId(game.getGameId());
            gameStruct.setMapName(mapRepoJPA.findById(game.getMapNameId()).get().getName());
            gameStruct.setServerName(serverNamesRepoJPA.findById(game.getServerNameId()).get().getName());
            gamess.add(gameStruct);
        }

        PlayerInfoResponse res = new PlayerInfoResponse();

        res.setGames(gamess);
        res.setNicknames(GetPlayerNicknames(playerId));
        res.setFrags(gamesRepoJpa.sumPlayerFrags(puid));
        res.setArts(gamesRepoJpa.sumPlayerArts(puid));
        res.setHoursIngame(gamesRepoJpa.sumPlayerMinutesIngame(puid) / 60);

        String bestWpn = "-";
        Integer bestWpnId = hitRepoJpa.bestWeaponId(puid);

        if(bestWpnId != null)
            bestWpn = weaponRepoJpa.findById(bestWpnId).get().getName();

        res.setFavouriteWeapon(bestWpn);

        final int deaths = gamesRepoJpa.sumPlayerDeaths(puid);
        if (deaths != 0)
            res.setKd((float) res.getFrags() / deaths);

        return res;
    }

    public byte[] getPlayerStatPdf(Integer playerId)
    {
        try
        {
            Document document = new Document();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, stream);
            document.open();

            BaseFont baseFont=BaseFont.createFont("C://Windows//Fonts//Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontHdr = new Font(baseFont, 20);
            Font font = new Font(baseFont, 16);
            Font fontBig = new Font(baseFont, 18);
            PlayerInfoResponse player = getPlayerStat(playerId);

            interface Line
            {
                void add(String text) throws DocumentException;
            }

            Line newLine = (String str)->
            {
                Chunk chunk = new Chunk("\n", font);
                document.add(chunk);
            };

            Line header = (String text) ->
            {
                Paragraph paragraph = new Paragraph(text, fontHdr);
                paragraph.setAlignment(Element.ALIGN_CENTER);
                paragraph.setSpacingAfter(2);
                document.add(paragraph);
            };

            Line text = (String txt) ->
            {
                Chunk chunk = new Chunk(txt, font);
                document.add(chunk);
                newLine.add("");
            };

            Line bigText = (String txt) ->
            {
                Chunk chunk = new Chunk(txt, fontBig);
                document.add(chunk);
                newLine.add("");
            };

            header.add("Игрок " + playerId.toString());

            bigText.add("Никнеймы:");
            text.add(player.getNicknames());
            newLine.add("");

            bigText.add("Убийств: " + player.getFrags());
            bigText.add("Убийств/Смертей: " + player.getKd());
            bigText.add("Артефактов: " + player.getArts());
            bigText.add("Часов в игре: " + player.getHoursIngame());
            bigText.add("Любимое оружие: " + player.getFavouriteWeapon());
            newLine.add("");

            header.add("Игры");
            PdfPTable table = new PdfPTable(3);

            Stream.of("Сервер", "Карта", "Дата")
                    .forEach(columnTitle -> {
                        PdfPCell hdr = new PdfPCell();
                        hdr.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        hdr.setBorderWidth(2);
                        hdr.setPadding(2);
                        hdr.setPhrase(new Phrase(columnTitle, fontBig));
                        hdr.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(hdr);
                    });

            PdfPCell cell = new PdfPCell();

            for(PlayerInfoResponse.GameStruct game: player.getGames())
            {
                cell.setPhrase(new Phrase(game.getServerName(), font));
                table.addCell(cell);
                cell.setPhrase(new Phrase(game.getMapName(), font));
                table.addCell(cell);
                cell.setPhrase(new Phrase(game.getDate(), font));
                table.addCell(cell);
            }

            document.add(table);
            document.close();
            return stream.toByteArray();
        }
        catch (Exception ex) {}

        return null;
    }

    public GameInfoResponse getGame(Integer gameId)
    {
        Optional<Game> gamee = gamesRepoJpa.findById(gameId);
        if(gamee.isEmpty())
            return null;
        Game game = gamee.get();

        GameInfoResponse res = new GameInfoResponse();
        res.setKills(game.getKills());
        res.setDeaths(game.getDeaths());

        res.setKillsAi(game.getKillsAi());
        res.setHoursIngame((float) game.getGameTimeMinutes() / 60);
        res.setKillsOneLife(game.getMaxKillsOneLife());

        res.setMap(mapRepoJPA.findById(game.getMapNameId()).get().getName());
        res.setServer(serverNamesRepoJPA.findById(game.getServerNameId()).get().getName());

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        res.setDate(formatter.format(game.getGameDate()));

        if (res.getDeaths() != 0)
            res.setKd((float) res.getKills() / res.getDeaths());

        List<Hit> hits = hitRepoJpa.SearchByGame(gameId);
        List<GameInfoResponse.WpnStat> wpnStat = new ArrayList<>();

        for (Hit hit : hits)
        {
            GameInfoResponse.WpnStat stat = new GameInfoResponse.WpnStat();
            stat.setHits(hit.getHits());
            stat.setWpnName(weaponRepoJpa.findById(hit.getWpn()).get().getName());
            wpnStat.add(stat);
        }

        res.setWpnStats(wpnStat);
        return res;
    }
}
