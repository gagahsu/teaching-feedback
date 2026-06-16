package com.tfb.controller;

import com.tfb.dto.CourseRequest;
import com.tfb.dto.CourseResponse;
import com.tfb.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/calendar")
    public ResponseEntity<Map<String, Object>> calendar(
            @RequestParam int year, @RequestParam int month) {
        return ResponseEntity.ok(courseService.getCalendar(year, month));
    }

    @GetMapping("/courses/{date}")
    public ResponseEntity<CourseResponse> getCourse(@PathVariable String date) {
        return courseService.getCourse(LocalDate.parse(date))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/courses/{date}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseResponse> upsertCourse(
            @PathVariable String date, @RequestBody CourseRequest req) {
        return ResponseEntity.ok(courseService.upsertCourse(LocalDate.parse(date), req));
    }
}
