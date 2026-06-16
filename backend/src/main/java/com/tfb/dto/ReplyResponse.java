package com.tfb.dto;

import com.tfb.entity.Reply;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReplyResponse {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private String authorName;
    private String authorRole;

    public static ReplyResponse from(Reply r) {
        ReplyResponse res = new ReplyResponse();
        res.id = r.getId();
        res.text = r.getText();
        res.createdAt = r.getCreatedAt();
        res.authorName = r.getUser().getName();
        res.authorRole = r.getUser().getRole().name();
        return res;
    }
}
