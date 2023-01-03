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
    @Column(name = "C_ID")
    @GeneratedValue
    private Long cId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "C_WRITER_ID")
    private Member cWriter;
    @Lob
    @Column(name = "C_CONTENT")
    private String cContent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Comment parent;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @Builder
    public Comment(Member cWriter, String cContent, Comment parent, boolean isDeleted) {
        this.cWriter = cWriter;
        this.cContent = cContent;
        this.parent = parent;
        this.isDeleted = isDeleted;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<Comment> childCommentList = new ArrayList<>();

    public void addChild(Comment childComment) {
        childCommentList.add(childComment);
    }
}
