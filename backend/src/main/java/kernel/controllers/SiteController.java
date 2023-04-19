package kernel.controllers;

import kernel.controllers.RequestsLogger.BaseRequestLogger;
import kernel.response.SiteStruct;
import kernel.services.SiteServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("PlayersSite/v1")
public class SiteController
{
    @Autowired
    @Qualifier("SiteReqLogger")
    private BaseRequestLogger requestsLogger;

    @Autowired
    private SiteServices services;

    @CrossOrigin(origins = "*")
    @GetMapping("/MyName")
    public String myName(@RequestParam("key") Integer key, HttpServletRequest request)
    {
        String params = "Key: " + key;
        requestsLogger.Log(request,params, "MyName");
        return services.MyName(key);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/SearchPlayers")
    public SiteStruct.SearchResponce searchPlayers(
            @RequestParam("key") Integer key,
            @RequestParam("search") String toSearch,
            @RequestParam("page") Integer page,
            HttpServletRequest request)
    {
        String params = "Key: " + key.toString() + ", SearchStr: " + toSearch + ", Page: " + page.toString();
        requestsLogger.Log(request,params,"SearchPlayers");
        return services.SearchPlayers(key, toSearch, page);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/Authorize")
    public String login(
            @RequestParam("login") String login,
            @RequestParam("password") String password,
            HttpServletRequest request)
    {
        String params = "Login: " + login + ", Password: " + password;
        requestsLogger.Log(request,params, "Authorize");
        return services.Login(login, password);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/GetPlayerInfo")
    public SiteStruct.PlayerInfoStruct getPlayerInfoRequest(
            @RequestParam("key") Integer key,
            @RequestParam("playerId") Integer playerId,
            HttpServletRequest request)
    {
        String params = "Key: " + key.toString() + ", PlayerId: " + playerId;
        requestsLogger.Log(request, params, "GetPlayerInfo");
        return services.GetPlayerInfo(key,playerId);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/addNote")
    public void addNote(
            @RequestParam("key") Integer key,
            @RequestParam("playerId") Integer playerId,
            @RequestParam("text") String text,
            HttpServletRequest request)
    {
        //String params = "Key: " + key.toString() + ", PlayerId: " + playerId;
        //requestsLogger.Log(request, params, "GetPlayerInfo");
        services.AddNote(key, playerId, text);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/editNote")
    public void editNote(
            @RequestParam("key") Integer key,
            @RequestParam("note") Integer note,
            @RequestParam("text") String text,
            HttpServletRequest request)
    {
        //String params = "Key: " + key.toString() + ", PlayerId: " + playerId;
        //requestsLogger.Log(request, params, "GetPlayerInfo");
        services.EditNote(key, note, text);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/delNote")
    public void delNote(
            @RequestParam("key") Integer key,
            @RequestParam("note") Integer note,
            HttpServletRequest request)
    {
        //String params = "Key: " + key.toString() + ", PlayerId: " + playerId;
        //requestsLogger.Log(request, params, "GetPlayerInfo");
        //return services.GetPlayerInfo(key,playerId);
        services.DelNote(key, note);
    }
}
