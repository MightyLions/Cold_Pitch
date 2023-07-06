package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Dislike;
import com.ColdPitch.domain.entity.Like;
import com.ColdPitch.domain.entity.Post;
import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.comment.CommentState;
import com.ColdPitch.domain.entity.dto.post.PostRequestDto;
import com.ColdPitch.domain.entity.dto.post.PostResponseDto;
import com.ColdPitch.domain.entity.post.LikeState;
import com.ColdPitch.domain.entity.post.PostState;
import com.ColdPitch.domain.repository.DislikeRepository;
import com.ColdPitch.domain.repository.LikeRepository;
import com.ColdPitch.domain.repository.PostRepository;
import com.ColdPitch.domain.repository.UserRepository;
import com.ColdPitch.exception.CustomException;
import com.ColdPitch.exception.handler.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ColdPitch.utils.SecurityUtil;
import java.util.stream.Collectors;
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

    /**
     * @param requestDto 게시글 정보
     * @apiNote 게시글 등록
     */
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto) {
        User user = getUserFromAuth();
        Post post = Post.toEntity(requestDto, user);
        user.addPost(post);
        return PostResponseDto.of(postRepository.save(post), LikeState.UNSELECTED);
    }

    /**
     * @param requestDto 게시글 정보, postId
     * @apiNote 게시물 수정
     */
    @Transactional
    public PostResponseDto updatePost(PostRequestDto requestDto) {
        User user = getUserFromAuth();
        Post post = getPostByAuth(requestDto.getId(), user.getId());
        post.updatePost(requestDto);
        return PostResponseDto.of(post, getSelection(user.getId(), post.getId()));
    }

    /**
     * @param requestDto 게시글 정보, postId, postStatus
     * @apiNote 게시물 상태 변경
     */
    @Transactional
    public PostResponseDto postStateChange(PostRequestDto requestDto) {
        User user = getUserFromAuth();
        Post post = getPostByAuth(requestDto.getId(), user.getId());
        post.setStatus(requestDto.getStatus());
        if (requestDto.getStatus() == PostState.DELETED) {
            post.getComments().stream().forEach(c -> c.setStatus(CommentState.DELETED));
        }
        return PostResponseDto.of(post, getSelection(user.getId(), post.getId()));
    }

    /**
     * @apiNote 게시글 조회
     * @param postId 게시글 ID
     * */
    public PostResponseDto findPost(Long postId) {
        User user = getUserFromAuth();
        Post post = Optional.ofNullable(
            SecurityUtil.checkCurrentUserRole("ADMIN")
            ? postRepository.findByIdForAdmin(user.getId())
            : postRepository.findByIdForUser(user.getId())
        ).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXISTS)).get();
        return PostResponseDto.of(post, getSelection(user.getId(), postId));
    }

    /**
     * @apiNote 게시글 리스트 조회
     */
    public List<PostResponseDto> findAllPosts() {
        User user = getUserFromAuth();
        List<Post> postList = SecurityUtil.checkCurrentUserRole("ADMIN")
            ? postRepository.findAllForAdmin()
            : postRepository.findAllForUser(user.getId());

        List<PostResponseDto> responseDtos = new ArrayList<>();
        for (Post post : postList) {
            responseDtos.add(PostResponseDto.of(post, LikeState.UNSELECTED));
        }
        return responseDtos;
    }

    /**
     * @apiNote 게시글 좋아요
     * @param postId 게시글 ID
     * */
    @Transactional
    public PostResponseDto likePost(Long postId) {
        User user = getUserFromAuth();
        Post post = getPostByAuth(postId, user.getId());
        // 싫어요를 누른 게시글은 좋아요 클릭 불가능
        dislikeRepository.findByUserIdAndPostId(user.getId(), postId).ifPresent(
            v -> {throw new CustomException(ErrorCode.DISLIKE_ALREADY_SELECTED);
        });
        likeRepository.findByUserIdAndPostId(user.getId(), postId).ifPresentOrElse(
            like -> { likeRepository.deleteById(like.getId()); post.minusLike();},
            () -> { likeRepository.save(Like.toEntity(user.getId(), postId)); post.plusLike();}
        );
        return PostResponseDto.of(post, getSelection(user.getId(), postId));
    }

    /**
     * @apiNote 게시글 싫어요
     * @param postId 게시글 ID
     * */
    @Transactional
    public PostResponseDto dislikePost(Long postId) {
        User user = getUserFromAuth();
        Post post = getPostByAuth(postId, user.getId());
        likeRepository.findByUserIdAndPostId(user.getId(), postId).ifPresent(
            v -> {throw new CustomException(ErrorCode.LIKE_ALREADY_SELECTED);
        });
        dislikeRepository.findByUserIdAndPostId(user.getId(), postId).ifPresentOrElse(
            dislike -> { dislikeRepository.deleteById(dislike.getId()); post.minusDislike();},
            () -> { dislikeRepository.save(Dislike.toEntity(user.getId(),postId)); post.plusDislike();}
        );
        return PostResponseDto.of(post, getSelection(user.getId(), postId));
    }

    /**
     * @implNote 유저가 누른 좋아요, 싫어요 조회
     * */
    public LikeState getSelection(Long userId, Long postId) {
        Optional<Like> like = likeRepository.findByUserIdAndPostId(userId, postId);
        Optional<Dislike> dislike = dislikeRepository.findByUserIdAndPostId(userId, postId);
        return like.isPresent() ? LikeState.LIKE : (dislike.isPresent() ? LikeState.DISLIKE : LikeState.UNSELECTED);
    }

    /**
     * @implNote 필터를 통해 저장된 유저 정보 불러오기
     * */
    public User getUserFromAuth() {
        return userRepository.findByEmail(
                SecurityUtil.getCurrentUserEmail()
                    .orElseThrow(() -> new CustomException(ErrorCode.FORBIDDEN)))
            .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_EXISTS));
    }

    /**
     * @implNote 유저의 권한을 확인하여 게시글 조회
     * */
    public Post getPostByAuth(Long postId, Long userId) {
        Optional<Post> OptionalPost = SecurityUtil.checkCurrentUserRole("ADMIN")
            ? postRepository.findByIdForAdmin(postId)
            : postRepository.findByIdForUser(postId);

        // 게시글이 존재하지 않는 익셉션 발생
        Post post = OptionalPost.orElseThrow(
            () -> new CustomException(ErrorCode.POST_NOT_EXISTS));

        // Admin이 아닌 유저가 자신의 것이 아닌 게시글을 수정할 때 익셉션 발생
        if (!post.getUser().getId().equals(userId) && SecurityUtil.checkCurrentUserRole("USER")) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return post;
    }
}
