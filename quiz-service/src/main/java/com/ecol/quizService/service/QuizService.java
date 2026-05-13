package com.ecol.quizService.service;

import com.ecol.quizService.mapper.QuizMapper;
import com.ecol.quizService.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuizService {
 private final QuizRepository quizRepository;
 private final QuizMapper quizMapper;
}
