package com.aico.aibayo.service.meal;

import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.meal.MealDetailDto;
import com.aico.aibayo.dto.meal.MealDto;
import com.aico.aibayo.dto.meal.MealSearchCondition;
import com.aico.aibayo.entity.MealDetailEntity;
import com.aico.aibayo.entity.MealEntity;
import com.aico.aibayo.repository.meal.MealDetailRepository;
import com.aico.aibayo.repository.meal.MealRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {
    private final AmazonS3Client amazonS3Client;

    private final MealRepository mealRepository;
    private final MealDetailRepository mealDetailRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${file.upload-dir}")
    private String uploadDirectory;

    private final String MEAL_IMG_DIR = "meal";

    @Override
    public List<MealDto> getAllByMealDate(MealSearchCondition condition) {
        if (condition == null || condition.getStartDate() == null || condition.getEndDate() == null) {
            return null;
        }

        return mealRepository.findAllByMealDate(condition);
    }

    @Override
    public MealDto getByMealNo(Long mealNo) {
        MealEntity target = mealRepository.findByMealNo(mealNo);
        return MealDto.toDto(target);
    }

    @Override
    @Transactional
    public MealDto insertMeal(MealDto mealDto, List<MultipartFile> files) {
        MealEntity target = MealEntity.builder()
                .kinderNo(mealDto.getKinderNo())
                .mealDate(mealDto.getMealDate())
                .mealRegDate(LocalDateTime.now())
                .mealDeleteFlag(BooleanEnum.FALSE.getBool())
                .mealDetails(new ArrayList<>())
                .build();

        MealEntity saved = mealRepository.save(target);

        List<MealDetailEntity> savedDetails = new ArrayList<>();
        List<MealDetailDto> mealDetails = mealDto.getMealDetails();
        for (MealDetailDto mealDetailDto : mealDetails) {
            setMealDetail(files, saved, savedDetails, mealDetailDto);

        }

        saved.setMealDetails(savedDetails);

        return MealDto.toDto(saved);
    }

    @Override
    @Transactional
    public MealDto updateMeal(MealDto mealDto, List<MultipartFile> files) {
        MealEntity target = mealRepository.findById(mealDto.getMealNo())
                .orElseThrow(() -> new EntityNotFoundException("엔티티를 찾을 수 없습니다."));

        target.setMealDate(mealDto.getMealDate() == null ? target.getMealDate() : mealDto.getMealDate());
        target.setMealModifyDate(LocalDateTime.now());

        List<MealDetailEntity> targetDetails = target.getMealDetails();
        List<MealDetailDto> modifyDetails = mealDto.getMealDetails();
        for (MealDetailDto detail : modifyDetails) {
            log.info("dto: {}", detail);
        }

        // 1. mealDetailNo를 기준으로 modifyDetails와 targetDetails를 Map으로 변환
        Map<Long, MealDetailEntity> targetMap = targetDetails.stream()
                .collect(Collectors.toMap(MealDetailEntity::getMealDetailNo, Function.identity()));

        Map<Long, MealDetailDto> modifyMap = modifyDetails.stream()
                .filter(detail -> detail.getMealDetailNo() != null)
                .collect(Collectors.toMap(MealDetailDto::getMealDetailNo, Function.identity()));

        // 2. 비교용 targetDetails와 modifyDetails의 mealDetailNo 구하기
        Set<Long> targetMealDetailNos = targetMap.keySet();
        Set<Long> modifyMealDetailNos = modifyMap.keySet();

        // 3. 삭제/수정/등록 필요한 값끼리 분류하여 작업
        // 삭제 작업
        targetMealDetailNos.stream()
                .filter(mealDetailNo -> !modifyMealDetailNos.contains(mealDetailNo))
                .forEach(mealDetailNo -> {
                    MealDetailEntity entity = targetMap.get(mealDetailNo);
                    entity.setMealInvisibleFlag(BooleanEnum.TRUE.getBool());
                    log.info("delete: {}", entity);

                    MealDetailEntity deleted = mealDetailRepository.save(entity);
                    log.info("delete: {}", deleted);

                    targetDetails.remove(targetMap.get(mealDetailNo));
                });

        // 수정 작업
        targetMealDetailNos.stream()
                .filter(modifyMealDetailNos::contains)
                .forEach(mealDetailNo -> {
                    MealDetailEntity targetEntity = targetMap.get(mealDetailNo);
                    MealDetailDto modifyDto = modifyMap.get(mealDetailNo);
                    log.info("modify: {}", modifyDto);

                    if (modifyDto.getMealMenu() != null) {
                        targetEntity.setMealMenu(modifyDto.getMealMenu());
                    }
                    if (modifyDto.getMealPicOriginalName() != null &&
                            !modifyDto.getMealPicOriginalName().isEmpty()) {
                        String uploadUrl = getUploadUrl(files, modifyDto);

                        targetEntity.setMealPicOriginalName(modifyDto.getMealPicOriginalName());
                        targetEntity.setMealPic(uploadUrl);
                    }

                    MealDetailEntity modified = mealDetailRepository.save(targetEntity);
                    int targetIndex = targetDetails.indexOf(targetEntity);
                    targetDetails.set(targetIndex, modified);
                });

        // 등록
        modifyDetails.stream()
                .filter(detail -> detail.getMealDetailNo() == null)
                .forEach(dto -> {
                    setMealDetail(files, target, targetDetails, dto);
                });

        target.setMealDetails(targetDetails);
        target.setMealModifyDate(LocalDateTime.now());

        MealEntity modified = mealRepository.save(target);

        return MealDto.toDto(modified);
    }

    @Override
    public MealDto deleteMeal(MealDto mealDto) {
        if (mealDto == null || mealDto.getMealNo() == null) {
            return null;
        }

        return mealRepository.findById(mealDto.getMealNo())
                .map( target -> {
                    target.setMealDeleteDate(LocalDateTime.now());
                    target.setMealDeleteFlag(BooleanEnum.TRUE.getBool());

                    MealEntity deleted = mealRepository.save(target);
                    return MealDto.toDto(deleted);
                }).orElse(null);
    }

    @Override
    public MealDto getByToday(MealSearchCondition condition) {
        return mealRepository.findTop1ByMealDateAndKinderNoAndMealDeleteFlag(
                        condition.getMealDate(),
                        condition.getKinderNo(),
                        condition.getMealDeleteFlag()
                )
                .map(MealDto::toDto)
                .orElse(null);
    }

    private void setMealDetail(List<MultipartFile> files, MealEntity target,
                               List<MealDetailEntity> targetDetails, MealDetailDto dto) {
        String uploadUrl = getUploadUrl(files, dto);

        MealDetailEntity mealDetailEntity = MealDetailEntity.builder()
                .mealType(dto.getMealType())
                .mealMenu(dto.getMealMenu())
                .mealPic(uploadUrl)
                .mealPicOriginalName(dto.getMealPicOriginalName())
                .mealInvisibleFlag(BooleanEnum.FALSE.getBool())
                .meal(target)
                .build();

        MealDetailEntity saved = mealDetailRepository.save(mealDetailEntity);
        targetDetails.add(saved);
    }

    private String getUploadUrl(List<MultipartFile> files, MealDetailDto dto) {
        String uploadUrl = "";

        // detail의 originalFileName과 동일한 이름을 가진 파일을 매칭
        OptionalInt index = IntStream.range(0, files.size())
                .filter(inx -> dto.getMealPicOriginalName()
                        .equals(files.get(inx).getOriginalFilename()))
                .findFirst();

        if (index.isPresent()) {
            MultipartFile multipartFile = files.get(index.getAsInt());
            uploadUrl = getUrlAfterUploadS3(multipartFile);
            files.remove(multipartFile);
        }

        log.info(">>>>>>>>> meal file saved in {}", uploadUrl);
        return uploadUrl;
    }

    private String getUrlAfterUploadS3(MultipartFile multipartFile) {
        // OS에 관계없이 현재 디렉토리 아래에 aibayo_upload 디렉토리 설정
        String rootPath = System.getProperty("user.dir"); // 현재 디렉토리
        String uploadPath = rootPath + uploadDirectory;
        File uploadDir = new File(uploadPath);

        // 로컬에 파일 등록
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        String filePathLocal = uploadPath + File.separator + fileName;
        String filePathS3 = MEAL_IMG_DIR + "/" + fileName;


        try {
            File uploadLocal = new File(filePathLocal);
            multipartFile.transferTo(uploadLocal);

            // s3 파일 등록
            amazonS3Client.putObject(new PutObjectRequest(bucket, filePathS3, uploadLocal));

            // 로컬 파일 삭제
            if (uploadLocal.delete()) {
                log.info(">>>>>>>>>>> local meal file delete succeed");
            } else {
                log.info(">>>>>>>>>>> local meal file delete failed");
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }

        return amazonS3Client.getUrl(bucket, filePathS3).toString();
    }

}
