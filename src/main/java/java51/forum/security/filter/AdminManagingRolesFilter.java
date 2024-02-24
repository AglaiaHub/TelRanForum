package java51.forum.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java51.forum.accounting.dao.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(20)

public class AdminManagingRolesFilter
//        implements Filter
{

    final UserAccountRepository userAccountRepository;
//    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (checkAndPoint(request.getMethod(), request.getServletPath())){
            //TODO
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkAndPoint(String method, String servletPath) {
        return servletPath.matches("/account/user/\\w+/role/\\w+");
    }
}
