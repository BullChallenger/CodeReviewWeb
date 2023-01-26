package cheshireCat.myRestApi.jwt;

import cheshireCat.myRestApi.constant.Role;
import cheshireCat.myRestApi.domain.Member;
import cheshireCat.myRestApi.repository.MemberRepository;
import cheshireCat.myRestApi.security.service.JwtService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class JwtServiceTest {

    @Autowired
    JwtService jwtService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERNAME_CLAIM = "username";
    private static final String BEARER = "Bearer ";

    private String username = "test@test.com";

    @BeforeEach
    public void init(){
        Member member = Member.builder().email(username).password("12345").mName("홍길동").nickName("테스트계정")
                        .role(Role.USER).age(22).build();
        memberRepository.save(member);
        clear();
    }

    private void clear(){
        em.flush();
        em.clear();
    }

    private DecodedJWT getVerify(String token) {
        return JWT.require(HMAC512(secret)).build().verify(token);
    }

    private HttpServletRequest setRequest(String accessToken, String refreshToken) throws IOException {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        jwtService.sendRefreshAndAccessToken(mockHttpServletResponse, accessToken, refreshToken);

        String headerAccessToken = mockHttpServletResponse.getHeader(accessHeader);
        String headerRefreshToken = mockHttpServletResponse.getHeader(refreshHeader);

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

        mockHttpServletRequest.addHeader(accessHeader, BEARER + headerAccessToken);
        mockHttpServletRequest.addHeader(refreshHeader, BEARER + headerRefreshToken);

        return mockHttpServletRequest;
    }

    @Test
    @DisplayName("[CREATE]_성공_엑세스_토큰_발급")
    public void createAccessTokenTest() {
        // given, when
        String accessToken = jwtService.createAccessToken(username);
        DecodedJWT verify = getVerify(accessToken);

        String subject = verify.getSubject();
        String findUsername = verify.getClaim(USERNAME_CLAIM).asString();

        // then
        assertThat(findUsername).isEqualTo(username);
        assertThat(subject).isEqualTo(ACCESS_TOKEN_SUBJECT);
    }

    @Test
    @DisplayName("[CREATE]_성공_리프레쉬_토큰_발급")
    public void createRefreshTokenTest() throws Exception {
        //given, when
        String refreshToken = jwtService.createRefreshToken();
        DecodedJWT verify = getVerify(refreshToken);
        String subject = verify.getSubject();
        String username = verify.getClaim(USERNAME_CLAIM).asString();

        //then
        assertThat(subject).isEqualTo(REFRESH_TOKEN_SUBJECT);
        assertThat(username).isNull();
    }

    @Test
    @DisplayName("[RESPONSE]_성공_엑세스_토큰_헤더")
    public void setAccessTokenHeaderTest() throws IOException {
        // given
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.setAccessTokenHeader(mockHttpServletResponse, accessHeader);

        // when
        jwtService.sendRefreshAndAccessToken(mockHttpServletResponse, accessToken, refreshToken);

        // then
        String headerAccessToken = mockHttpServletResponse.getHeader(accessHeader);

        assertThat(headerAccessToken).isEqualTo(accessToken);
    }

    @Test
    @DisplayName("[RESPONSE]_성공_리프레쉬_토큰_헤더")
    public void setRefreshTokenHeaderTest() throws IOException {
        // given
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.setAccessTokenHeader(mockHttpServletResponse, accessHeader);

        // when
        jwtService.sendRefreshAndAccessToken(mockHttpServletResponse, accessToken, refreshToken);

        // then
        String headerRefreshToken = mockHttpServletResponse.getHeader(refreshHeader);

        assertThat(headerRefreshToken).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("[RESPONSE]_성공_토큰_전송")
    public void sendAccessAndRefreshTokenTest() throws IOException {
        // given
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken();

        // when
        jwtService.sendRefreshAndAccessToken(mockHttpServletResponse, accessToken, refreshToken);

        String headerAccessToken = mockHttpServletResponse.getHeader(accessHeader);
        String headerRefreshToken = mockHttpServletResponse.getHeader(refreshHeader);

        // then
        assertThat(headerAccessToken).isEqualTo(accessToken);
        assertThat(headerRefreshToken).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("[REQUEST]_성공_엑세스_토큰_추출")
    public void extractAccessTokenTest() throws Exception {
        // given
        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken();

        HttpServletRequest httpServletRequest = setRequest(accessToken, refreshToken);

        // when
        String extractAccessToken = jwtService.extractAccessToken(httpServletRequest).orElseThrow(
                () -> new Exception("토큰이 존재하지 않습니다.")
        );

        // then
        assertThat(extractAccessToken).isEqualTo(accessToken);
        assertThat(getVerify(extractAccessToken).getClaim(USERNAME_CLAIM).asString()).isEqualTo(username);
    }

    @Test
    @DisplayName("[REQUEST]_성공_리프레쉬_토큰_추출")
    public void extractRefreshToken() throws Exception {
        // given
        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken();

        HttpServletRequest httpServletRequest = setRequest(accessToken, refreshToken);

        // when
        String extractRefreshToken = jwtService.extractRefreshToken(httpServletRequest).orElseThrow(
                () -> new Exception("토큰이 존재하지 않습니다.")
        );

        // then
        assertThat(extractRefreshToken).isEqualTo(refreshToken);
        assertThat(getVerify(extractRefreshToken).getSubject()).isEqualTo(REFRESH_TOKEN_SUBJECT);
    }

    @Test
    @DisplayName("[REQUEST]_성공_유저이름_추출")
    public void extractUsernameTest() throws Exception {
        // given
        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken();

        HttpServletRequest httpServletRequest = setRequest(accessToken, refreshToken);

        String requestAccessToken = jwtService.extractAccessToken(httpServletRequest).orElseThrow(
                () -> new Exception("토큰이 존재하지 않습니다.")
        );

        // when
        String extractUsername = jwtService.extractUsername(requestAccessToken).orElseThrow(
                () -> new Exception("토큰이 존재하지 않습니다.")
        );;

        // then
        assertThat(extractUsername).isEqualTo(username);
    }

    @Test
    @DisplayName("[BOOLEAN]_성공_토큰_유효성_검사")
    public void isTokenValidTest() {
        // given
        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken();

        // when, then
        assertThat(jwtService.isTokenValid(accessToken)).isTrue();
        assertThat(jwtService.isTokenValid(refreshToken)).isTrue();
        assertThat(jwtService.isTokenValid(accessToken + "a")).isFalse();
        assertThat(jwtService.isTokenValid(refreshToken + "a")).isFalse();
    }
}
