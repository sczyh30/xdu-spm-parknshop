package io.spm.parknshop.api.util;

import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;

/**
 * Common result for a REST API.
 *
 * @author Eric Zhao
 *
 * @param <R> type of API result
 */
public class Result<R> {

  private boolean success;
  private int statusCode;
  private String message;

  private R result = null;

  public static <T> Result<T> success(T r) {
    return new Result<>(true, SUCCESS, r);
  }

  public static <T> Result<T> successIfPresent(T r) {
    return r != null ? success(r) : notFound();
  }

  public static <T> Result<T> notFound() {
    return Result.failure(ErrorConstants.NOT_FOUND, "Not found");
  }

  public static <T> Result<T> unauthorized() {
    return Result.failure(ErrorConstants.NO_AUTH, "Authentication failed");
  }

  public static <T> Result<T> unknownError() {
    return Result.failure(ErrorConstants.INTERNAL_UNKNOWN_ERROR, "unknown_error");
  }

  public static <T> Result<T> failure(int statusCode, String message) {
    return new Result<>(false, statusCode, message, null);
  }

  public static <T> Result<T> failure(ServiceException ex) {
    return new Result<>(false, ex.getErrorCode(), ex.getMessage(), null);
  }

  public static <T> Result<T> failureWithResult(int statusCode, T result) {
    return new Result<>(false, statusCode, null, result);
  }

  public static <T> Result<T> failure(int statusCode, Throwable throwable) {
    return new Result<>(false, statusCode, throwable.getMessage(), null);
  }

  public Result(boolean success, int statusCode) {
    this.success = success;
    this.statusCode = statusCode;
  }

  public Result(boolean success, int statusCode, R result) {
    this.success = success;
    this.statusCode = statusCode;
    this.result = result;
  }

  public Result(boolean success, int statusCode, String message, R result) {
    this.success = success;
    this.statusCode = statusCode;
    this.message = message;
    this.result = result;
  }

  public boolean isSuccess() {
    return success;
  }

  public Result<R> setSuccess(boolean success) {
    this.success = success;
    return this;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public Result<R> setStatusCode(int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public Result<R> setMessage(String message) {
    this.message = message;
    return this;
  }

  public R getResult() {
    return result;
  }

  public Result<R> setResult(R result) {
    this.result = result;
    return this;
  }

  @Override
  public String toString() {
    return "Result{" +
      "success=" + success +
      ", statusCode=" + statusCode +
      ", message='" + message + '\'' +
      ", result=" + result +
      '}';
  }

  private static final int SUCCESS = 0;
}
