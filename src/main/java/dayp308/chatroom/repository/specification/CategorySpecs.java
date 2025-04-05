package dayp308.chatroom.repository.specification;

import dayp308.chatroom.entity.member.CategoryMember_;
import dayp308.chatroom.entity.category.PostCategory_;
import dayp308.chatroom.entity.user.User_;
import dayp308.chatroom.entity.category.PostCategory;
import dayp308.chatroom.entity.member.CategoryMember;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.util.QueryUtil;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CategorySpecs {
    public static Specification<PostCategory> getAllCategories(User user, Sort sort) {
        return (root, cq, cb) -> {
            if (cq == null) cq = cb.createTupleQuery();
            Join<PostCategory, CategoryMember> m = root.join(PostCategory_.CATEGORY_MEMBERS, JoinType.LEFT);
            Path<Integer> rank = root.get(PostCategory_.RANK);

            Predicate where = cb.conjunction();
            List<Order> orders = new ArrayList<>();
            sort.stream().forEach(order -> {
                switch (order.getProperty()) {
                    case CategoryMember_.EXPERIENCE:
                    case CategoryMember_.JOIN_DATE:
                        if (user != null) orders.add(QueryUtil.getOrder(order, cb, m.get(order.getProperty()))); break;
                    case PostCategory_.RANK: orders.add(QueryUtil.getOrder(order, cb, rank)); break;
                }
            });
            if (orders.isEmpty()) orders.add(cb.desc(rank));
            cq.orderBy(orders);
            if (user != null)
                where = cb.equal(m.join(CategoryMember_.USER).get(User_.ID), user.getId());
            return where;
        };
    }
}
