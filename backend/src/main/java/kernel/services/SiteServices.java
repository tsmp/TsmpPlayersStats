package kernel.services;

import kernel.entity.*;
import kernel.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SiteServices
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

    @Autowired
    ClGameRepoJPA clGameRepoJPA;

    private String notLogged = "Unauthorized";

    public String MyName(Integer key)
    {
        ActiveSession session = sessionManager.GetSession(key);

        if(session == null)
            return notLogged;

        return "unknown user"; //session.getName();
    }

    private List<Integer> RemoveDuplicates(List<Integer> list)
    {
        int end = list.size();
        Set<Integer> set = new HashSet<Integer>();

        for(int i = 0; i < end; i++)
            set.add(list.get(i));

        List<Integer> lst = new ArrayList<>();
        Iterator it = set.iterator();

        while(it.hasNext())
            lst.add((Integer)it.next());

        return lst;
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
        System.out.println("found by name: " + idsName.size() + " by ip: " + idsIp.size() + " total:" + ids.size());

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

    SiteStruct.SearchResponce PrepareResult(List<Player> lst, Integer page)
    {
//        int pagesCount = (lst.size() / ResultsPerPage) + 1;
        SiteStruct.SearchResponce res = new SiteStruct.SearchResponce();
//        res.players = new ArrayList<>();
//        res.pagesCnt = pagesCount;
//        res.resultsCnt = lst.size();
//        res.firstNumber=0;
//
//        if(page > pagesCount)
//            return res;
//
//        if(page == 1 && pagesCount > 1)
//            lst.subList(ResultsPerPage, lst.size()).clear();
//
//        if(page != 1)
//        {
//            int begin = ResultsPerPage*(page - 1);
//            int end = ResultsPerPage*(page);
//            res.firstNumber = begin;
//
//            if(end > lst.size())
//                end = lst.size();
//
//            lst = lst.subList(begin, end);
//        }
//
//        for(Player player: lst)
//        {
//            SiteStruct.PlayerStruct pl = new SiteStruct.PlayerStruct();
//            pl.addresses = new ArrayList<>();
//            pl.nicknames = new ArrayList<>();
//            pl.hwid = Optional.ofNullable(player.getHwid());
//            pl.playerId = player.getPlayerId();
//
//            for(Nickname name: player.getNicknames())
//                pl.nicknames.add(name.getNickname());
//
//            for(IpAddress addr: player.getAddresses())
//                pl.addresses.add(addr.getAddress());
//
//            res.players.add(pl);
//        }

        return res;
    }

    public SiteStruct.SearchResponce SearchPlayers(Integer key, String toSearch, Integer page)
    {
        List<Player> lst = playersBase.GetAllPlayers();

        if(!toSearch.isEmpty())
            lst = Search(toSearch, lst);

        return PrepareResult(lst, page);
    }

    public String Login(String login, String password)
    {
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

        if(!logged)
        {
            System.out.println("! Attempt to login failed!");
            return "authorize failed";
        }

        return sessionManager.StartSessionAdmin(login);
    }

    SiteStruct.PlayerInfoStruct GetPlayerInfo(Integer playerId)
    {
        Optional<Player> player = playersRepoJPA.findById(playerId);

        if(player.isEmpty())
        {
            System.out.println("! Player with this id not found!");
            return new SiteStruct.PlayerInfoStruct();
        }

        SiteStruct.PlayerInfoStruct info = new SiteStruct.PlayerInfoStruct();
        info.games = clGameRepoJPA.getGamesByPlayer(playerId);
        info.player = player.get();

        return info;
    }

    public SiteStruct.PlayerInfoStruct GetPlayerInfo(Integer key, Integer playerId)
    {
        return GetPlayerInfo(playerId);
    }
}
