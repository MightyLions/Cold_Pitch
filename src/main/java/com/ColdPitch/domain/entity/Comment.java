package com.ColdPitch.domain.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity
@Table(name = "comment")
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    Long commentId;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "poster_id", nullable = false)
    Long posterId;

    public void setText(String text) {
        this.text = text;
    }

    @Column(name = "text", nullable = false)
    String text;

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
        return getCommentId() != null && Objects.equals(getCommentId(),
            comment.getCommentId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Comment {" +
            "commentId=" + commentId +
            ", userId=" + userId +
            ", posterId=" + posterId +
            ", text='" + text + '\'' +
            ", createAt='" + super.getCreateAt() + '\'' +
            ", updatedAt-'" + super.getUpdatedAt() + '\'' +
            '}';
    }
}
