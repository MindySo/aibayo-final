package com.aico.aibayo.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/approval")
public class ElectronicApprovalController {
    @GetMapping("/list")
    public String list() {
        return "admin/electronicApproval/list";
    }

    @GetMapping("/detail")
    public String detail() {
        return "admin/electronicApproval/detail";
    }

    @GetMapping("/write")
    public String writeForm() {
        return "admin/electronicApproval/writeForm";
    }

    @GetMapping("/modify")
    public String modifyForm() {
        return "admin/electronicApproval/modifyForm";
    }
}
