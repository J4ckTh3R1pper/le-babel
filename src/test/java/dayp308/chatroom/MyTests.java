package dayp308.chatroom;

import dayp308.chatroom.entity.CategoryMemberId;
import dayp308.chatroom.entity.PostComment;
import dayp308.chatroom.entity.business.CommentCreationForm;
import dayp308.chatroom.entity.business.PostForm;
import dayp308.chatroom.entity.business.UserRegistrationForm;
import dayp308.chatroom.entity.dto.*;
import dayp308.chatroom.exception.InvalidFormException;
import dayp308.chatroom.repository.*;
import dayp308.chatroom.service.CategoryService;
import dayp308.chatroom.service.PostService;
import dayp308.chatroom.service.UserService;
import jakarta.persistence.EntityExistsException;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;

@SpringBootTest

class MyTests {

    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private PostRepository postRepository;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMemberRepository categoryMemberRepository;

    @ParameterizedTest
    @CsvSource({"qq", ".com"})
    public void searchUserByLoginName(String loginName) {
        System.out.println(userRepository.findByLoginName(loginName));
    }

    @ParameterizedTest
    @CsvSource({"coder", "s"})
    public void searchUserByNickName(String nickName) {
        System.out.println(userService.searchByNickName(nickName));
    }

    @ParameterizedTest
    @CsvSource({"spidey5852@foxmail.com, e10adc3949ba59abbe56e057f20f883e", "123456@example.com, password"})
    public void testLogin(String loginName, String password) {
        System.out.println(userService.checkLogin(loginName, password));
    }

    @Test
    public void testRegister() {
        UserRegistrationForm form = new UserRegistrationForm();
        form.setLoginName(RandomString.make(8) + "@example.com");
        form.setNickName(RandomString.make(8));
        form.setPassword(RandomString.make(8));
        System.out.println(userService.register(form));
    }

    @Test
    public void fullTest() {
        UserDTO user = null;
        CategoryDTO category = null;
        PostDTO post = null;
        CategoryMemberDTO member = null;
        CommentDTO comment = null;
        String token = null;

        UserRegistrationForm form = new UserRegistrationForm();
        form.setLoginName(RandomString.make(8) + "@example.com");
        form.setNickName(RandomString.make(8));
        form.setPassword(RandomString.make(8));
        try {
            user = userService.register(form);
            token = userRepository.getReferenceById(user.getId()).getToken();
            category = categoryService.createCategory(
                    new CategoryDTO(RandomString.make(8))
            );

            member = categoryService.addUserToCategory(category, user);

            post = postService.addPost(
                    new PostForm(
                            token,
                            category.getId(),
                            RandomString.make(16),
                            RandomString.make(512)
                    )
            );

            comment = postService.addComment(
                    new CommentCreationForm(
                            RandomString.make(512),
                            post.getId(),
                            null,
                            token
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (comment != null) commentRepository.deleteById(comment.getId());
            if (post != null) postRepository.deleteById(post.getId());
            if (member != null) categoryMemberRepository.deleteById(new CategoryMemberId(
                    member.getCategoryId(), member.getUserId()
            ));
            if (user != null) userRepository.deleteById(user.getId());
            if (category != null) categoryRepository.deleteById(category.getId());
        }
    }
}
