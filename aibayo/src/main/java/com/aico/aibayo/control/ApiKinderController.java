package com.aico.aibayo.control;

import com.aico.aibayo.dto.RegisterKinderDto;
import com.aico.aibayo.service.kinder.KinderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/kinder")
@RequiredArgsConstructor
public class ApiKinderController {
    private final KinderService kinderService;

    @GetMapping("/list")
    public ResponseEntity<List<RegisterKinderDto>> list (Model model){
        List<RegisterKinderDto> allKinder = kinderService.getAllKinder();
        log.info("allKinder : {}",allKinder);
        model.addAttribute("allKinder",allKinder);
        return ResponseEntity.ok(allKinder);
    }
}
