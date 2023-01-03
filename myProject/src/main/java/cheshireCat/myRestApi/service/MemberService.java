package cheshireCat.myRestApi.service;

import cheshireCat.myRestApi.dto.member.MemberSignUpDto;
import cheshireCat.myRestApi.dto.member.MemberUpdateDto;

public interface MemberService {
    void signUp(MemberSignUpDto memberSignUpDto);
    void update(MemberUpdateDto memberUpdateDto);

//    void updatePassword(String checkPassword, String newPassword);
//
//    void withdraw(String checkPassword);

//    MemberInfoDto getInfo(Long id);
}
