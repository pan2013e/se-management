package zju.se.management.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest req,
            @NotNull HttpServletResponse res,
            @NotNull Object handler) throws Exception {
        if(isDebug(req)) {
            return true;
        }
        if(req.isRequestedSessionIdValid()) {
            return true;
        } else{
            if(isApi(req)) {
                jsonResponse("{\"code\":-1,\"data\":null,\"message\":\"未登录\"}", res);
            } else {
                res.sendRedirect("/login");
            }
            return false;
        }
    }

    protected boolean isApi(@NotNull HttpServletRequest req) {
        return req.getRequestURI().contains("/api/");
    }

    protected boolean isDebug(@NotNull HttpServletRequest req) {
        String UA = req.getHeader("User-Agent");
        return UA.contains("PostmanRuntime") || UA.contains("curl");
    }

    protected void jsonResponse(String jsonStr, @NotNull HttpServletResponse res) {
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = res.getWriter()) {
            writer.print(jsonStr);
        } catch (IOException ignored) {
        }
    }
}
