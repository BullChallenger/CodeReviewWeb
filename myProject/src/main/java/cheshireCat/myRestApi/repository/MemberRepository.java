package cheshireCat.myRestApi.repository;

import cheshireCat.myRestApi.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Member> findByRefreshToken(String refreshToken);
}
