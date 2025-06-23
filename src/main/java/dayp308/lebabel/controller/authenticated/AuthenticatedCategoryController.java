package dayp308.lebabel.controller.authenticated;

import dayp308.lebabel.bean.ao.CategoryCreationForm;
import dayp308.lebabel.bean.entity.category.PostCategory;
import dayp308.lebabel.enumeration.Role;
import dayp308.lebabel.bean.entity.id.CategoryMemberId;
import dayp308.lebabel.bean.entity.user.User;
import dayp308.lebabel.service.CategoryMemberService;
import dayp308.lebabel.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth/category")
public class AuthenticatedCategoryController {

    private final CategoryService categoryService;
    private final CategoryMemberService categoryMemberService;

    public AuthenticatedCategoryController(CategoryService categoryService, CategoryMemberService categoryMemberService) {
        this.categoryService = categoryService;
        this.categoryMemberService = categoryMemberService;
    }

    @Operation(summary = "创建新板块",
        parameters = {
            @Parameter(name = "form", description = "包含新板块数据的表单")
        },
        responses = {
            @ApiResponse(description = "被创建板块的id")
        }
    )
    @PostMapping("/create")
    public ResponseEntity<Integer> createCategoryRequest(
            @Valid CategoryCreationForm form,
            Authentication auth
    ) {
        int id = categoryService.createCategory(form, (User) auth.getPrincipal());
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Operation(summary = "加入板块",
        parameters = {
            @Parameter(name = "id", description = "要加入的板块id")
        },
        responses = {
            @ApiResponse(description = "用户在板块中关系的主键id")
        }
    )
    @PutMapping("/join_category")
    public ResponseEntity<CategoryMemberId> joinCategory(
            Authentication auth,
            @RequestParam("id") PostCategory category)
    {
        User user = (User) auth.getPrincipal();
        CategoryMemberId id = categoryMemberService.addMember(
                category, user, Role.SUBSCRIBER);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @Operation(summary = "禁言用户",
        parameters = {
            @Parameter(name = "categoryId", description = "板块id"),
            @Parameter(name = "userId", description = "用户id"),
            @Parameter(name = "duration", description = "禁言时长，以秒为单位"),
        },
        responses = {
            @ApiResponse(description = "禁言截止时间的UNIX时间戳")
        }
    )
    @PreAuthorize("@authz.hasMembershipGe(#categoryId, 'MODERATOR')")
    @PostMapping("/mute_user")
    public ResponseEntity<String> muteUser(
            @RequestParam("categoryId") int categoryId,
            @RequestParam("userId") long userId,
            @RequestParam("duration") long duration
    ) {
        Instant expirationDate = categoryMemberService.muteUser(categoryId, userId, duration);
        return new ResponseEntity<>(expirationDate.getEpochSecond() + "000", HttpStatus.OK);
    }

    @Operation(summary = "解除用户禁言",
        parameters = {
            @Parameter(name = "categoryId", description = "板块id"),
            @Parameter(name = "userId", description = "用户id")
        }
    )
    @PreAuthorize("@authz.hasMembershipGe(#categoryId, 'MODERATOR')")
    @PostMapping("/unmute_user")
    public ResponseEntity<String> unmuteUser(
            @RequestParam("categoryId") int categoryId,
            @RequestParam("userId") long userId) {
        categoryMemberService.unmuteUser(categoryId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
