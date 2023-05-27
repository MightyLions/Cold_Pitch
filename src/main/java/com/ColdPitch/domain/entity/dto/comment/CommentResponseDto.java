package com.ColdPitch.domain.entity.dto.comment;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.entity.comment.CommentState;
import com.ColdPitch.domain.entity.dto.post.PostResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "댓글 응답 DTO")
public class CommentResponseDto {
    @NotNull
    @ApiModelProperty(name = "댓글 id")
    private Long id;

    @NotNull
    @ApiModelProperty(name = "댓글을 작성한 유저 id")
    private Long userId;

    @NotNull
    @ApiModelProperty(name = "댓글을 작성한 게시글 id")
    private Long postId;

    @NotNull
    @ApiModelProperty(name = "댓글 내용")
    private String text;

    @JsonProperty("pCommentId")
    @ApiModelProperty(name = "대댓글 id", allowEmptyValue = true)
    private Long pCommentId;

    @NotNull
    @ApiModelProperty(name = "댓글 현재 상태(OPEN, DELETED)")
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
