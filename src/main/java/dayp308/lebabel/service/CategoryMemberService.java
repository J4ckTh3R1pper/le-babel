package dayp308.lebabel.service;

import dayp308.lebabel.bean.entity.member.CategoryMember;
import dayp308.lebabel.bean.entity.user.User;
import dayp308.lebabel.bean.entity.id.CategoryMemberId;
import dayp308.lebabel.bean.entity.category.PostCategory;
import dayp308.lebabel.bean.entity.member.CategoryMemberDTO;
import dayp308.lebabel.enumeration.Role;
import dayp308.lebabel.bean.view.UserBriefView;
import dayp308.lebabel.exception.CategoryDeletedException;
import dayp308.lebabel.exception.CategoryPendingException;
import dayp308.lebabel.repository.jpa.CategoryMemberRepository;
import dayp308.lebabel.repository.jpa.CategoryRepository;
import dayp308.lebabel.repository.redis.MemberMinimalRedisRepository;
import dayp308.lebabel.repository.jpa.UserRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class CategoryMemberService {

    private final CategoryMemberRepository categoryMemberRepository;
    private final ConversionService conversionService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public CategoryMemberService(CategoryMemberRepository categoryMemberRepository, ConversionService conversionService, CategoryRepository categoryRepository, UserRepository userRepository, MemberMinimalRedisRepository memberMinimalRedisRepository) {
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
        Optional<CategoryMember> member = categoryMemberRepository.findById(
                new CategoryMemberId(category.getId(), user.getId())
        );
        if (member.isPresent()) {
            if (member.get().getRole() == Role.GUEST)
                member.get().setRole(role);
            else if (member.get().getRole() == Role.SUBSCRIBER)
                member.get().setRole(Role.GUEST);
            return categoryMemberRepository.saveAndFlush(member.get()).getId();
        } else {
            CategoryMember newMember = new CategoryMember(category, user, role);
            return categoryMemberRepository.saveAndFlush(newMember).getId();
        }

    }

    public void setUserRole(PostCategory category, User user, Role role) {
        checkAvailability(category);
        CategoryMember member = categoryMemberRepository.getReferenceById(
                new CategoryMemberId(category.getId(), user.getId()));
        member.setRole(role);
    }

    public Instant muteUser(int categoryId, long userId, long seconds) {
        User user = userRepository.getReferenceById(userId);
        CategoryMember membership;
        membership = categoryMemberRepository.getReferenceById(
                new CategoryMemberId(categoryId, user.getId())
        );
        membership.setMuteExpirationDate(Instant.now().plusSeconds(Math.max(0, seconds)));
        categoryMemberRepository.saveAndFlush(membership);
        return membership.getMuteExpirationDate();
    }

    public void unmuteUser(int categoryId, long userId) {
        User user = userRepository.getReferenceById(userId);
        CategoryMember membership;
        membership = categoryMemberRepository.getReferenceById(
                new CategoryMemberId(categoryId, user.getId())
        );
        membership.setMuteExpirationDate(Instant.now());
        categoryMemberRepository.saveAndFlush(membership);
    }

    public boolean hasMembership(PostCategory category, User user) {
        return categoryMemberRepository.existsById(new CategoryMemberId(category.getId(), user.getId()));
    }

    public boolean hasAuthorityGreaterOrEquals(PostCategory category, User user, Role role) {
        int roleOrdinal = role.ordinal();
        try {
            return ( categoryMemberRepository.getReferenceById(
                    new CategoryMemberId(category.getId(), user.getId()))
                            .getRole().ordinal() >= roleOrdinal
                    );
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasAuthority(PostCategory category, User user, Role role) {
        try {
            return
            ( categoryMemberRepository.getReferenceById(
                    new CategoryMemberId(category.getId(), user.getId())).getRole().equals(role) );
        } catch (Exception e) {
            return false;
        }
    }

    private void checkAvailability(PostCategory category) {
        if (category.getIsDeleted()) throw new CategoryDeletedException("category has been deleted");
        if (category.getIsPending()) throw new CategoryPendingException("category is currently pending");
    }

}
