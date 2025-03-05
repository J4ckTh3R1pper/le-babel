package dayp308.chatroom.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.entity.CategoryMember;
import dayp308.chatroom.entity.Post;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.view.PostBriefView;
import dayp308.chatroom.entity.view.PostUserView;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.CommentRepository;
import dayp308.chatroom.repository.PostLikeRepository;
import org.apache.commons.io.FilenameUtils;
import org.aspectj.weaver.ast.Literal;
import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.node.Paragraph;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostToBriefView implements Converter<Post, PostBriefView> {
    private final CategoryMemberRepository categoryMemberRepository;
    private final CategoryMemberToBriefViewConverter categoryMemberToBriefViewConverter;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final ObjectMapper jacksonObjectMapper;

    public PostToBriefView(CategoryMemberRepository categoryMemberRepository, CategoryMemberToBriefViewConverter categoryMemberToBriefViewConverter, PostLikeRepository postLikeRepository, CommentRepository commentRepository, ObjectMapper jacksonObjectMapper) {
        this.categoryMemberRepository = categoryMemberRepository;
        this.categoryMemberToBriefViewConverter = categoryMemberToBriefViewConverter;
        this.postLikeRepository = postLikeRepository;
        this.commentRepository = commentRepository;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @Override
    public PostBriefView convert(Post source) {
        PostBriefView target = new PostBriefView();
        BeanUtils.copyProperties(source, target);
        CategoryMember member = categoryMemberRepository.getReferenceById(
                new CategoryMemberId(
                        source.getPostCategory().getId(),
                        source.getPublishUser().getId()
                )
        );
        target.setPostContent(cropContent(target.getPostContent()));
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
        target.setThumbnails((getThumbnail(source.getPostContent())));
        target.setPostViews(source.getPostViews());
        return target;
    }

    public static String cropContent(String content) {
        Parser parser = Parser.builder().build();
        Node node = parser.parse(content).getFirstChild().getFirstChild();
        return ( (Text) node).getLiteral();
    }

    public static List<String> getThumbnail(String md) {
        if (md.isBlank()) return List.of();
        Parser parser = Parser.builder().build();
        List<String> thumbnails = new ArrayList();
        Node document = parser.parse(md);
        Node node = document.getFirstChild().getFirstChild();
        int count = 0;
        while (count < 3 && node != null ) {
            if (node instanceof Image) {
                String dest = ((Image) node).getDestination();
                thumbnails.add(
                        FilenameUtils.getPath(dest) +
                                FilenameUtils.getBaseName(dest) +
                                "-thumbnail.webp");
            }
            node = node.getNext();
            ++count;
        }
        return thumbnails;
    }

}
