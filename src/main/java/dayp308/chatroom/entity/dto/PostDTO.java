package dayp308.chatroom.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.apache.commons.io.FilenameUtils;
import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class PostDTO {
    private Long id;
    private Long publishUserId;
    private String postTitle;
    private String postContent;
    private Integer postCategoryId;
    private Byte postStatus;
    private Long postViews;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Instant lastUpdateTime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Instant createTime;
    private String postTags;
    public PostDTO(Long userId, Integer categoryId, String title, String content) {
        this.publishUserId = userId;
        this.postCategoryId = categoryId;
        this.postTitle = title;
        this.postContent = content;
    }
}
