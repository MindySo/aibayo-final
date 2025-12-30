package com.aico.aibayo.control;

import com.aico.aibayo.common.MealTypeEnum;
import com.aico.aibayo.dto.meal.MealDetailDto;
import com.aico.aibayo.dto.meal.MealDto;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.service.meal.MealService;
import com.aico.aibayo.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController {
    private final MemberService memberService;
    private final MealService mealService;

    @GetMapping("/admin/list")
    public String adminList(@ModelAttribute("loginInfo") MemberDto loginInfo) {

        return "admin/meal/list";
    }

    @GetMapping("/user/list")
    public String userList(@ModelAttribute("loginInfo") MemberDto loginInfo) {

        return "user/meal/list";
    }

    @GetMapping("/admin/write")
    public String writeForm(@ModelAttribute("loginInfo") MemberDto loginInfo) {

        return "admin/meal/writeForm";
    }

    @GetMapping("/admin/modify/{mealNo}")
    public String modifyForm(@PathVariable Long mealNo, Model model) {
        MealDto meal = mealService.getByMealNo(mealNo);
        List<Integer> selectedTypes = meal.getMealDetails().stream().map(MealDetailDto::getMealType).toList();
        log.info("selectedTypes: {}", selectedTypes);
        model.addAttribute("meal", meal);
        model.addAttribute("selectedTypes", selectedTypes);
        model.addAttribute("mealTypes", MealTypeEnum.values());
//        meal.getMealDetails().get(meal.getMealDetails().indexOf())

        return "admin/meal/modifyForm";
    }
}
