package com.ColdPitch.domain.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = true)
    private Long boardId;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Post{" +
            "postId=" + postId +
            ", title='" + title + '\'' +
            ", text='" + text + '\'' +
            ", status='" + status + '\'' +
            ", category='" + category + '\'' +
            ", userId=" + userId +
            ", boardId=" + boardId +
            ", " + super.toString() +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(postId, post.getPostId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, title, text, status, category, userId);
    }

}


