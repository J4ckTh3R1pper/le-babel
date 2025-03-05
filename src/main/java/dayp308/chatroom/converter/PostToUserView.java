package dayp308.chatroom.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.entity.CategoryMember;
import dayp308.chatroom.entity.Post;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.view.PostUserView;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.CommentRepository;
import dayp308.chatroom.repository.PostLikeRepository;
import org.apache.commons.io.FilenameUtils;
import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostToUserView implements Converter<Post, PostUserView> {
    private final CategoryMemberRepository categoryMemberRepository;
    private final CategoryMemberToBriefViewConverter categoryMemberToBriefViewConverter;
    private final PostLikeRepository postLikeRepository;
    private final ObjectMapper jacksonObjectMapper;
    private final CommentRepository commentRepository;

    public PostToUserView(CategoryMemberRepository categoryMemberRepository, CategoryMemberToBriefViewConverter categoryMemberToBriefViewConverter, PostLikeRepository postLikeRepository, ObjectMapper jacksonObjectMapper, CommentRepository commentRepository) {
        this.categoryMemberRepository = categoryMemberRepository;
        this.categoryMemberToBriefViewConverter = categoryMemberToBriefViewConverter;
        this.postLikeRepository = postLikeRepository;
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.commentRepository = commentRepository;
    }

    @Override
    public PostUserView convert(Post source) {
        PostUserView target = new PostUserView();
        BeanUtils.copyProperties(source, target);
        CategoryMember member = categoryMemberRepository.getReferenceById(
                new CategoryMemberId(
                        source.getPostCategory().getId(),
                        source.getPublishUser().getId()
                )
        );
        target.setUserBriefView(categoryMemberToBriefViewConverter.convert(member));
        target.setLikes(postLikeRepository.countByPostId(source.getId()));
        target.setComments(commentRepository.countByPostId(source.getId()));
        try {
            target.setTags(jacksonObjectMapper.readValue(source.getPostTags(), List.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        target.setPostCategoryId(source.getPostCategory().getId());
        target.setLiked(false);
        target.setPostViews(source.getPostViews());
        return target;
    }

}
