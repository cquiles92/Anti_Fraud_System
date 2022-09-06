package antifraud.repository;

import antifraud.entity.CardDetails;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardDetailsRepository extends PagingAndSortingRepository<CardDetails, Long> {
    Optional<CardDetails> findByNumber(String number);


}


