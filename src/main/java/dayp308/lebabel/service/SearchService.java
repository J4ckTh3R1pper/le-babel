package dayp308.lebabel.service;

import java.util.List;

import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dayp308.lebabel.entity.post.Post;
import dayp308.lebabel.entity.post.Post_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class SearchService {

    @PersistenceContext
    EntityManager em;

    public List<Post> searchPost(String keyword) {
        SearchSession searchSession = Search.session( em ); 

        SearchResult<Post> result = searchSession.search( Post.class ) 
            .where( f -> f.match() 
            .fields( Post_.TITLE, Post_.CONTENT )
            .matching( keyword ) )
            .fetch( 15 ); 

        long totalHitCount = result.total().hitCount();
        return result.hits();
    }
    
}
