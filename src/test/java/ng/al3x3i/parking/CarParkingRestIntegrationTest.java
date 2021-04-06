package ng.al3x3i.parking;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import ng.al3x3i.parking.CarParkingController.ParkingSlotReservationRequestPayload;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarParkingRestIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Getter
    @Autowired
    private ParkingBuildingRepository parkingBuildingRepository;

    @Getter
    @Autowired
    private ParkingFloorRepository parkingFloorRepository;

    @Getter
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    private CarParkingService carParkingService;

    private ParkingSlotReservationRequestPayload payload;

    private ParkingSlotEntity parkingSlotEntity;

    private ResultActions resultActions;


    @BeforeEach
    public void setup() {
        givenParkingBuilding();
    }

    @AfterEach
    public void clean() {
        parkingBuildingRepository.deleteAll();
        parkingFloorRepository.deleteAll();
        parkingSlotRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void should_add_car_to_parking() {
        // GIVEN
        givenSmallSizeCarPayload();

        // WHEN
        resultActions = mockMvc.perform(post("/parking")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andDo(print());

        // THEN
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("slotNumber", equalTo("1-1")))
                .andExpect(jsonPath("floorNumber", is(1)))
                .andExpect(jsonPath("isRegistered", equalTo(true)));
        then(parkingSlotRepository.findAll()).hasSize(1);
    }

    @Test
    @SneakyThrows
    public void should_get_parking_invoice() {
        // GIVEN
        givenOneParkingCar();

        // WHEN
        resultActions = mockMvc.perform(post("/parking/{id}/create-invoice", parkingSlotEntity.getId())
                .contentType(APPLICATION_JSON))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("parkingSlotId").value(parkingSlotEntity.getId()))
                .andExpect(jsonPath("totalAmount").isNotEmpty());
    }

    @Test
    @SneakyThrows
    public void should_fail_to_add_car_with_missing_weight_in_request() {
        // GIVEN
        givenCarPayloadWithoutWeight();

        // WHEN
        resultActions = mockMvc.perform(post("/parking")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andDo(print());

        // THEN
        resultActions.andExpect(status().isBadRequest());
        then(parkingSlotRepository.findAll()).hasSize(0);
    }

    @Test
    @SneakyThrows
    public void should_fail_to_add_car_with_negative_weight_in_request() {
        // GIVEN
        givenCarPayloadWithNegativeWeight();

        // WHEN
        resultActions = mockMvc.perform(post("/parking")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andDo(print());

        // THEN
        resultActions.andExpect(status().isBadRequest());
        then(parkingSlotRepository.findAll()).hasSize(0);
    }

    public void givenOneParkingCar() {
        givenSmallSizeCarPayload();
        parkingSlotEntity = carParkingService.addCarToParkingLot(payload).orElseThrow();
    }

    public void givenSmallSizeCarPayload() {
        payload = ParkingSlotReservationRequestPayload.builder()
                .height(1550)
                .weight(1300)
                .build();
    }

    public void givenCarPayloadWithoutWeight() {
        payload = ParkingSlotReservationRequestPayload.builder()
                .height(1550)
                .build();
    }

    public void givenCarPayloadWithNegativeWeight() {
        payload = ParkingSlotReservationRequestPayload.builder()
                .height(1550)
                .weight(-100)
                .build();
    }
}
