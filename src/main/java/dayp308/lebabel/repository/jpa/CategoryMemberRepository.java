package dayp308.lebabel.repository.jpa;

import dayp308.lebabel.bean.entity.member.CategoryMember;
import dayp308.lebabel.bean.entity.member.CategoryMemberDTO;
import dayp308.lebabel.bean.entity.user.User;
import dayp308.lebabel.bean.entity.id.CategoryMemberId;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryMemberRepository extends JpaRepository<CategoryMember, CategoryMemberId> {
    List<CategoryMember> findAll(Specification<CategoryMember> categoryMember);
    List<CategoryMemberDTO> findAllByCategoryId(Integer categoryId);

    Optional<CategoryMember> findByUser(User user);
    <T> T findById(CategoryMemberId categoryMemberId, Class<T> clazz);

    @Query("""
        SELECT c.id
        FROM CategoryMember m
        LEFT JOIN m.user as u
        LEFT JOIN m.category AS c
        WHERE u.id = :userId AND m.role <> dayp308.lebabel.enumeration.Role.GUEST
    """)
    List<Integer> findCategoryIdsByJoinUserId(@Param("userId") long userId);
}
