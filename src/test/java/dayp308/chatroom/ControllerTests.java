package dayp308.chatroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.entity.CategoryMember;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.business.UserRegistrationForm;
import dayp308.chatroom.entity.CategoryMemberDTO;
import dayp308.chatroom.entity.UserDTO;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.UserRepository;
import dayp308.chatroom.service.CategoryMemberService;
import dayp308.chatroom.service.UserService;
import jakarta.servlet.http.Cookie;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private CategoryMemberService categoryMemberService;
    @Autowired
    private CategoryMemberRepository categoryMemberRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    UserDTO randomUser( String password ) {
        UserRegistrationForm form = new UserRegistrationForm();
        form.setLoginName(RandomString.make(8) + "@example.com");
        form.setNickName(RandomString.make(8));
        form.setPassword(password);
        long id = userService.register(form);
        return userRepository.findById(id, UserDTO.class);
    }

    @Test
    public void testUnLogin() {
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/test_moderator")
                            .param("categoryId", "5")
            ).andExpect(status().is(401));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExp() {
    }

    @Test
    public void testLogin() {
        String password = RandomString.make(72);
        UserDTO uDto = randomUser(password);

        CategoryMemberId mDto = categoryMemberService.addMember(
                categoryRepository.getReferenceById(6),
                userRepository.getReferenceById(uDto.id()),
                null
        );
        final Cookie[][] cookies = new Cookie[1][1];
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/login")
                            .formField("loginName", uDto.loginName())
                            .formField("password", password)
                            .formField("remember-me", "true")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().is2xxSuccessful())
                    .andDo(result -> cookies[0] = result.getResponse().getCookies())
                    .andDo(print());

            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/omgwtfbbq")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .cookie(cookies[0])
            ).andExpect(status().is2xxSuccessful())
                    .andDo(result -> cookies[0] = result.getResponse().getCookies())
                    .andDo(print());

            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/test_moderator")
                            .param("categoryId", mDto.getCategoryId().toString())
                            .cookie(cookies[0])
            ).andExpect(status().is2xxSuccessful())
                    .andDo(result -> cookies[0] = result.getResponse().getCookies())
                    .andDo(print());

            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/test_moderator")
                            .param("categoryId", "5")
                            .cookie(cookies[0])
            ).andExpect(status().is4xxClientError()).andDo(print());

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if ( mDto != null ) categoryMemberRepository.deleteById(mDto);
            userRepository.deleteById(uDto.id());
        }
    }

    @Test
    public void testPostList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/no_auth/post/get_posts")
                        .param("categoryId", "1")
                        .param("pageNum", "0")
        ).andExpect(status().is2xxSuccessful()).andDo(print());
    }
}
