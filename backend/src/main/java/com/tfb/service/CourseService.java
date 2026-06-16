package com.tfb.service;

import com.tfb.dto.CourseRequest;
import com.tfb.dto.CourseResponse;
import com.tfb.entity.Course;
import com.tfb.repository.CourseRepository;
import com.tfb.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepo;
    private final MessageRepository msgRepo;

    public Map<String, Object> getCalendar(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        List<Course> courses = courseRepo.findByDateBetweenOrderByDateAsc(ym.atDay(1), ym.atEndOfMonth());
        Map<String, Object> result = new LinkedHashMap<>();
        courses.forEach(c -> {
            long msgCount  = msgRepo.countByDate(c.getDate());
            long helpCount = msgRepo.countOpenHelpByDate(c.getDate());
            result.put(c.getDate().toString(), Map.of(
                    "title",     c.getTitle() != null ? c.getTitle() : "",
                    "msgCount",  msgCount,
                    "helpCount", helpCount
            ));
        });
        return result;
    }

    public Optional<CourseResponse> getCourse(LocalDate date) {
        return courseRepo.findByDate(date).map(CourseResponse::from);
    }

    public CourseResponse upsertCourse(LocalDate date, CourseRequest req) {
        Course c = courseRepo.findByDate(date).orElse(new Course());
        c.setDate(date);
        c.setTitle(req.getTitle());
        c.setContent(req.getContent());
        c.setUpdatedAt(LocalDateTime.now());
        return CourseResponse.from(courseRepo.save(c));
    }
}
