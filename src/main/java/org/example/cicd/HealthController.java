package org.example.cicd;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private final Instant startedAt = Instant.now();

    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/api/info")
    public Map<String, String> info() throws UnknownHostException {
        return Map.of(
                "app", "cicd",
                "hostname", InetAddress.getLocalHost().getHostName(),
                "startedAt", startedAt.toString()
        );
    }

    @GetMapping("/api/new01")
    public Map<String, String> newfunc() throws UnknownHostException {
        return Map.of(
                "version", "cicd01",
                "hostname", InetAddress.getLocalHost().getHostName(),
                "startedAt", startedAt.toString(),
                "desc","0209 첫 CICD 동작 입니다."
        );
    }
}
