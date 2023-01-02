package cheshireCat.myRestApi.domain;

import cheshireCat.myRestApi.constant.Role;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBER")
@Data
@Builder
public class Member {
    @Id
    @Column(name = "M_ID")
    @GeneratedValue
    private Long m_id;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "NICK_NAME")
    private String nick_name;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "M_NAME")
    private String m_name;
    @Column(name = "AGE")
    private int age;
    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;
}
