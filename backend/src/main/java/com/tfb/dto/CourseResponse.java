package com.tfb.dto;

import com.tfb.entity.Course;
import lombok.Data;

@Data
public class CourseResponse {
    private Long id;
    private String date;
    private String title;
    private String content;

    public static CourseResponse from(Course c) {
        CourseResponse r = new CourseResponse();
        r.id = c.getId();
        r.date = c.getDate().toString();
        r.title = c.getTitle();
        r.content = c.getContent();
        return r;
    }
}
