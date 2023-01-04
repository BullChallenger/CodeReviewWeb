package cheshireCat.myRestApi.domain;

import cheshireCat.myRestApi.constant.Role;
import cheshireCat.myRestApi.dto.member.MemberUpdateDto;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @Column(name = "M_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mId;
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    @Column(name = "NICK_NAME", nullable = false)
    private String nickName;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "M_NAME", nullable = false)
    private String mName;
    @Column(name = "AGE", nullable = false)
    private int age;
    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String email, String nickName, String password, String mName, Integer age, Role role) {
        Assert.hasText(email, "email must not be blank");
        Assert.hasText(nickName, "nickName must not be blank");
        Assert.hasText(password, "password must not be blank");
        Assert.hasText(mName, "mName must not be blank");
        Assert.notNull(age, "age must not be null");
        Assert.notNull(role, "role must not be null");

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

    public void updateMember (MemberUpdateDto memberUpdateDto) {
        this.email = memberUpdateDto.getEmail();
        this.nickName = memberUpdateDto.getNickName();
        this.mName = memberUpdateDto.getMName();
        this.age = memberUpdateDto.getAge();
    }
}
