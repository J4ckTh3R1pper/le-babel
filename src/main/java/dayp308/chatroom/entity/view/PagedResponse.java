package dayp308.chatroom.entity.view;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class PagedResponse<T> {
    protected List<T> content = new ArrayList<>();
    protected boolean hasNext;
    protected boolean hasPrevious;
    protected int pageNum;
    protected int pageSize;
}
