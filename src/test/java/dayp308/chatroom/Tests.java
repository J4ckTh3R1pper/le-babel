package dayp308.chatroom;

import dayp308.chatroom.entity.dto.UserDTO;
import dayp308.chatroom.exception.InvalidRegistrationFormException;
import dayp308.chatroom.repository.UserRepository;
import dayp308.chatroom.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

class Tests {

    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;


    @ParameterizedTest
    @CsvSource({"qq", ".com"})
    public void searchUserByLoginName(String loginName) {
        System.out.println(userRepository.findByLoginName(loginName));
    }

    @ParameterizedTest
    @CsvSource({"coder", "s"})
    public void searchUserByNickName(String nickName) {
        System.out.println(userService.findByNickName(nickName));
    }

    @ParameterizedTest
    @CsvSource({"spidey5852@foxmail.com, e10adc3949ba59abbe56e057f20f883e", "123456@example.com, password"})
    public void testLogin(String loginName, String password) {
        System.out.println(userService.checkLogin(loginName, password));
    }

    @Test
    public void testRegister() throws InvalidRegistrationFormException {
        UserDTO userDTO = new UserDTO();
        userDTO.setLoginName(RandomString.make(8) + "@example.com");
        userDTO.setNickName(RandomString.make(8));
        String passwordMD5 = RandomString.make(8);
        System.out.println(userService.register(userDTO, passwordMD5));
    }
}
