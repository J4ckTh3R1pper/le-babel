package dayp308.chatroom.repository;

import dayp308.chatroom.entity.CategoryMember;
import dayp308.chatroom.entity.CategoryMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMemberRepository extends JpaRepository<CategoryMember, CategoryMemberId> {
}
