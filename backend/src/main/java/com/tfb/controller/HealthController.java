package com.tfb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.Map;

/**
 * 保持 Render 服務不休眠用的 keep-alive 端點。
 * 用 cron job 定時打 GET /api/ping 即可，不需登入、不碰資料庫。
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of("status", "ok", "time", Instant.now().toString());
    }
}
