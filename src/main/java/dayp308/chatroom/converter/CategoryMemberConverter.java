package dayp308.chatroom.converter;

import dayp308.chatroom.entity.CategoryMember;
import dayp308.chatroom.entity.CategoryMemberId;
import dayp308.chatroom.entity.dto.CategoryMemberDTO;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryMemberConverter implements Converter<CategoryMemberDTO, CategoryMember> {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public CategoryMemberConverter(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CategoryMember convert(CategoryMemberDTO source) {
        CategoryMember categoryMember = new CategoryMember();
        categoryMember.setCategory(categoryRepository.getReferenceById(source.getCategoryId()));
        categoryMember.setUser(userRepository.getReferenceById(source.getUserId()));
        BeanUtils.copyProperties(source, categoryMember);
        return categoryMember;
    }
}
