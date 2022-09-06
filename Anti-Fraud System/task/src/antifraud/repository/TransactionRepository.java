package antifraud.repository;

import antifraud.entity.Transaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
    @Override
    List<Transaction> findAll(Sort sort);

    @Query("select t from Transaction t where t.number = ?1 and t.date >= ?2 and t.date <= ?3")
    List<Transaction> findTransactionsInLastHour(String number, LocalDateTime date, LocalDateTime currentDate);

    List<Transaction> findByNumber(String cardNumber);
}