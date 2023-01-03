package cheshireCat.myRestApi.service.impl;

import cheshireCat.myRestApi.domain.Member;
import cheshireCat.myRestApi.dto.member.MemberSignUpDto;
import cheshireCat.myRestApi.mapper.MemberMapper;
import cheshireCat.myRestApi.repository.MemberRepository;
import cheshireCat.myRestApi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    @Override
    public void signUp(MemberSignUpDto memberSignUpDto) {
        Member member = memberMapper.toEntity(memberSignUpDto);
        memberRepository.save(member);
    }
}
