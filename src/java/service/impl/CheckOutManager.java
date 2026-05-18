package java.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.entity.*;
import java.DAO.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
public class CheckOutManager {
    private final CheckOutApplicationDAO checkOutApplicationDAO = CheckOutApplicationDAO.getInstance();
    private final BedDAO bedDAO = BedDAO.getInstance();
    private static final String BUILDING = "606";

    public List<checkOutApplication> findCheckOutApply(String clientName) {
        return checkOutApplicationDAO.findAll();
    }

    public void auditCheckOut(String applyId, String auditStatus, String auditor) {
        Optional<checkOutApplication> applyOptional = checkOutApplicationDAO.findById(applyId);
        if (!applyOptional.isPresent()) return;

        checkOutApplication apply = applyOptional.get();
        apply.setStatue(auditStatus);
        checkOutApplicationDAO.update(apply);

    }
}