package dayp308.chatroom.converter;

import dayp308.chatroom.entity.Post;
import dayp308.chatroom.entity.PostCategory;
import dayp308.chatroom.entity.business.PostForm;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.UserRepository;
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
        Post target = new Post();
        target.setCategory(category);
        target.setTitle(source.getTitle());
        target.setContent(source.getContent());
        return target;
    }
}
