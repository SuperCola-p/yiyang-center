// NursingLevelDAO.java
package java.DAO;

import java.entity.NursingLevel;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NursingLevelDAO extends BaseFileDAO<NursingLevel, Long> {

    private static NursingLevelDAO instance;

    private NursingLevelDAO() {
        super(NursingLevel.class);
    }

    public static synchronized NursingLevelDAO getInstance() {
        if (instance == null) {
            instance = new NursingLevelDAO();
        }
        return instance;
    }

    @Override
    protected Long getId(NursingLevel entity) {
        return entity.getId();
    }

    @Override
    protected void setId(NursingLevel entity, Long id) {
        entity.setId(id);
    }

    public Optional<NursingLevel> findByLevelName(String levelName) {
        return findAll().stream()
                .filter(level -> levelName.equals(level.getLevelName()))
                .findFirst();
    }

    public List<NursingLevel> findByStatus(String status) {
        return findAll().stream()
                .filter(level -> status.equals(level.getStatus()))
                .collect(Collectors.toList());
    }
}