package com.hireflow.common.repository;

import com.hireflow.common.entity.InterviewBooking;
import com.hireflow.common.entity.InterviewSlot;
import com.hireflow.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewBookingRepository extends JpaRepository<InterviewBooking, Long>
{

    List<InterviewBooking> findByCandidate(User candidate);

    boolean existsByInterviewSlot(InterviewSlot slot);

    List<InterviewBooking> findByInterviewSlot(InterviewSlot slot);
}