package java51.forum.security.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java51.forum.accounting.dao.UserAccountRepository;
import java51.forum.accounting.model.Role;
import java51.forum.accounting.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;

@Component
@RequiredArgsConstructor
@Order(30)
public class DeleteUserFilter
//        implements Filter
{
    final UserAccountRepository userAccountRepository;

//    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//        if (checkRole(request, response) && checkName(request, response))

        if (checkAndPoint(request.getMethod(), request.getServletPath())) {
            Principal principal = request.getUserPrincipal();
            String[] arr = request.getServletPath().split("/");
            String userName = arr[arr.length - 1];

            User user = userAccountRepository
                    .findById(((HttpServletRequest) request).getUserPrincipal().getName()).get();


            if (!(principal.getName().equalsIgnoreCase(userName) || user.getRoles().contains(Role.ADMINISTRATOR))) {
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
