package ng.al3x3i.parking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PARKING_FLOOR")
@EqualsAndHashCode
public class ParkingFloorEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ParkingBuildingEntity parkingBuilding;

    @OneToMany(mappedBy = "parkingFloor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ParkingSlotEntity> parkingSlots = new ArrayList<>();

    private Integer floorNumber;
    private Integer maxWeight;
    private Integer maxHeight;
    private Integer numberOfSlots;

    private Integer currentWeight;

    public void addParkingSlot(ParkingSlotEntity parkingSlotEntity) {
        parkingSlots.add(parkingSlotEntity);
    }

    public boolean isFloorFull() {
        return parkingSlots.size() >= numberOfSlots;
    }

}
