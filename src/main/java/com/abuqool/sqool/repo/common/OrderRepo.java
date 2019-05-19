package com.abuqool.sqool.repo.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abuqool.sqool.dao.common.OrderInfo;

public interface OrderRepo extends JpaRepository<OrderInfo, Integer>{

//    List<OrderInfo> findByUserIdAndBySchoolCode(int userId, String schoolCode);//TODO

    List<OrderInfo> findByUserIdOrderByCreateTimeDesc(int userId);

    OrderInfo findByOrderId(String orderId);
}
