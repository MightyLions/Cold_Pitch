package com.ColdPitch.domain.entity;

import com.ColdPitch.domain.entity.comment.CommentState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "p_comment_id")
    private Long pId;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CommentState status;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long postId;

    public void setText(String text) {
        this.text = text;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public void setStatus(CommentState status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(
                o)) {
            return false;
        }
        Comment comment = (Comment) o;
        return getId() != null && Objects.equals(getId(),
                comment.getId());
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + id +
                ", userId=" + userId +
                ", postId=" + postId +
                ", text='" + text + '\'' +
                ", " + super.toString() +
                "}";
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
