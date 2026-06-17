package com.tfb.controller;

import com.tfb.dto.MessageRequest;
import com.tfb.dto.MessageResponse;
import com.tfb.dto.ReplyRequest;
import com.tfb.dto.ReplyResponse;
import com.tfb.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{date}")
    public ResponseEntity<List<MessageResponse>> getMessages(
            @PathVariable String date,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(messageService.getMessages(LocalDate.parse(date), user.getUsername()));
    }

    @PostMapping("/{date}")
    public ResponseEntity<MessageResponse> postMessage(
            @PathVariable String date,
            @RequestBody MessageRequest req,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(messageService.postMessage(LocalDate.parse(date), user.getUsername(), req));
    }

    @PostMapping("/{msgId}/replies")
    public ResponseEntity<ReplyResponse> postReply(
            @PathVariable Long msgId,
            @RequestBody ReplyRequest req,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(messageService.postReply(msgId, user.getUsername(), req));
    }

    @PatchMapping("/{msgId}/resolve")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<MessageResponse> toggleResolve(@PathVariable Long msgId) {
        return ResponseEntity.ok(messageService.toggleResolved(msgId));
    }
}
