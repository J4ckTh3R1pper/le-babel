package dayp308.chatroom.converter;

import dayp308.chatroom.entity.Post;
import dayp308.chatroom.entity.dto.PostDTO;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostToDtoConverter implements Converter<Post, PostDTO> {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public PostToDtoConverter(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDTO convert(Post source) {
        PostDTO target = new PostDTO();
        target.setPublishUserId(source.getPublishUser().getId());
        target.setPostCategoryId(source.getPostCategory().getId());
        BeanUtils.copyProperties(source, target);
        return target;
    }

}
