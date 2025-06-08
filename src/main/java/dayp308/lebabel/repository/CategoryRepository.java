package dayp308.lebabel.repository;


import dayp308.lebabel.entity.category.CategoryProjection;
import dayp308.lebabel.entity.category.PostCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<PostCategory, Integer>, JpaSpecificationExecutor<PostCategory> {
    List<PostCategory> findByNameContainsIgnoreCase(String searchKey);
    <T> List<T> findBy(Pageable pageable, Class<T> clazz);
    <T> T findById(Integer id, Class<T> clazz);

    @Query("""
        SELECT new dayp308.lebabel.entity.category.CategoryProjection(
            c.id,
            c.name,
            c.createTime,
            c.avatar,
            c.info,
            c.rule,
            count(m)
        )
        FROM PostCategory AS c
        LEFT JOIN CategoryMember AS m ON m.category = c AND m.role <> Role.GUEST
        WHERE c.id = :id
    """)
    CategoryProjection findProjectionById(@Param("id") int id);
}
