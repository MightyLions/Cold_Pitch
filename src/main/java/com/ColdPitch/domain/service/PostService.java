package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Dislike;
import com.ColdPitch.domain.entity.Like;
import com.ColdPitch.domain.entity.Post;
import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.dto.post.PostRequestDto;
import com.ColdPitch.domain.entity.dto.post.PostResponseDto;
import com.ColdPitch.domain.entity.post.LikeState;
import com.ColdPitch.domain.entity.post.PostState;
import com.ColdPitch.domain.repository.DislikeRepository;
import com.ColdPitch.domain.repository.LikeRepository;
import com.ColdPitch.domain.repository.PostRepository;
import com.ColdPitch.domain.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;

    @Transactional
    public PostResponseDto createPost(String userEmail, PostRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        requestDto.setStatus(PostState.OPEN);
        Post post = Post.toEntity(requestDto,user.getId());
        postRepository.save(post);
        return PostResponseDto.of(post, LikeState.UNSELECTED);
    }

    @Transactional
    public PostResponseDto updatePost(String userEmail, PostRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        // 게시 유저와 유저가 같으면 권한 부여
        Post post = postRepository.findById(requestDto.getId()).orElseThrow();
        post.updatePost(requestDto);
        return PostResponseDto.of(post, getLikeDislike(user.getId(), post.getId()));
    }

    @Transactional
    public PostResponseDto postStateChange(String userEmail, Long postId, PostState state) {
        // 게시 유저와 유저가 같으면 권한 부여
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        post.setStatus(state);
        return PostResponseDto.of(post, getLikeDislike(user.getId(), postId));
    }

    public PostResponseDto getPost(String userEmail, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return PostResponseDto.of(post, getLikeDislike(user.getId(), postId));
    }

    public List<PostResponseDto> getAllPost() {
        List<Post> postList = postRepository.findAll();
        List<PostResponseDto> responseDtos = new ArrayList<>();
        for (Post post : postList) {
            responseDtos.add(PostResponseDto.of(post,null));
        }
        return responseDtos;
    }

    @Transactional
    public PostResponseDto likePost(String userEmail, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        // 이미 싫어요 누른 게시물은 좋아요 불가능
        dislikeRepository.findByUserIdAndPostId(user.getId(), postId).ifPresent(v -> {
            log.info("싫어요를 선택한 게시물에는 좋아요를 선택할 수 없습니다."); // 익셉션 던지기
        });
        // 이미 좋아요 누른 게시물은 좋아요 취소
        likeRepository.findByUserIdAndPostId(user.getId(), postId).ifPresentOrElse(
            like -> {
                likeRepository.deleteById(like.getId());
                post.minusLike();
            }, () -> {
                Like like = Like.builder().userId(user.getId()).postId(postId).build();
                likeRepository.save(like);
                post.plusLike();
            }
        );
        return PostResponseDto.of(post, getLikeDislike(user.getId(), postId));
    }

    @Transactional
    public PostResponseDto dislikePost(String userEmail, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        // 이미 좋어요 누른 게시물은 싫어요 불가능
        likeRepository.findByUserIdAndPostId(user.getId(), postId).ifPresent(v->{
            log.info("좋아요를 선택한 게시물에는 싫어요를 선택할 수 없습니다."); // 익셉션 던지기
        });
        // 이미 싫어요를 누른 게시물은 싫어요 취소
        dislikeRepository.findByUserIdAndPostId(user.getId(), postId).ifPresentOrElse(
            dislike -> {
                dislikeRepository.deleteById(dislike.getId());
                post.minusDislike();
            }, () -> {
                Dislike dislike = Dislike.builder().userId(user.getId()).postId(postId).build();
                dislikeRepository.save(dislike);
                post.plusDislike();
            }
        );
        return PostResponseDto.of(post, getLikeDislike(user.getId(), postId));
    }

    @Transactional
    public LikeState getLikeDislike(Long userId, Long postId) {
        Optional<Like> like = likeRepository.findByUserIdAndPostId(userId, postId);
        Optional<Dislike> dislike = dislikeRepository.findByUserIdAndPostId(userId, postId);
        return like.isPresent() ? LikeState.LIKE
            : (dislike.isPresent() ? LikeState.DISLIKE : LikeState.UNSELECTED);
    }

}
