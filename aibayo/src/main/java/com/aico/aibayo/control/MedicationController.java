package com.aico.aibayo.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/medication")
public class MedicationController {
    @GetMapping("/admin/card")
    public String admincard(){
        return "admin/medication/card";
    }
    @GetMapping("/admin/list")
    public String adminlist(){
        return "admin/medication/list";
    }
    @GetMapping("/admin/detail")
    public String admindetail(){
        return "admin/medication/detail";
    }
    @GetMapping("/user/write")
    public String write() {
        return "user/medication/writeForm";
    }
    @GetMapping("/user/detail")
    public String userdetail(){
        return "user/medication/detail";
    }
    @GetMapping("/user/card")
    public String usercard(){
        return "user/medication/card";
    }
    @GetMapping("/user/list")
    public String userlist(){
        return "user/medication/list";
    }

}
