package com.aico.aibayo.control;

import com.aico.aibayo.dto.meal.MealDto;
import com.aico.aibayo.dto.meal.MealSearchCondition;
import com.aico.aibayo.service.meal.MealService;
import com.amazonaws.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class ApiMealController {
    private final MealService mealService;

    @PostMapping("/getByMonth")
    public ResponseEntity<List<MealDto>> getByMonth(@RequestBody MealSearchCondition condition) {
        List<MealDto> mealDtos = mealService.getAllByMealDate(condition);

        return mealDtos == null ? ResponseEntity.badRequest().build() :
                                  ResponseEntity.ok(mealDtos);
    }

    @PostMapping("/detail")
    public ResponseEntity<MealDto> adminDetail(@RequestBody MealDto dto) {
        log.info("dto: {}", dto);

        if (dto == null || dto.getMealNo() == null) {
            return ResponseEntity.badRequest().build();
        }

        MealDto result = mealService.getByMealNo(dto.getMealNo());
        log.info("selected: {}", result);

        return result == null ? ResponseEntity.badRequest().build() :
                                ResponseEntity.ok(result);
    }

    @PostMapping("/writeOk")
    public ResponseEntity<MealDto> writeOk(@RequestPart(value = "mealDto") MealDto mealDto,
                                           @RequestPart(value = "files") List<MultipartFile> files){
        log.info("insert mealDto: {}", mealDto);
        for (MultipartFile file : files) {
            log.info("file: {}", file.getOriginalFilename());
        }

        MealDto inserted = mealService.insertMeal(mealDto, files);
//        MealDto inserted = null;

        return inserted == null ? ResponseEntity.badRequest().build() :
                                  ResponseEntity.ok(inserted);
    }

    @PutMapping("/modifyOk")
    public ResponseEntity<MealDto> modifyOk(@RequestPart(value = "mealDto") MealDto mealDto,
                                            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        log.info("modify mealDto: {}", mealDto);
        if (files != null) {
            for (MultipartFile file : files) {
                log.info("file: {}", file.getOriginalFilename());
            }
        }

        MealDto modified = mealService.updateMeal(mealDto, files);

        return modified == null ? ResponseEntity.badRequest().build() :
                                  ResponseEntity.ok(modified);
//        return null;
    }

    @DeleteMapping("/deleteOk")
    public ResponseEntity<MealDto> deleteOk(@RequestBody MealDto mealDto) {
        log.info("delete: {}", mealDto);

        MealDto deleted = mealService.deleteMeal(mealDto);

        return deleted == null ? ResponseEntity.badRequest().build() :
                                 ResponseEntity.ok(deleted);
    }
}
