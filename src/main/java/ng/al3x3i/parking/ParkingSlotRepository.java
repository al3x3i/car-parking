package ng.al3x3i.parking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlotEntity, Long> {
}
