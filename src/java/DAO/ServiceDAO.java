package java.DAO;

import java.entity.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServiceDAO extends BaseFileDAO<Service, String> {

    private static ServiceDAO instance;

    private ServiceDAO() {
        super(Service.class);
    }

    public static synchronized ServiceDAO getInstance() {
        if (instance == null) {
            instance = new ServiceDAO();
        }
        return instance;
    }

    @Override
    protected String getId(Service entity) {
        return entity.getLoginCode();
    }

    @Override
    protected void setId(Service entity, String id) {
        entity.setLoginCode(id);
    }

    public Optional<Service> findByRealName(String realName) {
        return findAll().stream()
                .filter(service -> realName.equals(service.getRealName()))
                .findFirst();
    }

    public boolean validateLogin(String loginCode, String password) {
        return findAll().stream()
                .anyMatch(service -> loginCode.equals(service.getLoginCode()) &&
                        password.equals(service.getPassword()));
    }
}