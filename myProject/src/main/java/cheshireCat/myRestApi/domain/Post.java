package cheshireCat.myRestApi.domain;

import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "POST")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue
    @Column(name = "P_ID")
    private Long pId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member pWriter;
    @Column(name = "TITLE")
    private String title;
    @Lob
    @Column(name = "P_CONTENT")
    private String pContent;
    @Column(name = "FILE_PATH")
    private String filePath;

    @Builder
    public Post(Member pWriter, String title, String pContent, String filePath) {
        this.pWriter = pWriter;
        this.title = title;
        this.pContent = pContent;
        this.filePath = filePath;
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    public void addComment(Comment comment) {
        commentList.add(comment);
    }
}
