package dayp308.chatroom.service;

import dayp308.chatroom.entity.business.CategoryCreationForm;
import dayp308.chatroom.entity.category.CategoryMinimal;
import dayp308.chatroom.entity.category.PostCategory;
import dayp308.chatroom.entity.enums.CategoryOrderType;
import dayp308.chatroom.entity.user.User;
import dayp308.chatroom.entity.category.CategoryDTO;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.exception.CategoryDeletedException;
import dayp308.chatroom.exception.CategoryPendingException;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.PostRepository;
import dayp308.chatroom.repository.UserRepository;
import dayp308.chatroom.repository.specification.CategorySpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    public PostCategory createCategory(CategoryCreationForm form) {
        PostCategory category = conversionService.convert(form, PostCategory.class);
        return categoryRepository.saveAndFlush(category);
    }

    public int createCategory(CategoryCreationForm form, User user) {
        PostCategory category = conversionService.convert(form, PostCategory.class);
        categoryRepository.saveAndFlush(category);
        categoryMemberService.addMember(category, user, Role.ADMIN);
        return category.getId();
    }

    public List<CategoryMinimal> getMinimalByUser(User user, int pageNum, int pageSize, CategoryOrderType orderType, boolean asc) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Slice<CategoryMinimal> slice =
                categoryRepository.findBy(CategorySpecs.getCategoryByUser(user, orderType.getColumn(), asc),
                q -> q.as(CategoryMinimal.class).page(pageable)
        );
        return slice.getContent();
    }


    private void checkAvailability(CategoryDTO dto) {
        if (dto.isDeleted()) throw new CategoryDeletedException("category has been deleted");
        if (dto.isPending()) throw new CategoryPendingException("category is currently pending");
    }
}
