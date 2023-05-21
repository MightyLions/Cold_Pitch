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
    public PostResponseDto createPost(String userEmail, PostRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail);
        Post post = Post.toEntity(requestDto.getTitle(), requestDto.getText(), requestDto.getCategory(), user.getId(), PostState.OPEN);
        postRepository.save(post);
        return convertDto(post, userEmail);
    }

    @Transactional
    public PostResponseDto updatePost(String userEmail, PostRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail);
        Post post = postRepository.findById(requestDto.getId()).orElseThrow();
        post.setTitle(requestDto.getTitle());
        post.setText(requestDto.getText());
        post.setCategory(requestDto.getCategory());
        // 게시 유저와 유저가 같으면 권한 부여
        return convertDto(post, userEmail);
    }

    @Transactional
    public String deletePost(String userEmail, PostRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getId()).orElseThrow();
        post.setStatus(PostState.DELETED);
        // 게시 유저와 유저가 같으면 권한 부여
        return "post deleted successfully";
    }

    public PostResponseDto getPost(String userEmail, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findByEmail(userEmail);
        return convertDto(post, user.getName());
    }

    public PostResponseDto convertDto(Post post, String userName) {
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
