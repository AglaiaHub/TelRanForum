package java51.forum.security;

import java51.forum.accounting.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import java.util.function.Supplier;

@Configuration
public class AuthorizationConfig {

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.csrf(c -> c.disable()); //cross site - выключили
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/account/register", "forum/posts/**")
                    .permitAll()            //отмена любой авторизации, всем все можно
                .requestMatchers("/account/user/{login}/role{role}")
                    .hasRole(Role.ADMINISTRATOR.name())
                .requestMatchers(HttpMethod.PUT, "/account/user/{login}")
                    .access(new WebExpressionAuthorizationManager("#login == authentication.name")) // authentication = principal in Spring Secur
                // Spel Spring - синтаксический анализатор спринга
                .requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
                    .access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMINISTRATOR')")) // язык
                .requestMatchers(HttpMethod.POST, "/forum/post/{author}")
                    .access(new WebExpressionAuthorizationManager("#author == authentication.name"))
                .requestMatchers(HttpMethod.PUT, "/forum/post/{id}/comment/{author}")
                    .access(new WebExpressionAuthorizationManager("#author == authentication.name"))
//                .requestMatchers(HttpMethod.PUT, "/forum/post/{id}")
//                    .access(new WebExpressionAuthorizationManager("@customSecurity.checkPostAuthor(#id, authentication.name)"))// с помощью @ обращаемся к объекту из аппликационного контекста
//                        .access(new AuthorizationManager<T>() {
//                            @Override
//                            public AuthorizationDecision check(Supplier<Authentication> authentication, Object object) {
//                                authentication.get().getName();
//                                return null;
//                            }
//                        })

                // TODO два метода остались: PUT post/{id} + DELETE post/{id} (аналогично но + или hasRole MODERATOR
                // TODO почему T не видит
                .anyRequest()
                    .authenticated()// любой запрос требует авторизации // login, GET user etc
        );
        return http.build();
    }
}
