package java51.forum.security.context;

import java51.forum.security.model.UserPrincipal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class SecurityContextImpl implements SecurityContext{

    private Map<String, UserPrincipal> context = new ConcurrentHashMap<>();
    @Override
    public UserPrincipal addUserSession(String sessionId, UserPrincipal userPrincipal) {
        return context.put(sessionId, userPrincipal);
    }

    @Override
    public UserPrincipal removeUserPrincipal(String sessionId) {
        return context.remove(sessionId);
    }

    @Override
    public UserPrincipal getUserBySessionId(String sessionId) {
        return context.get(sessionId);
    }
}
