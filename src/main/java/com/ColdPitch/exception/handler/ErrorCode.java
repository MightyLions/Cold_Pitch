package com.ColdPitch.exception.handler;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {
    NO_SUCH_ELEMENT(HttpStatus.NO_CONTENT, "해당 컨텐츠를 찾을 수 없습니다"),                        //  204
    MOVED_PERMANT(HttpStatus.MOVED_PERMANENTLY, "URI가 변경되었습니다"),                        //  301
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),                                   //  400
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),                                     //  403
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 컨텐츠가 존재하지 않습니다"),                               //  404
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 접근 방식입니다"),                  //  405
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "접근 요청시간이 만료되었습니다"),                    //  408
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부적인 서버 에러가 발생했습니다"),      //  500
    NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "구현되지 않은 기능입니다"),                       //  501
    INVALID_JWT_AUTHORIZATION(HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 JWT 서명입니다."),
    EXPIRED_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 JWT 서명입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 JWT 서명입니다."),
    INVALID_JWT_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 JWT 서명입니다."),
    NO_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 서명이 존재하지 않습니다."),
    NO_AUTHORIZATION_TOKEN(HttpStatus.FORBIDDEN, "권한이 맞지 않습니다."),


    // User
    EMAIL_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다"),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다"),
    USER_NOT_MATCH(HttpStatus.INTERNAL_SERVER_ERROR, "유저 정보가 일치하지 않습니다"),
    USER_CURRENT_USER_REQUEST_USER_NOT_MATCH(HttpStatus.BAD_REQUEST, "요청한 유저정보와 현재 유저정보가 일치하지 않습니다"),
    USER_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "로그아웃 된 사용자입니다."),
    USER_INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 Refresh Token 입니다."),
    USER_INVALID_USER_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "토큰의 정보가 유저 정보가 일치하지 않습니다."),
    USER_NICKNAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 닉네임 입니다"),
    USER_ALREADY_WITHDRAWN(HttpStatus.BAD_REQUEST, "탈퇴한 회원입니다."),

    // Post
    POST_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글입니다"),
    POST_REGISTRATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "게시글을 등록하던 중 오류가 발생했습니다"),
    POST_UPDATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "게시글을 업데이트하던 중 오류가 발생했습니다"),
    POST_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "게시글을 삭제하던 중 오류가 발생했습니다"),
    POST_BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),

    // Comment
    COMMENT_USER_COMMENT_NOT_EXISTS(HttpStatus.NO_CONTENT, "주어진 유저 ID로 등록된 댓글이 없습니다."),
    COMMENT_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 댓글입니다"),
    COMMENT_REGISTRATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "댓글을 등록하던 중 오류가 발생했습니다"),
    COMMENT_UPDATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "댓글을 업데이트하던 중 오류가 발생했습니다"),
    COMMENT_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "댓글을 삭제하던 중 오류가 발생했습니다"),
    COMMENT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
    COMMENT_AUTHOR_NOT_MATCH(HttpStatus.FORBIDDEN, "댓글을 등록한 유저가 아닙니다"),
    COMMENT_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "댓글을 처리하던 중 서버 에러가 발생했습니다"),

    // Like
    LIKE_ALREADY_SELECTED(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 게시물입니다."),
    DISLIKE_ALREADY_SELECTED(HttpStatus.BAD_REQUEST, "이미 싫어요를 누른 게시물입니다."),

    // CompanyRegistration
    COMPANY_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 기업입니다."),
    COMPANY_EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "외부 요청 API에서 에러가 발생하였습니다."),
    COMPANY_BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    ;
    // Sentiment
    SENTIMENT_EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "외부 요청 API에서 에러가 발생하였습니다.");
  
    // file
    FILE_NOT_EXIST(HttpStatus.BAD_REQUEST, "파일이 비어있습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "알 수 없는 파일입니다."),

    private final HttpStatus status;
    private final String message;


    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
