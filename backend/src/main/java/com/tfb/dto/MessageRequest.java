package com.tfb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageRequest {
    private String type;
    private String text;
    @JsonProperty("isPrivate")
    private boolean isPrivate = false;
    private Long taggedCourseId;
}
