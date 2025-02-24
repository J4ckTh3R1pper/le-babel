package dayp308.chatroom.repository;

import dayp308.chatroom.entity.User;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> findByNickName(String nickName);
}
