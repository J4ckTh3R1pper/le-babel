package dayp308.chatroom.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayp308.chatroom.Constants;
import lombok.Data;

@Data
public class JwtToken {
    @JsonProperty(Constants.JWT_ACCESS_KEY)
    private String accessToken;
    @JsonProperty(Constants.JWT_REFRESH_KEY)
    private String refreshToken;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        if (accessToken != null)
            builder.append("\"access_token\":\"").append(accessToken).append("\"");
        if (refreshToken != null)
            builder.append(",\"refresh_token\":\"").append(refreshToken).append("\"");
        builder.append("}");
        return builder.toString();
    }
}
