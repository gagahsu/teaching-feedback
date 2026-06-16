package com.tfb.repository;

import com.tfb.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.tfb.entity.Message;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("SELECT r FROM Reply r JOIN FETCH r.user WHERE r.message = :msg ORDER BY r.createdAt ASC")
    List<Reply> findByMessageWithUser(@Param("msg") Message msg);
}
