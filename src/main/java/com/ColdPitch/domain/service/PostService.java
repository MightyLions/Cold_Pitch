package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Post;
import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.dto.post.PostRequestDto;
import com.ColdPitch.domain.entity.dto.post.PostResponseDto;
import com.ColdPitch.domain.entity.post.PostState;
import com.ColdPitch.domain.repository.PostRepository;
import com.ColdPitch.domain.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @Transactional
    public PostResponseDto createPost(String userName, PostRequestDto requestDto) {
        User user = userRepository.findByName(userName);
        Post post = Post.toEntity(requestDto.getTitle(), requestDto.getText(), requestDto.getCategory(), user.getId(), PostState.OPEN);
        postRepository.save(post);
        return PostResponseDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .text(post.getText())
            .userName(userName)
            .category(post.getCategory())
            .createAt(post.getCreateAt())
            .modifyAt(post.getModifiedAt())
            .status(post.getStatus())
            .build();
    }

    @Transactional
    public PostResponseDto updatePost(String userName, PostRequestDto requestDto) {
        User user = userRepository.findByName(userName);
        Post post = postRepository.findById(requestDto.getId()).orElseThrow();
        post.setTitle(requestDto.getTitle());
        post.setText(requestDto.getText());
        post.setCategory(requestDto.getCategory());
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .text(post.getText())
                .userName(userName)
                .category(post.getCategory())
                .createAt(post.getCreateAt())
                .modifyAt(post.getModifiedAt())
                .status(post.getStatus())
                .build();
    }
}
