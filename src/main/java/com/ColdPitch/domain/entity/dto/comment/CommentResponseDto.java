package com.ColdPitch.domain.entity.dto.comment;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.entity.comment.CommentState;
import com.ColdPitch.domain.entity.dto.post.PostResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentResponseDto {
    @NotNull
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long postId;

    @NotNull
    private String text;

    @JsonProperty("pCommentId")
    private Long pCommentId;

    @NotNull
    private CommentState status;

    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
            .id(comment.getId())
            .userId(comment.getUserId())
            .postId(comment.getPostId())
            .text(comment.getText())
            .pCommentId(comment.getPCommentId())
            .status(comment.getStatus())
            .createAt(comment.getCreateAt())
            .createBy(comment.getCreatedBy())
            .modifiedAt(comment.getModifiedAt())
            .modifiedBy(comment.getModifiedBy())
            .build();
    }
}
