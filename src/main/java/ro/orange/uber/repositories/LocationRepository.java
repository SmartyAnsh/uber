package ro.orange.uber.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.orange.uber.entities.Location;

@Repository
public interface LocationRepository extends PagingAndSortingRepository<Location, Long> {

    Location findByName(String name);
}
