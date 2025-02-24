package dayp308.chatroom.converter;

import dayp308.chatroom.entity.CategoryMember;
import dayp308.chatroom.entity.dto.CategoryMemberDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryMemberDTOConverter implements Converter<CategoryMember, CategoryMemberDTO> {
    @Override
    public CategoryMemberDTO convert(CategoryMember source) {
        CategoryMemberDTO target = new CategoryMemberDTO();
        target.setCategoryId(source.getCategory().getId());
        target.setUserId(source.getUser().getId());
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
