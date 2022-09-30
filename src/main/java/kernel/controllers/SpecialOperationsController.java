//package kernel.controllers;
//
//import kernel.entity.*;
//import kernel.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("Special/v1")
//public class SpecialOperationsController
//{
//    @Autowired
//    private OldGamesRepoJPA oldGamesRepoJPA;
//
//    @Autowired
//    private GamesRepoJPA gamesRepoJPA;
//
//    @Autowired
//    private ServerNamesRepoJPA serverNamesRepoJPA;
//
//    @Autowired
//    private PlayerGameRepoJPA playerGameRepoJPA;
//
//    private Integer GetSrvNameId(String name)
//    {
//        ServerName srvName = null;
//        List<ServerName> lst = serverNamesRepoJPA.findAll();
//
//        for (ServerName srvn: lst)
//        {
//            if(srvn.getName().equals(name))
//            {
//                srvName = srvn;
//                break;
//            }
//        }
//
//        if(srvName == null)
//        {
//            System.out.println(name+" not found, created");
//            srvName = new ServerName();
//            srvName.setName(name);
//            serverNamesRepoJPA.save(srvName);
//            return srvName.getId();
//        }
//
//        System.out.println(name+"found");
//        return srvName.getId();
//    }
//
//    private Integer GetGameId(Integer srvNameId, GameOld go)
//    {
//        List<Game> lst = gamesRepoJPA.findAll();
//
//        for (Game game: lst)
//        {
//            if(game.getServerNameId().equals(srvNameId) && go.getEnterGameDate().equals(game.getGameDate()))
//            {
//                return game.getGameId();
//            }
//        }
//
//        Game newGame = new Game();
//        newGame.setGameDate(go.getEnterGameDate());
//        newGame.setServerNameId(srvNameId);
//        gamesRepoJPA.save(newGame);
//        return newGame.getGameId();
//    }
//
//    @GetMapping("/MigrateGames")
//    public String myName()
//    {
//        List<GameOld> oldGames = oldGamesRepoJPA.findAll();
//        int counter = 0;
//
//        for (GameOld oldGame: oldGames)
//        {
//            counter++;
//            System.out.println("processing "+Integer.toString(counter));
//
//            Integer srvNameId = GetSrvNameId(oldGame.getServerName());
//            Integer gameId = GetGameId(srvNameId,oldGame);
//
//            List<PlayerGame> pgLst = playerGameRepoJPA.findAll();
//            boolean found = false;
//
//            for (PlayerGame pg: pgLst)
//            {
//                if(pg.getGameId().equals(gameId) && pg.getPlayerId().equals(oldGame.getPlayerId()))
//                    found=true;
//            }
//
//            if(!found)
//            {
//                PlayerGame newPg = new PlayerGame();
//                newPg.setGameId(gameId);
//                newPg.setPlayerId(oldGame.getPlayerId());
//                playerGameRepoJPA.save(newPg);
//            }
//        }
//
//        return "ok";
//    }
//}
