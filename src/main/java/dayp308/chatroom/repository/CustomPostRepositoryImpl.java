package dayp308.chatroom.repository;

import dayp308.chatroom.entity.category.PostCategory;
import dayp308.chatroom.entity.category.PostCategory_;
import dayp308.chatroom.entity.comment.PostComment;
import dayp308.chatroom.entity.post.Post;
import dayp308.chatroom.entity.post.Post_;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.entity.user.User_;
import dayp308.chatroom.repository.projection.PostProjection;
import dayp308.chatroom.util.QueryUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dayp308.chatroom.repository.specification.PostSpecs.categoryIdEquals;
import static dayp308.chatroom.repository.specification.PostSpecs.isVisible;

public class CustomPostRepositoryImpl implements CustomPostRepository {

    @PersistenceContext
    private EntityManager em;

    private static void buildQuery(
            Root<Post> root, CriteriaQuery<PostProjection> cq, CriteriaBuilder cb, User user,
            Selection<?>... additionalSelect)
    {
        Join<Post, User> userJoin = root.join(Post_.USER, JoinType.LEFT);
        Join<Post, PostCategory> categoryJoin = root.join(Post_.CATEGORY, JoinType.LEFT);

        Join<Post, User> currentUserLikeJoin = null;
        if (user != null) {
            currentUserLikeJoin = root.join(Post_.USERS_LIKED, JoinType.LEFT);
            currentUserLikeJoin.on(
                    cb.equal(currentUserLikeJoin.get(User_.ID), user.getId())
            );
        }
        Expression<Object> liked = cb.selectCase().when(
                currentUserLikeJoin == null ? cb.disjunction()
                        : cb.isNotNull(currentUserLikeJoin.get(User_.ID)), Boolean.TRUE
        ).otherwise(Boolean.FALSE);

        Selection<?>[] selections = new Selection[]{
                root.get(Post_.ID).alias("id"),
                userJoin.get(User_.ID).alias("userId"),
                root.get(Post_.TITLE).alias("title"),
                root.get(Post_.CONTENT).alias("content"),
                categoryJoin.get(PostCategory_.ID).alias("categoryId"),
                root.get(Post_.CREATE_TIME).alias("createTime"),
                root.get(Post_.LAST_UPDATE_TIME).alias("lastUpdateTime"),
                root.get(Post_.TAGS).alias("tags"),
                root.get(Post_.VIEW_COUNT).alias("viewCount"),
                liked.alias("liked"),
        };

        List<Selection<?>> selectionList = new ArrayList<>(Arrays.asList(selections));
        selectionList.addAll(Arrays.asList(additionalSelect));
        Selection<?>[] finalSelections = selectionList.toArray(selectionList.toArray(new Selection[ selections.length + additionalSelect.length ]));
        cq.select(cb.construct(PostProjection.class, finalSelections));
    }

    @Override
    public PostProjection findProjById(long id, @Nullable User user) throws NoResultException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PostProjection> cq = cb.createQuery(PostProjection.class);
        Root<Post> root = cq.from(Post.class);
        Selection<Long> likeCount = cb.countDistinct(
                root.join(Post_.USERS_LIKED, JoinType.LEFT)
        ).alias("likeCount");
        Selection<Long> commentCount = cb.countDistinct(
                root.join(Post_.COMMENTS, JoinType.LEFT)
        ).alias("commentCount");
        buildQuery(root, cq, cb, user, likeCount, commentCount);
        cq.where(cb.equal(root.get(Post_.ID), id)).groupBy(root.get(Post_.ID));
        return em.createQuery(cq).getSingleResult();
    }

    @Override
    public Slice<PostProjection> findAllProjById(
            @Nullable Integer categoryId,
            @Nullable User user,
            boolean visibleOnly,
            Pageable pageable
            ) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PostProjection> cq = cb.createQuery(PostProjection.class);
        Root<Post> root = cq.from(Post.class);
        var id = root.get(Post_.ID);
        Join<Post, User> likeJoin = root.join(Post_.USERS_LIKED, JoinType.LEFT);
        Join<Post, PostComment> commentJoin = root.join(Post_.COMMENTS, JoinType.LEFT);

        Expression<Long> likeCount = cb.countDistinct(likeJoin);
        Expression<Long> commentCount = cb.countDistinct(commentJoin);

        buildQuery(root, cq, cb, user,
                likeCount.alias("likeCount"),
                commentCount.alias("commentCount")
        );

        // 拼接WHERE条件
        Predicate p = cb.conjunction();
        if ( categoryId != null )
            p = cb.and(p, categoryIdEquals(root, cb, categoryId));
        if ( visibleOnly )
            p = cb.and(p, isVisible(root, cb));

        cq.where(p).groupBy(root.get(Post_.ID));

        // 获取排序
        if (pageable.getSort().isSorted()) {
            List<Order> order = new ArrayList<>();
            try {
                List<Order> finalOrder = new ArrayList<>();
                pageable.getSort().stream().forEach(o -> {
                    switch (o.getProperty()) {
                        case Post_.ID:
                        case Post_.LAST_UPDATE_TIME:
                        case Post_.CREATE_TIME:
                        case Post_.VIEW_COUNT: finalOrder.add(
                                QueryUtil.getOrder(o, cb, root.get(o.getProperty()))
                            ); break;
                        case "likeCount": finalOrder.add(QueryUtil.getOrder(o, cb, likeCount)); break;
                        case "commentCount": finalOrder.add(QueryUtil.getOrder(o, cb, commentCount)); break;
                    }
                });
                if (finalOrder.isEmpty())
                    throw new IllegalStateException("Invalid order, falling back to default");
                order = finalOrder;
            } catch (Exception e) {
                order = List.of(new Order[]{cb.desc(id)});
            }
            finally { cq.orderBy(order) ;}
        } else cq.orderBy(cb.desc(id));

        // 创建动态查询
        TypedQuery<PostProjection> query = em.createQuery(cq);
        // 分页
        int pageSize = 0;
        if (pageable.isPaged()) {
            pageSize = pageable.getPageSize();
            query.setMaxResults(pageable.getPageSize() + 1);
            query.setFirstResult((int) pageable.getOffset());
        }
        List<PostProjection> resultList = query.getResultList();
        boolean hasNext = pageable.isPaged() && resultList.size() > pageSize;

        return new SliceImpl<>( hasNext ? resultList.subList(0, pageSize) : resultList, pageable, hasNext);
    }
}
