package cheshireCat.myRestApi.domain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "COMMENT")
@Data
@Builder
public class Comment {
    @Id
    @Column(name = "C_ID")
    @GeneratedValue
    private Long c_id;
    @Lob
    @Column(name = "CONTENT")
    private String c_content;
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "PARENT_COMMENT")
    private Comment parent_id;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
}
