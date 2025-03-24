package dayp308.chatroom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.entity.category.CategoryMinimal;
import dayp308.chatroom.entity.enums.CategoryOrderType;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.entity.business.CategoryCreationForm;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.PostRepository;
import dayp308.chatroom.service.CategoryMemberService;
import dayp308.chatroom.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/api/category/get_user")
    public ResponseEntity<String> getUserBriefView(
                                                  @RequestParam("categoryId") int categoryId,
                                                  @RequestParam("userId") long userId,
                                                  HttpServletRequest request) throws JsonProcessingException {
        return new ResponseEntity<>(
                objectMapper.writeValueAsString(categoryMemberService.getUserBriefView(categoryId, userId)),
                HttpStatus.OK
        );
    }

    @PostMapping("/api/category/create_request")
    public ResponseEntity<String> createCategoryRequest(
            @RequestParam CategoryCreationForm form,
            Authentication auth
    ) {
        categoryService.createCategory(form, (User) auth.getPrincipal());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/category/join_category")
    public ResponseEntity<String> joinCategory(Authentication auth,
                                               int categoryId) throws JsonProcessingException {
        User user = (User) auth.getPrincipal();
        CategoryMemberId id = categoryMemberService.addMember(
                categoryRepository.getReferenceById(categoryId), user, Role.SUBSCRIBER);
        return new ResponseEntity<>(objectMapper.writeValueAsString(id), HttpStatus.OK);
    }

    @PostMapping("/api/category/get_joined_category")
    public ResponseEntity<String> getJoinedCategory(
            Authentication auth,
            @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "orderBy", defaultValue = "JOIN_DATE" ) CategoryOrderType orderBy,
            @RequestParam(value = "ascending", defaultValue = "false") Boolean ascending
                                                    ) throws JsonProcessingException {
        User user = (User) auth.getPrincipal();
        List<CategoryMinimal> list = categoryService.getMinimalList(user, pageNum, pageSize, orderBy, ascending);
        String responseBody = objectMapper.writeValueAsString(list);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/api/no_auth/category/get_category_list")
    public ResponseEntity<String> getCategoryList(
            @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "orderBy", defaultValue = "JOIN_DATE" ) CategoryOrderType orderBy,
            @RequestParam(value = "ascending", defaultValue = "false") Boolean ascending
    ) throws JsonProcessingException {
        List<CategoryMinimal> list = categoryService.getMinimalList(null, pageNum, pageSize, orderBy, ascending);
        String responseBody = objectMapper.writeValueAsString(list);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }


    @PreAuthorize("@authz.hasMembershipGe(#root, #categoryId, 'MODERATOR')")
    @PostMapping("/api/category/mute_user")
    public ResponseEntity<String> muteUser(@RequestParam int categoryId, @RequestParam("userId") long userId, @RequestParam long seconds) {
        Instant expirationDate = categoryMemberService.muteUser(categoryId, userId, seconds);
        return new ResponseEntity<>( "expirationDate:" + expirationDate.getEpochSecond() + "000", HttpStatus.OK);
    }

    @PreAuthorize("@authz.hasMembershipGe(#root, #categoryId, 'MODERATOR')")
    @PostMapping("/api/category/unmute_user")
    public ResponseEntity<String> unmuteUser(@RequestParam int categoryId, @RequestParam("userId") long userId) {
        categoryMemberService.unmuteUser(categoryId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
