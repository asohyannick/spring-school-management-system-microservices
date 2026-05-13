package com.ecol.assignmentService.repository;

import com.ecol.assignmentService.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssignmentRepository extends JpaRepository< Assignment, UUID > {

}
