package cheshireCat.myRestApi.security.filter;

import cheshireCat.myRestApi.dto.member.MemberLoginDto;
import cheshireCat.myRestApi.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    public static final String HTTP_METHOD = "POST";
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/login", HTTP_METHOD);
    private static final String CONTENT_TYPE = "application/json";

    public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);

        this.objectMapper = objectMapper;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

        if (!request.getMethod().equals(HTTP_METHOD)) {
            log.error("POST 요청이 아닙니다!");
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else if (!request.getContentType().equals(CONTENT_TYPE)) {
            log.error("JSON이 아닙니다!");
            log.error("ContentType : " + request.getContentType());
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        MemberLoginDto memberLoginDto = objectMapper.readValue(messageBody, MemberLoginDto.class);

        String username = memberLoginDto.getEmail();
        String password = memberLoginDto.getPassword();

        log.info("attemptAuthentication username = {}", username);
        log.info("attemptAuthentication password = {}", password);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
