package dayp308.lebabel.bean.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dayp308.lebabel.bean.entity.category.PostCategory;
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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private PostCategory category;

    @Column(name = "channel_name", nullable = false, length = 16)
    private String channelName;

    @JsonManagedReference
    @OneToMany(mappedBy = "target")
    private Set<ChatMessagePublic> chatMessages = new LinkedHashSet<>();

}