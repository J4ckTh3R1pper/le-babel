package dayp308.chatroom.repository;

import dayp308.chatroom.entity.CategoryMember;
import dayp308.chatroom.entity.id.CategoryMemberId;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMemberRepository extends JpaRepository<CategoryMember, CategoryMemberId> {
    List<CategoryMember> findAll(Specification<CategoryMember> categoryMember);
}
