package com.ColdPitch.domain.service;

import com.ColdPitch.domain.entity.Comment;
import com.ColdPitch.domain.entity.comment.CommentState;
import com.ColdPitch.domain.entity.dto.comment.CommentRequestDto;
import com.ColdPitch.domain.entity.dto.comment.CommentResponseDto;
import com.ColdPitch.domain.repository.CommentRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
            // @TODO Comment status를 CommentState Enum을 사용해서 구현하기
            .status(requestDto.getStatus())
            .build();

        comment = commentRepository.saveAndFlush(comment);

        return commentToResponseDto(comment);
    }

    public static CommentResponseDto commentToResponseDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        return CommentResponseDto.builder()
            .id(comment.getId())
            .postId(comment.getPostId())
            .userId(comment.getUserId())
            .text(comment.getText())
            .pCommentId(comment.getPCommentId())
            //  @Todo CommentResponseDto status를 CommentState Enum을 사용해서 구현하기
            .status(comment.getStatus())
            .createAt(comment.getCreateAt())
            .createBy(comment.getCreatedBy())
            .modifiedAt(comment.getModifiedAt())
            .modifiedBy(comment.getModifiedBy())
            .build();
    }

    public CommentResponseDto findCommentsByCommentId(Long commentId) {
        return commentToResponseDto(commentRepository.findById(commentId).orElse(null));
    }

    public List<CommentResponseDto> findCommentsByPostId(Long postId) {
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

        entity.setText(updatedText);

        return commentToResponseDto(commentRepository.saveAndFlush(entity));
    }

    public List<CommentResponseDto> findCommentsByParentId(Long parentId) {
        return commentRepository
            .findAllByParentId(parentId)
            .stream()
            .map(CommentService::commentToResponseDto)
            .collect(Collectors.toList());
    }

    public List<CommentResponseDto> findCommentsByUserId(Long userId) {
        return commentRepository
            .findAllByUserId(userId)
            .stream()
            .map(CommentService::commentToResponseDto)
            .collect(Collectors.toList());
    }
}
