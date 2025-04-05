package dayp308.chatroom;

import dayp308.chatroom.converter.post.PostToBriefView;
import dayp308.chatroom.entity.business.CategoryCreationForm;
import dayp308.chatroom.entity.category.PostCategory;
import dayp308.chatroom.entity.comment.CommentDTO;
import dayp308.chatroom.entity.enums.PostOrderType;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.business.CommentCreationForm;
import dayp308.chatroom.entity.business.PostForm;
import dayp308.chatroom.entity.business.UserRegistrationForm;
import dayp308.chatroom.entity.post.PostDTO;
import dayp308.chatroom.entity.post.Post_;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.entity.user.UserDTO;
import dayp308.chatroom.entity.user.UserDetailedProj;
import dayp308.chatroom.entity.view.comment.CommentBriefView;
import dayp308.chatroom.entity.view.comment.CommentDetailedView;
import dayp308.chatroom.repository.*;
import dayp308.chatroom.repository.projection.PostProjection;
import dayp308.chatroom.service.CategoryMemberService;
import dayp308.chatroom.service.CategoryService;
import dayp308.chatroom.service.PostService;
import dayp308.chatroom.service.UserService;
import jakarta.persistence.NoResultException;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

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

    UserDTO randomUser(String password ) {
        UserRegistrationForm form = new UserRegistrationForm();
        form.setLoginName(RandomString.make(8) + "@example.com");
        form.setNickName(RandomString.make(8));
        form.setPassword(password);
        long id = userService.register(form);
        return userRepository.findById(id, UserDTO.class);
    }

    @ParameterizedTest
    @CsvSource({"qq", ".com"})
    public void searchUserByLoginName(String loginName) {
        System.out.println(userRepository.findByLoginName(loginName, UserDTO.class));
    }

    @ParameterizedTest
    @CsvSource({"coder", "s"})
    public void searchUserByNickName(String nickName) {
        System.out.println(userService.searchByNickName(nickName));
    }

    @Test
    public void testRegister() {
        String password = RandomString.make(8);
        System.out.println(randomUser(password));
    }

    @Test
    public void fullTest() {
        PostCategory category = null;
        PostDTO post = null;
        CategoryMemberId member = null;
        CommentDTO comment = null;

        String password = RandomString.make(8);
        UserDTO user = randomUser(password);

        try {
            User user1 = userRepository.findById(user.id(), User.class);
            category = categoryService.createCategory(
                    new CategoryCreationForm(RandomString.make(8), RandomString.make(8), RandomString.make(8))
            );

            member = categoryMemberService.addMember(category, user1, Role.SUBSCRIBER);

            post = postService.addPost(
                    new PostForm(
                            category.getId(),
                            RandomString.make(16),
                            RandomString.make(512)
                    ), user1
            );

            comment = postService.addComment(
                    new CommentCreationForm(
                            RandomString.make(512),
                            post.getId(),
                            null
                    ), user1
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (comment != null) commentRepository.deleteById(comment.getId());
            if (post != null) postRepository.deleteById(post.getId());
            if (member != null) categoryMemberRepository.deleteById(member);
            if (user != null) userRepository.deleteById(user.id());
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
        CommentDetailedView view = postService.getCommentDetailedView(32, null);
        System.out.println(view.toString());
    }

    @Test
    void testUserDetail() {
        UserDetailedProj proj = userRepository.findDetailById(126L);
        System.out.println(proj.getFollowingCount());
        System.out.println(proj.getFollowerCount());

        Slice<UserDetailedProj> projections = userRepository.findDetailsById(
                PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "followerCount"))

        );
        projections.getContent().forEach( p -> {
            System.out.println(p.getCreateTime() + ":" + p.getFollowerCount() + "," + p.getFollowingCount());
        });
    }

    @Test
    void testPostDetailProj() {
        User user = userRepository.getReferenceById(135L);
        PostProjection projection = null;
        PostProjection projection2 = null;
        try {
            projection2 = postRepository.findProjById(17, null);
            projection = postRepository.findProjById(18, null);
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        System.out.println(projection);
        System.out.println(projection2);
    }
    @Test
    void testPostBriefSlice() {
        User user = userRepository.getReferenceById(135L);
        Slice<PostProjection> slice = postRepository.findAllProjById(2, user,true, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, Post_.LAST_UPDATE_TIME)));
        System.out.println(slice.getContent());
    }
}
