package java.DAO;

import java.entity.BedDetails;
import java.util.List;
import java.util.stream.Collectors;

public class BedDetailsDAO extends BaseFileDAO<BedDetails, Integer> {

    private static BedDetailsDAO instance;

    private BedDetailsDAO() {
        super(BedDetails.class);
    }

    public static synchronized BedDetailsDAO getInstance() {
        if (instance == null) {
            instance = new BedDetailsDAO();
        }
        return instance;
    }

    @Override
    protected Integer getId(BedDetails entity) {
        return entity.getId();
    }

    @Override
    protected void setId(BedDetails entity, Integer id) {
        entity.setId(id);
    }

    public List<BedDetails> findByCustomerId(Integer customerId) {
        return findAll().stream()
                .filter(detail -> customerId.equals(detail.getCustomerId()))
                .collect(Collectors.toList());
    }

    public List<BedDetails> findByBedId(Integer bedId) {
        return findAll().stream()
                .filter(detail -> bedId.equals(detail.getBedId()))
                .collect(Collectors.toList());
    }
}