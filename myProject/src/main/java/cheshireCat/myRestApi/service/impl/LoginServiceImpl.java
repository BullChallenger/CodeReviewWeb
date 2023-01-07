package cheshireCat.myRestApi.service.impl;

import cheshireCat.myRestApi.domain.Member;
import cheshireCat.myRestApi.repository.MemberRepository;
import cheshireCat.myRestApi.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 가진 계정이 존재하지 않습니다."));

        return User.builder()
                            .username(member.getEmail())
                            .password(member.getPassword())
                            .roles(member.getRole().name())
                            .build();
    }
}
