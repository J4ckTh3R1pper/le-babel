package dayp308.lebabel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dayp308.lebabel.entity.comment.PostComment;
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
    private CommentClosureId id = new CommentClosureId();

    @JsonIgnore
    @MapsId("ancestor")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ancestor", nullable = false)
    private PostComment ancestor;

    @JsonIgnore
    @MapsId("descendant")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "descendant", nullable = false)
    private PostComment descendant;

    @NotNull
    @Column(name = "depth", nullable = false)
    private Long depth;

}