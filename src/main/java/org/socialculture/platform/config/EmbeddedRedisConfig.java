package org.socialculture.platform.config;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * @author yechanKim
 */
@Slf4j
@Profile("local")
@Configuration
public class EmbeddedRedisConfig {

    @Value("${spring.data.redis.port}")
    private int redisPort;
    // 윈도우를 위한 maxmemory 설정
    @Value("${spring.data.redis.maxmemory}")
    private String redisMaxMemory;

    private RedisServer redisServer;


    @PostConstruct
    public void startRedis() throws IOException {
        int port = isRedisRunning() ? findAvailablePort() : redisPort;
        if (isArmArchitecture()) {
            log.info("ARM Architecture");
            redisServer = new RedisServer(Objects.requireNonNull(getRedisServerExecutable()), port);
        }else{
            redisServer = RedisServer.builder()
                    .port(port)
                    .setting("maxmemory " + redisMaxMemory)
                    .build();
        }
        redisServer.start();
    }


    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }

    private int findAvailablePort() throws IOException {
        for (int port = 10000; port < 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }
        throw new GeneralException(ErrorStatus.AVAILABLE_PORT_NOT_FOUND);
    }

    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }


    private Process executeGrepProcessCommand(int port) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();

        String command;
        if (os.contains("win")) {
            // 윈도우인 경우
            command = String.format("netstat -nao | find \"LISTENING\" | find \":%d\"", port);
            String[] cmd = {"cmd.exe", "/y", "/c", command};
            return Runtime.getRuntime().exec(cmd);
        }
        // Unix 계열인 경우 (맥OS, 리눅스)
        command = String.format("netstat -nat | grep LISTEN | grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }


    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.ERROR_EXECUTING_EMBEDDED_REDIS);
        }
        return StringUtils.hasText(pidInfo.toString());
    }

    // mac os용 redis 바이너리 파일
    private File getRedisServerExecutable() {
        try {
            return new File("src/main/resources/redis/redis-server-7.2.6-mac-arm64");
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.REDIS_SERVER_EXECUTABLE_NOT_FOUND);
        }
    }

    // mac os 인지 확인
    private boolean isArmArchitecture() {
        return System.getProperty("os.arch").contains("aarch64");
    }
}
