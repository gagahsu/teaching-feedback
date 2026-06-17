package com.tfb.repository;

import com.tfb.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findFirstByDateOrderByIdAsc(LocalDate date);
    List<Course> findAllByDateOrderByIdAsc(LocalDate date);
    List<Course> findByDateBetweenOrderByDateAscIdAsc(LocalDate start, LocalDate end);
}
