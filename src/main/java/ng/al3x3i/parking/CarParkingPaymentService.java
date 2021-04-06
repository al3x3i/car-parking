package ng.al3x3i.parking;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class CarParkingPaymentService {

    double costPerSecond = 0.001;

    public String getParkingPrice(ParkingSlotEntity parkingSlot) {
        LocalDateTime localDateTime = parkingSlot.getCreatedAt();
        long seconds = ChronoUnit.SECONDS.between(localDateTime, LocalDateTime.now());
        String totalAmount = BigDecimal.valueOf(costPerSecond).multiply(BigDecimal.valueOf(seconds)).toString();
        return totalAmount;
    }

}
