package dayp308.chatroom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "tb_chat_message_private")
public class ChatMessagePrivate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private dayp308.chatroom.entity.User sender;

    @Lob
    @Column(name = "message_text", nullable = false)
    private String messageText;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reply_to")
    private ChatMessagePrivate replyTo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_id", nullable = false)
    private dayp308.chatroom.entity.User target;

    @OneToMany(mappedBy = "replyTo")
    private Set<ChatMessagePrivate> tbChatMessagePrivates = new LinkedHashSet<>();

}