package com.aico.aibayo.aop;

import com.aico.aibayo.dto.notepad.NotepadDto;

import java.util.Collection;
import java.util.List;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // 특정 서비스 패키지의 모든 메서드를 타겟으로 설정
    @Pointcut("execution(* com.aico.aibayo.service..*(..))")
    public void serviceMethods() {}

    @Pointcut("execution(* com.aico.aibayo.repository..*(..))")
    public void repositoryMethods() {}

    // 메서드가 정상적으로 반환될 때 실행되는 후처리
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logInfo(Object result) {

        // Page<T>를 반환할 때 로깅
        if (result instanceof Page) {
            Page<?> page = (Page<?>) result;

            log.info(">>>>>>>>>>>>>>>>>>>>logPageInfo");
            log.info("getPageNumber: {}", page.getPageable().getPageNumber());
            log.info("getOffset: {}", page.getPageable().getOffset());
            log.info("hasPrevious: {}", page.getPageable().hasPrevious());
            log.info("first: {}", page.getPageable().first());
            log.info("next: {}", page.getPageable().next());
            log.info("getTotalPages: {}", page.getTotalPages());
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

            log.info(">>>>>>>>>>>>>>>>>>>>logPageItems");
            page.forEach(item -> log.info("\n{}", item.toString()));
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        }

        if (result instanceof List) {
            List<?> list = (List<?>) result;
            log.info(">>>>>>>>>>>>>>>>>>>>logListItems");
            list.forEach(item -> log.info("\n{}", item.toString()));
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        }

        if (result instanceof NotepadDto) {
            NotepadDto notepadDto = (NotepadDto) result;

            log.info(">>>>>>>>>>>>>>>>>>>>logNotepadInfo");
            log.info(notepadDto.toString());
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
    }

    @AfterReturning(pointcut = "repositoryMethods()", returning = "result")
    public void logAfterReturning(Object result) {
        if (result instanceof Collection) {
            Collection<?> collection = (Collection<?>) result;
            log.info(">>>>>>>>>>>>>>>>>>>>return entities");
            collection.forEach(entity -> log.info("\n{}", entity.toString()));
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        } else if (result != null) {
            log.info(">>>>>>>>>>>>>>>>>>>>return entity");
            log.info("{}", result);
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
    }
}
