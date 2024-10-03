package com.example.integratedbackend.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;


@Component
public class Interceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object o) throws Exception {
//        if (httpServletRequest.getMethod().equalsIgnoreCase("POST") && httpServletRequest.getRequestURI().contains("/boards")) {
//            ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(httpServletRequest);
//
//            if (cachedRequest.getContentAsByteArray().length == 0) {
//                httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//                httpServletResponse.getWriter().write("Board creation failed. Request body is required.");
//                return false;
//            }
//        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //
    }
}
