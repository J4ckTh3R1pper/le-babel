package dayp308.lebabel.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dayp308.lebabel.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "tb_chat_message_private")
public class ChatMessagePrivate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Lob
    @Column(name = "message_text", nullable = false)
    private String messageText;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to")
    private ChatMessagePrivate replyTo;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_id", nullable = false)
    private User target;

    @Column(name = "create_time", nullable = false)
    private Instant createTime = Instant.now();
}