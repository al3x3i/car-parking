package ng.al3x3i.parking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class CarParkingController {

    @Autowired
    private CarParkingService carParkingService;

    @Autowired
    private CarParkingPaymentService carParkingPaymentService;

    @PostMapping("parking")
    public ResponseEntity parkCar(@RequestBody @Valid ParkingSlotReservationRequestPayload payload) {

        return carParkingService.addCarToParkingLot(payload).map(carSlot -> {

            var response = ParkingSlotReservationResponsePayload.builder()
                    .slotId(carSlot.getId())
                    .slotNumber(carSlot.getSlotNumber())
                    .floorNumber(carSlot.getFloorNumber())
                    .isRegistered(true)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }).orElseGet(() -> new ResponseEntity<>(
                ParkingSlotReservationResponsePayload.builder()
                        .isRegistered(false).build(), HttpStatus.OK));
    }

    @PostMapping("parking/{parkingSlot}/create-invoice")
    public ResponseEntity createInvoice(@PathVariable("parkingSlot") Optional<ParkingSlotEntity> parkingSlot) {

        return parkingSlot.map(slot -> {
            var totalToPay = carParkingPaymentService.getParkingPrice(slot);
            var response = ParkingSlotPaymentReceiveResponsePayload.builder()
                    .parkingSlotId(slot.getId())
                    .totalAmount(totalToPay).build();
            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Data
    @AllArgsConstructor
    @Builder
    static class ParkingSlotReservationRequestPayload {
        @NotNull
        @Min(0)
        private Integer weight;
        @NotNull
        @Min(0)
        private Integer height;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    @Builder
    static class ParkingSlotReservationResponsePayload {
        private Long slotId;
        private String slotNumber;
        private Integer floorNumber;
        private Boolean isRegistered;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    @Builder
    static class ParkingSlotPaymentReceiveResponsePayload {
        private Long parkingSlotId;
        private String totalAmount;
    }
}
