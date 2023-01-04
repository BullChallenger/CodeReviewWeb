package cheshireCat.myRestApi.mapper;

import cheshireCat.myRestApi.domain.Member;
import cheshireCat.myRestApi.dto.member.MemberUpdateDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberUpdateMapper extends GenericMapper<MemberUpdateDto, Member> {

}
