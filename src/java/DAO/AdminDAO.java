package java.DAO;

import java.entity.admin;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdminDAO extends BaseFileDAO<admin, String> {

    private static AdminDAO instance;

    private AdminDAO() {
        super(admin.class);
    }

    public static synchronized AdminDAO getInstance() {
        if (instance == null) {
            instance = new AdminDAO();
        }
        return instance;
    }

    @Override
    protected String getId(admin entity) {
        return entity.getLoginCode();
    }

    @Override
    protected void setId(admin entity, String id) {
        entity.setLoginCode(id);
    }

    public Optional<admin> findByRealName(String realName) {
        return findAll().stream()
                .filter(admin -> realName.equals(admin.getRealName()))
                .findFirst();
    }

    public boolean validateAdminLogin(String loginCode, String password) {
        return findAll().stream()
                .anyMatch(admin -> loginCode.equals(admin.getLoginCode()) &&
                        password.equals(admin.getPassword()));
    }
}