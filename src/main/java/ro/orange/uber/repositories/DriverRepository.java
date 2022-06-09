package ro.orange.uber.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.orange.uber.entities.Driver;

@Repository
public interface DriverRepository extends PagingAndSortingRepository<Driver, Long> {

    Driver findByPhoneNumber(String phoneNumber);

}
