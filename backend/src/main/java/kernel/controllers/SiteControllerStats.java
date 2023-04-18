package kernel.controllers;

import kernel.controllers.RequestsLogger.BaseRequestLogger;
import kernel.response.PlayerInfoResponse;
import kernel.response.PlayersStatsResponse;
import kernel.services.SiteServices;
import kernel.services.StatsSiteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
}
