package com.abuqool.sqool.repo.common;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abuqool.sqool.dao.common.EventInfo;

public interface EventRepo extends JpaRepository<EventInfo, Integer> {

    @Query("SELECT e FROM EventInfo e WHERE e.schoolCode = :code AND e.endTime >= :now AND e.status = 0")
    List<EventInfo> findOpenEventsForSchool(@Param("code") String schoolCode, @Param("now") Date now);
}
