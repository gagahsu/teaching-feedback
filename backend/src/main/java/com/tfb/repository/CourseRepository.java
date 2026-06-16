package com.tfb.repository;

import com.tfb.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByDate(LocalDate date);
    List<Course> findByDateBetweenOrderByDateAsc(LocalDate start, LocalDate end);
}
