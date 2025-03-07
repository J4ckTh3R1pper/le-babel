package dayp308.chatroom.repository;


import dayp308.chatroom.entity.Post;
import dayp308.chatroom.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<PostCategory, Integer> {
    List<PostCategory> findByNameContainsIgnoreCase(String searchKey);

}
