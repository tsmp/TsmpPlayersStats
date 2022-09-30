package kernel.controllers;

import kernel.entity.*;
import kernel.repository.*;
import kernel.services.PlayersBase;
import kernel.services.SessionManager;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.lang.System.in;

@RestController
@RequestMapping("PlayersSite/v1")
public class PlayersBaseControllerV1Site
{
    @Autowired
    private PlayersBase playersBase;

    @Autowired
    private AdminsRepoJPA adminsRepoJPA;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private NicknamesRepoJPA nicknamesRepoJPA;

    @Autowired
    private IpAddressesRepoJPA ipAddressesRepoJPA;

    @Autowired
    private PlayersRepoJPA playersRepoJPA;

    private String notLogged = "Unauthorized";

    @CrossOrigin(origins = "*")
    @GetMapping("/MyName")
    public String myName(@RequestParam("key") Integer key, HttpServletRequest request)
    {
        System.out.println(request.getRemoteAddr() + " requests site MyName for key: "+key);

        ActiveSession session = sessionManager.GetSession(key);

        if(session == null)
            return notLogged;

        return session.getName();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/ListPlayers")
    public List<Player> listPlayers(HttpServletRequest request)
    {
        System.out.println(request.getRemoteAddr() + " requests ListPlayers site");
        List<Player> lst = playersBase.GetAllPlayers();

        int k = lst.size();
        if ( k > 100 )
            lst.subList(100, k).clear();

        return lst;
    }

    private boolean StringContains(String str, String pattern)
    {
        //return str.contains(pattern);
        return str.toLowerCase().contains(pattern.toLowerCase());
    }

    private boolean PlayerMatch(Player player, String search)
    {
        Set<Nickname> nicknames = player.getNicknames();
        for(Nickname name: nicknames)
        {
            if(StringContains(name.getNickname(), search))
                return true;
        }

//        Set<IpAddress> addresses = player.getAddresses();
//        for(IpAddress address: addresses)
//        {
//            if(StringContains(address.getAddress(), search))
//                return true;
//        }
//
//        if(StringContains(player.getHwid(), search))
//            return true;

        return false;
    }

    private List<Integer> RemoveDuplicates(List<Integer> list)
    {
        int end = list.size();
        Set<Integer> set = new HashSet<Integer>();

        for(int i = 0; i < end; i++){
            set.add(list.get(i));
        }

        List<Integer> lst = new ArrayList<>();

        Iterator it = set.iterator();
        while(it.hasNext())
            lst.add((Integer)it.next());

        return lst;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    class PlayerStruct
    {
        List<String> nicknames;
        List<String> addresses;
        Integer playerId;
        Optional<String> hwid;
        Optional<String> info;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    class SearchResponce
    {
        int resultsCnt;
        int pagesCnt;
        int firstNumber;
        List<PlayerStruct> players;
    }

    private List<Player> Search(String searchFor, List<Player> lstFull)
    {
        List<Player> lst = new ArrayList<>();
        List<Integer> idsName = nicknamesRepoJPA.SearchByNickname(searchFor.toLowerCase());
        List<Integer> idsIp = ipAddressesRepoJPA.SearchByStr(searchFor);
        List<Integer> ids = new ArrayList<>();

        for(Integer id: idsName)
            ids.add(id);

        for(Integer id: idsIp)
            ids.add(id);

        ids = RemoveDuplicates(ids);
        System.out.println("found by name: "+idsName.size()+" by ip: "+idsIp.size()+" total:" +ids.size());

        for(Player player: lstFull)
        {
            for(Integer id: ids)
            {
                if(player.getPlayerId().equals(id))
                    lst.add(player);
            }
        }

        return lst;
    }

    final int ResultsPerPage = 50;

    SearchResponce PrepareResult(List<Player> lst, Integer page)
    {
        int pagesCount = (lst.size() / ResultsPerPage) + 1;
        SearchResponce res = new SearchResponce();
        res.players = new ArrayList<>();
        res.pagesCnt = pagesCount;
        res.resultsCnt = lst.size();
        res.firstNumber=0;

        if(page > pagesCount)
            return res;

        if(page == 1 && pagesCount > 1)
            lst.subList(ResultsPerPage, lst.size()).clear();

        if(page != 1)
        {
            int begin = ResultsPerPage*(page-1);
            int end = ResultsPerPage*(page);
            res.firstNumber = begin;

            if(end > lst.size())
                end = lst.size();

            lst = lst.subList(begin, end);
        }

        for(Player player: lst)
        {
            PlayerStruct pl = new PlayerStruct();
            pl.addresses = new ArrayList<>();
            pl.nicknames = new ArrayList<>();
            pl.hwid = Optional.ofNullable(player.getHwid());
            pl.playerId = player.getPlayerId();

            for(Nickname name: player.getNicknames())
                pl.nicknames.add(name.getNickname());

            for(IpAddress addr: player.getAddresses())
                pl.addresses.add(addr.getAddress());

            res.players.add(pl);
        }

        return res;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/SearchPlayers")
    public SearchResponce searchPlayers(@RequestParam("key") Integer key,
                                      @RequestParam("search") String toSearch,
                                      @RequestParam("page") Integer page,
                                      HttpServletRequest request)
    {
        System.out.println(request.getRemoteAddr() + " requests searchPlayers site, id: "+ key.toString()+"; search: "+toSearch+"; page: "+page.toString());
        List<Player> lst = playersBase.GetAllPlayers();

        if(!toSearch.isEmpty())
            lst = Search(toSearch,lst);

        return PrepareResult(lst, page);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/ListPlayersFull")
    public List<Player> listPlayersF(HttpServletRequest request)
    {
        System.out.println(request.getRemoteAddr() + " requests ListPlayers site");
        List<Player> lst = playersBase.GetAllPlayers();

//        int k = lst.size();
//        if ( k > 100 )
//            lst.subList(100, k).clear();

        return lst;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/Authorize")
    public String login( @RequestParam("login") String login,
                         @RequestParam("password") String password,
                         HttpServletRequest request)
    {
        System.out.println(request.getRemoteAddr() + " requests authorize with login: "+login+"; password: "+password);

        List<BaseAdmin> lst = adminsRepoJPA.findAll();
        boolean logged = false;

        for(int i=0;i<lst.size(); i++)
        {
            String log = lst.get(i).getLogin();
            String psw = lst.get(i).getPassword();

            if(log.equals(login) && psw.equals(password))
            {
                logged=true;
                break;
            }
        }

        if(logged==false)
        {
            System.out.println("! Attempt to login failed!");
            return "authorize failed";
        }

        return sessionManager.StartSessionAdmin(login);
    }

    @NoArgsConstructor
    @Getter
    @Setter
    class PlayerInfoStruct
    {
        Player player;
        List<ClGame> games;
    }

    PlayerInfoStruct GetPlayerInfo(Integer playerId)
    {
        Optional<Player> player = playersRepoJPA.findById(playerId);

        if(player.isEmpty())
        {
            System.out.println("! Player with this id not found!");
            return new PlayerInfoStruct();
        }

        PlayerInfoStruct info = new PlayerInfoStruct();
        info.games = clGameRepoJPA.getGamesByPlayer(playerId);
        info.player = player.get();

        return info;
    }

    @Autowired
    ClGameRepoJPA clGameRepoJPA;

    @CrossOrigin(origins = "*")
    @GetMapping("/GetPlayerInfo")
    public PlayerInfoStruct getPlayerInfoRequest(@RequestParam("key") Integer key,
                                        @RequestParam("playerId") Integer playerId,
                                        HttpServletRequest request)
    {
        System.out.println(request.getRemoteAddr() + " requests playerInfo site, id: "+ key.toString()+"; playerId: "+playerId);
        return GetPlayerInfo(playerId);
    }
}
