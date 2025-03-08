package dayp308.chatroom.repository;


import dayp308.chatroom.entity.category.PostCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Function;

@Repository
public interface CategoryRepository extends JpaRepository<PostCategory, Integer>, JpaSpecificationExecutor<PostCategory> {
    List<PostCategory> findByNameContainsIgnoreCase(String searchKey);

}
