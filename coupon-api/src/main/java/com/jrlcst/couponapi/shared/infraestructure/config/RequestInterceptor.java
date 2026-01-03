package com.jrlcst.couponapi.shared.infraestructure.config;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID = "requestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        final String requestUUid = UuidCreator.getTimeOrderedEpoch().toString();
        MDC.put(REQUEST_ID, requestUUid);
        request.setAttribute(REQUEST_ID, requestUUid);

        //TODO: implementar autenticacao, i18n
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex
    ) {
        MDC.clear();
    }
}
