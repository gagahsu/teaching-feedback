package com.tfb.service;

import com.tfb.dto.MessageRequest;
import com.tfb.dto.MessageResponse;
import com.tfb.dto.ReplyRequest;
import com.tfb.dto.ReplyResponse;
import com.tfb.entity.*;
import com.tfb.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository msgRepo;
    private final ReplyRepository replyRepo;
    private final CourseRepository courseRepo;
    private final UserRepository userRepo;

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessages(LocalDate date, String username) {
        User viewer = username != null ? userRepo.findByName(username).orElse(null) : null;
        boolean isTeacher = viewer != null && viewer.getRole() == User.Role.TEACHER;
        Long viewerId = viewer != null ? viewer.getId() : null;
        return courseRepo.findAllByDateOrderByIdAsc(date).stream()
                .flatMap(course -> msgRepo.findByCourseWithUser(course).stream())
                .filter(m -> !m.isPrivate() || isTeacher || (viewerId != null && viewerId.equals(m.getUser().getId())))
                .map(m -> {
                    List<ReplyResponse> replies = replyRepo
                            .findByMessageWithUser(m)
                            .stream()
                            .map(ReplyResponse::from)
                            .collect(Collectors.toList());
                    return MessageResponse.from(m, replies);
                })
                .collect(Collectors.toList());
    }

    public MessageResponse postMessage(LocalDate date, String username, MessageRequest req) {
        Course course = courseRepo.findFirstByDateOrderByIdAsc(date).orElseGet(() -> {
            Course c = new Course();
            c.setDate(date);
            return courseRepo.save(c);
        });
        User user = userRepo.findByName(username).orElseThrow();
        Message m = new Message();
        m.setCourse(course);
        m.setUser(user);
        m.setType(Message.MessageType.valueOf(req.getType()));
        m.setText(req.getText());
        m.setPrivate(req.isPrivate());
        if (req.getTaggedCourseId() != null) {
            courseRepo.findById(req.getTaggedCourseId()).ifPresent(m::setTaggedCourse);
        }
        msgRepo.save(m);
        return MessageResponse.from(m, List.of());
    }

    public ReplyResponse postReply(Long msgId, String username, ReplyRequest req) {
        Message msg  = msgRepo.findById(msgId).orElseThrow();
        User user    = userRepo.findByName(username).orElseThrow();
        Reply r = new Reply();
        r.setMessage(msg);
        r.setUser(user);
        r.setText(req.getText());
        replyRepo.save(r);
        return ReplyResponse.from(r);
    }

    public MessageResponse toggleResolved(Long msgId) {
        Message m = msgRepo.findById(msgId).orElseThrow();
        m.setResolved(!m.isResolved());
        msgRepo.save(m);
        List<ReplyResponse> replies = replyRepo.findByMessageWithUser(m)
                .stream().map(ReplyResponse::from).collect(Collectors.toList());
        return MessageResponse.from(m, replies);
    }
}
