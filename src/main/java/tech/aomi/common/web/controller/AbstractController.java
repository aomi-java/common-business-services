package tech.aomi.common.web.controller;

import tech.aomi.common.exception.ErrorCode;

import java.io.Serializable;

/**
 * @author 田尘殇Sean(sean.snow @ live.com) createAt 2016/12/26
 */
public class AbstractController {

    protected Result success() {
        return success(null);
    }

    protected Result success(Object payload) {
        return Result.create(ErrorCode.SUCCESS, ErrorCode.SUCCESS.getMessage(), payload);
    }

}
