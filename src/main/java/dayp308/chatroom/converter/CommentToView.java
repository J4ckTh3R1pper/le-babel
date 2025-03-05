package dayp308.chatroom.converter;

import dayp308.chatroom.entity.CategoryMember;
import dayp308.chatroom.entity.PostComment;
import dayp308.chatroom.entity.id.CategoryMemberId;
import dayp308.chatroom.entity.view.comment.CommentView;
import dayp308.chatroom.repository.CategoryMemberRepository;
import dayp308.chatroom.repository.CommentLikeRepository;
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
                        source.getPost().getPostCategory().getId(),
                        source.getCommentUser().getId()
                )
        );
        target.setUserBriefView(briefViewConverter.convert(member));
        if (source.getParentComment() != null)
            target.setParentCommentId(source.getParentComment().getId());
        target.setCommentId(source.getId());
        target.setCommentBody(source.getCommentBody());
        target.setPostId(source.getPost().getId());
        target.setLiked(false);
        target.setCommentCreateTime(source.getCommentCreateTime());
        target.setLikes( commentLikeRepository.countByCommentId( source.getId()) );
        return target;
    }
}
