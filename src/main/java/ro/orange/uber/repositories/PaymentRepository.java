package ro.orange.uber.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.orange.uber.entities.Payment;

@Repository
public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {
}
