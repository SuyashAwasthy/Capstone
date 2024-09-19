package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.ContactReply;

public interface ContactReplyRepository extends JpaRepository<ContactReply, Long> {
    List<ContactReply> findByContactMessageId(Long contactMessageId);
}

