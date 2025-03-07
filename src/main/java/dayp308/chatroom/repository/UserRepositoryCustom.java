package dayp308.chatroom.repository;

import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.UserDetailedProj;

import java.util.List;

public interface UserRepositoryCustom {

    UserDetailedProj getUserDetailedProj(Long id);
}
