package com.demo.service;

/**
 * 业务校验异常，消息直接展示给用户。
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }
}
