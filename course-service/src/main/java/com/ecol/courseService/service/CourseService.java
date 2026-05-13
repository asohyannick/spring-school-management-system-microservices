package com.ecol.courseService.service;

import com.ecol.courseService.mapper.CourseMapper;
import com.ecol.courseService.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseService {
	private final CourseRepository courseRepository;
	private final CourseMapper courseMapper;
}
