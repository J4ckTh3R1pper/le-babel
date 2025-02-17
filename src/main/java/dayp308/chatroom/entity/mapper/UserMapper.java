package dayp308.chatroom.entity.mapper;

import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends Converter<User, UserDTO> {
    @Override
    UserDTO convert(User source);
}
