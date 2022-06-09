package ro.orange.uber.repositories;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.orange.uber.entities.Trip;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends PagingAndSortingRepository<Trip, Long> {

    List<Trip> findAllByDriverId(long driverId);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Trip t where t.id = :id")
    Optional<Trip> findByIdWithPessimisticLock(Long id);
}
