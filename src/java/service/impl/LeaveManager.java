package java.service.impl;

import java.util.List;
import java.util.Optional;
import java.entity.LeaveApplication;
import java.entity.Bed;
import java.DAO.LeaveApplicationDAO;
import java.DAO.BedDAO;

public class LeaveManager {
    private final LeaveApplicationDAO leaveApplicationDAO = LeaveApplicationDAO.getInstance();
    private final BedDAO bedDAO = BedDAO.getInstance();
    private static final String BUILDING = "606";

    // 查询所有外出申请
    public List<LeaveApplication> findLeaveApply(String clientName) {
        return leaveApplicationDAO.findAll();
    }

    // 审核外出（只调用你实体里有的方法）
    public void auditLeave(String applyId, String auditStatus, String auditor) {
        Optional<LeaveApplication> applyOptional = leaveApplicationDAO.findById(applyId);
        if (!applyOptional.isPresent()) return;

        LeaveApplication apply = applyOptional.get();
        apply.setStatue(auditStatus); // 正确存在
        leaveApplicationDAO.update(apply);


    }
}