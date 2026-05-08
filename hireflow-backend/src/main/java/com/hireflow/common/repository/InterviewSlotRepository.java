package com.hireflow.common.repository;

import com.hireflow.common.entity.InterviewSlot;
import com.hireflow.common.entity.User;
import com.hireflow.common.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InterviewSlotRepository extends JpaRepository<InterviewSlot, Long>
{

    List<InterviewSlot> findBySlotStatus(SlotStatus slotStatus);

    List<InterviewSlot> findByInterviewer(User interviewer);

    List<InterviewSlot> findByInterviewDate(LocalDate interviewDate);
}