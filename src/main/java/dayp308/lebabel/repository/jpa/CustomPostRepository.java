package dayp308.lebabel.repository.jpa;

import dayp308.lebabel.bean.entity.category.PostCategory;
import dayp308.lebabel.bean.entity.user.User;
import dayp308.lebabel.bean.entity.projection.PostProjection;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;

public interface CustomPostRepository {
    PostProjection findProjById(long id, @Nullable User user);

    Slice<PostProjection> findAllProj(
            @Nullable PostCategory category,
            @Nullable User user,
            @Nullable User targetUser,
            @Nullable String keyword,
            boolean visibleOnly,
            Pageable pageable
    );

    List<PostProjection> findAllProjByIds(List<Long> ids);

}
