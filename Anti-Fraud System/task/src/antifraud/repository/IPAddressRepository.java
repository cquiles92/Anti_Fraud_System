package antifraud.repository;

import antifraud.entity.IPAddress;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface IPAddressRepository extends PagingAndSortingRepository<IPAddress, Long> {
    @Override
    List<IPAddress> findAll(Sort id);

    Optional<IPAddress> findByIp(String ip);
}
