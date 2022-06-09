package ro.orange.uber.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.orange.uber.entities.DriverRating;

import java.util.List;

@Repository
public interface DriverRatingRepository extends PagingAndSortingRepository<DriverRating, Long> {

    List<DriverRating> findAllByDriverId(long driverId);

}
