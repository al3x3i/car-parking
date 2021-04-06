package ng.al3x3i.parking;

import ng.al3x3i.parking.CarParkingController.ParkingSlotReservationRequestPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarParkingService {


    @Autowired
    private ParkingBuildingRepository parkingBuildingRepository;

    @Autowired
    private ParkingFloorRepository parkingFloorRepository;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;


    public Optional<ParkingSlotEntity> addCarToParkingLot(ParkingSlotReservationRequestPayload payload) {

        Optional<ParkingFloorEntity> availableFloorForParking = findFreeParkingSlot(payload.getWeight(), payload.getHeight());

        return availableFloorForParking.map(
                parkingFloor -> registerCarSlot(parkingFloor, payload.getWeight())
        );
    }

    private ParkingSlotEntity registerCarSlot(ParkingFloorEntity parkingFloor, int carWeight) {

        int newFloorWeight = parkingFloor.getCurrentWeight() + carWeight;
        int nextSlotNumber = parkingFloor.getParkingSlots().size() + 1;
        parkingFloor.setCurrentWeight(newFloorWeight);

        String slotNumber = String.format("%s-%s", parkingFloor.getFloorNumber(), nextSlotNumber);

        var parkingSlot = ParkingSlotEntity.builder()
                .slotNumber(slotNumber)
                .parkingFloor(parkingFloor)
                .floorNumber(parkingFloor.getFloorNumber())
                .build();

        return parkingSlotRepository.saveAndFlush(parkingSlot);
    }

    private Optional<ParkingFloorEntity> findFreeParkingSlot(int carWeight, int carHeight) {
        List<ParkingFloorEntity> parkingFloors = findAllParkingFloors();

        Optional<ParkingFloorEntity> availableFloorForParking = parkingFloors.stream()
                .filter(parkingFloor -> {

                    if (!parkingFloor.isFloorFull()) {

                        int currentWeight = parkingFloor.getCurrentWeight();

                        if (carHeight <= parkingFloor.getMaxHeight()) {

                            return currentWeight + carWeight <= parkingFloor.getMaxWeight();
                        }
                    }
                    return false;
                }).findFirst();

        return availableFloorForParking;
    }

    public List<ParkingFloorEntity> findAllParkingFloors() {

        var parkingBuilding = parkingBuildingRepository.findAll().stream().findFirst();
        return parkingBuilding.map(ParkingBuildingEntity::getParkingFloors)
                .orElseGet(() -> {
                    throw new RuntimeException("Error, no parking building");
                });
    }
}
