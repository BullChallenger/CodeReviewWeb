package cheshireCat.myRestApi.mapper;

import cheshireCat.myRestApi.domain.Member;
import cheshireCat.myRestApi.dto.member.MemberSignUpDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberSignUpMapper extends GenericMapper<MemberSignUpDto, Member> {

}
