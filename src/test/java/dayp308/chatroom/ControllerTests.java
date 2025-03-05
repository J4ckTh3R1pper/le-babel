package dayp308.chatroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.business.UserRegistrationForm;
import dayp308.chatroom.entity.dto.CategoryDTO;
import dayp308.chatroom.entity.dto.CategoryMemberDTO;
import dayp308.chatroom.entity.dto.UserDTO;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.UserRepository;
import dayp308.chatroom.service.CategoryMemberService;
import dayp308.chatroom.service.UserService;
import jakarta.servlet.http.Cookie;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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

    UserDTO randomUser( String password ) {
        UserRegistrationForm form = new UserRegistrationForm();
        form.setLoginName(RandomString.make(8) + "@example.com");
        form.setNickName(RandomString.make(8));
        form.setPassword(password);
        return userService.register(form);
    }

    @Test
    public void testUnlogin() {
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
        CategoryDTO cDto = new CategoryDTO();
        cDto.setId(6);
        String password = RandomString.make(72);
        UserDTO uDto = randomUser(password);

        String password2 = RandomString.make(72);
        UserDTO uDto2 = randomUser(password2);

        CategoryMemberDTO memberDTO = categoryMemberService.addMember(cDto, uDto, Role.SUBSCRIBER);
        memberDTO.setExperience(5000);
        CategoryMemberDTO memberDTO2 = categoryMemberService.addMember(cDto, uDto2, Role.SUBSCRIBER);
        memberDTO2.setExperience(4000);

        categoryMemberService.updateMember(memberDTO);
        categoryMemberService.updateMember(memberDTO2);

        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/no_auth/user/get_overall_exp")
                            .param("userId", uDto.getId().toString())
            ).andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            categoryMemberRepository.deleteById(new CategoryMemberId(cDto.getId(), uDto.getId()));
            userRepository.deleteById(uDto.getId());

            categoryMemberRepository.deleteById(new CategoryMemberId(cDto.getId(), uDto2.getId()));
            userRepository.deleteById(uDto2.getId());
        }
    }

    @Test
    public void testLogin() {
        String password = RandomString.make(72);
        UserDTO uDto = randomUser(password);

        CategoryDTO cDto = new CategoryDTO();
        cDto.setId(6);
        CategoryMemberDTO mDto = null;
        final Cookie[][] cookies = new Cookie[1][1];
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/login")
                            .formField("loginName", uDto.getLoginName())
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

            mDto = categoryMemberService.addMember(cDto, uDto, Role.SUBSCRIBER);

            mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/test_moderator")
                            .param("categoryId", cDto.getId().toString())
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
            if ( mDto != null ) categoryMemberRepository.deleteById(new CategoryMemberId(6, uDto.getId()));
            userRepository.deleteById(uDto.getId());
        }
    }

    @Test
    public void testMembership() {
        String password = RandomString.make(72);
        UserDTO uDTO = randomUser(password);

    }
}
