package com.ColdPitch.domain.entity.dto.post;

import com.ColdPitch.domain.entity.post.Category;
import com.ColdPitch.domain.entity.post.LikeState;
import com.ColdPitch.domain.entity.post.PostState;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String text;
    private String userName;
    private Category category;
    private PostState status;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
    // 좋아요 관련
    private int likeCnt;
    private int dislikeCnt;
    private LikeState userChoice;
}
