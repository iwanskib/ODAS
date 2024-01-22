package odas.repository;

import odas.model.User;
import odas.model.UserSensitiveData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSensitiveDataRepository extends JpaRepository<UserSensitiveData, Long> {
    UserSensitiveData findByUser(User user);
}
