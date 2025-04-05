package dayp308.chatroom.repository;

import dayp308.chatroom.entity.comment.PostComment;
import dayp308.chatroom.entity.comment.PostComment_;
import dayp308.chatroom.entity.enums.CommentOrderType;
import dayp308.chatroom.entity.post.Post;
import dayp308.chatroom.entity.post.Post_;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.entity.user.User_;
import dayp308.chatroom.repository.projection.CommentProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.support.PageableUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CustomCommentRepositoryImpl implements CustomCommentRepository {
    @PersistenceContext
    EntityManager em;

    private static void buildQuery(
            Root<PostComment> root, CriteriaBuilder cb, CriteriaQuery<CommentProjection> cq, User user,
            Selection<?>... additionalSelect
    ) {
        Join<PostComment, User> userJoin = root.join(PostComment_.USER, JoinType.LEFT);
        Join<PostComment, Post> postJoin = root.join(PostComment_.POST, JoinType.LEFT);
        Join<PostComment, PostComment> parentJoin = root.join(PostComment_.PARENT_COMMENT, JoinType.LEFT);

        Join<PostComment, User> currentUserLikeJoin = null;
        if (user != null) {
            currentUserLikeJoin = root.join(PostComment_.USERS_LIKED, JoinType.LEFT);
            currentUserLikeJoin.on(cb.equal(currentUserLikeJoin.get(User_.ID), user.getId()));
        }

        Expression<Object> liked = cb.selectCase().when(
                currentUserLikeJoin == null ? cb.disjunction()
                        : cb.isNotNull(currentUserLikeJoin.get(User_.ID)), Boolean.TRUE
        ).otherwise(Boolean.FALSE);

        Selection<?>[] selections = new Selection[] {
                root.get(PostComment_.ID).alias("id"),
                postJoin.get(Post_.ID).alias("postId"),
                userJoin.get(User_.ID).alias("userId"),
                root.get(PostComment_.COMMENT_BODY).alias("commentBody"),
                root.get(PostComment_.CREATE_TIME).alias("createTime"),
                root.get(PostComment_.IS_DELETED).alias("isDeleted"),
                parentJoin.get(PostComment_.ID).alias("parentCommentId"),
                liked.alias("liked")

        };
        List<Selection<?>> selectionList = new ArrayList<>(Arrays.asList(selections));
        selectionList.addAll(Arrays.asList(additionalSelect));
        Selection<?>[] finalSelections = selectionList.toArray(selectionList.toArray(new Selection[ selections.length + additionalSelect.length ]));
        cq.select(cb.construct(CommentProjection.class, finalSelections));
    }

    @Override
    public CommentProjection findProjById(long id, User user) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CommentProjection> cq = cb.createQuery(CommentProjection.class);
        Root<PostComment> root = cq.from(PostComment.class);
        Join<PostComment, User> likeJoin = root.join(PostComment_.USERS_LIKED, JoinType.LEFT);
        Expression<Long> likeCount = cb.countDistinct(likeJoin);
        buildQuery(root, cb, cq, user,
                likeCount.alias("likeCount")
        );
        cq.where(cb.equal(root.get(PostComment_.ID), id)).groupBy(root.get(PostComment_.ID));
        return em.createQuery(cq).getSingleResult();
    }

    @Override
    public Slice<CommentProjection> findAllProjByPostId(long postId, User user, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CommentProjection> cq = cb.createQuery(CommentProjection.class);
        Root<PostComment> root = cq.from(PostComment.class);

        Join<PostComment, User> likeJoin = root.join(PostComment_.USERS_LIKED, JoinType.LEFT);
        Expression<Long> likeCount = cb.countDistinct(likeJoin);

        buildQuery(root, cb, cq, user,
                likeCount.alias("likeCount")
        );
        cq.where(
                cb.and(
                        cb.equal(root.join(PostComment_.POST).get(Post_.ID), postId),
                        cb.isNull(root.join(PostComment_.PARENT_COMMENT))
                )
        ).groupBy(root.get(PostComment_.ID));

        List<Order> orders = QueryUtils.toOrders(
                pageable.getSortOr(
                        Sort.by("id").descending()), root, cb);
        cq.orderBy(orders);

        TypedQuery<CommentProjection> query = em.createQuery(cq);
        int pageSize = 0;
        if (pageable.isPaged()) {
            pageSize = pageable.getPageSize();
            query.setMaxResults(pageable.getPageSize() + 1);
            query.setFirstResult((int) pageable.getOffset());
        }
        List<CommentProjection> resultList = query.getResultList().subList(0, pageSize);
        boolean hasNext = pageable.isPaged() && resultList.size() > pageSize;
        return new SliceImpl<>(resultList, pageable, hasNext);
    }

    @Override
    public List<CommentProjection> findAllProjByCommentId(long commentId, User user, int limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CommentProjection> cq = cb.createQuery(CommentProjection.class);
        Root<PostComment> root = cq.from(PostComment.class);
        Join<PostComment, User> likeJoin = root.join(PostComment_.USERS_LIKED, JoinType.LEFT);
        Expression<Long> likeCount = cb.countDistinct(likeJoin);

        buildQuery(root, cb, cq, user,
                likeCount.alias("likeCount")
        );
        cq.where(cb.equal(root.join(PostComment_.PARENT_COMMENT).get(PostComment_.ID), commentId))
                .groupBy(root.get(PostComment_.ID)).orderBy(cb.desc(root.get(PostComment_.ID)));
        TypedQuery<CommentProjection> query = em.createQuery(cq);
        if (limit > 0)
            query.setMaxResults(limit);
        return query.getResultList();
    }
}
