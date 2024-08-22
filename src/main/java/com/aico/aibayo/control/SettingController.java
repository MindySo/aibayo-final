package com.aico.aibayo.control;

import com.aico.aibayo.config.SSLUtilities;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/setting")
public class SettingController {
    @GetMapping("/menu")
    public String menu(){
        return "/admin/setting/menu";
    }
//    @GetMapping("/function")
//    public String function(){
//        return "/setting/function";
//    }
    //펑션은 안하기로 했음요 !
    @GetMapping("/add")
    public String add(){
        return "/admin/setting/add";
    }
    @GetMapping("/modify")
    public String modify(){
        return "/admin/setting/modify";
    }
    @GetMapping("/info")
    public String info(){
        return "/admin/setting/info";
    }
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/test")
    public String test(){
        return "/admin/setting/copy";
    }

//    @GetMapping("/api/proxy")
//    public ResponseEntity<String> proxyApiCall() {
//        SSLUtilities.disableSSLCertificateChecking();
//        String url = "https://e-childschoolinfo.moe.go.kr/api/notice/basicInfo2.do?key=77e6a98ff03a4fa687221a817ce91948&sidoCode=27&sggCode=27140";
//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject(url, String.class);
//        return ResponseEntity.ok(result);
//    }



}
