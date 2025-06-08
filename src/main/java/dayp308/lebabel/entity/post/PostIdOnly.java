package dayp308.lebabel.entity.post;

public interface PostIdOnly {
    Long getId();
    User getUser();
    Category getCategory();

    interface User {
        Long getId();
    }
    interface Category {
        Integer getId();
    }
}
