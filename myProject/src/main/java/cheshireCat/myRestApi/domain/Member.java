package cheshireCat.myRestApi.domain;

import cheshireCat.myRestApi.constant.Role;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @Column(name = "M_ID")
    @GeneratedValue
    private Long mId;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "NICK_NAME")
    private String nickName;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "M_NAME")
    private String mName;
    @Column(name = "AGE")
    private int age;
    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String email, String nickName, String password, String mName, Integer age, Role role) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.mName = mName;
        this.age = age;
        this.role = role;
    }

    @OneToMany(mappedBy = "pWriter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();
    @OneToMany(mappedBy = "cWriter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    public void addPost(Post post) {
        postList.add(post);
    }
    public void addComment(Comment comment) {
        commentList.add(comment);
    }
}
