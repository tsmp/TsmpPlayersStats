package kernel.controllers;

import kernel.controllers.RequestsLogger.BaseRequestLogger;
import kernel.services.ServServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("PlayersBase/v1")
public class ServController
{
    @Autowired
    @Qualifier("ServReqLogger")
    private BaseRequestLogger requestsLogger;

    @Autowired
    private ServServices services;

    @GetMapping("/hello")
    public String getHello(HttpServletRequest request)
    {
        requestsLogger.Log(request,"","Hello");
        return services.Hello();
    }

    @GetMapping("/StartSession")
    public String startSession(
            @RequestParam("srv") String serverName,
            @RequestParam("key") int secretKey,
            @RequestParam("ver") String srvVer,
            HttpServletRequest request)
    {
        String params = "SrvName: "+ serverName + ", Ver: " + srvVer + ", Key: " + Integer.toString(secretKey);
        requestsLogger.Log(request,params,"StartSession");
        return services.StartSession(serverName, secretKey, srvVer, "");
    }

    @GetMapping("/StartSession2")
    public String startSession(
            @RequestParam("srv") String serverName,
            @RequestParam("key") int secretKey,
            @RequestParam("ver") String srvVer,
            @RequestParam("map") String mapName,
            HttpServletRequest request)
    {
        String params = "SrvName: "+ serverName + ", Ver: " + srvVer + ", Key: " + Integer.toString(secretKey) + ", map: " + mapName;
        requestsLogger.Log(request,params,"StartSession");
        return services.StartSession(serverName, secretKey, srvVer, mapName);
    }

    @GetMapping("/BanCheater")
    public String banCheater(
            @RequestParam("ip") String ip,
            @RequestParam("reasonId") Integer reasonId,
            @RequestParam("nick") String nickname,
            @RequestParam("key") int key,
            HttpServletRequest request)
    {
        String params = "Ip: " + ip + ", Name: " + nickname + ", ReasonId: " + reasonId + ", Key: " + key;
        requestsLogger.Log(request, params, "BanCheater");
        return services.BanCheater(ip, reasonId, nickname, key);
    }

    @GetMapping("/IsBanned")
    public String setupForm1(
            @RequestParam("ip") String ip,
            @RequestParam("name") String name,
            @RequestParam("key") int key,
            HttpServletRequest request)
    {
        String params = "Ip: " + ip + ", Name: " + name + ", Key: " + Integer.toString(key);
        requestsLogger.Log(request,params,"IsBanned");
        return services.IsBanned(ip, name, key, 0);
    }

    @GetMapping("/IsBanned2")
    public String IsBanned(
            @RequestParam("ip") String ip,
            @RequestParam("name") String name,
            @RequestParam("key") int key,
            @RequestParam("uid") int uid,
            HttpServletRequest request)
    {
        String params = "Ip: " + ip + ", Name: " + name + "uid: " + Integer.toString(uid) + ", Key: " + Integer.toString(key);
        requestsLogger.Log(request,params,"IsBanned2");
        return services.IsBanned(ip, name, key, uid);
    }
}
