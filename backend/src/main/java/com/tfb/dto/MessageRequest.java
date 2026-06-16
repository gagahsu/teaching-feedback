package com.tfb.dto;

import lombok.Data;

@Data
public class MessageRequest {
    private String type;
    private String text;
}
