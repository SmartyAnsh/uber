package ro.orange.uber.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ro.orange.uber.entities.Customer;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    Customer findByPhoneNumber(String phoneNumber);
}
