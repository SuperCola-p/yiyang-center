package java.DAO;

import java.entity.Operator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OperatorDAO extends BaseFileDAO<Operator, String> {

    private static OperatorDAO instance;

    private OperatorDAO() {
        super(Operator.class);
    }

    public static synchronized OperatorDAO getInstance() {
        if (instance == null) {
            instance = new OperatorDAO();
        }
        return instance;
    }

    @Override
    protected String getId(Operator entity) {
        return entity.getLoginCode();
    }

    @Override
    protected void setId(Operator entity, String id) {
        entity.setLoginCode(id);
    }

    public Optional<Operator> findByRealName(String realName) {
        return findAll().stream()
                .filter(operator -> realName.equals(operator.getRealName()))
                .findFirst();
    }

    public boolean validateLogin(String loginCode, String password) {
        return findAll().stream()
                .anyMatch(operator -> loginCode.equals(operator.getLoginCode()) &&
                        password.equals(operator.getPassword()));
    }
}