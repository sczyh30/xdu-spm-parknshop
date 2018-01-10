package io.spm.parknshop.api.util;

import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;

/**
 * @author Eric Zhao
 */
public final class ResultUtils {

  public static <R> HttpStatus getStatus(Result<R> result) {
    if (result.getStatusCode() == ErrorConstants.NO_AUTH) {
      return HttpStatus.UNAUTHORIZED;
    } else if (result.getStatusCode() == ErrorConstants.NOT_FOUND) {
      return HttpStatus.NOT_FOUND;
    }
    return HttpStatus.OK;
  }

  public static <R> Result<R> toApiResult(Throwable ex) {
    if (ex instanceof ServiceException) {
      return Result.failureWithResult(((ServiceException) ex).getErrorCode(), ex, (R) ((ServiceException) ex).getAttach());
    } else if (ex instanceof ServerWebInputException) {
      ex.printStackTrace();
      return Result.failure(ErrorConstants.BAD_REQUEST, "Invalid input");
    }  else if (ex instanceof ResponseStatusException) {
      if (((ResponseStatusException) ex).getStatus().value() == 404) {
        return Result.<R>notFound().setStatusCode(404);
      }
      return Result.failure(((ResponseStatusException) ex).getStatus().value(), "Unknown error");
    } else {
      return Result.failure(ErrorConstants.SERVER_ERROR, ex);
    }
  }

  private ResultUtils() {}
}
