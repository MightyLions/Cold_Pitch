package com.ColdPitch.domain.entity;

import com.ColdPitch.domain.entity.dto.post.PostRequestDto;
import com.ColdPitch.domain.entity.post.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;


import com.ColdPitch.domain.entity.post.PostState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private PostState status;

    @Column(nullable = false)
    private Category category;


    @Column
    private Long boardId;

    @Column(nullable = false)
    private int likeCnt;

    @Column(nullable = false)
    private int dislikeCnt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private List<Comment> comments;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setStatus(PostState status) {
        this.status = status;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void plusLike() {
        this.likeCnt++;
    }

    public void minusLike() {
        this.likeCnt = Math.max(this.likeCnt - 1, 0);
    }

    public void plusDislike() {
        this.dislikeCnt++;
    }

    public void minusDislike() {
        this.dislikeCnt = Math.max(this.dislikeCnt - 1, 0);
    }

    @Override
    public String toString() {
        return "Post{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", text='" + text + '\'' +
            ", status='" + status + '\'' +
            ", category='" + category + '\'' +
            ", userId=" + user.toString() +
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
        return Objects.equals(id, post.getId());
    }

    public static Post toEntity(PostRequestDto requestDto, User user) {
        return Post.builder()
            .title(requestDto.getTitle())
            .text(requestDto.getText())
            .category(requestDto.getCategory())
            .user(user)
            .status(requestDto.getStatus())
            .comments(new ArrayList<>())
            .build();
    }

    public void updatePost(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.text = requestDto.getText();
        this.category = requestDto.getCategory();
    }

}


