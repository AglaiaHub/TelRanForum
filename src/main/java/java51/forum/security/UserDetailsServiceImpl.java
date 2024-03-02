package java51.forum.security;

import java51.forum.accounting.dao.UserAccountRepository;
import java51.forum.accounting.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    final UserAccountRepository userAccountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // todo Auto-generated method stub
        UserAccount userAccount = userAccountRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Collection<String> authorities = userAccount.getRoles()
                .stream()
                .map(r -> "ROLE_" + r.name())   // по правилам спринг секрьрити имя должно быть в форме ROLE_BLABLABLA
                .collect(Collectors.toList());
        return new User(username, userAccount.getPassword(),
                AuthorityUtils.createAuthorityList(authorities));
    }
}
