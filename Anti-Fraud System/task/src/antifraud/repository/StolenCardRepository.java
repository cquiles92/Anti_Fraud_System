package antifraud.repository;

import antifraud.entity.StolenCard;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StolenCardRepository extends PagingAndSortingRepository<StolenCard, Long> {
    @Override
    List<StolenCard> findAll(Sort sort);

    Optional<StolenCard> findByNumber(String number);
}
