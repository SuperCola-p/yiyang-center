// TestFileStorage.java - 测试文件存储功能
package test;

import java.entity.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.Util.*;

public class TestFileStorage {

    public static void main(String[] args) {
        // 初始化存储
        java.util.FileStorageUtil.initStorageDirectory();

        // 测试护理项目存储
        testNursingItemStorage();

        // 测试护理级别存储
        testNursingLevelStorage();

        // 测试客户护理设置存储
        testClientNursingSettingStorage();

        // 测试护理记录存储
        testNursingRecordStorage();

        System.out.println("所有测试完成！");
    }

    private static void testNursingItemStorage() {
        System.out.println("\n=== 测试护理项目存储 ===");
        NursingItemDAO dao = NursingItemDAO.getInstance();

        // 创建护理项目
        NursingItem item1 = new NursingItem();
        item1.setCode("N001");
        item1.setName("日常护理");
        item1.setPrice(new BigDecimal("100.00"));
        item1.setStatus("启用");
        item1.setExecPeriod("每日");
        item1.setExecTimes(1);

        NursingItem item2 = new NursingItem();
        item2.setCode("N002");
        item2.setName("康复护理");
        item2.setPrice(new BigDecimal("200.00"));
        item2.setStatus("启用");
        item2.setExecPeriod("每周");
        item2.setExecTimes(3);

        // 保存护理项目
        dao.save(item1);
        dao.save(item2);

        // 查询所有护理项目
        System.out.println("所有护理项目:");
        dao.findAll().forEach(item ->
                System.out.println("  " + item.getCode() + ": " + item.getName()));

        // 根据状态查询
        System.out.println("\n启用的护理项目:");
        dao.findByStatus("启用").forEach(item ->
                System.out.println("  " + item.getName()));
    }

    private static void testNursingLevelStorage() {
        System.out.println("\n=== 测试护理级别存储 ===");
        NursingLevelDAO dao = NursingLevelDAO.getInstance();
        NursingItemDAO itemDao = NursingItemDAO.getInstance();

        // 获取护理项目
        NursingItem basicItem = itemDao.findAll().stream()
                .filter(item -> "日常护理".equals(item.getName()))
                .findFirst().orElse(null);

        // 创建护理级别
        NursingLevel level1 = new NursingLevel();
        level1.setLevelName("基础护理");
        level1.setStatus("启用");

        if (basicItem != null) {
            NursingItemArray items = new NursingItemArray();
            items.addItem(basicItem);
            level1.setNursingItems(items);
        }

        // 保存护理级别
        dao.save(level1);

        // 查询所有护理级别
        System.out.println("所有护理级别:");
        dao.findAll().forEach(level ->
                System.out.println("  " + level.getLevelName()));
    }

    private static void testClientNursingSettingStorage() {
        System.out.println("\n=== 测试客户护理设置存储 ===");
        ClientNursingSettingDAO dao = ClientNursingSettingDAO.getInstance();
        NursingItemDAO itemDao = NursingItemDAO.getInstance();

        // 获取护理项目
        NursingItem item = itemDao.findAll().stream()
                .filter(i -> "日常护理".equals(i.getName()))
                .findFirst().orElse(null);

        if (item != null) {
            // 创建客户护理设置
            ClientNursingSetting setting = new ClientNursingSetting();
            setting.setClientId(1001L);
            setting.setNursingItemId(item.getId());
            setting.setPurchaseDate(LocalDate.now());
            setting.setTotalQuantity(10);
            setting.setRemainingQuantity(10);
            setting.setServiceDueDate(LocalDate.now().plusMonths(1));
            setting.setServiceStatus("正常");

            // 保存设置
            dao.save(setting);

            // 查询客户的护理设置
            System.out.println("客户1001的护理设置:");
            dao.findByClientId(1001L).forEach(s ->
                    System.out.println("  项目ID: " + s.getNursingItemId() +
                            ", 剩余次数: " + s.getRemainingQuantity()));
        }
    }

    private static void testNursingRecordStorage() {
        System.out.println("\n=== 测试护理记录存储 ===");
        NursingRecordDAO dao = NursingRecordDAO.getInstance();

        // 创建护理记录
        NursingRecord record = new NursingRecord();
        record.setClientId(1001L);
        record.setNursingItemId(1L);
        record.setHealthAssistantId(201L);
        record.setNursingTime(LocalDateTime.now());
        record.setExecQuantity(1);

        // 保存记录
        dao.save(record);

        // 查询客户的护理记录
        System.out.println("客户1001的护理记录:");
        dao.findByClientId(1001L).forEach(r ->
                System.out.println("  时间: " + r.getNursingTime() +
                        ", 项目ID: " + r.getNursingItemId()));
    }
}