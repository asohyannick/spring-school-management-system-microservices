package com.ecol.studentProfileService.repository;

import com.ecol.studentProfileService.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentProfileRepository extends JpaRepository < StudentProfile, UUID > {
}
