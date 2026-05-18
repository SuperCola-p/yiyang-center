package java.DAO;

import java.entity.LeaveApplication;
import java.util.List;
import java.util.stream.Collectors;

public class LeaveApplicationDAO extends BaseFileDAO<LeaveApplication, String> {

    private static LeaveApplicationDAO instance;

    private LeaveApplicationDAO() {
        super(LeaveApplication.class);
    }

    public static synchronized LeaveApplicationDAO getInstance() {
        if (instance == null) {
            instance = new LeaveApplicationDAO();
        }
        return instance;
    }

    // 使用复合键：reason + time
    @Override
    protected String getId(LeaveApplication entity) {
        return entity.getReason() + "_" + entity.getTime();
    }

    @Override
    protected void setId(LeaveApplication entity, String id) {
        // LeaveApplication 没有 id 字段，不进行任何操作
    }

    public List<LeaveApplication> findByStatue(String statue) {
        return findAll().stream()
                .filter(app -> statue.equals(app.getStatue()))
                .collect(Collectors.toList());
    }

    public List<LeaveApplication> findUnprocessed() {
        return findAll().stream()
                .filter(app -> "false".equals(app.getStatue()))
                .collect(Collectors.toList());
    }
}