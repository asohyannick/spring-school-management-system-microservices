package com.ecol.admissionService.repository;

import com.ecol.admissionService.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdmissionRepository extends JpaRepository< Admission, UUID > {

}
