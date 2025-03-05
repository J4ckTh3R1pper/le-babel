package dayp308.chatroom.repository;

import dayp308.chatroom.entity.PersistentToken;
import io.lettuce.core.protocol.CommandType;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisPipelineException;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@Repository
public class RedisPersistentTokenRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Jackson2HashMapper jackson2HashMapper;

    @Autowired
    public RedisPersistentTokenRepository(RedisTemplate<String, Object> redisTemplate, Jackson2HashMapper jackson2HashMapper) {
        this.redisTemplate = redisTemplate;
        this.jackson2HashMapper = jackson2HashMapper;
    }

    public void createNewToken(String serial, PersistentToken token, Instant expiry) {
        redisTemplate.opsForHash().putAll(serial, jackson2HashMapper.toHash(token));
        redisTemplate.expireAt(serial, expiry);
        try {
            redisTemplate.executePipelined((RedisCallback<Object>) conn -> {
                conn.hashCommands().hSet("userId_key_index".getBytes(), serial.getBytes(), token.getUserId().toString().getBytes());
                conn.execute(CommandType.HEXPIREAT.name(), "userId_key_index".getBytes(), String.valueOf(expiry.getEpochSecond()).getBytes(),
                        "FIELDS".getBytes(), "1".getBytes(), token.getToken().getBytes());
                return null;

            });
        } catch ( RedisPipelineException ignored) {}
    }

    public void updateToken(String serial, String tokenValue, Instant expiry) {
        HashOperations<String, String, Object> hashOp =  redisTemplate.opsForHash();
        Map<String, Object> hash =  Optional.of(hashOp.entries(serial)).orElseThrow(
                () -> new RememberMeAuthenticationException("No persistent token found for series id: " + serial)
        );
        PersistentToken token = (PersistentToken) jackson2HashMapper.fromHash(hash);
        token.setToken(tokenValue);
        redisTemplate.opsForHash().putAll(serial, jackson2HashMapper.toHash(token));
        redisTemplate.expireAt(serial, expiry);
        try {
            redisTemplate.executePipelined( (RedisCallback<Object>) conn -> {
                conn.hashCommands().hSet("userId_key_index".getBytes(), serial.getBytes(), token.getUserId().toString().getBytes());
                conn.execute("HEXPIREAT", "userId_key_index".getBytes(), String.valueOf(expiry.getEpochSecond()).getBytes(),
                        "FIELDS".getBytes(), "1".getBytes(), token.getToken().getBytes());
                return null;
            });
        } catch (Exception ignored) {}

    }

    public PersistentToken getTokenForSerial(String serial) {
        HashOperations<String, String, Object> hashOp =  redisTemplate.opsForHash();
        Map<String, Object> hash =  Optional.of(hashOp.entries(serial)).orElseThrow(
                () -> new RememberMeAuthenticationException("No persistent token found for series id: " + serial)
        );
        return (PersistentToken) jackson2HashMapper.fromHash(hash);
    }

    public void removeUserTokens(Long userId) {
        ScanOptions hashScanOptions = ScanOptions.NONE;
        List<Map.Entry<Object, Object>> keys;
        try (Cursor<Map.Entry<Object, Object>> entryCursor =
                     redisTemplate.opsForHash().scan("userId_key_index", hashScanOptions)
        ) { keys = entryCursor.stream().filter(e -> {
                String value =
                e.getValue().toString();
                return value.equals(userId.toString()); }
            ).toList();
        }
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            keys.forEach(e -> {
                connection.commands().del(e.getKey().toString().getBytes(StandardCharsets.UTF_8));
            });
            return null;
        });
    }
}
