package com.ecol.quizService.repository;

import com.ecol.quizService.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuizRepository extends JpaRepository < Quiz, UUID > {
}
