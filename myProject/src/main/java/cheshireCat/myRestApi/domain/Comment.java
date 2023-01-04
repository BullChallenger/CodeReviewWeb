package cheshireCat.myRestApi.domain;

import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMMENT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @Column(name = "c_id")
    @GeneratedValue
    private Long cId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_writer_id")
    private Member cWriter;
    @Lob
    @Column(name = "c_content")
    private String cContent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public Comment(Member cWriter, String cContent, Comment parent, boolean isDeleted) {
        this.cWriter = cWriter;
        this.cContent = cContent;
        this.parent = parent;
        this.isDeleted = isDeleted;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<Comment> childCommentList = new ArrayList<>();

    public void addChild(Comment childComment) {
        childCommentList.add(childComment);
    }
}
