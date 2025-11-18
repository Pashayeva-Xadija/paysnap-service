package az.devlab.paysnapservice.repository;

import az.devlab.paysnapservice.model.BlacklistedToken;
import org.springframework.data.repository.CrudRepository;

public interface BlacklistedTokenRepository extends CrudRepository<BlacklistedToken, String> {

    boolean existsByToken(String token);
}
