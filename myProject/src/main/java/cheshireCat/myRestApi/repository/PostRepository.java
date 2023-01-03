package cheshireCat.myRestApi.repository;

import cheshireCat.myRestApi.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
