package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.entity.Dislike;
import com.ColdPitch.domain.entity.Like;
import com.ColdPitch.domain.entity.Post;
import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.entity.comment.CommentState;
import com.ColdPitch.domain.entity.dto.post.PostRequestDto;
import com.ColdPitch.domain.entity.dto.post.PostResponseDto;
import com.ColdPitch.domain.entity.post.LikeState;
import com.ColdPitch.domain.entity.post.PostState;
import com.ColdPitch.domain.repository.CommentRepository;
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
    private final CommentRepository commentRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto) {
        User user = getUserFromAuth();
        requestDto.setStatus(PostState.OPEN);
        Post post = Post.toEntity(requestDto,user);
        user.addPost(post);
        postRepository.save(post);
        return PostResponseDto.of(post, LikeState.UNSELECTED);
    }

    @Transactional
    public PostResponseDto updatePost(PostRequestDto requestDto) {
        User user = getUserFromAuth();
        Post post = getPostByAuth(requestDto.getId(), user.getId());
        post.updatePost(requestDto);
        return PostResponseDto.of(post, getLikeDislike(user.getId(), post.getId()));
    }

    @Transactional
    public PostResponseDto postStateChange(PostRequestDto requestDto) {
        User user = getUserFromAuth();
        Post post = getPostByAuth(requestDto.getId(), user.getId());
        post.setStatus(requestDto.getStatus());
        if (requestDto.getStatus() == PostState.DELETED) {
            List<Comment> comments = post.getComments();
            for (int i = 0; i < comments.size(); i++) {
                Comment comment = comments.get(i);
                comment.setStatus(CommentState.DELETED);
                comment = commentRepository.saveAndFlush(comment);
                comments.set(i, comment);
            }
        }
        return PostResponseDto.of(post, getLikeDislike(user.getId(), post.getId()));
    }

    public PostResponseDto findPost(Long postId) {
        User user = getUserFromAuth();
        Optional<Post> OptionalPost = SecurityUtil.checkCurrentUserRole("ADMIN")
            ? postRepository.findByIdForAdmin(user.getId())
            : postRepository.findByIdForUser(user.getId());

        Post post = OptionalPost.orElseThrow(
            () -> new CustomException(ErrorCode.POST_NOT_EXISTS));

        return PostResponseDto.of(post, getLikeDislike(user.getId(), postId));
    }

    public List<PostResponseDto> findAllPosts() {
        User user = getUserFromAuth();
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
        User user = getUserFromAuth();
        Post post = getPostByAuth(postId, user.getId());
        dislikeRepository.findByUserIdAndPostId(user.getId(), postId).ifPresent(v -> {
            throw new CustomException(ErrorCode.LIKE_ALREADY_SELECTED);
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
        User user = getUserFromAuth();
        Post post = getPostByAuth(postId, user.getId());
        likeRepository.findByUserIdAndPostId(user.getId(), postId).ifPresent(v->{
            throw new CustomException(ErrorCode.DISLIKE_ALREADY_SELECTED);
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

    public LikeState getLikeDislike(Long userId, Long postId) {
        Optional<Like> like = likeRepository.findByUserIdAndPostId(userId, postId);
        Optional<Dislike> dislike = dislikeRepository.findByUserIdAndPostId(userId, postId);
        return like.isPresent() ? LikeState.LIKE
            : (dislike.isPresent() ? LikeState.DISLIKE : LikeState.UNSELECTED);
    }

    public User getUserFromAuth() {
        return userRepository.findByEmail(
                SecurityUtil.getCurrentUserEmail()
                    .orElseThrow(() -> new CustomException(ErrorCode.FORBIDDEN)))
            .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_EXISTS));
    }

    public Post getPostByAuth(Long postId, Long userId) {
        Optional<Post> OptionalPost = SecurityUtil.checkCurrentUserRole("ADMIN")
            ? postRepository.findByIdForAdmin(postId)
            : postRepository.findByIdForUser(postId);

        Post post = OptionalPost.orElseThrow(
            () -> new CustomException(ErrorCode.POST_NOT_EXISTS)); // 게시글이 존재하지 않는 익셉션 발생

        if (!post.getUser().getId().equals(userId) && SecurityUtil.checkCurrentUserRole("USER")) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        } // Admin이 아닌 유저가 자신의 것이 아닌 게시글을 수정할 때 익셉션 발생

        return post;
    }
}
