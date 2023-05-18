package com.ColdPitch.domain.entity;

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

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String text;

    @Column(name = "p_comment_id")
    private Long pCommentId;

    public void setText(String text) {
        this.text = text;
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
