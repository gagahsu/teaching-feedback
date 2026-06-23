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
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepo;
    private final MessageRepository msgRepo;

    public Map<String, Object> getCalendar(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end   = ym.atEndOfMonth();

        // 1 query: all courses for the month
        List<Course> courses = courseRepo.findByDateBetweenOrderByDateAscIdAsc(start, end);

        // 2 queries: message counts for entire month in one shot each
        Map<LocalDate, Long> msgCounts = new HashMap<>();
        msgRepo.countGroupedByDate(start, end)
                .forEach(row -> msgCounts.put((LocalDate) row[0], (Long) row[1]));

        Map<LocalDate, Long> helpCounts = new HashMap<>();
        msgRepo.countOpenHelpGroupedByDate(start, end)
                .forEach(row -> helpCounts.put((LocalDate) row[0], (Long) row[1]));

        // group titles by date (no more per-date DB calls)
        Map<LocalDate, List<String>> titlesByDate = new LinkedHashMap<>();
        courses.forEach(c -> {
            if (c.getTitle() != null && !c.getTitle().isBlank()) {
                titlesByDate.computeIfAbsent(c.getDate(), d -> new ArrayList<>()).add(c.getTitle());
            } else {
                titlesByDate.computeIfAbsent(c.getDate(), d -> new ArrayList<>());
            }
        });

        Map<String, Object> result = new LinkedHashMap<>();
        titlesByDate.forEach((date, titles) ->
            result.put(date.toString(), Map.of(
                "titles",    titles,
                "msgCount",  msgCounts.getOrDefault(date, 0L),
                "helpCount", helpCounts.getOrDefault(date, 0L)
            ))
        );
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
