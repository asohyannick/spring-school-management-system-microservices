package com.ecol.courseService.repository;

import com.ecol.courseService.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseRepository extends JpaRepository< Course, UUID >{

}
