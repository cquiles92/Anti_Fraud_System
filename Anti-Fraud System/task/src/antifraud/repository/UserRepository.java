package antifraud.repository;

import antifraud.entity.RegisteredUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<RegisteredUser, Long> {
    Optional<RegisteredUser> findByName(String name);

    @Override
    List<RegisteredUser> findAll();

    @Override
    List<RegisteredUser> findAll(Sort id);

    Optional<RegisteredUser> findByUsernameIgnoreCase(String username);

    Optional<RegisteredUser> findById(long id);


}
