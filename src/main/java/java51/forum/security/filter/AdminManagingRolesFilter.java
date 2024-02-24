package java51.forum.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java51.forum.accounting.dao.UserAccountRepository;
import java51.forum.accounting.model.Role;
import java51.forum.accounting.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(20)

public class AdminManagingRolesFilter
        implements Filter
{

    final UserAccountRepository userAccountRepository;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (checkAndPoint(request.getMethod(), request.getServletPath())) {
            User userAccount = userAccountRepository
                    .findById(request.getUserPrincipal().getName()).get();
            if (!userAccount.getRoles().contains(Role.ADMINISTRATOR)) {
                response.sendError(403, "Permission denied");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkAndPoint(String method, String servletPath) {
        return servletPath.matches("/account/user/\\w+/role/\\w+");
    }
}
