package com.ColdPitch.domain.entity.dto.comment;

import com.ColdPitch.domain.entity.comment.CommentState;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "댓글 요청 DTO")
public class CommentRequestDto {
    @ApiParam(name = "댓글 id")
    private Long id;

    @ApiParam(name = "댓글을 작성한 유저 id")
    @NotNull
    @Size
    private Long userId;

    @ApiParam(name = "댓글을 작성한 게시글 id")
    @NotNull
    @Size
    private Long postId;

    @ApiParam(name = "댓글 내용")
    @NotBlank
    private String text;

    @JsonProperty("pCommentId")
    @ApiParam(name = "대댓글 id", allowEmptyValue = true)
    @Size
    private Long pCommentId;

    @ApiParam(name = "현재 댓글 상태 (OPEN, DELETED)", allowEmptyValue = true)
    private CommentState status;
}
