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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public List<PlayersStatsResponse> getPlayers()
    {
        // TODO: переписать все нафиг) для демо подходит, для продакшена - нет

        List<Player> players = playersRepoJPA.findAll();

        for(int i= 0; i< players.size(); i++)
        {
            if(players.get(i).getUID() == null)
            {
                players.remove(i);
                i--;
            }
        }

        List<Nickname> names = nicknamesRepoJpa.findAll();
        List<Game> games = gamesRepoJpa.findAll();
        List<PlayersStatsResponse> res = new ArrayList<>();

        int counter = 1;

        for(Player player: players)
        {
            PlayersStatsResponse response = new PlayersStatsResponse();

            String namesStr = "";
            Integer pid = player.getPlayerId();
            Integer puid = player.getUID();

            for(Nickname nickname: names)
            {
                if(nickname.getPlayerId() == pid) {
                    if (!namesStr.isEmpty())
                        namesStr += ", ";

                    namesStr += nickname.getNickname();
                }
            }

            response.setNicknames(namesStr);
            int deaths = 0;

            for(Game game: games)
            {
                if(game.getPlayerUID() == puid)
                {
                    response.setArts(response.getArts()+game.getArtefacts());
                    response.setFrags(response.getFrags() + game.getKills());
                    response.setHoursIngame(response.getHoursIngame() + game.getGameTimeMinutes());
                    deaths+= game.getDeaths();
                }
            }

            if(deaths!=0)
                response.setKd((float)response.getFrags() / deaths);

            response.setHoursIngame(response.getHoursIngame() / 60);
            response.setNum(counter);
            response.setId(player.getPlayerId());
            res.add(response);
            counter++;
        }

        return res;
    }

    public PlayerInfoResponse getPlayerStat(Integer playerId)
    {
        // TODO: переписать все нафиг) для демо подходит, для продакшена - нет

        List<Player> players = playersRepoJPA.findAll();
        Integer puid = 0;

        for(int i= 0; i< players.size(); i++)
        {
            if(players.get(i).getPlayerId() == playerId)
            {
                puid = players.get(i).getUID();
                break;
            }
        }

        if(puid == 0)
            return null;

        List<Nickname> names = nicknamesRepoJpa.findAll();
        List<Game> games = gamesRepoJpa.findAll();
        PlayerInfoResponse res = new PlayerInfoResponse();
        List<PlayerInfoResponse.GameStruct> gamess = new ArrayList<>();
        String namesStr = "";

        for (Nickname nickname : names)
        {
            if (nickname.getPlayerId() == playerId)
            {
                if (!namesStr.isEmpty())
                    namesStr += ", ";

                namesStr += nickname.getNickname();
            }
        }

        res.setNicknames(namesStr);

        // TODO: реализовать
        res.setFavouriteWeapon("lr300");
        int deaths = 0;

        List<Weapon> weapons = weaponRepoJpa.findAll();
        List<Hit> hits = hitRepoJpa.findAll();
        List<ServerName> srvNames = serverNamesRepoJPA.findAll();
        List<MapEntity> mapNames = mapRepoJPA.findAll();
        Map<Integer, Integer> wpnUsages = new HashMap<>();

        for (Game game : games)
        {
            if (game.getPlayerUID() == puid)
            {
                PlayerInfoResponse.GameStruct gameStruct = new PlayerInfoResponse.GameStruct();

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                gameStruct.setDate(formatter.format(game.getGameDate()));

                for(MapEntity map: mapNames)
                {
                    if(game.getMapNameId() == map.getId())
                    {
                        gameStruct.setMapName(map.getName());
                        break;
                    }
                }

                gameStruct.setId(game.getGameId());

                for(ServerName srv: srvNames)
                {
                    if((int)srv.getId() == (int)game.getServerNameId())
                    {
                        gameStruct.setServerName(srv.getName());
                        break;
                    }
                }

                for(Hit hit: hits)
                {
                    if(hit.getGame() == game.getGameId())
                    {
                        Integer wpn = hit.getWpn();
                        Integer count = hit.getHits();

                        if(wpnUsages.get(wpn) != null)
                            count += wpnUsages.get(wpn);

                        wpnUsages.put(wpn, count);
                    }
                }

                gamess.add(gameStruct);

                res.setArts(res.getArts() + game.getArtefacts());
                res.setFrags(res.getFrags() + game.getKills());
                res.setHoursIngame(res.getHoursIngame() + game.getGameTimeMinutes());
                deaths += game.getDeaths();
            }
        }

        AtomicInteger maxCnt = new AtomicInteger(0);
        AtomicInteger wpnId = new AtomicInteger(0);

        wpnUsages.forEach((id, cnt) ->
        {
            if(cnt > maxCnt.get())
            {
                maxCnt.set(cnt);
                wpnId.set(id);
            }
        });

        for (Weapon wp: weapons)
        {
            if(wp.getId() == wpnId.get())
            {
                res.setFavouriteWeapon(wp.getName());
                break;
            }
        }

        res.setGames(gamess);

        if (deaths != 0)
            res.setKd((float) res.getFrags() / deaths);

        res.setHoursIngame(res.getHoursIngame() / 60);

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
        // TODO: переписать все нафиг) для демо подходит, для продакшена - нет

        GameInfoResponse res = new GameInfoResponse();
        List<GameInfoResponse.WpnStat> wpnStat = new ArrayList<>();

        List<Game> games = gamesRepoJpa.findAll();
        List<Weapon> weapons = weaponRepoJpa.findAll();
        List<Hit> hits = hitRepoJpa.findAll();
        List<ServerName> srvNames = serverNamesRepoJPA.findAll();

        for(Game game: games)
        {
            if(game.getGameId() == gameId)
            {
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                res.setDate(formatter.format(game.getGameDate()));
                res.setKills(game.getKills());
                res.setDeaths(game.getDeaths());

                if(res.getDeaths() != 0)
                    res.setKd((float)res.getKills() / res.getDeaths());

                res.setKillsAi(game.getKillsAi());
                res.setHoursIngame((float)game.getGameTimeMinutes() / 60);
                res.setKillsOneLife(game.getMaxKillsOneLife());

                // TODO: MAP name


                res.setMap("Бассеин");

                for(ServerName srv: srvNames)
                {
                    if(srv.getId() == game.getServerNameId())
                    {
                        res.setServer(srv.getName());
                        break;
                    }
                }

                break;
            }
        }

        for(Hit hit: hits)
        {
            if(hit.getGame() == gameId)
            {
                GameInfoResponse.WpnStat stat = new GameInfoResponse.WpnStat();
                stat.setHits(hit.getHits());

                for(Weapon wpn: weapons)
                {
                    if(wpn.getId() == hit.getWpn())
                    {
                        stat.setWpnName(wpn.getName());
                        break;
                    }
                }

                wpnStat.add(stat);
            }
        }

        res.setWpnStats(wpnStat);
        return res;
    }
}
