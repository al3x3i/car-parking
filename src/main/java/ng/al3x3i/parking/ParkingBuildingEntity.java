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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "PARKING_BUILDING")
@EqualsAndHashCode
public class ParkingBuildingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "parkingBuilding", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ParkingFloorEntity> parkingFloors = new ArrayList<>();


    public void addParkingFloor(ParkingFloorEntity parkingFloorEntity) {
        parkingFloors.add(parkingFloorEntity);
    }
}
