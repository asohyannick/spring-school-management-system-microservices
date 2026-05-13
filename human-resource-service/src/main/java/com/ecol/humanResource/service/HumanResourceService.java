package com.ecol.humanResource.service;

import com.ecol.humanResource.mapper.HumanResourceMapper;
import com.ecol.humanResource.repository.HumanResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HumanResourceService {
	 private final HumanResourceRepository humanResourceRepository;
	 private final HumanResourceMapper humanResourceMapper;
}
