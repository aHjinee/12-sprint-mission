package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOArgument(IOException ex) {
        System.out.println("IOException : " + ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(ErrorCode.FILE_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        System.out.println("IllegalArgumentException : " + ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE , ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleChannelNotFound(UserNotFoundException ex){
        System.out.println("UserNotFoundException: " + ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(ErrorCode.USER_NOT_FOUND, ex.getMessage()));

    }

    @ExceptionHandler(ChannelNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(ChannelNotFoundException ex){
        System.out.println("ChannelNotFoundException: " + ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(ErrorCode.CHANNEL_NOT_FOUND, ex.getMessage()));

    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotFound(MessageNotFoundException ex){
        System.out.println("MessageNotFoundException: " + ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(ErrorCode.MESSAGE_NOT_FOUND, ex.getMessage()));

    }

    @ExceptionHandler(BinaryContentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBinaryContentNotFound(BinaryContentNotFoundException ex){
        System.out.println("BinaryContentNotFoundException: " + ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(ErrorCode.BINARY_CONTENT_NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(ReadStatusNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReadStatusNotFound(ReadStatusNotFoundException ex){
        System.out.println("ReadStatusNotFoundException: " + ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(ErrorCode.READ_STATUS_NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(UserStatusNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserStatusNotFound(UserStatusNotFoundException ex){
        System.out.println("UserStatusNotFoundException: " + ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(ErrorCode.USER_STATUS_NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        System.out.println("AccessDeniedException: " + ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(ErrorCode.ACCESS_DENIED, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUnhandled(Exception ex, WebRequest request) {
        System.out.println("Exception: " + ex.getMessage());
        System.out.println("Request URI: " + request.getDescription(false));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, "예기치 못한 오류가 발생했습니다."));
    }
}
