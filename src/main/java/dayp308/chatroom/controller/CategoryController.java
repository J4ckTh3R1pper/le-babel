package dayp308.chatroom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dayp308.chatroom.entity.category.CategoryDTO;
import dayp308.chatroom.entity.category.CategoryMinimal;
import dayp308.chatroom.entity.category.CategoryProjection;
import dayp308.chatroom.entity.category.PostCategory;
import dayp308.chatroom.entity.category.PostCategory_;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.member.MemberMinimal;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.entity.business.CategoryCreationForm;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.PostRepository;
import dayp308.chatroom.service.CategoryMemberService;
import dayp308.chatroom.service.CategoryService;
import jakarta.persistence.NoResultException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
public class CategoryController {
    private final CategoryMemberRepository categoryMemberRepository;
    private final CategoryMemberService categoryMemberService;
    private final ObjectMapper objectMapper;
    private final CategoryService categoryService;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryMemberRepository categoryMemberRepository, CategoryMemberService categoryMemberService, ObjectMapper objectMapper, CategoryService categoryService, PostRepository postRepository, CategoryRepository categoryRepository) {
        this.categoryMemberRepository = categoryMemberRepository;
        this.categoryMemberService = categoryMemberService;
        this.objectMapper = objectMapper;
        this.categoryService = categoryService;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/api/no_auth/category/get_user")
    public ResponseEntity<String> getUserBriefView(
            @RequestParam("categoryId") int categoryId,
            @RequestParam("userId") long userId
    ) throws JsonProcessingException {
        return new ResponseEntity<>(
                objectMapper.writeValueAsString(categoryMemberService.getUserBriefView(categoryId, userId)),
                HttpStatus.OK
        );
    }

    @GetMapping("/api/no_auth/category/get_info")
    public CategoryProjection getCategoryInfo(@RequestParam("id") int id) {
        return categoryRepository.findProjectionById(id);
    }

    @GetMapping("/api/no_auth/category/get_member")
    public MemberMinimal getMemberCache(
            @RequestParam("categoryId") int categoryId,
            @RequestParam("userId") long userId
    ) {
        MemberMinimal member = categoryMemberRepository.findById(new CategoryMemberId(categoryId, userId), MemberMinimal.class);
        return member;

    }

    @GetMapping("/api/no_auth/category/get_cache")
    public CategoryMinimal getMinimal(@RequestParam("id") int id) {
        return categoryRepository.findById(id, CategoryMinimal.class);
    }
    

    @PostMapping("/api/category/create_request")
    public ResponseEntity<Integer> createCategoryRequest(
            @RequestParam CategoryCreationForm form,
            Authentication auth
    ) {
        int id = categoryService.createCategory(form, (User) auth.getPrincipal());
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping("/api/category/join_category")
    public ResponseEntity<CategoryMemberId> joinCategory(Authentication auth,
                                               @RequestParam("id") PostCategory category) {
        User user = (User) auth.getPrincipal();
        CategoryMemberId id = categoryMemberService.addMember(
                category, user, Role.SUBSCRIBER);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/api/no_auth/category/get_category_list")
    public List<CategoryMinimal> getCategoryList(
            @PageableDefault(
                    sort = PostCategory_.RANK, direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return categoryRepository.findBy(pageable, CategoryMinimal.class);
    }

    @PreAuthorize("@authz.hasMembershipGe(#categoryId, 'MODERATOR')")
    @PostMapping("/api/category/mute_user")
    public ResponseEntity<String> muteUser(@RequestParam int categoryId, @RequestParam("userId") long userId, @RequestParam long seconds) {
        Instant expirationDate = categoryMemberService.muteUser(categoryId, userId, seconds);
        return new ResponseEntity<>( "expirationDate:" + expirationDate.getEpochSecond() + "000", HttpStatus.OK);
    }

    @PreAuthorize("@authz.hasMembershipGe(#categoryId, 'MODERATOR')")
    @PostMapping("/api/category/unmute_user")
    public ResponseEntity<String> unmuteUser(@RequestParam int categoryId, @RequestParam("userId") long userId) {
        categoryMemberService.unmuteUser(categoryId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
