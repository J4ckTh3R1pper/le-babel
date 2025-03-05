package dayp308.chatroom;

import dayp308.chatroom.converter.PostToBriefView;
import dayp308.chatroom.entity.business.CategoryCreationForm;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.business.CommentCreationForm;
import dayp308.chatroom.entity.business.PostForm;
import dayp308.chatroom.entity.business.UserRegistrationForm;
import dayp308.chatroom.entity.dto.*;
import dayp308.chatroom.entity.view.PagedResponse;
import dayp308.chatroom.entity.view.comment.CommentBriefView;
import dayp308.chatroom.entity.view.comment.CommentDetailResponse;
import dayp308.chatroom.entity.view.comment.CommentDetailedView;
import dayp308.chatroom.repository.*;
import dayp308.chatroom.service.CategoryMemberService;
import dayp308.chatroom.service.CategoryService;
import dayp308.chatroom.service.PostService;
import dayp308.chatroom.service.UserService;
import dayp308.chatroom.util.PageUtil;
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
    @Autowired
    private CategoryMemberService categoryMemberService;

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

        UserRegistrationForm form = new UserRegistrationForm();
        form.setLoginName(RandomString.make(8) + "@example.com");
        form.setNickName(RandomString.make(8));
        form.setPassword(RandomString.make(8));
        try {
            user = userService.register(form);
            category = categoryService.createCategory(
                    new CategoryCreationForm(RandomString.make(8), RandomString.make(8), RandomString.make(8))
            );

            member = categoryMemberService.addMember(category, user, Role.SUBSCRIBER);

            post = postService.addPost(
                    new PostForm(
                            category.getId(),
                            RandomString.make(16),
                            RandomString.make(512)
                    ), conversionService.convert(user, User.class)
            );

            comment = postService.addComment(
                    new CommentCreationForm(
                            RandomString.make(512),
                            post.getId(),
                            null
                    ), conversionService.convert(user, User.class)
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
    @Test
    void testCropMarkdown() {
        String md = """
                # h1 Heading 8-)
                ## h2 Heading
                ### h3 Heading
                #### h4 Heading
                ##### h5 Heading
                ###### h6 Heading
                
                
                ## Horizontal Rules
                
                ___
                
                ---
                
                ***
                """;
        System.out.println(PostToBriefView.cropContent(md));
    }

    @Test
    void testCommentTree() {
        CommentDetailResponse view = postService.getCommentDetailedView(32, null);
        System.out.println(view.toString());
    }
    @Test
    void testCommentTreeCropped() {
        CommentBriefView view = postService.getCommentBriefView(32);
        System.out.println(view.toString());
    }

    @Test
    void testPageComment() {
        PagedResponse<CommentBriefView> resp = postService.getBriefCommentSliceByPost(17, 0, 25, null);
        System.out.println(resp.getContent());
    }
}
