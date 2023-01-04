package cheshireCat.myRestApi.dto.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import org.springframework.util.Assert;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberSignUpDto {
    @Email
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;
    private String nickName;
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
    @NotBlank(message = "실명은 필수 입력 값입니다.")
    private String mName;
    @NotNull(message = "나이는 필수 입력 값입니다.")
    private Integer age;
    @Builder
    public MemberSignUpDto(String email, String nickName, String password, String mName, Integer age) {
        Assert.hasText(email, "email must not be blank");
        Assert.hasText(password, "password must not be blank");
        Assert.hasText(mName, "mName must not be blank");
        Assert.notNull(age, "age must not be null");

        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.mName = mName;
        this.age = age;
    }
}
