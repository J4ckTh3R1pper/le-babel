package dayp308.lebabel.service;

import dayp308.lebabel.entity.business.CategoryCreationForm;
import dayp308.lebabel.entity.category.CategoryMinimal;
import dayp308.lebabel.entity.category.PostCategory;
import dayp308.lebabel.entity.user.User;
import dayp308.lebabel.entity.enums.Role;
import dayp308.lebabel.repository.CategoryMemberRepository;
import dayp308.lebabel.repository.CategoryRepository;
import dayp308.lebabel.repository.PostRepository;
import dayp308.lebabel.repository.UserRepository;
import dayp308.lebabel.repository.specification.CategorySpecs;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

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

    public Slice<CategoryMinimal> getMinimalSlice(@Nullable User user, Pageable pageable) {
        Pageable p1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Slice<CategoryMinimal> slice =
                categoryRepository.findBy(CategorySpecs.getAllCategories(user, pageable.getSort()),
                q -> q.as(CategoryMinimal.class).page(p1)
        );
        return slice;
    }

}
