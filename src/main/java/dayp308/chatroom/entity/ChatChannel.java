package dayp308.chatroom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "tb_chat_channel")
public class ChatChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private PostCategory category;

    @Column(name = "channel_name", nullable = false, length = 16)
    private String channelName;

    @OneToMany(mappedBy = "target")
    private Set<ChatMessagePublic> chatMessages = new LinkedHashSet<>();

}