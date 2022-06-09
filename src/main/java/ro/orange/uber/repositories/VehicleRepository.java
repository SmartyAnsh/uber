package ro.orange.uber.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.orange.uber.entities.Vehicle;

import java.util.List;

@Repository
public interface VehicleRepository extends PagingAndSortingRepository<Vehicle, Long> {

    List<Vehicle> findAllByDriverId(long driverId);

    List<Vehicle> findAllByDriverIdAndIsActive(long driverId, boolean isActive);
}
