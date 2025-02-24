package dayp308.chatroom.service;

import dayp308.chatroom.entity.CategoryMember;
import dayp308.chatroom.entity.CategoryMemberId;
import dayp308.chatroom.entity.PostCategory;
import dayp308.chatroom.entity.User;
import dayp308.chatroom.entity.dto.CategoryDTO;
import dayp308.chatroom.entity.dto.CategoryMemberDTO;
import dayp308.chatroom.entity.dto.UserDTO;
import dayp308.chatroom.entity.enums.Roles;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.CategoryRepository;
import dayp308.chatroom.repository.PostRepository;
import dayp308.chatroom.repository.UserRepository;
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

    @Autowired
    public CategoryService(UserRepository userRepository, CategoryRepository categoryRepository, ConversionService conversionService, PostRepository postRepository, CategoryMemberRepository categoryMemberRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.conversionService = conversionService;
        this.postRepository = postRepository;
        this.categoryMemberRepository = categoryMemberRepository;
    }

    public CategoryDTO createCategory(CategoryDTO dto) {
        PostCategory postCategory = categoryRepository.saveAndFlush(Objects.requireNonNull(conversionService.convert(dto, PostCategory.class)));
        return conversionService.convert(postCategory, CategoryDTO.class);
    }

    public CategoryMemberDTO addUserToCategory(CategoryDTO categoryDto, UserDTO userDto) {
        User user = userRepository.getReferenceById(userDto.getId());
        PostCategory category = categoryRepository.getReferenceById(categoryDto.getId());
        CategoryMember member = categoryMemberRepository.findById(new CategoryMemberId(category.getId(), user.getId())).orElse(null);
        if (member != null)
            return conversionService.convert(
                    member,
                    CategoryMemberDTO.class
            );
        member = new CategoryMember();
        member.setCategory(category);
        member.setUser(user);
        return conversionService.convert(
                categoryMemberRepository.saveAndFlush(member),
                CategoryMemberDTO.class
        );
    }

    public void setUserRole(CategoryDTO categoryDto, UserDTO userDto, Roles role) {
        CategoryMember member = categoryMemberRepository.findById(new CategoryMemberId(categoryDto.getId(), userDto.getId())).orElseThrow();
        member.setRole(role);
        categoryMemberRepository.save(member);
    }
}
