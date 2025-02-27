package dayp308.chatroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.configuration.WebMvcConfiguration;
import dayp308.chatroom.controller.AuthController;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.business.LoginForm;
import dayp308.chatroom.entity.business.UserRegistrationForm;
import dayp308.chatroom.entity.dto.UserDTO;
import dayp308.chatroom.repository.UserRepository;
import dayp308.chatroom.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testLogin() {
        UserRegistrationForm form = new UserRegistrationForm();
        String password = RandomString.make(72);
        form.setLoginName(RandomString.make(8) + "@example.com");
        form.setNickName(RandomString.make(8));
        form.setPassword(password);
        UserDTO dto = userService.register(form);
        User user = userRepository.getReferenceById(dto.getId());

        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/login")
                            .content(objectMapper.writeValueAsString(
                                    new LoginForm(dto.getLoginName(), password, false)
                                    )
                            )
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().is2xxSuccessful()).andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            userRepository.deleteById(dto.getId());
        }
    }
}
