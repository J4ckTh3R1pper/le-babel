package dayp308.lebabel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.captcha.SpecCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import dayp308.lebabel.bean.entity.id.CategoryMemberId;
import dayp308.lebabel.bean.ao.UserRegistrationForm;
import dayp308.lebabel.bean.entity.post.Post_;
import dayp308.lebabel.bean.entity.user.UserDTO;
import dayp308.lebabel.repository.jpa.CategoryMemberRepository;
import dayp308.lebabel.repository.jpa.CategoryRepository;
import dayp308.lebabel.repository.redis.RedisCaptchaRepository;
import dayp308.lebabel.repository.jpa.UserRepository;
import dayp308.lebabel.service.CategoryMemberService;
import dayp308.lebabel.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
    private CategoryMemberService categoryMemberService;
    @Autowired
    private CategoryMemberRepository categoryMemberRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ObjectMapper jacksonObjectMapper;
    @Autowired
    private RedisCaptchaRepository redisCaptchaRepository;

    Map.Entry<String, String> mockCaptcha() {
        Captcha captcha = new SpecCaptcha();
        String uuid = redisCaptchaRepository.saveCaptcha(captcha);
        return Map.entry(uuid, captcha.text());
    }

    UserDTO randomUser( String password ) {
        UserRegistrationForm form = new UserRegistrationForm();
        form.setLoginName(RandomString.make(8) + "@example.com");
        form.setNickName(RandomString.make(8));
        form.setPassword(password);
        long id = userService.register(form);
        return userRepository.findById(id, UserDTO.class).orElseThrow();
    }

    String formLoginAndGetToken(UserDTO user, String password) throws Exception {
        return formLoginAndGetToken(user.loginName(), password);
    }

    String formLoginAndGetToken(String loginName, String password) throws Exception {
        Map.Entry<String, String> captcha = mockCaptcha();
        AtomicReference<String> token =  new AtomicReference<>();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .formField("loginName", loginName)
                        .formField("password", password)
                        .formField("captcha", captcha.getValue())
                        .formField("uuid", captcha.getKey())
                        .formField("remember-me", "true")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().is2xxSuccessful())
                .andDo(result ->
                        token.set(result.getResponse().getHeader(Constants.JWT_HEADER_NAME))
                )
                .andDo(print());
        return token.get();
    }

    @Test
    public void testLogin() throws Exception {
        String password = RandomString.make(8);
        UserDTO userDTO = randomUser(password);
        try {
            String token = formLoginAndGetToken(userDTO, password);
            System.out.println(token);
        } finally {
            userRepository.deleteById(userDTO.id());
        }
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
    public void testAuthority() {
        String password = RandomString.make(72);
        UserDTO uDto = randomUser(password);

        CategoryMemberId mDto = categoryMemberService.addMember(
                categoryRepository.getReferenceById(6),
                userRepository.getReferenceById(uDto.id()),
                null
        );
        String token;
        try {
            token = formLoginAndGetToken(uDto, password);

            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/omgwtfbbq")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header(Constants.JWT_HEADER_NAME, token)
            ).andExpect(status().is2xxSuccessful())
                    .andDo(print());

            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/test_moderator")
                            .param("categoryId", mDto.getCategoryId().toString())
                            .header(Constants.JWT_HEADER_NAME, token)
            ).andExpect(status().is2xxSuccessful())
                    .andDo(print());

            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/test_moderator")
                            .param("categoryId", "5")
                            .header(Constants.JWT_HEADER_NAME, token)
            ).andExpect(status().is4xxClientError()).andDo(print());

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if ( mDto != null ) categoryMemberRepository.deleteById(mDto);
            userRepository.deleteById(uDto.id());
        }
    }

    @Test
    public void testPostList() {
        String token;
        String password = RandomString.make(72);
        UserDTO user = randomUser(password);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/no_auth/post/get_posts")
//                            .contentType(MediaType.APPLICATION_JSON)
                            .param("categoryId", "1")
                            .param("page", "2")
                            .param("size", "15")
                            .param("sort", Post_.CREATE_TIME+",desc")
            ).andExpect(status().is2xxSuccessful()).andDo(print());

            token = formLoginAndGetToken(user, password);

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/no_auth/post/get_posts")
//                            .contentType(MediaType.APPLICATION_JSON)
                            .param("categoryId", "1")
                            .param("page", "1")
                            .param("size", "5")
                            .param("sort", Post_.CREATE_TIME+",desc")
                            .header(Constants.JWT_HEADER_NAME, token)
            ).andExpect(status().is2xxSuccessful()).andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if  ( user != null ) userRepository.deleteById(user.id());
        }

    }

    @Test
    public void testGetUserProj() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/no_auth/user/get_info")
                        .param("userId", "126")
        ).andExpect(status().is2xxSuccessful()).andDo(print());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/no_auth/user/get_full_info")
                        .param("userId", "126")
        ).andExpect(status().is2xxSuccessful()).andDo(print());
    }

    @Test
    public void testCommentSlice() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/no_auth/post/get_comments")

        ).andExpect(status().is4xxClientError()).andDo(print());
    }

    @Test
    public void testCategoryList() throws Exception {
        String token;
        String password = RandomString.make(72);
        UserDTO user = randomUser(password);
        token = formLoginAndGetToken(user, password);
        try {
            for(int i=1; i<6; ++i) {
                mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/category/join_category")
                        .header(Constants.JWT_HEADER_NAME, token)
                        .param("categoryId", i+"")
                ).andExpect(status().is2xxSuccessful()).andDo(print());
            }
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/category/get_joined_category")
                    .header(Constants.JWT_HEADER_NAME, token)
                    .param("pageNum", "0")
                    .param("pageSize", "10")
            ).andExpect(status().is2xxSuccessful()).andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            List<CategoryMemberId> ids = new ArrayList<>();
            for( int i=1; i<6; ++i) {
                ids.add(new CategoryMemberId(i, user.id()));
            }
            categoryMemberRepository.deleteAllById(ids);
            userRepository.deleteById(user.id());
        }
    }

    @Test
    public void testDelete() throws Exception {
        String token;
        String password = RandomString.make(72);
        UserDTO user = randomUser(password);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/post/delete")
                            .param("postId", "2")
            ).andExpect(status().is4xxClientError()).andDo(print());

            token = formLoginAndGetToken(user, password);

            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/post/delete")
                    .param("id", "2")
                    .header(Constants.JWT_HEADER_NAME, token)
            ).andExpect(status().is4xxClientError()).andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if ( user != null ) userRepository.deleteById(user.id());
        }
    }

}
