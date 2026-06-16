package com.tfb.repository;

import com.tfb.entity.Course;
import com.tfb.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m JOIN FETCH m.user WHERE m.course = :course ORDER BY m.createdAt ASC")
    List<Message> findByCourseWithUser(@Param("course") Course course);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.course.date = :date")
    long countByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.course.date = :date AND m.type = 'help' AND m.resolved = false")
    long countOpenHelpByDate(@Param("date") LocalDate date);
}
