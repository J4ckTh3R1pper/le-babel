package dayp308.chatroom.entity.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayp308.chatroom.entity.category.PostCategory_;
import dayp308.chatroom.entity.member.CategoryMember_;
import lombok.Getter;

@Getter
public enum CategoryOrderType {
    @JsonProperty(CategoryMember_.JOIN_DATE)
    JOIN_DATE(CategoryMember_.JOIN_DATE),

    @JsonProperty(CategoryMember_.EXPERIENCE)
    EXPERIENCE(CategoryMember_.EXPERIENCE),

    @JsonProperty(PostCategory_.RANK)
    RANK(PostCategory_.RANK);

    private final String column;

    CategoryOrderType(String column) {
        this.column = column;
    }

}
