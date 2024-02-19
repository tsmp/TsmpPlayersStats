package kernel.controllers;

import kernel.controllers.RequestsLogger.BaseRequestLogger;
import kernel.services.SpecServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

// Special operations controller

@RestController
@RequestMapping("Special/v1")
public class SpecController
{
    @Autowired
    @Qualifier("SpecReqLogger")
    private BaseRequestLogger requestsLogger;

    @Autowired
    private SpecServices services;

    @GetMapping("/hash")
    public int getHash(@RequestParam("str") String str, HttpServletRequest request)
    {
        String params = "From: " + str;
        requestsLogger.Log(request, params, "Hash");
        return services.Hast(str);
    }
}
