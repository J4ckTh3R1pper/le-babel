package dayp308.chatroom.converter;

import dayp308.chatroom.entity.Post;
import dayp308.chatroom.entity.PostCategory;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.business.PostForm;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FormPostConverter implements Converter<PostForm, Post> {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public FormPostConverter(UserRepository userRepository, CategoryRepository categoryRepository) {

        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Post convert(PostForm source) {
        PostCategory category = categoryRepository.findById(source.getCategoryId()).orElseThrow();
        User user = userRepository.findByToken(source.getToken()).orElseThrow();
        Post target = new Post();
        target.setPostCategory(category);
        target.setPostTitle(source.getTitle());
        target.setPostContent(source.getContent());
        target.setPublishUser(user);
        return target;
    }
}
