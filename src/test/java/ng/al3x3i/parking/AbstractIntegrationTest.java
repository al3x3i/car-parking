package ng.al3x3i.parking;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

public abstract class AbstractIntegrationTest {

    public abstract ParkingBuildingRepository getParkingBuildingRepository();

    public abstract ParkingFloorRepository getParkingFloorRepository();

    public abstract ParkingSlotRepository getParkingSlotRepository();

    @Transactional
    public void givenParkingBuilding() {
        var building = getParkingBuildingRepository().saveAndFlush(new ParkingBuildingEntity());

        var firstFloor = ParkingFloorEntity.builder()
                .floorNumber(1)
                .maxHeight(2000)
                .maxWeight(7000)
                .currentWeight(0)
                .numberOfSlots(10)
                .parkingBuilding(building)
                .parkingSlots(new ArrayList<>())
                .build();
        getParkingFloorRepository().saveAndFlush(firstFloor);

        var secondFloor = ParkingFloorEntity.builder()
                .floorNumber(2)
                .maxHeight(1700)
                .maxWeight(4000)
                .currentWeight(0)
                .numberOfSlots(10)
                .parkingBuilding(building)
                .parkingSlots(new ArrayList<>())
                .build();
        getParkingFloorRepository().saveAndFlush(secondFloor);

        var thirdFloor = ParkingFloorEntity.builder()
                .floorNumber(3)
                .maxHeight(1600)
                .maxWeight(4000)
                .currentWeight(0)
                .numberOfSlots(10)
                .parkingBuilding(building)
                .parkingSlots(new ArrayList<>())
                .build();

        getParkingFloorRepository().saveAndFlush(thirdFloor);
    }
}
