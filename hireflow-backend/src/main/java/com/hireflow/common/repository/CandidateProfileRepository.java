package com.hireflow.common.repository;

import com.hireflow.common.entity.CandidateProfile;
import com.hireflow.common.entity.User;
import com.hireflow.common.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CandidateProfileRepository
        extends JpaRepository<CandidateProfile, Long> {

    Optional<CandidateProfile> findByUser(User user);

    @Query("""
           SELECT cp
           FROM CandidateProfile cp
           JOIN FETCH cp.user
           WHERE cp.user = :user
           """)
    Optional<CandidateProfile> findByUserWithDetails(User user);

    List<CandidateProfile> findByApplicationStatus(
            ApplicationStatus status
    );

    boolean existsByUser(User user);
}