//package kernel.controllers;
//
//import kernel.repository.BannedRepositoryJPA;
//import lombok.ToString;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import jakarta.servlet.http.HttpServletRequestHttpServletRequest;
//
//@RestController
//@RequestMapping("BanSyncService/v2")
//public class PlayersBanControllerV2 // Обработка запросов HTTP
//{
//    @Autowired
//    private BannedRepositoryJPA bannedRepositoryJPA;
//
//    @GetMapping("/IsBanned")
//    public String setupForm1(@RequestParam("ip") String ip,@RequestParam("name") String name, HttpServletRequest request)
//    {
//
//        System.out.println(request.getRemoteAddr() +":"+ Integer.toString(request.getRemotePort())+ " requests IsBanned for " + ip + ", name " + name);
//        return Integer.toString(bannedRepositoryJPA.isRecordExist(ip));
//    }
//}
