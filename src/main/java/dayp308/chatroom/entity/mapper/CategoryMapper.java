package dayp308.chatroom.entity.mapper;

import dayp308.chatroom.entity.PostCategory;
import dayp308.chatroom.entity.dto.CategoryDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends Converter<PostCategory, CategoryDTO> {
    @Override
    CategoryDTO convert(PostCategory source);

    @InheritInverseConfiguration
    @DelegatingConverter
//    @Mapping(target = "posts", ignore = true)
//    @Mapping(target = "categoryMembers", ignore = true)
//    @Mapping(target = "chatChannels", ignore = true)
    PostCategory inverseConvert(CategoryDTO source);

}
