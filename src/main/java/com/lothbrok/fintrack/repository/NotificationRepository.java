package com.lothbrok.fintrack.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lothbrok.fintrack.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Page<Notification> findByUserId(UUID userId, Pageable pageable);

    Page<Notification> findByUserIdAndReadFalse(UUID userId, Pageable pageable);

    List<Notification> findTop20ByUserIdOrderByCreatedAtDesc(UUID userId);
}
