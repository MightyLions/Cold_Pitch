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
        Post post = Post.toEntity(requestDto.getTitle(), requestDto.getText(),
            requestDto.getCategory(), user.getId(), PostState.OPEN);
        postRepository.save(post);
        return convertDto(post, user.getName(), LikeState.UNSELECTED);
    }

    @Transactional
    public PostResponseDto updatePost(String userEmail, PostRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        // 게시 유저와 유저가 같으면 권한 부여
        Post post = postRepository.findById(requestDto.getId()).orElseThrow();
        post.setTitle(requestDto.getTitle());
        post.setText(requestDto.getText());
        post.setCategory(requestDto.getCategory());
        return convertDto(post, user.getName(), getLikeDislike(user.getId(), post.getId()));
    }

    @Transactional
    public PostResponseDto postStateChange(String userEmail, Long postId, String state) {
        // 게시 유저와 유저가 같으면 권한 부여
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        post.setStatus(PostState.valueOf(state));
        return state.equals("DELETED") ? null
            : convertDto(post, user.getName(), getLikeDislike(user.getId(), postId));
    }

    public PostResponseDto getPost(String userEmail, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return convertDto(post, user.getName(), getLikeDislike(user.getId(), postId));
    }

    @Transactional
    public PostResponseDto likePost(String userEmail, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        // 이미 싫어요 누른 게시물은 좋아요 불가능
        Optional<Dislike> originDislike = dislikeRepository.findByUserIdAndPostId(user.getId(),
            postId);
        if (originDislike.isPresent()) { // 예외처리 때 변경
            log.info("싫어요를 선택한 게시물에는 좋아요를 선택할 수 없습니다.");
            return null;
        }
        // 이미 좋아요 누른 게시물은 좋아요 취소
        Optional<Like> originLike = likeRepository.findByUserIdAndPostId(user.getId(), postId);
        if (originLike.isPresent()) {
            likeRepository.deleteById(originLike.get().getId());
            post.minusLike();
        } else {
            // 아직 아무 버튼도 누르지 않은 게시물
            Like like = Like.builder().userId(user.getId()).postId(postId).build();
            likeRepository.save(like);
            post.plusLike();
        }
        return convertDto(post, user.getName(), getLikeDislike(user.getId(), postId));
    }

    // PostResponseDto에 좋아요 싫어요 찾아서 있는지 추가해주기
    @Transactional
    public PostResponseDto dislikePost(String userEmail, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        // 이미 좋어요 누른 게시물은 싫어요 불가능
        Optional<Like> originLike = likeRepository.findByUserIdAndPostId(user.getId(), postId);
        if (originLike.isPresent()) { // 예외처리 때 변경
            log.info("좋아요를 선택한 게시물에는 싫어요를 선택할 수 없습니다.");
            return null;
        }
        // 이미 싫어요를 누른 게시물은 싫어요 취소
        Optional<Dislike> originDislike = dislikeRepository.findByUserIdAndPostId(user.getId(),
            postId);
        if (originDislike.isPresent()) {
            dislikeRepository.deleteById(originDislike.get().getId());
            post.minusDislike();
        } else {
            // 아직 아무 버튼도 누르지 않은 게시물
            Dislike dislike = Dislike.builder().userId(user.getId()).postId(postId).build();
            dislikeRepository.save(dislike);
            post.plusDislike();
        }
        return convertDto(post, user.getName(), getLikeDislike(user.getId(), postId));
    }

    @Transactional
    public LikeState getLikeDislike(Long userId, Long postId) {
        Optional<Like> like = likeRepository.findByUserIdAndPostId(userId, postId);
        Optional<Dislike> dislike = dislikeRepository.findByUserIdAndPostId(userId, postId);
        log.info(like.toString() + " " + dislike.toString());
        return like.isPresent() ? LikeState.LIKE
            : (dislike.isPresent() ? LikeState.DISLIKE : LikeState.UNSELECTED);
    }

    public PostResponseDto convertDto(Post post, String userName, LikeState userChoice) {
        return PostResponseDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .text(post.getText())
            .userName(userName)
            .category(post.getCategory())
            .createAt(post.getCreateAt())
            .modifyAt(post.getModifiedAt())
            .status(post.getStatus())
            .likeCnt(post.getLikeCnt())
            .dislikeCnt(post.getDislikeCnt())
            .userChoice(userChoice)
            .build();
    }
}
