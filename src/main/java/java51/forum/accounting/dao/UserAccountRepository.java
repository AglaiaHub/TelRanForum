package java51.forum.accounting.dao;

import java51.forum.accounting.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserAccountRepository extends CrudRepository<User, String> {
}
