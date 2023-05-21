package com.ColdPitch.domain.entity.dto.comment;

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
    private String status;

    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
