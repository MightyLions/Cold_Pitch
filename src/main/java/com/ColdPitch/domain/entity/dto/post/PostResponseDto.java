package com.ColdPitch.domain.entity.dto.post;

import com.ColdPitch.domain.entity.Post;
import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.dto.comment.CommentResponseDto;
import com.ColdPitch.domain.entity.post.Category;
import com.ColdPitch.domain.entity.post.LikeState;
import com.ColdPitch.domain.entity.post.PostState;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostResponseDto {
    private Long id;
    private String title;
    private String text;
    private String userName;
    private Category category;
    private PostState status;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
    private String createdBy;
    private String modifiedBy;
    private List<CommentResponseDto> comments;

    // 좋아요 관련
    private int likeCnt;
    private int dislikeCnt;
    private LikeState userChoice;

    public static PostResponseDto of(Post post, LikeState userChoice) {
        return PostResponseDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .text(post.getText())
            .userName(post.getUser().getName())
            .category(post.getCategory())
            .createAt(post.getCreateAt())
            .modifyAt(post.getModifiedAt())
            .createdBy(post.getCreatedBy())
            .modifiedBy(post.getModifiedBy())
            .status(post.getStatus())
            .likeCnt(post.getLikeCnt())
            .dislikeCnt(post.getDislikeCnt())
            .userChoice(userChoice)
            .comments(post.getComments()
                .stream()
                .map(CommentResponseDto::of)
                .collect(Collectors.toList()))
            .build();
    }

}
