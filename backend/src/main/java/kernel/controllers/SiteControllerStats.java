package kernel.controllers;

import kernel.controllers.RequestsLogger.BaseRequestLogger;
import kernel.response.GameInfoResponse;
import kernel.response.PlayerInfoResponse;
import kernel.response.PlayersStatsResponse;
import kernel.services.SiteServices;
import kernel.services.StatsSiteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("StatsSite/v1")
public class SiteControllerStats
{
    @Autowired
    @Qualifier("SiteReqLogger")
    private BaseRequestLogger requestsLogger;

    @Autowired
    private StatsSiteServices services;

    @CrossOrigin(origins = "*")
    @GetMapping("/GetPlayers")
    public List<PlayersStatsResponse> getPlayers(HttpServletRequest request)
    {
        requestsLogger.Log(request,"", "getPlayers");
        return services.getPlayers();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/GetPlayer")
    public PlayerInfoResponse getPlayerStat(HttpServletRequest request, @RequestParam("player") Integer playerId)
    {
        requestsLogger.Log(request,"", "getPlayer");
        return services.getPlayerStat(playerId);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/GetGame")
    public GameInfoResponse getGame(HttpServletRequest request, @RequestParam("game") Integer gameId)
    {
        requestsLogger.Log(request,"", "getGame");
        return services.getGame(gameId);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(
            value = "/GetPlayerPdf",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPlayerStatPdf(HttpServletRequest request, @RequestParam("player") Integer playerId)
    {
        requestsLogger.Log(request,"", "getPlayerPdf");
        return services.getPlayerStatPdf(playerId);
    }
}
