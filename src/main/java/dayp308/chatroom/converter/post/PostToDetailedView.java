package dayp308.chatroom.converter.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.converter.member.CategoryMemberToBriefViewConverter;
import dayp308.chatroom.entity.member.CategoryMember;
import dayp308.chatroom.entity.post.Post;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.view.post.PostDetailedView;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.CommentRepository;
import dayp308.chatroom.repository.PostLikeRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostToDetailedView implements Converter<Post, PostDetailedView> {
    private final CategoryMemberRepository categoryMemberRepository;
    private final CategoryMemberToBriefViewConverter categoryMemberToBriefViewConverter;
    private final PostLikeRepository postLikeRepository;
    private final ObjectMapper jacksonObjectMapper;
    private final CommentRepository commentRepository;

    public PostToDetailedView(CategoryMemberRepository categoryMemberRepository, CategoryMemberToBriefViewConverter categoryMemberToBriefViewConverter, PostLikeRepository postLikeRepository, ObjectMapper jacksonObjectMapper, CommentRepository commentRepository) {
        this.categoryMemberRepository = categoryMemberRepository;
        this.categoryMemberToBriefViewConverter = categoryMemberToBriefViewConverter;
        this.postLikeRepository = postLikeRepository;
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.commentRepository = commentRepository;
    }

    @Override
    public PostDetailedView convert(Post source) {
        PostDetailedView target = new PostDetailedView();
        BeanUtils.copyProperties(source, target);
        CategoryMember member = categoryMemberRepository.getReferenceById(
                new CategoryMemberId(
                        source.getCategory().getId(),
                        source.getPublishUser().getId()
                )
        );
        target.setUserBriefView(categoryMemberToBriefViewConverter.convert(member));
        target.setLikeCount(Hibernate.size(source.getUsersLiked()));
        target.setCommentCount(Hibernate.size(source.getComments()));
        try {
            target.setTags(jacksonObjectMapper.readValue(source.getTags(), List.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        target.setLiked(false);
        target.setCategoryId(source.getCategory().getId());
        target.setViewCount(source.getViews());
        return target;
    }

}
