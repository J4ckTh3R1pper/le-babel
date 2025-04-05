package dayp308.chatroom.repository;

import dayp308.chatroom.entity.enums.PostOrderType;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.repository.projection.PostProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;

public interface CustomPostRepository {
    PostProjection findProjById(long id, @Nullable User user);

    Slice<PostProjection> findAllProjById(
            @Nullable Integer categoryId,
            @Nullable User user,
            boolean visibleOnly,
            Pageable pageable
    );
}
