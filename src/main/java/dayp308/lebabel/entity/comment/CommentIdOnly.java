package dayp308.lebabel.entity.comment;

public interface CommentIdOnly {
    Long getId();
    Post getPost();
    User getUser();
    interface Post {
        Long getId();
        User getUser();
        Category getCategory();
    }
    interface User {
        Long getId();
    }
    interface Category {
        Long getId();
    }
}
