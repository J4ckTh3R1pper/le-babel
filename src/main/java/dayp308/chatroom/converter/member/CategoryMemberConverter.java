package dayp308.chatroom.converter.member;

import dayp308.chatroom.entity.member.CategoryMember;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.member.CategoryMemberDTO;
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
        CategoryMember target = new CategoryMember();
        target.setCategory(categoryRepository.getReferenceById(source.getCategory().getId()));
        target.setUser(userRepository.getReferenceById(source.getUser().getId()));
        target.setId(new CategoryMemberId(source.getCategory().getId(), source.getUser().getId()));
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
