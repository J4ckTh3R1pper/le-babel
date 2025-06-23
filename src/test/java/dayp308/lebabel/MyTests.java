package dayp308.lebabel;

import dayp308.lebabel.converter.post.PostToBriefView;
import dayp308.lebabel.bean.ao.CategoryCreationForm;
import dayp308.lebabel.bean.entity.category.PostCategory;
import dayp308.lebabel.bean.entity.comment.CommentDTO;
import dayp308.lebabel.enumeration.Role;
import dayp308.lebabel.bean.entity.id.CategoryMemberId;
import dayp308.lebabel.bean.ao.CommentCreationForm;
import dayp308.lebabel.bean.ao.PostForm;
import dayp308.lebabel.bean.ao.UserRegistrationForm;
import dayp308.lebabel.bean.entity.post.PostDTO;
import dayp308.lebabel.bean.entity.user.User;
import dayp308.lebabel.bean.entity.user.UserDTO;
import dayp308.lebabel.bean.entity.user.UserDetailedProjection;
import dayp308.lebabel.repository.jpa.*;
import dayp308.lebabel.bean.entity.projection.PostProjection;
import dayp308.lebabel.service.CategoryMemberService;
import dayp308.lebabel.service.CategoryService;
import dayp308.lebabel.service.PostService;
import dayp308.lebabel.service.UserService;
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
    void testUserDetail() {
        UserDetailedProjection proj = userRepository.findDetailById(126L);
        System.out.println(proj.getFollowingCount());
        System.out.println(proj.getFollowerCount());

        Slice<UserDetailedProjection> projections = userRepository.findDetailsById(
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
}
