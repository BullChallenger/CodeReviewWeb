package cheshireCat.myRestApi.service.impl;

import cheshireCat.myRestApi.domain.Member;
import cheshireCat.myRestApi.dto.member.MemberSignUpDto;
import cheshireCat.myRestApi.dto.member.MemberUpdateDto;
import cheshireCat.myRestApi.mapper.MemberSignUpMapper;
import cheshireCat.myRestApi.mapper.MemberUpdateMapper;
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
    private final MemberSignUpMapper memberSignUpMapper;
    private final MemberUpdateMapper memberUpdateMapper;
    @Override
    public void signUp(MemberSignUpDto memberSignUpDto) {
        Member member = memberSignUpMapper.toEntity(memberSignUpDto);
        validateDuplicateMember(member);

        memberRepository.save(member);
    }

    @Override
    public void update(MemberUpdateDto memberUpdateDto) {
//        Member member = memberRepository.findByEmail()
    }

    private void validateDuplicateMember(Member member) {
        boolean alreadyExists = memberRepository.existsByEmail(member.getEmail());
        if (alreadyExists) throw new IllegalStateException("이미 가입된 회원입니다.");
    }
}
