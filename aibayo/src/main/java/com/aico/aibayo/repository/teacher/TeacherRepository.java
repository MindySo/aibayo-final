package com.aico.aibayo.repository.teacher;

import com.aico.aibayo.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<MemberEntity, Long>, TeacherRepositoryCustom {
}
