package com.ecol.admissionService.service;
import com.ecol.admissionService.mapper.AdmissionMapper;
import com.ecol.admissionService.repository.AdmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdmissionService {
	private final AdmissionRepository admissionRepository;
	private final AdmissionMapper  admissionMapper;

}
