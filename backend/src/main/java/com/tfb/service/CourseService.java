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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepo;
    private final MessageRepository msgRepo;

    public Map<String, Object> getCalendar(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        List<Course> courses = courseRepo.findByDateBetweenOrderByDateAscIdAsc(ym.atDay(1), ym.atEndOfMonth());
        Map<String, Object> result = new LinkedHashMap<>();
        Map<LocalDate, String> firstTitleByDate = new LinkedHashMap<>();
        courses.forEach(c -> firstTitleByDate.putIfAbsent(c.getDate(), c.getTitle() != null ? c.getTitle() : ""));
        firstTitleByDate.forEach((date, title) -> {
            long msgCount  = msgRepo.countByDate(date);
            long helpCount = msgRepo.countOpenHelpByDate(date);
            result.put(date.toString(), Map.of("title", title, "msgCount", msgCount, "helpCount", helpCount));
        });
        return result;
    }

    public List<CourseResponse> getCourses(LocalDate date) {
        return courseRepo.findAllByDateOrderByIdAsc(date)
                .stream().map(CourseResponse::from).collect(Collectors.toList());
    }

    public CourseResponse addCourse(LocalDate date, CourseRequest req) {
        Course c = new Course();
        c.setDate(date);
        c.setTitle(req.getTitle());
        c.setContent(req.getContent());
        c.setUpdatedAt(LocalDateTime.now());
        return CourseResponse.from(courseRepo.save(c));
    }

    public CourseResponse updateCourse(Long id, CourseRequest req) {
        Course c = courseRepo.findById(id).orElseThrow();
        c.setTitle(req.getTitle());
        c.setContent(req.getContent());
        c.setUpdatedAt(LocalDateTime.now());
        return CourseResponse.from(courseRepo.save(c));
    }

    public void deleteCourse(Long id) {
        courseRepo.deleteById(id);
    }
}
