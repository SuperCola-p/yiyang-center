package java.DAO;

import java.entity.Client;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientDAO extends BaseFileDAO<Client, Integer> {

    private static ClientDAO instance;

    private ClientDAO() {
        super(Client.class);
    }

    public static synchronized ClientDAO getInstance() {
        if (instance == null) {
            instance = new ClientDAO();
        }
        return instance;
    }

    @Override
    protected Integer getId(Client entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Client entity, Integer id) {
        entity.setId(id);
    }

    public Optional<Client> findByName(String name) {
        return findAll().stream()
                .filter(client -> name.equals(client.getName()))
                .findFirst();
    }

    public List<Client> findByNameLike(String name) {
        return findAll().stream()
                .filter(client -> client.getName() != null &&
                        client.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Client> findByType(String type) {
        return findAll().stream()
                .filter(client -> type.equals(client.getType()))
                .collect(Collectors.toList());
    }

    public List<Client> findByNursingLevel(String nursingLevel) {
        return findAll().stream()
                .filter(client -> nursingLevel.equals(client.getNursingLevel()))
                .collect(Collectors.toList());
    }

    public Optional<Client> findByBed(String buildingNo, String roomNo, String bedNo) {
        return findAll().stream()
                .filter(client -> buildingNo.equals(client.getBuildingNo()) &&
                        roomNo.equals(client.getRoomNo()) &&
                        bedNo.equals(client.getBedNo()))
                .findFirst();
    }
}