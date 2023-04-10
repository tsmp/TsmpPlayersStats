package kernel.controllers;

import kernel.controllers.RequestsLogger.BaseRequestLogger;
import kernel.services.StatsServServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("PlayersStats/v1")
public class ServControllerStats
{
    @Autowired
    @Qualifier("StatsReqLogger")
    private BaseRequestLogger requestsLogger;

    @Autowired
    private StatsServServices services;

    @GetMapping("/GenPlayerID")
    public String GetPlayerID(@RequestParam("key") int key, HttpServletRequest request)
    {
        requestsLogger.Log(request,"key: " + Integer.toString(key),"GetPlayerID");
        return services.GenNewPlayerUID(key);
    }
}
