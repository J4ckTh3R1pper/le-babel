package dayp308.chatroom.repository;

import dayp308.chatroom.entity.member.CategoryMember;
import dayp308.chatroom.entity.member.CategoryMemberDTO;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.entity.id.CategoryMemberId;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryMemberRepository extends JpaRepository<CategoryMember, CategoryMemberId> {
    List<CategoryMember> findAll(Specification<CategoryMember> categoryMember);
    List<CategoryMemberDTO> findAllByCategoryId(Integer categoryId);

    Optional<CategoryMember> findByUser(User user);
    <T> Optional<T> findById(CategoryMemberId categoryMemberId, Class<T> clazz);
}
