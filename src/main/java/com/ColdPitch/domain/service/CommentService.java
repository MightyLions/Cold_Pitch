package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.entity.comment.CommentState;
import com.ColdPitch.domain.entity.dto.comment.CommentRequestDto;
import com.ColdPitch.domain.entity.dto.comment.CommentResponseDto;
import com.ColdPitch.domain.repository.CommentRepository;
import com.ColdPitch.domain.repository.UserRepository;
import com.ColdPitch.exception.CustomException;
import com.ColdPitch.exception.handler.ErrorCode;
import com.ColdPitch.utils.SecurityUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto saveCommentRequestDto(CommentRequestDto requestDto) {
        if (requestDto == null) {
            throw new CustomException(ErrorCode.COMMENT_BAD_REQUEST);
        }

        Comment comment = Comment.builder()
            .postId(requestDto.getPostId())
            .userId(requestDto.getUserId())
            .text(requestDto.getText())
            .pId(requestDto.getPCommentId())
            .status(CommentState.OPEN)
            .build();

        comment = commentRepository.save(comment);

        return commentToResponseDto(comment);
    }

    public static CommentResponseDto commentToResponseDto(Comment comment) {
        if (comment == null) {
            throw new CustomException(ErrorCode.COMMENT_INTERNAL_SERVER_ERROR);
        }

        return CommentResponseDto.of(comment);
    }

    @Transactional
    public List<CommentResponseDto> findAll() {
        if (SecurityUtil.checkCurrentUserRole("ADMIN")) {
            return commentRepository.findAll()
                .stream()
                .map(CommentService::commentToResponseDto)
                .collect(Collectors.toList());
        }

        return commentRepository.findAllNotIncludingDeleted()
            .stream()
            .map(CommentService::commentToResponseDto)
            .collect(Collectors.toList());
    }

    public CommentResponseDto findCommentById(Long commentId) {
        if (SecurityUtil.checkCurrentUserRole("ADMIN")) {
            return commentToResponseDto(commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_EXISTS)));
        }

        return commentToResponseDto(
                commentRepository
                        .findByIdNotIncludingDeleted(commentId)
                        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_EXISTS))
        );
    }

    public List<CommentResponseDto> findListByPostId(Long postId) {
        if (SecurityUtil.checkCurrentUserRole("ADMIN")) {
            return commentRepository
                .findAllByPostId(postId)
                .stream()
                .map(CommentService::commentToResponseDto)
                .collect(Collectors.toList());
        }

        return commentRepository
            .findAllByPostIdNotIncludingDeleted(postId)
            .stream()
            .map(CommentService::commentToResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto save(Comment comment) {
        return commentToResponseDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, String updatedText) {
        Comment entity = commentRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_EXISTS));

        assert entity != null;
        entity.setText(updatedText);

        return commentToResponseDto(entity);
    }

    @Transactional
    public boolean changeState(Long id, CommentState state) {
        Comment comment = commentRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_EXISTS));

        comment.setStatus(state);

        return comment.getStatus().equals(state);
    }

    public List<CommentResponseDto> findCommentsByParentId(Long parentId) {
        if (SecurityUtil.checkCurrentUserRole("ADMIN")) {
            return commentRepository
                .findAllByParentId(parentId)
                .stream()
                .map(CommentService::commentToResponseDto)
                .collect(Collectors.toList());
        }

        return commentRepository
            .findAllByParentIdNotIncludeDeleted(parentId)
            .stream()
            .map(CommentService::commentToResponseDto)
            .collect(Collectors.toList());
    }

    public List<CommentResponseDto> findCommentsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        if (commentRepository.findAllByUserId(userId).isEmpty()) {
            throw new CustomException(ErrorCode.COMMENT_BAD_REQUEST);
        }

        if (SecurityUtil.checkCurrentUserRole("ADMIN")) {
            return commentRepository
                .findAllByUserId(userId)
                .stream()
                .map(CommentService::commentToResponseDto)
                .collect(Collectors.toList());
        }

        return commentRepository
            .findAllByUserIdNotIncludingDeleted(userId)
            .stream()
            .map(CommentService::commentToResponseDto)
            .collect(Collectors.toList());
    }
}
