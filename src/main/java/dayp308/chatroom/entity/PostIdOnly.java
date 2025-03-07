package dayp308.chatroom.entity;

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
