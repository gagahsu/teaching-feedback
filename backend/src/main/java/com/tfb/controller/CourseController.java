package com.tfb.controller;

import com.tfb.dto.CourseRequest;
import com.tfb.dto.CourseResponse;
import com.tfb.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
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
    public ResponseEntity<List<CourseResponse>> getCourses(@PathVariable String date) {
        return ResponseEntity.ok(courseService.getCourses(LocalDate.parse(date)));
    }

    @PostMapping("/courses/{date}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseResponse> addCourse(
            @PathVariable String date, @RequestBody CourseRequest req) {
        return ResponseEntity.ok(courseService.addCourse(LocalDate.parse(date), req));
    }

    @PutMapping("/courses/{date}/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable String date, @PathVariable Long id, @RequestBody CourseRequest req) {
        return ResponseEntity.ok(courseService.updateCourse(id, req));
    }

    @DeleteMapping("/courses/{date}/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable String date, @PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
