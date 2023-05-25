package com.ColdPitch.domain.entity.comment;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 조회시 요청하는 타입")
public enum CommentRequestType {
    @Schema(description = "모든 댓글을 조회")
    ALL,
    @Schema(description = "게시글 id를 기반으로 조회")
    POST_ID,
    @Schema(description = "댓글 id를 기반으로 조회")
    COMMENT_ID,
    @Schema(description = "유저 id를 기반으로 조회")
    USER_ID
}
