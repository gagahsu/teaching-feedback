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

    // 批次：一次取回整月每天的留言數
    @Query("SELECT m.course.date, COUNT(m) FROM Message m WHERE m.course.date BETWEEN :start AND :end GROUP BY m.course.date")
    List<Object[]> countGroupedByDate(@Param("start") LocalDate start, @Param("end") LocalDate end);

    // 批次：一次取回整月每天的未解決求救數
    @Query("SELECT m.course.date, COUNT(m) FROM Message m WHERE m.course.date BETWEEN :start AND :end AND m.type = 'help' AND m.resolved = false GROUP BY m.course.date")
    List<Object[]> countOpenHelpGroupedByDate(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
