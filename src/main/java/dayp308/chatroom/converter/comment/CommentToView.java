package dayp308.chatroom.converter.comment;

import dayp308.chatroom.converter.member.CategoryMemberToBriefViewConverter;
import dayp308.chatroom.entity.member.CategoryMember;
import dayp308.chatroom.entity.comment.PostComment;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.view.comment.CommentView;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.CommentLikeRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommentToView implements Converter<PostComment, CommentView> {
    private final CommentLikeRepository commentLikeRepository;
    private final CategoryMemberRepository categoryMemberRepository;
    private final CategoryMemberToBriefViewConverter briefViewConverter;

    @Autowired
    public CommentToView(CommentLikeRepository commentLikeRepository, CategoryMemberRepository categoryMemberRepository, CategoryMemberToBriefViewConverter briefViewConverter) {
        this.commentLikeRepository = commentLikeRepository;
        this.categoryMemberRepository = categoryMemberRepository;
        this.briefViewConverter = briefViewConverter;
    }

    @Override
    public CommentView convert(PostComment source) {
        CommentView target = new CommentView();
        CategoryMember member = categoryMemberRepository.getReferenceById(
                new CategoryMemberId(
                        source.getPost().getCategory().getId(),
                        source.getUser().getId()
                )
        );
        target.setUserBriefView(briefViewConverter.convert(member));
        if (source.getParentComment() != null)
            target.setParentCommentId(source.getParentComment().getId());
        target.setId(source.getId());
        target.setCommentBody(source.getCommentBody());
        target.setPostId(source.getPost().getId());
        target.setCreateTime(source.getCreateTime());
        target.setLikes(Hibernate.size(source.getUsersLiked()));
        return target;
    }
}
