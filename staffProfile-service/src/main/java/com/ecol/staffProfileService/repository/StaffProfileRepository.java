package com.ecol.staffProfileService.repository;

import com.ecol.staffProfileService.entity.StaffProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StaffProfileRepository extends JpaRepository < StaffProfile, UUID > {
}
