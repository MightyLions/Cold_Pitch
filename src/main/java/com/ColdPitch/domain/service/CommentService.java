package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.entity.comment.CommentState;
import com.ColdPitch.domain.entity.dto.comment.CommentRequestDto;
import com.ColdPitch.domain.entity.dto.comment.CommentResponseDto;
import com.ColdPitch.domain.repository.CommentRepository;
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
@Transactional()
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentResponseDto saveCommentRequestDto(CommentRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        Comment comment = Comment.builder()
            .postId(requestDto.getPostId())
            .userId(requestDto.getUserId())
            .text(requestDto.getText())
            .pCommentId(requestDto.getPCommentId())
            .status(CommentState.OPEN)
            .build();

        comment = commentRepository.saveAndFlush(comment);

        return commentToResponseDto(comment);
    }

    public static CommentResponseDto commentToResponseDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        return CommentResponseDto.of(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAll() {
        if (SecurityUtil.checkCurrentUserRole("ADMIN")) {
            return commentRepository.findAllForAdmin()
                .stream()
                .map(CommentService::commentToResponseDto)
                .collect(Collectors.toList());
        }

        return commentRepository.findAllForUser()
            .stream()
            .map(CommentService::commentToResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommentResponseDto findCommentsByCommentId(Long commentId) {
        if (SecurityUtil.checkCurrentUserRole("ADMIN")) {
            return commentToResponseDto(commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_EXISTS)));
        }

        return commentToResponseDto(commentRepository.findByIdForUser(commentId));
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findCommentsByPostId(Long postId) {
        if (SecurityUtil.checkCurrentUserRole("ADMIN")) {
            return commentRepository
                .findAllByPostIdForAdmin(postId)
                .stream()
                .map(CommentService::commentToResponseDto)
                .collect(Collectors.toList());
        }

        return commentRepository
            .findAllByPostId(postId)
            .stream()
            .map(CommentService::commentToResponseDto)
            .collect(Collectors.toList());
    }

    public CommentResponseDto save(Comment comment) {
        return commentToResponseDto(commentRepository.saveAndFlush(comment));
    }

    public CommentResponseDto updateComment(Long id, String updatedText) {
        Comment entity = commentRepository.findById(id).orElse(null);

        assert entity != null;
        entity.setText(updatedText);

        return commentToResponseDto(commentRepository.saveAndFlush(entity));
    }

    public boolean changeState(Long id, CommentState state) {
        if (!commentRepository.existsById(id)) {
            return false;
        }

        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment == null) {
            return false;
        }

        comment.setStatus(state);
        comment = commentRepository.saveAndFlush(comment);

        if (comment.getStatus().equals(state.toString())) {
            return true;
        }

        return false;
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findCommentsByParentId(Long parentId) {
        if (SecurityUtil.checkCurrentUserRole("ADMIN")) {
            return commentRepository
                .findAllByParentIdForAdmin(parentId)
                .stream()
                .map(CommentService::commentToResponseDto)
                .collect(Collectors.toList());
        }

        return commentRepository
            .findAllByParentId(parentId)
            .stream()
            .map(CommentService::commentToResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findCommentsByUserId(Long userId) {
        if (commentRepository.findAllByUserIdForAdmin(userId).isEmpty()) {
            throw new CustomException(ErrorCode.COMMENT_BAD_REQUEST);
        }

        if (SecurityUtil.checkCurrentUserRole("ADMIN")) {
            return commentRepository
                .findAllByUserIdForAdmin(userId)
                .stream()
                .map(CommentService::commentToResponseDto)
                .collect(Collectors.toList());
        }

        return commentRepository
            .findAllByUserId(userId)
            .stream()
            .map(CommentService::commentToResponseDto)
            .collect(Collectors.toList());
    }
}
