package dayp308.chatroom.entity.post;

public interface PostIdOnly {
    Long getId();
    User getPublishUser();
    Category getCategory();

    interface User {
        Long getId();
    }
    interface Category {
        Integer getId();
    }
}
