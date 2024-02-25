package java51.forum.security.context;

import java51.forum.security.model.UserPrincipal;

public interface SecurityContext {
    UserPrincipal addUserSession (String sessionId, UserPrincipal userPrincipal);

    UserPrincipal removeUserPrincipal (String sessionId);

    UserPrincipal getUserBySessionId(String sessionId);
}
