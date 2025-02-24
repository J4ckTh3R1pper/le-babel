package dayp308.chatroom.converter;

import dayp308.chatroom.entity.Post;
import dayp308.chatroom.entity.PostCategory;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.dto.PostDTO;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostDtoToPostConverter implements Converter<PostDTO, Post> {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public PostDtoToPostConverter(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Post convert(PostDTO source) {
        User user = userRepository.findById(source.getPublishUserId()).orElseThrow();
        PostCategory postCategory = categoryRepository.findById(source.getPostCategoryId()).orElseThrow();
        Post target = new Post();
        BeanUtils.copyProperties(source, target);
        target.setPublishUser(user);
        target.setPostCategory(postCategory);
        return target;
    }
}
