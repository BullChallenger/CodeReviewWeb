package cheshireCat.myRestApi.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        String errorMessage;

        if (exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException){
            errorMessage="이메일 혹은 비밀번호가 일치하지 않습니다.";
        }else if (exception instanceof UsernameNotFoundException){
            errorMessage="해당 이메일을 사용하는 계정이 존재하지 않습니다.";
        }
        else{
            errorMessage="명확한 원인을 파악할 수 없는 에러가 발생했습니다!";
        }

        log.error(errorMessage);

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("fail " + response.getContentType());

        log.info("로그인 실패");
    }
}