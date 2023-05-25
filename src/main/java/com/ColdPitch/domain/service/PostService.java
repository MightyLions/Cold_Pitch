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

import com.ColdPitch.utils.SecurityUtil;
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
    public PostResponseDto createPost(String userEmail,PostRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        requestDto.setStatus(PostState.OPEN);
        Post post = Post.toEntity(requestDto,user);
        user.addPost(post);
        postRepository.save(post);
        return PostResponseDto.of(post, LikeState.UNSELECTED);
    }

    @Transactional
    public PostResponseDto updatePost(PostRequestDto requestDto) {
        String userEmail = SecurityUtil.getCurrentUserEmail().orElseThrow();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Post post = postRepository.findByIdForAdmin(requestDto.getId()).orElseThrow();
        if (!post.getCreatedBy().equals(user.getName())) {
            log.info("유저 불일치"); // 변경 필요
        }
        post.updatePost(requestDto);
        return PostResponseDto.of(post, getLikeDislike(user.getId(), post.getId()));
    }

    @Transactional
    public PostResponseDto postStateChange(Long postId, PostState state) {
        String userEmail = SecurityUtil.getCurrentUserEmail().orElseThrow();
        Post post = postRepository.findByIdForAdmin(postId).orElseThrow();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        if (post.getCreatedBy().equals(user.getName())) {
            log.info("유저 일치"); // 변경 필요가
        }
        post.setStatus(state);
        return PostResponseDto.of(post, getLikeDislike(user.getId(), postId));
    }

    public PostResponseDto findPost(Long postId) {
        String userEmail = SecurityUtil.getCurrentUserEmail().orElseThrow(); // 익셉션 필요
        Post post = postRepository.findByIdForAdmin(postId).orElseThrow();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return PostResponseDto.of(post, getLikeDislike(user.getId(), postId));
    }

    public List<PostResponseDto> findAllPosts() {
        String userEmail = SecurityUtil.getCurrentUserEmail().orElseThrow(); // 익셉션 처리
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<Post> postList = SecurityUtil.checkCurrentUserRole("ADMIN")
            ? postRepository.findAllForAdmin()
            : postRepository.findAllForUser(user.getId());

        List<PostResponseDto> responseDtos = new ArrayList<>();
        for (Post post : postList) {
            responseDtos.add(PostResponseDto.of(post,null));
        }

        return responseDtos;
    }

    @Transactional
    public PostResponseDto likePost(Long postId) {
        String userEmail = SecurityUtil.getCurrentUserEmail().orElseThrow();
        Post post = postRepository.findByIdForAdmin(postId).orElseThrow();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        dislikeRepository.findByUserIdAndPostId(user.getId(), postId).ifPresent(v -> {
            log.info("싫어요를 선택한 게시물에는 좋아요를 선택할 수 없습니다."); // 익셉션 던지기
        });
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
    public PostResponseDto dislikePost(Long postId) {
        String userEmail = SecurityUtil.getCurrentUserEmail().orElseThrow(); // 유저 validtion 필요
        Post post = postRepository.findByIdForAdmin(postId).orElseThrow();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        likeRepository.findByUserIdAndPostId(user.getId(), postId).ifPresent(v->{
            log.info("좋아요를 선택한 게시물에는 싫어요를 선택할 수 없습니다."); // 익셉션 던지기
        });
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
