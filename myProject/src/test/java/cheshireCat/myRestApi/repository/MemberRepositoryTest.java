package cheshireCat.myRestApi.repository;

import cheshireCat.myRestApi.constant.Role;
import cheshireCat.myRestApi.domain.Member;
import cheshireCat.myRestApi.dto.member.MemberUpdateDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    private void clear() {
        em.flush();
        em.clear();
    }

    @AfterEach
    private void after() {
        em.clear();
    }

    @Test
    @DisplayName("회원_저장_성공")
    public void saveMember() {

        // given
        Member member = Member.builder()
                                .email("test@test.com")
                                .nickName("테스트 계정")
                                .password("1111")
                                .mName("홍길동")
                                .age(22)
                                .role(Role.USER)
                                .build();

        // when
        Member saveMember = memberRepository.save(member);

        // then
        Member findMember =
                memberRepository.findByEmail(saveMember.getEmail()).orElseThrow(EntityNotFoundException::new);

        assertThat(findMember).isSameAs(saveMember);
        assertThat(findMember).isSameAs(member);
    }

    @Test
    @DisplayName("오류_회원가입_이메일_없음")
    public void saveMemberException01() throws Exception {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> Member.builder()
                                                                    .email(null)
                                                                    .nickName("테스트 계정")
                                                                    .password("1111")
                                                                    .mName("홍길동")
                                                                    .age(22)
                                                                    .role(Role.USER)
                                                                    .build());
    }

    @Test
    @DisplayName("오류_회원가입_닉네임_없음")
    public void saveMemberException02() {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> Member.builder()
                                                                    .email("test@test.com")
                                                                    .password("1111")
                                                                    .mName("홍길동")
                                                                    .age(22)
                                                                    .role(Role.USER)
                                                                    .build());
    }

    @Test
    @DisplayName("오류_회원가입_비밀번호_없음")
    public void saveMemberException03() {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> Member.builder()
                                                                    .email("test@test.com")
                                                                    .nickName("테스트 계정")
                                                                    .mName("홍길동")
                                                                    .age(22)
                                                                    .role(Role.USER)
                                                                    .build());
    }

    @Test
    @DisplayName("오류_회원가입_이름_없음")
    public void saveMemberException04() {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> Member.builder()
                                                                    .email("test@test.com")
                                                                    .nickName("테스트 계정")
                                                                    .password("1111")
                                                                    .age(22)
                                                                    .role(Role.USER)
                                                                    .build());
    }

    @Test
    @DisplayName("오류_회원가입_나이_없음")
    public void saveMemberException05() {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> Member.builder()
                                                                    .email("test@test.com")
                                                                    .nickName("테스트 계정")
                                                                    .password("1111")
                                                                    .mName("홍길동")
                                                                    .role(Role.USER)
                                                                    .build());
    }

    @Test
    @DisplayName("오류_회원가입_역할_없음")
    public void saveMemberException06() {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> Member.builder()
                                                                    .email("test@test.com")
                                                                    .nickName("테스트 계정")
                                                                    .password("1111")
                                                                    .mName("홍길동")
                                                                    .age(22)
                                                                    .build());
    }

    @Test
    @DisplayName("오류_회원가입_중복된_이메일_존재")
    public void saveMemberException07() {
        //given
        Member member1 = Member.builder()
                                .email("test@test01.com")
                                .nickName("테스트 계정1")
                                .password("1111")
                                .mName("홍길동")
                                .age(22)
                                .role(Role.USER)
                                .build();

        Member member2 = Member.builder()
                                .email("test@test01.com")
                                .nickName("테스트 계정2")
                                .password("1111")
                                .mName("임꺽정")
                                .age(22)
                                .role(Role.USER)
                                .build();

        memberRepository.save(member1);
        clear();

        memberRepository.save(member2);

        //when, then
        assertThrows(Exception.class, () -> em.flush());
    }

    private void memberSave(Member member) {
        memberRepository.save(member);
        clear();
    }

    private Member createMember() {
        Member member = Member.builder()
                .email("test@test.com")
                .nickName("테스트 계정")
                .password("1111")
                .mName("홍길동")
                .age(22)
                .role(Role.USER)
                .build();

        memberSave(member);

        return member;
    }

    @Test
    @DisplayName("성공_회원수정")
    public void updateMember() {

        // given
        Member member = createMember();

        String updateEmail = "updatePassword";
        String updateNickName = "updateNickName";
        String updateMName = "updateName";
        int updateAge = 33;

        MemberUpdateDto memberUpdateDto = MemberUpdateDto.builder()
                                                            .email(updateEmail)
                                                            .nickName(updateNickName)
                                                            .mName(updateMName)
                                                            .age(updateAge)
                                                            .build();

        Member updateMember =
                memberRepository.findByEmail(member.getEmail()).orElseThrow(EntityNotFoundException::new);

        // when
        updateMember.updateMember(memberUpdateDto);
        memberSave(updateMember);

        // then
        Member findUpdatedMember =
                memberRepository.findByEmail(updateMember.getEmail()).orElseThrow(EntityNotFoundException::new);

        assertThat(findUpdatedMember.getEmail()).isEqualTo(memberUpdateDto.getEmail());
        assertThat(findUpdatedMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(findUpdatedMember.getNickName()).isEqualTo(memberUpdateDto.getNickName());
        assertThat(findUpdatedMember.getMName()).isEqualTo(memberUpdateDto.getMName());
        assertThat(findUpdatedMember.getAge()).isEqualTo(memberUpdateDto.getAge());
    }

    @Test
    @DisplayName("성공_회원삭제")
    public void delete() {

        // given
        Member member = createMember();

        // when
        memberRepository.delete(member);
        clear();

        // then
        assertThrows(Exception.class, () -> memberRepository.findByEmail(member.getEmail())
                                                            .orElseThrow(EntityNotFoundException::new));
    }

    @Test
    @DisplayName("성공_회원가입시_생성시간_등록")
    public void timeCheck() {

        // given
        Member member = createMember();

        // when
        Member findMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(EntityNotFoundException::new);

        // then
        assertThat(findMember.getRegTime()).isNotNull();
        assertThat(findMember.getLastModifiedTime()).isNotNull();
    }
}