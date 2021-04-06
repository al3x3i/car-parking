package ng.al3x3i.parking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingBuildingRepository extends JpaRepository<ParkingBuildingEntity, Long> {
}
