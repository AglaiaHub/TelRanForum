package java51.forum.security.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java51.forum.accounting.dao.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;

@Component
@RequiredArgsConstructor
@Order(30)
public class UpdateByOnewFilter
        implements Filter
{
    final UserAccountRepository userAccountRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //достать из строки имя пользователя и проверить что он это я
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (checkAndPoint(request.getMethod(), request.getServletPath())) {

            Principal principal = request.getUserPrincipal();
            String[] arr = request.getServletPath().split("/");
            String user = arr[arr.length - 1];

            if (!principal.getName().equalsIgnoreCase(user)) {
                response.sendError(403);
                return;
            }

        }
        filterChain.doFilter(request, response);
    }

    private boolean checkAndPoint(String method, String servletPath) {
        return HttpMethod.PUT.matches(method) && servletPath.matches("/account/user/\\w+");
    }
}
