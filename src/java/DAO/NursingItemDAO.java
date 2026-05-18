// NursingItemDAO.java
package java.DAO;

import java.entity.NursingItem;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NursingItemDAO extends BaseFileDAO<NursingItem, Long> {

    private static NursingItemDAO instance;

    private NursingItemDAO() {
        super(NursingItem.class);
    }

    public static synchronized NursingItemDAO getInstance() {
        if (instance == null) {
            instance = new NursingItemDAO();
        }
        return instance;
    }

    @Override
    protected Long getId(NursingItem entity) {
        return entity.getId();
    }

    @Override
    protected void setId(NursingItem entity, Long id) {
        entity.setId(id);
    }

    // 特殊查询方法
    public Optional<NursingItem> findByCode(String code) {
        return findAll().stream()
                .filter(item -> code.equals(item.getCode()) &&
                        (item.getDeleted() == null || !item.getDeleted()))
                .findFirst();
    }

    public List<NursingItem> findByStatus(String status) {
        return findAll().stream()
                .filter(item -> status.equals(item.getStatus()) &&
                        (item.getDeleted() == null || !item.getDeleted()))
                .collect(Collectors.toList());
    }

    public List<NursingItem> findByNameLike(String name) {
        return findAll().stream()
                .filter(item -> item.getName() != null &&
                        item.getName().toLowerCase().contains(name.toLowerCase()) &&
                        (item.getDeleted() == null || !item.getDeleted()))
                .collect(Collectors.toList());
    }
}