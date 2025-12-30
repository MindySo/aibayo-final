package com.aico.aibayo.repository.announce;

import com.aico.aibayo.entity.AnnounceMemberEntity;
import com.aico.aibayo.entity.AnnounceMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnounceMemberRepository extends JpaRepository<AnnounceMemberEntity, AnnounceMemberId> {
}
