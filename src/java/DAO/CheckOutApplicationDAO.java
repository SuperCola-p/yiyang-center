package java.DAO;

import java.entity.checkOutApplication;
import java.util.List;
import java.util.stream.Collectors;

public class CheckOutApplicationDAO extends BaseFileDAO<checkOutApplication, String> {

    private static CheckOutApplicationDAO instance;

    private CheckOutApplicationDAO() {
        super(checkOutApplication.class);
    }

    public static synchronized CheckOutApplicationDAO getInstance() {
        if (instance == null) {
            instance = new CheckOutApplicationDAO();
        }
        return instance;
    }

    // 使用复合键：type + time + reason
    @Override
    protected String getId(checkOutApplication entity) {
        return entity.getType() + "_" + entity.getTime() + "_" + entity.getReason();
    }

    @Override
    protected void setId(checkOutApplication entity, String id) {
        // checkOutApplication 没有 id 字段，不进行任何操作
    }

    public List<checkOutApplication> findByStatue(String statue) {
        return findAll().stream()
                .filter(app -> statue.equals(app.getStatue()))
                .collect(Collectors.toList());
    }

    public List<checkOutApplication> findByType(String type) {
        return findAll().stream()
                .filter(app -> type.equals(app.getType()))
                .collect(Collectors.toList());
    }
}