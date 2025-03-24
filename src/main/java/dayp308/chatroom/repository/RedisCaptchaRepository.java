package dayp308.chatroom.repository;

import com.pig4cloud.captcha.SpecCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Repository
public class RedisCaptchaRepository {
    private static final String KEY_PREFIX = "captcha:";
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisCaptchaRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public String saveCaptcha(Captcha captcha) {
        String uuid = UUID.randomUUID().toString().toLowerCase();
        String uuidKey = KEY_PREFIX + uuid;
        String captchaText = captcha.text().toLowerCase();
        redisTemplate.opsForValue().set(uuidKey, captchaText);
        redisTemplate.expire(uuidKey, Duration.ofMinutes(5));
        return uuid;
    }

    @Transactional(readOnly = true)
    public boolean checkCaptcha(String uuid, String captcha) {
        String key = KEY_PREFIX + uuid;
        Object value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            String captchaText = value.toString().toLowerCase();
            return captchaText.equals(captcha);
        }
        return false;
    }
}
