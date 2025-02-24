package dayp308.chatroom.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class CategoryDTO {

    private Integer id;

    private String categoryName;

    private Integer categoryRank = 1;

    private Boolean isDeleted = false;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Instant createTime = Instant.now();

    private String categoryAvatar = "";

    private String categoryInfo = "";

    public CategoryDTO(String categoryName) {
        this.categoryName = categoryName;
    }
}
