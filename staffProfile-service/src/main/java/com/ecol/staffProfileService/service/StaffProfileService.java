package com.ecol.staffProfileService.service;

import com.ecol.staffProfileService.mapper.StaffProfileMapper;
import com.ecol.staffProfileService.repository.StaffProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StaffProfileService {
 private final StaffProfileRepository staffProfileRepository;
 private final StaffProfileMapper staffProfileMapper;
}
