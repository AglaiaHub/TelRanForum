package java51.forum.security.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import java51.forum.accounting.dao.UserAccountRepository;
import java51.forum.accounting.model.UserAccount;
import java51.forum.security.context.SecurityContext;
import java51.forum.security.model.UserPrincipal;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(10) //фильтры выполняются от меньшего к большему
public class AuthenticationFilter
        implements Filter
{

    final UserAccountRepository userAccountRepository;
    final SecurityContext securityContext;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        if (checkEndPoint(request.getMethod(), request.getServletPath())) {
            String sessionId = request.getSession().getId();
            UserPrincipal userPrincipal = securityContext.getUserBySessionId(sessionId);
            if (userPrincipal == null) {
                try {
                    String[] credentials = getCredentials(request.getHeader("Authorization"));
                    UserAccount userAccount = userAccountRepository.findById(credentials[0])
                            .orElseThrow(RuntimeException::new);
                    if (!BCrypt.checkpw(credentials[1], userAccount.getPassword())) {
                        throw new RuntimeException();
                    }
                    Set<String> roles = userAccount.getRoles().stream().map(r -> r.name()).collect(Collectors.toSet());
                    userPrincipal = new UserPrincipal(userAccount.getLogin(), roles);
                    securityContext.addUserSession(sessionId, userPrincipal);
                } catch (Exception e) {
                    response.sendError(401);
                    return;
                }
//                roles = userAccount.getRoles().stream().map(r -> r.toString()).collect(Collectors.toSet());
            }
            request = new WrappedRequest(request, userPrincipal.getName(), userPrincipal.getRoles());
        }

        chain.doFilter(request, response);

    }

    private boolean checkEndPoint(String method, String path) {
        return !(
                (HttpMethod.POST.matches(method) && path.matches("/account/register"))
                        || path.matches("/forum/posts/\\w+(/\\w+)?")
        );
    }

    private String[] getCredentials(String header) {
        String token = header.split(" ")[1];
        String decode = new String(Base64.getDecoder().decode(token));
        return decode.split(":");
    }

    private class WrappedRequest extends HttpServletRequestWrapper {
        private String login;
        private Set<String> roles;

        public WrappedRequest(HttpServletRequest request, String login, Set<String> roles) {
            super(request);
            this.login = login;
            this.roles = roles;
        }

        @Override
        public Principal getUserPrincipal() {
            return new UserPrincipal(login, roles);
        }

    }
}
