package ng.al3x3i.parking;

import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarParkingSystemIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CarParkingService carParkingService;

    @Getter
    @Autowired
    private ParkingBuildingRepository parkingBuildingRepository;

    @Getter
    @Autowired
    private ParkingFloorRepository parkingFloorRepository;

    @Getter
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    private CarParkingController.ParkingSlotReservationRequestPayload payload;

    @BeforeEach
    public void setup() {
        givenParkingBuilding();
    }

    @AfterEach
    public void clean()  {
        parkingBuildingRepository.deleteAll();
        parkingFloorRepository.deleteAll();
        parkingSlotRepository.deleteAll();
    }

    @Test
    public void should_fail_to_add_big_car_when_parking_is_full_by_weight() {
        // GIVEN

        givenThreeBigSizeCarsInCarParking();

        // WHEN
        Optional<ParkingSlotEntity> parkingSlot = carParkingService.addCarToParkingLot(payload);

        // THEN
        then(parkingSlot).isEmpty();

        then(parkingSlotRepository.findAll()).hasSize(3);
    }

    @Test
    public void should_fail_to_add_small_cars_to_different_floors() {
        // GIVEN
        givenFiveSmallSizeCarsInCarParking();

        // WHEN
        Optional<ParkingSlotEntity> parkingSlot = carParkingService.addCarToParkingLot(payload);

        // THEN
        then(parkingSlot).isEmpty();

        then(parkingSlotRepository.findAll()).hasSize(15);
        var carSlots = parkingFloorRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        then(carSlots.get(0).getParkingSlots()).hasSize(7);
        then(carSlots.get(1).getParkingSlots()).hasSize(4);
        then(carSlots.get(2).getParkingSlots()).hasSize(4);
    }

    public void givenThreeBigSizeCarsInCarParking() {
        givenBigSizeCarPayload();
        IntStream.range(0, 3).forEach(index -> carParkingService.addCarToParkingLot(payload));
    }

    public void givenFiveSmallSizeCarsInCarParking() {
        givenSmallSizeCarPayload();
        IntStream.range(0, 20).forEach(index -> carParkingService.addCarToParkingLot(payload));
    }

    public void givenSmallSizeCarPayload() {
        payload = CarParkingController.ParkingSlotReservationRequestPayload.builder()
                .height(1550)
                .weight(900)
                .build();
    }

    public void givenBigSizeCarPayload() {
        payload = CarParkingController.ParkingSlotReservationRequestPayload.builder()
                .height(1800)
                .weight(1800)
                .build();
    }
}
