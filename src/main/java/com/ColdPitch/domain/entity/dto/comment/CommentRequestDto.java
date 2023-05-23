package com.ColdPitch.domain.entity.dto.comment;

import com.ColdPitch.domain.entity.comment.CommentState;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentRequestDto {
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long postId;

    @NotNull
    private String text;

    @JsonProperty("pCommentId")
    private Long pCommentId;

    private CommentState status;
}
