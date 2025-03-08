package dayp308.chatroom.converter.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.converter.member.CategoryMemberToBriefViewConverter;
import dayp308.chatroom.entity.member.CategoryMember;
import dayp308.chatroom.entity.post.Post;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.view.post.PostBriefView;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.CommentRepository;
import dayp308.chatroom.repository.PostLikeRepository;
import dayp308.chatroom.util.MarkdownUtil;
import org.apache.commons.io.FilenameUtils;
import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.hibernate.Hibernate;
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
                        source.getCategory().getId(),
                        source.getPublishUser().getId()
                )
        );
        target.setContent(cropContent(source.getContent()));
        target.setUserBriefView(categoryMemberToBriefViewConverter.convert(member));
        target.setLikeCount(Hibernate.size(source.getUsersLiked()));
        target.setCommentCount(Hibernate.size(source.getComments()));
        target.setBookmarks(Hibernate.size(source.getBookmarkedUsers()));
        try {
            target.setTagList(jacksonObjectMapper.readValue(source.getTags(), List.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        target.setCategoryId(source.getCategory().getId());
        target.setThumbnails((getThumbnail(source.getContent())));
        target.setViews(source.getViews());
        return target;
    }

    public static String cropContent(String content) {
        String stripped = MarkdownUtil.stripMarkdown(content);
        return stripped.substring(0, Math.min(stripped.length(), 50));
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
