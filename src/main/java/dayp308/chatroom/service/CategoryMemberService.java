package dayp308.chatroom.service;

import dayp308.chatroom.entity.CategoryMember;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.PostCategory;
import dayp308.chatroom.entity.dto.CategoryDTO;
import dayp308.chatroom.entity.dto.CategoryMemberDTO;
import dayp308.chatroom.entity.dto.UserDTO;
import dayp308.chatroom.entity.enums.Role;
import dayp308.chatroom.entity.view.UserBriefView;
import dayp308.chatroom.exception.CategoryDeletedException;
import dayp308.chatroom.exception.CategoryPendingException;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.UserRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public CategoryMemberDTO updateMember(CategoryMemberDTO dto) {
        PostCategory category = categoryRepository.getReferenceById(dto.getCategoryId());
        checkAvailability(category);
        CategoryMember membership = conversionService.convert(dto, CategoryMember.class);
        categoryMemberRepository.saveAndFlush(membership);
        return conversionService.convert(membership, CategoryMemberDTO.class);
    }

    public CategoryMemberDTO addMember(CategoryDTO categoryDto, UserDTO userDto, @Nullable Role role) {
        PostCategory category = categoryRepository.getReferenceById(categoryDto.getId());
        checkAvailability(category);
        CategoryMemberId memberId = new CategoryMemberId(categoryDto.getId(), userDto.getId());
        if ( categoryMemberRepository.existsById(memberId)
                && role != null
                && categoryMemberRepository.getReferenceById(memberId).getRole().ordinal() <= role.ordinal() ) {
            return setUserRole(categoryDto, userDto, role);
        }

        User user = userRepository.getReferenceById(userDto.getId());
        CategoryMember member = new CategoryMember();
        member.setCategory(category);
        member.setUser(user);
        if (role != null) member.setRole(role);

        categoryMemberRepository.saveAndFlush(member);
        return conversionService.convert(
                member,
                CategoryMemberDTO.class
        );
    }

    public CategoryMemberDTO setUserRole(CategoryDTO categoryDto, UserDTO userDto, Role role) {
        checkAvailability(categoryRepository.getReferenceById(categoryDto.getId()));
        CategoryMember member = categoryMemberRepository.getReferenceById(new CategoryMemberId(categoryDto.getId(), userDto.getId()));
        member.setRole(role);
        return conversionService.convert( categoryMemberRepository.saveAndFlush(member), CategoryMemberDTO.class );
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
