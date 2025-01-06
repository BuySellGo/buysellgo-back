package com.buysellgo.helpdeskservice.repository;

import com.buysellgo.helpdeskservice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
