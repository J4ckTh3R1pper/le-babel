package dayp308.chatroom.entity;

import dayp308.chatroom.entity.comment.PostComment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comment_closure")
public class CommentClosure {
    @EmbeddedId
    private CommentClosureId id;

    @MapsId("ancestor")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ancestor", nullable = false)
    private PostComment ancestor;

    @MapsId("descendant")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "descendant", nullable = false)
    private PostComment descendant;

    @NotNull
    @Column(name = "depth", nullable = false)
    private Byte depth;

}