package com.aico.aibayo.service.schedule;

import com.aico.aibayo.common.BoardTypeEnum;
import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.schedule.ScheduleDto;
import com.aico.aibayo.dto.schedule.ScheduleSearchCondition;
import com.aico.aibayo.entity.BoardEntity;
import com.aico.aibayo.entity.ScheduleClassEntity;
import com.aico.aibayo.entity.ScheduleClassId;
import com.aico.aibayo.entity.ScheduleEntity;
import com.aico.aibayo.repository.BoardRepository;
import com.aico.aibayo.repository.schedule.ScheduleClassRepository;
import com.aico.aibayo.repository.schedule.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final BoardRepository boardRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleClassRepository scheduleClassRepository;

    @Override
    public List<ScheduleDto> getAllByKinderNo(ScheduleSearchCondition condition) {
        return scheduleRepository.findAllByKinderNo(condition);
    }

    @Override
    public List<ScheduleDto> getAllByDay(ScheduleSearchCondition condition) {
        return scheduleRepository.findAllByDay(condition);
    }

    @Override
    public List<ScheduleDto> getAllByClassNo(ScheduleSearchCondition condition) {
        return scheduleRepository.findAllByClass(condition);
    }

    @Override
    @Transactional
    public void insertSchedule(Map<String, Object> requestBody) {

        Long writer = (Long)requestBody.get("writer");
        String boardTitle = (String)requestBody.get("boardTitle");
        String boardContents = (String)requestBody.get("boardContents");
        Long kinderNo = (Long)requestBody.get("kinderNo");
        LocalDateTime startDate = LocalDateTime.parse((String)(requestBody.get("startDate")));
        LocalDateTime endDate = LocalDateTime.parse((String)(requestBody.get("endDate")));
        Double mapLat = (Double)requestBody.get("mapLat");
        Double mapLng = (Double)requestBody.get("mapLng");

        List<?> classes = (List<?>) requestBody.get("classList");
        List<Long> classList = classes.stream()
                .map(element -> Long.parseLong(element.toString())) // String을 Long으로 변환
                .toList();

        // board insert
        BoardEntity boardEntity = BoardEntity.builder()
                .invisibleFlag(BooleanEnum.FALSE.getBool())
                .boardType(BoardTypeEnum.SCHEDULE.getNum())
                .writer(writer)
                .boardTitle(boardTitle)
                .boardContents(boardContents)
                .boardRegDate(LocalDateTime.now())
                .kinderNo(kinderNo)
                .build();

        BoardEntity savedBoard = boardRepository.save(boardEntity);

        // schedule insert
        ScheduleEntity scheduleEntity = ScheduleEntity.builder()
                .boardNo(savedBoard.getBoardNo())
                .scheduleStartDate(startDate)
                .scheduleEndDate(endDate)
                .mapLat(mapLat)
                .mapLng(mapLng)
                .build();

        ScheduleEntity savedSchedule = scheduleRepository.save(scheduleEntity);

        // scheduleClass insert
        for(Long classNo : classList) {
            ScheduleClassEntity scheduleClassEntity = ScheduleClassEntity.builder()
                    .scheduleNo(savedSchedule.getScheduleNo())
                    .classNo(classNo)
                    .build();
            scheduleClassRepository.save(scheduleClassEntity);
        }
    }

    @Override
    public ScheduleDto getOneByScheduleNo(ScheduleSearchCondition condition) {
        return scheduleRepository.findOneByScheduleNo(condition);
    }

    @Override
    public void updateSchedule(Map<String, Object> requestBody) {
        Long boardNo = Long.parseLong(requestBody.get("boardNo").toString());
        Long scheduleNo = Long.parseLong(requestBody.get("scheduleNo").toString());
        Long writer = (Long)requestBody.get("writer");
        String boardTitle = (String)requestBody.get("boardTitle");
        String boardContents = (String)requestBody.get("boardContents");
        LocalDateTime startDate = LocalDateTime.parse((String)(requestBody.get("startDate")));
        LocalDateTime endDate = LocalDateTime.parse((String)(requestBody.get("endDate")));
        Double mapLat = requestBody.get("mapLat") == null ?  null : Double.parseDouble((String)requestBody.get("mapLat"));
        Double mapLng = requestBody.get("mapLng") == null ?  null : Double.parseDouble((String)requestBody.get("mapLng"));

        List<?> classes = (List<?>) requestBody.get("classList");
        List<Long> classList = classes.stream()
                .map(element -> Long.parseLong(element.toString())) // String을 Long으로 변환
                .toList();

        // 기존 schedule_class 삭제
        List<?> originClasses = (List<?>) requestBody.get("originClassList");
        List<Long> originClassList = originClasses.stream()
                .map(element -> Long.parseLong(element.toString())) // String을 Long으로 변환
                .toList();

        for(Long classNo : originClassList) {
            ScheduleClassId scheduleClassId = new ScheduleClassId(scheduleNo, classNo);
            ScheduleClassEntity scheduleClassEntity = scheduleClassRepository.findById(scheduleClassId).orElse(null);
            assert scheduleClassEntity != null;
            scheduleClassRepository.delete(scheduleClassEntity);
        }

        // board modify
        BoardEntity boardEntity = boardRepository.findById(boardNo).orElse(null);
        assert boardEntity != null;
        boardEntity.setBoardTitle(boardTitle);
        boardEntity.setBoardContents(boardContents);
        boardEntity.setBoardModifyDate(LocalDateTime.now());
        boardEntity.setWriter(writer);

        boardRepository.save(boardEntity);

        // schedule modify
        ScheduleEntity scheduleEntity = scheduleRepository.findById(scheduleNo).orElse(null);
        assert scheduleEntity != null;
        scheduleEntity.setScheduleNo(scheduleNo);
        scheduleEntity.setScheduleStartDate(startDate);
        scheduleEntity.setScheduleEndDate(endDate);
        scheduleEntity.setMapLat(mapLat);
        scheduleEntity.setMapLng(mapLng);


        scheduleRepository.save(scheduleEntity);

        // scheduleClass insert
        for(Long classNo : classList) {
            ScheduleClassEntity scheduleClassEntity = ScheduleClassEntity.builder()
                    .scheduleNo(scheduleNo)
                    .classNo(classNo)
                    .build();
            scheduleClassRepository.save(scheduleClassEntity);
        }
    }

    @Override
    public void deleteSchedule(Map<String, Object> requestBody) {
        Long boardNo = Long.parseLong(requestBody.get("boardNo").toString());
//        List<?> classes = (List<?>) requestBody.get("classList");
//        List<Long> classList = classes.stream()
//                .map(element -> Long.parseLong(element.toString())) // String을 Long으로 변환
//                .toList();

        // board modify
        BoardEntity boardEntity = boardRepository.findById(boardNo).orElse(null);
        assert boardEntity != null;
        boardEntity.setBoardDeleteDate(LocalDateTime.now());
        boardEntity.setInvisibleFlag(BooleanEnum.TRUE.getBool());

        boardRepository.save(boardEntity);
    }

    @Override
    public List<ScheduleDto> getAllByClassList(ScheduleSearchCondition condition) {
        return scheduleRepository.findAllByClassList(condition);
    }

    @Override
    public List<ScheduleDto> getAllByDayAndClassList(ScheduleSearchCondition condition) {
        return scheduleRepository.findAllByDayAndClassList(condition);
    }
}