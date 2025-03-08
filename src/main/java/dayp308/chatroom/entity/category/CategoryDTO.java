package dayp308.chatroom.entity.category;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

public record CategoryDTO(Integer id, String name, Integer rank, Boolean isDeleted, Boolean isMuted,
                          Boolean isPending,
                          @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
                          @DateTimeFormat(pattern = "yyyy-MM-dd")
                          Instant createTime,
                          String avatar, String info, String rule) {
}
