package java51.forum.accounting.dao;

import java51.forum.accounting.model.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepository extends CrudRepository<UserAccount, String> {
}
