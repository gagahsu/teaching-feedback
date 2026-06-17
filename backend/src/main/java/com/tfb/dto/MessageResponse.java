package com.tfb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tfb.entity.Message;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MessageResponse {
    private Long id;
    private String type;
    private String text;
    private boolean resolved;
    @JsonProperty("isPrivate")
    private boolean isPrivate;
    private LocalDateTime createdAt;
    private String authorName;
    private String authorRole;
    private List<ReplyResponse> replies;

    public static MessageResponse from(Message m, List<ReplyResponse> replies) {
        MessageResponse r = new MessageResponse();
        r.id = m.getId();
        r.type = m.getType().name();
        r.text = m.getText();
        r.resolved = m.isResolved();
        r.isPrivate = m.isPrivate();
        r.createdAt = m.getCreatedAt();
        r.authorName = m.getUser().getName();
        r.authorRole = m.getUser().getRole().name();
        r.replies = replies;
        return r;
    }
}
