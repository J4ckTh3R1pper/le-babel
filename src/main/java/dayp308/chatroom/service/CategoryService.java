package dayp308.chatroom.service;

import dayp308.chatroom.entity.CategoryMember;
import dayp308.chatroom.entity.business.CategoryCreationForm;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.PostCategory;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.dto.CategoryDTO;
import dayp308.chatroom.entity.dto.CategoryMemberDTO;
import dayp308.chatroom.entity.dto.UserDTO;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.exception.CategoryDeletedException;
import dayp308.chatroom.exception.CategoryPendingException;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.PostRepository;
import dayp308.chatroom.repository.UserRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CategoryService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ConversionService conversionService;
    private final PostRepository postRepository;
    private final CategoryMemberRepository categoryMemberRepository;
    private final CategoryMemberService categoryMemberService;

    @Autowired
    public CategoryService(UserRepository userRepository, CategoryRepository categoryRepository, ConversionService conversionService, PostRepository postRepository, CategoryMemberRepository categoryMemberRepository, CategoryMemberService categoryMemberService) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.conversionService = conversionService;
        this.postRepository = postRepository;
        this.categoryMemberRepository = categoryMemberRepository;
        this.categoryMemberService = categoryMemberService;
    }

    public CategoryDTO createCategory(CategoryCreationForm form) {
        PostCategory category = conversionService.convert(form, PostCategory.class);
        category.setIsPending(false);
        categoryRepository.saveAndFlush(category);
        return conversionService.convert(category, CategoryDTO.class);
    }

    public CategoryDTO createCategory(CategoryCreationForm form, User user) {
        PostCategory category = conversionService.convert(form, PostCategory.class);
        category.setIsPending(false);
        categoryRepository.saveAndFlush(category);
        CategoryDTO categoryDTO = conversionService.convert(category, CategoryDTO.class);
        categoryMemberService.addMember(categoryDTO, conversionService.convert(user, UserDTO.class), Role.ADMIN);
        return categoryDTO;
    }




    private void checkAvailability(CategoryDTO dto) {
        if (dto.getIsDeleted()) throw new CategoryDeletedException("category has been deleted");
        if (dto.getIsPending()) throw new CategoryPendingException("category is currently pending");
    }
}
