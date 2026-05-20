// ClientNursingSettingDAO.java
package java.DAO;

import java.entity.ClientNursingSetting;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientNursingSettingDAO extends BaseFileDAO<ClientNursingSetting, Long> {

    private static ClientNursingSettingDAO instance;

    private ClientNursingSettingDAO() {
        super(ClientNursingSetting.class);
    }

    public static synchronized ClientNursingSettingDAO getInstance() {
        if (instance == null) {
            instance = new ClientNursingSettingDAO();
        }
        return instance;
    }

    @Override
    protected Long getId(ClientNursingSetting entity) {
        return entity.getId();
    }

    @Override
    protected void setId(ClientNursingSetting entity, Long id) {
        entity.setId(id);
    }

    public List<ClientNursingSetting> findByClientId(Long clientId) {
        return findAll().stream()
                .filter(setting -> clientId.equals(setting.getClientId()) &&
                        (setting.getDeleted() == null || !setting.getDeleted()))
                .collect(Collectors.toList());
    }

    public Optional<ClientNursingSetting> findByClientIdAndItemId(Long clientId, Long itemId) {
        return findAll().stream()
                .filter(setting -> clientId.equals(setting.getClientId()) &&
                        itemId.equals(setting.getNursingItemId()) &&
                        (setting.getDeleted() == null || !setting.getDeleted()))
                .findFirst();
    }
}