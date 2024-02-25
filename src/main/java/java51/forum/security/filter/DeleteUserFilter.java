package java51.forum.security.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java51.forum.accounting.model.Role;
import java51.forum.accounting.model.UserAccount;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;

@Component
@Order(40)
public class DeleteUserFilter
        implements Filter
{

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (checkAndPoint(request.getMethod(), request.getServletPath())) {
            Principal principal = request.getUserPrincipal();
            String[] arr = request.getServletPath().split("/");
            String userName = arr[arr.length - 1];

            UserAccount userAccount = (UserAccount) request.getUserPrincipal();


            if (!(principal.getName().equalsIgnoreCase(userName) || userAccount.getRoles().contains(Role.ADMINISTRATOR))) {
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
