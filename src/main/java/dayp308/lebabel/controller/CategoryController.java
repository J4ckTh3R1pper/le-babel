package dayp308.lebabel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dayp308.lebabel.entity.category.CategoryMinimal;
import dayp308.lebabel.entity.category.CategoryProjection;
import dayp308.lebabel.entity.category.PostCategory;
import dayp308.lebabel.entity.category.PostCategory_;
import dayp308.lebabel.entity.enums.Role;
import dayp308.lebabel.entity.id.CategoryMemberId;
import dayp308.lebabel.entity.member.MemberMinimal;
import dayp308.lebabel.entity.user.User;
import dayp308.lebabel.entity.business.CategoryCreationForm;
import dayp308.lebabel.repository.CategoryMemberRepository;
import dayp308.lebabel.repository.CategoryRepository;
import dayp308.lebabel.repository.PostRepository;
import dayp308.lebabel.service.CategoryMemberService;
import dayp308.lebabel.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/no_auth/category")
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

    @Operation(summary = "获取用户板块内的基本信息",
        parameters = {
            @Parameter(name = "categoryId", description = "板块id"),
            @Parameter(name = "userId", description = "用户id")
        }
    )
    @GetMapping("/get_user")
    public ResponseEntity<String> getUserBriefView(
            @RequestParam("categoryId") int categoryId,
            @RequestParam("userId") long userId
    ) throws JsonProcessingException {
        return new ResponseEntity<>(
                objectMapper.writeValueAsString(categoryMemberService.getUserBriefView(categoryId, userId)),
                HttpStatus.OK
        );
    }

    @GetMapping("/get_info")
    public CategoryProjection getCategoryInfo(@RequestParam("id") int id) {
        return categoryRepository.findProjectionById(id);
    }

    @GetMapping("/get_member")
    public MemberMinimal getMemberCache(
            @RequestParam("categoryId") int categoryId,
            @RequestParam("userId") long userId
    ) {
        MemberMinimal member = categoryMemberRepository.findById(new CategoryMemberId(categoryId, userId), MemberMinimal.class);
        return member;

    }

    @GetMapping("/get_cache")
    public CategoryMinimal getMinimal(@RequestParam("id") int id) {
        return categoryRepository.findById(id, CategoryMinimal.class);
    }

    @GetMapping("/get_category_list")
    public List<CategoryMinimal> getCategoryList(
            @PageableDefault(
                    sort = PostCategory_.RANK, direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return categoryRepository.findBy(pageable, CategoryMinimal.class);
    }

}
