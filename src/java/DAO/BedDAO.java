// BedDAO.java
package java.DAO;

import java.entity.Bed;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BedDAO extends BaseFileDAO<Bed, Long> {

    private static BedDAO instance;

    private BedDAO() {
        super(Bed.class);
    }

    public static synchronized BedDAO getInstance() {
        if (instance == null) {
            instance = new BedDAO();
        }
        return instance;
    }

    @Override
    protected Long getId(Bed entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Bed entity, Long id) {
        entity.setId(id);
    }

    public Optional<Bed> findByBuildingAndRoomAndBed(String building, Integer roomNo, String bedNo) {
        return findAll().stream()
                .filter(bed -> building.equals(bed.getBuilding()) &&
                        roomNo.equals(bed.getRoomNo()) &&
                        bedNo.equals(bed.getBedNo()))
                .findFirst();
    }

    public List<Bed> findAvailableBeds() {
        return findAll().stream()
                .filter(bed -> bed.getBedStatus() != null && bed.getBedStatus() == 1)
                .collect(Collectors.toList());
    }
}