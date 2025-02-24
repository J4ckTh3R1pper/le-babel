package dayp308.chatroom.entity.mapper;

import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.dto.UserDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends Converter<User, UserDTO> {
    @Override
    UserDTO convert(User source);

    @InheritInverseConfiguration
    @DelegatingConverter
    @Mapping(target = "passwordMd5", ignore = true)
    @Mapping(target = "categoriesMember", ignore = true)
    @Mapping(target = "chatMessagesPrivate", ignore = true)
    @Mapping(target = "receivedChatMessagesPrivate", ignore = true)
    @Mapping(target = "chatMessagesPublic", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "bookmarkedPosts", ignore = true)
    @Mapping(target = "bookmarkedComments", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "token", ignore = true)
    User inverseConvert(UserDTO source);
}
