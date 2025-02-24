package dayp308.chatroom.entity.dto;

import dayp308.chatroom.entity.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CategoryMemberDTO {
    private Long userId;
    private Integer categoryId;
    private Roles role = Roles.SUBSCRIBER;
    private String title;
    private Integer experience = 0;
    private Instant muteExpirationDate = Instant.EPOCH;

    public CategoryMemberDTO(Long userId, Integer categoryId) {
        this.userId = userId;
        this.categoryId = categoryId;
    }
}
