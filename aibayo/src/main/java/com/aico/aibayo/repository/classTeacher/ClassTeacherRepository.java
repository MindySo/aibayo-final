package com.aico.aibayo.repository.classTeacher;

import com.aico.aibayo.dto.classTeacher.ClassTeacherDto;
import com.aico.aibayo.entity.ClassTeacherEntity;
import com.aico.aibayo.repository.classKid.ClassKidRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassTeacherRepository extends JpaRepository<ClassTeacherEntity, Long>, ClassTeacherRepositoryCustom {

}
