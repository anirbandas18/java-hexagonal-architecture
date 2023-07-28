package com.teenthofabud.demo.hexagonal.architecture.cookbook.error;

import com.teenthofabud.core.common.data.vo.ErrorVo;
import com.teenthofabud.core.common.error.TOABBaseException;
import com.teenthofabud.core.common.handler.TOABBaseWebExceptionHandler;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.CuisineException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CookbookWebExceptionHandler extends TOABBaseWebExceptionHandler {

    @ExceptionHandler(value = { CuisineException.class })
    public ResponseEntity<ErrorVo> handleCookbookSubDomainExceptions(TOABBaseException e) {
        ResponseEntity<ErrorVo>  response = super.parseExceptionToResponse(e);
        return response;
    }

}
