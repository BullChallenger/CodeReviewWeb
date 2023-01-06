package cheshireCat.myRestApi.login;

import cheshireCat.myRestApi.constant.Role;
import cheshireCat.myRestApi.domain.Member;
import cheshireCat.myRestApi.dto.member.MemberLoginDto;
import cheshireCat.myRestApi.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class JsonLoginTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;
    @Autowired
    PasswordEncoder BCryptPasswordEncoder;
    @Autowired
    ObjectMapper objectMapper;

    private static final String LOGIN_URL = "/login";

    private void clear() {
        em.flush();
        em.clear();
    }

    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "11111";

    @BeforeEach
    private void init() {
        memberRepository.save(Member.builder()
                .email(EMAIL)
                .password(BCryptPasswordEncoder.encode(PASSWORD))
                .nickName("테스트용 계정")
                .mName("테스트")
                .age(22)
                .role(Role.USER)
                .build());

        clear();
    }

    private MemberLoginDto createMemberLoginDto(String email, String password) {
        return MemberLoginDto.builder()
                                .email(email)
                                .password(password)
                                .build();
    }

    private ResultActions perform(String url, MediaType contentType, MemberLoginDto memberLoginDto) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(String.valueOf(contentType))
                .content(objectMapper.writeValueAsString(memberLoginDto)));
    }

    @Test
    @DisplayName("성공_로그인")
    public void loginTest() throws Exception {
        // given
        MemberLoginDto memberLoginDto = createMemberLoginDto(EMAIL, PASSWORD);

        // when, then
        MvcResult result = perform(LOGIN_URL, APPLICATION_JSON, memberLoginDto)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

}
