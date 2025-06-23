package dayp308.lebabel.listener;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dayp308.lebabel.bean.entity.post.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
@Transactional
public class ApplicationStartup {

    @PersistenceContext
    EntityManager em;

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        SearchSession searchSession = Search.session( em ); 

        MassIndexer indexer = searchSession.massIndexer( Post.class ) 
        .threadsToLoadObjects( 4 ); 

        try {
            indexer.startAndWait();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
}