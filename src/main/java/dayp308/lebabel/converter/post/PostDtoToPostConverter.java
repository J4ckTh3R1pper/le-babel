package dayp308.lebabel.converter.post;

import dayp308.lebabel.entity.post.Post;
import dayp308.lebabel.entity.category.PostCategory;
import dayp308.lebabel.entity.user.User;
import dayp308.lebabel.entity.post.PostDTO;
import dayp308.lebabel.repository.CategoryRepository;
import dayp308.lebabel.repository.UserRepository;
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
        User user = userRepository.findById(source.getUser().getId()).orElseThrow();
        PostCategory postCategory = categoryRepository.findById(source.getCategory().getId()).orElseThrow();
        Post target = new Post();
        BeanUtils.copyProperties(source, target);
        target.setUser(user);
        target.setCategory(postCategory);
        return target;
    }
}
