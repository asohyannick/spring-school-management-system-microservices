package com.ecol.reviewService.service;

import com.ecol.reviewService.mapper.ReviewMapper;
import com.ecol.reviewService.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final ReviewMapper reviewMapper;
}
