package com.abuqool.sqool.repo.common;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abuqool.sqool.dao.common.EventPhaseInfo;

public interface EventPhaseRepo extends JpaRepository<EventPhaseInfo, Integer> {

    @Query("SELECT p FROM EventPhaseInfo p WHERE p.schoolCode = :code AND p.startTime <= :now AND p.endTime >= :now")
    EventPhaseInfo findCurrentPhaseForSchool(@Param("code") String schoolCode, @Param("now") Date now);
}
