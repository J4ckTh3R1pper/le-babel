package dayp308.lebabel.repository.specification;

import dayp308.lebabel.bean.entity.*;
import dayp308.lebabel.bean.entity.member.CategoryMember;
import dayp308.lebabel.bean.entity.member.CategoryMember_;
import dayp308.lebabel.bean.entity.user.Subscription_;
import dayp308.lebabel.bean.entity.user.User;
import dayp308.lebabel.bean.entity.user.User_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecs {

    public static Specification<User> detailedProjection(Long id) {
        return (root, query, builder) -> {
            if (query == null) query = builder.createQuery();
            Join<User, Long> subscriber = root.join(Subscription_.USER);
            query.multiselect(
                    root.get(User_.ID),
                    root.get(User_.NICK_NAME),
                    root.get(User_.HEAD_IMG_URL),
                    root.get(User_.CREATE_TIME),
                    root.get(User_.GENDER),
                    root.get(User_.LOCATION),
                    root.get(User_.INTRODUCE),
                    root.get(User_.LAST_LOGIN_TIME),
                    builder.count(builder.selectCase()
                            .when(builder.equal(subscriber.get(User_.ID), id), 1L
                            )))
                    .where(builder.equal(subscriber.get(User_.ID), id));
            return query.getRestriction();
        };
    }

    public static Specification<CategoryMember> userMemberships(User user) {
        return (root, query, builder) -> {
            long userId = user.getId();
            return builder.equal(root.join(CategoryMember_.USER).get(User_.ID), userId);
        };
    }

}
