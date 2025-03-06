package dayp308.chatroom.service;

import dayp308.chatroom.entity.CategoryMember;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.PostCategory;
import dayp308.chatroom.entity.CategoryMemberDTO;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.entity.view.UserBriefView;
import dayp308.chatroom.exception.CategoryDeletedException;
import dayp308.chatroom.exception.CategoryPendingException;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.UserRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class CategoryMemberService {

    private final CategoryMemberRepository categoryMemberRepository;
    private final ConversionService conversionService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public CategoryMemberService(CategoryMemberRepository categoryMemberRepository, ConversionService conversionService, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryMemberRepository = categoryMemberRepository;
        this.conversionService = conversionService;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public CategoryMemberDTO getCategoryMemberById(int categoryId, long userId) {
        CategoryMember member = categoryMemberRepository.getReferenceById(new CategoryMemberId(categoryId, userId));
        return conversionService.convert(member, CategoryMemberDTO.class);
    }

    public UserBriefView getUserBriefView(int categoryId, long userId) {
        CategoryMember member = categoryMemberRepository.getReferenceById(new CategoryMemberId(categoryId, userId));
        return conversionService.convert(member, UserBriefView.class);
    }

    public CategoryMemberId addMember(PostCategory category, User user, @Nullable Role role) {
        checkAvailability(category);
        if (categoryMemberRepository.findByUser(user).isPresent()) throw new EntityExistsException();

        CategoryMember member = new CategoryMember();
        member.setCategory(category);
        member.setUser(user);
        if (role != null) member.setRole(role);

        return categoryMemberRepository.saveAndFlush(member).getId();
    }

    public void setUserRole(PostCategory category, User user, Role role) {
        checkAvailability(category);
        CategoryMember member = categoryMemberRepository.getReferenceById(new CategoryMemberId(category.getId(), user.getId()));
        member.setRole(role);
    }

    public boolean hasMembership(int categoryId, long userId) {
        return categoryMemberRepository.existsById(new CategoryMemberId(categoryId, userId));
    }

    public boolean hasAuthorityGreaterOrEquals(int categoryId, long userId, Role role) {
        int roleOrdinal = role.ordinal();
        try {
            return ( categoryMemberRepository.getReferenceById(new CategoryMemberId(categoryId, userId))
                            .getRole().ordinal() >= roleOrdinal
                    );
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasAuthority(int categoryId, long userId, Role role) {
        try {
            return
            ( categoryMemberRepository.getReferenceById(new CategoryMemberId(categoryId, userId)).getRole().equals(role) );
        } catch (Exception e) {
            return false;
        }
    }

    private void checkAvailability(PostCategory category) {
        if (category.getIsDeleted()) throw new CategoryDeletedException("category has been deleted");
        if (category.getIsPending()) throw new CategoryPendingException("category is currently pending");
    }

}
