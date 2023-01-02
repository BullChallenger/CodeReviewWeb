package cheshireCat.myRestApi.domain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "POST")
@Data
@Builder
public class Post {

}
