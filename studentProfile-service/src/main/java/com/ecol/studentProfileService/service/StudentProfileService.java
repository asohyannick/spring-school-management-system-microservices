package com.ecol.studentProfileService.service;

import com.ecol.studentProfileService.mapper.StudentProfileMapper;
import com.ecol.studentProfileService.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentProfileService {
 private final StudentProfileRepository studentProfileRepository;
 private  final StudentProfileMapper studentProfileMapper;
}
