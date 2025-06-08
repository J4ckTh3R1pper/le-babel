package dayp308.lebabel.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;


@Component
public class JwtOperations {
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("2cd1f549323940b1bad6f6960ec26a24".getBytes()); // 生成安全的密钥
    private static final long REFRESH_EXPIRATION_TIME = 604800000; // 七天代表的毫秒数
    private static final long EXPIRATION_TIME = 864000000; // 同上，但是是24小时

    public static JwtToken issueToken(UserDetails userDetails, boolean rememberMe) {
        long now = System.currentTimeMillis();
        JwtToken token = new JwtToken();
        token.setAccessToken(buildAccessToken(userDetails, now));
        if (rememberMe) token.setRefreshToken(buildRefreshToken(userDetails, now));
        return token;
    }

    private static JwtBuilder buildBase(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .id(UUID.randomUUID().toString().replaceAll("-", ""))
                .issuer("lebabel.dev")
                // 加密算法和密钥
                .signWith(SECRET_KEY);
                // 主题
    }

    private static String buildAccessToken(UserDetails userDetails, long currentTimeMillis) {
        return buildBase( userDetails )
                // 签发时间
                .issuedAt(new Date(currentTimeMillis))
                // 过期时间
                .expiration(new Date(currentTimeMillis + EXPIRATION_TIME))
                .compact();
    }

    private static String buildRefreshToken(UserDetails userDetails, long currentTimeMillis) {
        return buildBase( userDetails )
                .issuedAt(new Date(currentTimeMillis))
                .notBefore(new Date(currentTimeMillis + EXPIRATION_TIME))
                .expiration(new Date(currentTimeMillis + REFRESH_EXPIRATION_TIME))
                .compact();
    }

    public static Claims parse(String token) {
        try {
            return Jwts.parser().verifyWith(SECRET_KEY).build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
