package cheshireCat.myRestApi.encoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class PasswordEncodingTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("성공_비밀번호_인코딩")
    public void encodingTest() {
        // given
        String password = "1213131";

        // when
        String encodedPassword = passwordEncoder.encode(password);

        // then
        assertThat(encodedPassword).isNotEqualTo(password);
    }

    @Test
    @DisplayName("성공_비밀번호_랜덤_변경")
    public void encodingTest02() {
        // given
        String password = "1213131";

        // when
        String encodedPassword = passwordEncoder.encode(password);
        String encodedPassword02 = passwordEncoder.encode(password);

        // then
        assertThat(encodedPassword).isNotEqualTo(encodedPassword02);
    }

    @Test
    @DisplayName("성공_비밀번호_매치")
    public void encodingTest03() {
        // given
        String password = "1213131";

        // when
        String encodedPassword = passwordEncoder.encode(password);

        // then
        assertThat(passwordEncoder.matches(password, encodedPassword)).isTrue();
    }
}

