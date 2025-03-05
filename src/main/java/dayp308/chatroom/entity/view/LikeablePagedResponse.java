package dayp308.chatroom.entity.view;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class LikeablePagedResponse<T> extends PagedResponse<T> {
    private Set<Long> likedIds;
}
