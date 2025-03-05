package dayp308.chatroom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.business.CategoryCreationForm;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.service.CategoryMemberService;
import dayp308.chatroom.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
    private final CategoryMemberRepository categoryMemberRepository;
    private final CategoryMemberService categoryMemberService;
    private final ObjectMapper objectMapper;
    private final CategoryService categoryService;

    public CategoryController(CategoryMemberRepository categoryMemberRepository, CategoryMemberService categoryMemberService, ObjectMapper objectMapper, CategoryService categoryService) {
        this.categoryMemberRepository = categoryMemberRepository;
        this.categoryMemberService = categoryMemberService;
        this.objectMapper = objectMapper;
        this.categoryService = categoryService;
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
}
