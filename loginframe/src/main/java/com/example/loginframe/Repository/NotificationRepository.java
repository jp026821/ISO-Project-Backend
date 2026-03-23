package com.example.loginframe.Repository;

import com.example.loginframe.Entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUserEmailOrderByCreatedAtDesc(String userEmail);
}