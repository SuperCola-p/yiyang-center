package com.yiyang.shell;

import com.yiyang.entity.*;
import com.yiyang.repository.*;
import com.yiyang.service.BedService;
import com.yiyang.service.ClientNursingSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 控制台主菜单 - Spring Shell 交互式菜单
 * 提供与网页端相同的功能，通过 CLI 操作
 */
@ShellComponent
public class MainMenuCommand {

    @Autowired private NursingItemRepository nursingItemRepository;
    @Autowired private NursingLevelRepository nursingLevelRepository;
    @Autowired private NursingLevelItemRepository nursingLevelItemRepository;
    @Autowired private BedRepository bedRepository;
    @Autowired private BedDetailsRepository bedDetailsRepository;
    @Autowired private ClientRepository clientRepository;
    @Autowired private ClientNursingSettingRepository clientNursingSettingRepository;
    @Autowired private NursingRecordRepository nursingRecordRepository;
    @Autowired private CheckOutApplicationRepository checkOutApplicationRepository;
    @Autowired private LeaveApplicationRepository leaveApplicationRepository;
    @Autowired private OperatorRepository operatorRepository;
    @Autowired private BedService bedService;
    @Autowired private ClientNursingSettingService clientNursingSettingService;

    private final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 在读取用户输入前确保输出缓冲被刷新，防止提示信息显示在输入之后。
     */
    private String readLine() {
        System.out.flush();
        return scanner.nextLine();
    }

    /** 带提示的读取 */
    private String readLine(String prompt) {
        System.out.print(prompt);
        System.out.flush();
        return scanner.nextLine();
    }

    // ==================== 主页菜单 ====================

    @ShellMethod("显示主菜单")
    public void menu() {
        System.out.println("\n====================================");
        System.out.println("  东软颐养中心管理系统 - 控制台菜单");
        System.out.println("====================================");
        System.out.println("  1. 护理项目管理");
        System.out.println("  2. 护理等级管理");
        System.out.println("  3. 床位管理");
        System.out.println("  4. 老人管理");
        System.out.println("  5. 护理服务设置");
        System.out.println("  6. 护理记录");
        System.out.println("  7. 退住申请");
        System.out.println("  8. 请假管理");
        System.out.println("  9. 操作员管理");
        System.out.println("  0. 退出系统");
        System.out.println("====================================");
        System.out.print("请选择 (0-9): ");
        String input = readLine().trim();
        handleMenuChoice(input);
    }

    private void handleMenuChoice(String input) {
        switch (input) {
            case "1": nursingItemMenu(); break;
            case "2": nursingLevelMenu(); break;
            case "3": bedMenu(); break;
            case "4": clientMenu(); break;
            case "5": nursingSettingMenu(); break;
            case "6": nursingRecordMenu(); break;
            case "7": checkOutMenu(); break;
            case "8": leaveMenu(); break;
            case "9": operatorMenu(); break;
            case "0":
                System.out.println("感谢使用！再见。");
                System.exit(0);
            default:
                System.out.println("无效选项，请重新输入。");
                menu();
        }
    }

    // ==================== 1. 护理项目管理 ====================

    private void nursingItemMenu() {
        System.out.println("\n--- 护理项目管理 ---");
        System.out.println("1. 查看列表  2. 新增  3. 修改  4. 删除  5. 切换状态  0. 返回主菜单");
        System.out.print("请选择: ");
        String input = readLine().trim();
        switch (input) {
            case "1": listNursingItems(); break;
            case "2": addNursingItem(); break;
            case "3": updateNursingItem(); break;
            case "4": deleteNursingItem(); break;
            case "5": toggleNursingItemStatus(); break;
            case "0": menu(); return;
            default: System.out.println("无效选项"); nursingItemMenu(); return;
        }
        nursingItemMenu();
    }

    private void listNursingItems() {
        List<NursingItem> list = nursingItemRepository.findByIsDeletedFalse();
        System.out.println("\n===== 护理项目列表 =====");
        System.out.printf("%-5s %-15s %-20s %-10s %-10s %-10s %-10s%n", "ID", "编码", "名称", "价格", "状态", "周期", "次数");
        System.out.println("-------------------------------------------------------------");
        for (NursingItem item : list) {
            System.out.printf("%-5d %-15s %-20s %-10s %-10s %-10s %-10d%n",
                    item.getId(), item.getCode(), item.getName(),
                    item.getPrice(), item.getStatus(), item.getExecPeriod(), item.getExecQuantity());
        }
        System.out.println("共 " + list.size() + " 条记录");
    }

    private void addNursingItem() {
        System.out.println("\n--- 新增护理项目 ---");
        try {
            NursingItem item = new NursingItem();
            System.out.print("项目编码: "); item.setCode(readLine());
            System.out.print("项目名称: "); item.setName(readLine());
            System.out.print("价格: "); item.setPrice(new BigDecimal(readLine()));
            System.out.print("状态(ENABLED/DISABLED): "); item.setStatus(readLine());
            System.out.print("执行周期(DAILY/WEEKLY/MONTHLY): "); item.setExecPeriod(readLine());
            System.out.print("执行次数: "); item.setExecQuantity(Integer.parseInt(readLine()));
            System.out.print("描述: "); item.setDescription(readLine());
            nursingItemRepository.save(item);
            System.out.println("新增成功！ID: " + item.getId());
        } catch (Exception e) {
            System.out.println("新增失败: " + e.getMessage());
        }
    }

    private void updateNursingItem() {
        System.out.print("请输入要修改的项目ID: ");
        Long id = Long.parseLong(readLine().trim());
        Optional<NursingItem> opt = nursingItemRepository.findById(id);
        if (!opt.isPresent() || opt.get().getIsDeleted()) {
            System.out.println("项目不存在");
            return;
        }
        NursingItem item = opt.get();
        try {
            System.out.print("名称(" + item.getName() + "): ");
            String v = readLine().trim();
            if (!v.isEmpty()) item.setName(v);
            System.out.print("价格(" + item.getPrice() + "): ");
            v = readLine().trim();
            if (!v.isEmpty()) item.setPrice(new BigDecimal(v));
            System.out.print("状态(" + item.getStatus() + "): ");
            v = readLine().trim();
            if (!v.isEmpty()) item.setStatus(v);
            nursingItemRepository.save(item);
            System.out.println("修改成功！");
        } catch (Exception e) {
            System.out.println("修改失败: " + e.getMessage());
        }
    }

    private void deleteNursingItem() {
        System.out.print("请输入要删除的项目ID: ");
        Long id = Long.parseLong(readLine().trim());
        Optional<NursingItem> opt = nursingItemRepository.findById(id);
        if (opt.isPresent()) {
            NursingItem item = opt.get();
            item.setIsDeleted(true);
            nursingItemRepository.save(item);
            System.out.println("删除成功");
        } else {
            System.out.println("项目不存在");
        }
    }

    private void toggleNursingItemStatus() {
        System.out.print("请输入项目ID: ");
        Long id = Long.parseLong(readLine().trim());
        Optional<NursingItem> opt = nursingItemRepository.findById(id);
        if (opt.isPresent() && !opt.get().getIsDeleted()) {
            NursingItem item = opt.get();
            item.setStatus("ENABLED".equals(item.getStatus()) ? "DISABLED" : "ENABLED");
            nursingItemRepository.save(item);
            System.out.println("状态已切换为: " + item.getStatus());
        } else {
            System.out.println("项目不存在");
        }
    }

    // ==================== 2. 护理等级管理 ====================

    private void nursingLevelMenu() {
        System.out.println("\n--- 护理等级管理 ---");
        System.out.println("1. 查看列表  2. 新增  3. 修改  4. 删除  5. 关联项目  0. 返回");
        System.out.print("请选择: ");
        String input = readLine().trim();
        switch (input) {
            case "1": listNursingLevels(); break;
            case "2": addNursingLevel(); break;
            case "3": updateNursingLevel(); break;
            case "4": deleteNursingLevel(); break;
            case "5": linkItemsToLevel(); break;
            case "0": menu(); return;
            default: System.out.println("无效选项"); break;
        }
        nursingLevelMenu();
    }

    private void listNursingLevels() {
        List<NursingLevel> list = nursingLevelRepository.findByDeletedFalse();
        System.out.println("\n===== 护理等级列表 =====");
        System.out.printf("%-5s %-20s %-10s %-10s%n", "ID", "等级名称", "状态", "关联项目数");
        for (NursingLevel lv : list) {
            long cnt = nursingLevelItemRepository.findByLevelId(lv.getId()).size();
            System.out.printf("%-5d %-20s %-10s %-10d%n", lv.getId(), lv.getLevelName(), lv.getStatus(), cnt);
        }
    }

    private void addNursingLevel() {
        System.out.println("\n--- 新增护理等级 ---");
        try {
            NursingLevel lv = new NursingLevel();
            System.out.print("等级名称: "); lv.setLevelName(readLine());
            System.out.print("状态(ENABLED/DISABLED): "); lv.setStatus(readLine());
            nursingLevelRepository.save(lv);
            System.out.println("新增成功！ID: " + lv.getId());
        } catch (Exception e) {
            System.out.println("失败: " + e.getMessage());
        }
    }

    private void updateNursingLevel() {
        System.out.print("等级ID: ");
        Long id = Long.parseLong(readLine().trim());
        Optional<NursingLevel> opt = nursingLevelRepository.findById(id);
        if (!opt.isPresent() || opt.get().getDeleted()) return;
        NursingLevel lv = opt.get();
        System.out.print("名称(" + lv.getLevelName() + "): ");
        String v = readLine().trim();
        if (!v.isEmpty()) lv.setLevelName(v);
        System.out.print("状态(" + lv.getStatus() + "): ");
        v = readLine().trim();
        if (!v.isEmpty()) lv.setStatus(v);
        nursingLevelRepository.save(lv);
        System.out.println("修改成功");
    }

    private void deleteNursingLevel() {
        System.out.print("等级ID: ");
        Long id = Long.parseLong(readLine().trim());
        Optional<NursingLevel> opt = nursingLevelRepository.findById(id);
        if (opt.isPresent()) {
            NursingLevel lv = opt.get();
            lv.setDeleted(true);
            nursingLevelRepository.save(lv);
            System.out.println("删除成功");
        }
    }

    private void linkItemsToLevel() {
        System.out.print("等级ID: ");
        Long levelId = Long.parseLong(readLine().trim());
        // 显示可选项目
        List<NursingItem> items = nursingItemRepository.findByIsDeletedFalse();
        System.out.println("可选护理项目:");
        for (NursingItem item : items) {
            System.out.println("  ID:" + item.getId() + " " + item.getName());
        }
        System.out.print("请输入要关联的项目ID(逗号分隔): ");
        String ids = readLine().trim();
        // 先删除旧关联
        List<NursingLevelItem> old = nursingLevelItemRepository.findByLevelId(levelId);
        nursingLevelItemRepository.deleteAll(old);
        // 添加新关联
        for (String s : ids.split(",")) {
            Long itemId = Long.parseLong(s.trim());
            NursingLevelItem li = NursingLevelItem.builder().levelId(levelId).itemId(itemId).build();
            nursingLevelItemRepository.save(li);
        }
        System.out.println("关联成功");
    }

    // ==================== 3. 床位管理 ====================

    private void bedMenu() {
        System.out.println("\n--- 床位管理 ---");
        System.out.println("1. 查看列表  2. 新增  3. 修改状态  4. 删除  0. 返回");
        System.out.print("请选择: ");
        String input = readLine().trim();
        switch (input) {
            case "1": listBeds(); break;
            case "2": addBed(); break;
            case "3": updateBedStatus(); break;
            case "4": deleteBed(); break;
            case "0": menu(); return;
            default: System.out.println("无效选项"); break;
        }
        bedMenu();
    }

    private void listBeds() {
        List<Bed> list = bedRepository.findAll();
        System.out.println("\n===== 床位列表 =====");
        System.out.printf("%-5s %-10s %-10s %-10s %-10s%n", "ID", "楼栋", "房间号", "床位号", "状态");
        for (Bed b : list) {
            String statusStr = b.getBedStatus() == 1 ? "空闲" : b.getBedStatus() == 2 ? "已入住" : "维修";
            System.out.printf("%-5d %-10s %-10d %-10s %-10s%n", b.getId(), b.getBuilding(), b.getRoomNo(), b.getBedNo(), statusStr);
        }
    }

    private void addBed() {
        System.out.println("\n--- 新增床位 ---");
        try {
            Bed bed = new Bed();
            System.out.print("楼栋: "); bed.setBuilding(readLine());
            System.out.print("房间号: "); bed.setRoomNo(Integer.parseInt(readLine()));
            System.out.print("床位号: "); bed.setBedNo(readLine());
            System.out.print("状态(1=空闲 2=已入住 3=维修): ");
            bed.setBedStatus(Integer.parseInt(readLine()));
            bedRepository.save(bed);
            System.out.println("新增成功");
        } catch (Exception e) {
            System.out.println("失败: " + e.getMessage());
        }
    }

    private void updateBedStatus() {
        System.out.print("床位ID: ");
        Long id = Long.parseLong(readLine().trim());
        Optional<Bed> opt = bedRepository.findById(id);
        if (!opt.isPresent()) return;
        System.out.print("新状态(1=空闲 2=已入住 3=维修): ");
        opt.get().setBedStatus(Integer.parseInt(readLine().trim()));
        bedRepository.save(opt.get());
        System.out.println("修改成功");
    }

    private void deleteBed() {
        System.out.print("床位ID: ");
        Long id = Long.parseLong(readLine().trim());
        bedRepository.deleteById(id);
        System.out.println("删除成功");
    }

    // ==================== 4. 老人管理 ====================

    private void clientMenu() {
        System.out.println("\n--- 老人管理 ---");
        System.out.println("1. 查看列表  2. 新增  3. 修改  4. 删除  5. 分配护理等级  0. 返回");
        System.out.print("请选择: ");
        String input = readLine().trim();
        switch (input) {
            case "1": listClients(); break;
            case "2": addClient(); break;
            case "3": updateClient(); break;
            case "4": deleteClient(); break;
            case "5": assignNursingLevel(); break;
            case "0": menu(); return;
            default: System.out.println("无效选项"); break;
        }
        clientMenu();
    }

    private void listClients() {
        List<Client> list = clientRepository.findByDeletedFalse();
        System.out.println("\n===== 老人列表 =====");
        System.out.printf("%-5s %-10s %-5s %-10s %-20s %-10s%n", "ID", "姓名", "年龄", "性别", "电话", "护理等级");
        for (Client c : list) {
            System.out.printf("%-5d %-10s %-5d %-10s %-20s %-10s%n",
                    c.getId(), c.getName(), c.getAge(), c.getGender(), c.getPhone(), c.getNursingLevel());
        }
    }

    private void addClient() {
        System.out.println("\n--- 新增老人 ---");
        try {
            Client c = new Client();
            System.out.print("姓名: "); c.setName(readLine());
            System.out.print("年龄: "); c.setAge(Integer.parseInt(readLine()));
            System.out.print("性别(男/女): "); c.setGender(readLine());
            System.out.print("血型: "); c.setBloodType(readLine());
            System.out.print("电话: "); c.setPhone(readLine());
            System.out.print("家属联系方式: "); c.setFamilyContact(readLine());
            System.out.print("身份证号: "); c.setIdCard(readLine());
            System.out.print("楼栋: "); c.setBuildingNo(readLine());
            System.out.print("房间: "); c.setRoomNo(readLine());
            System.out.print("床位: "); c.setBedNo(readLine());
            System.out.print("出生日期(yyyy-MM-dd): "); c.setBirthday(LocalDate.parse(readLine()));
            System.out.print("入住日期(yyyy-MM-dd): "); c.setCheckInDate(LocalDate.parse(readLine()));
            System.out.print("护理等级: "); c.setNursingLevel(readLine());
            System.out.print("类型(自理/半自理/全护理): "); c.setType(readLine());
            clientRepository.save(c);
            System.out.println("新增成功！ID: " + c.getId());
        } catch (Exception e) {
            System.out.println("失败: " + e.getMessage());
        }
    }

    private void updateClient() {
        System.out.print("老人ID: ");
        Integer id = Integer.parseInt(readLine().trim());
        Optional<Client> opt = clientRepository.findById(id);
        if (!opt.isPresent() || opt.get().getDeleted()) return;
        Client c = opt.get();
        try {
            System.out.print("姓名(" + c.getName() + "): ");
            String v = readLine().trim();
            if (!v.isEmpty()) c.setName(v);
            System.out.print("电话(" + c.getPhone() + "): ");
            v = readLine().trim();
            if (!v.isEmpty()) c.setPhone(v);
            System.out.print("护理等级(" + c.getNursingLevel() + "): ");
            v = readLine().trim();
            if (!v.isEmpty()) c.setNursingLevel(v);
            clientRepository.save(c);
            System.out.println("修改成功");
        } catch (Exception e) {
            System.out.println("失败: " + e.getMessage());
        }
    }

    private void deleteClient() {
        System.out.print("老人ID: ");
        Integer id = Integer.parseInt(readLine().trim());
        Optional<Client> opt = clientRepository.findById(id);
        if (opt.isPresent()) {
            Client c = opt.get();
            c.setDeleted(true);
            clientRepository.save(c);
            System.out.println("删除成功");
        }
    }

    private void assignNursingLevel() {
        System.out.print("老人ID: "); Integer cid = Integer.parseInt(readLine().trim());
        System.out.print("护理等级名称: "); String level = readLine().trim();
        Optional<Client> opt = clientRepository.findById(cid);
        if (opt.isPresent()) {
            Client c = opt.get();
            c.setNursingLevel(level);
            clientRepository.save(c);
            System.out.println("等级分配成功");
        }
    }

    // ==================== 5. 护理服务设置 ====================

    private void nursingSettingMenu() {
        System.out.println("\n--- 护理服务设置 ---");
        System.out.println("1. 查看服务  2. 购买服务  3. 消费扣次  4. 续费  5. 检查到期  0. 返回");
        System.out.print("请选择: ");
        String input = readLine().trim();
        switch (input) {
            case "1": listNursingSettings(); break;
            case "2": purchaseService(); break;
            case "3": consumeService(); break;
            case "4": renewService(); break;
            case "5": checkExpiry(); break;
            case "0": menu(); return;
            default: System.out.println("无效选项"); break;
        }
        nursingSettingMenu();
    }

    private void listNursingSettings() {
        System.out.print("老人ID: ");
        Long cid = Long.parseLong(readLine().trim());
        List<ClientNursingSetting> list = clientNursingSettingRepository
                .findByClientIdAndIsDeletedFalse(cid);
        System.out.println("\n===== 护理服务列表 =====");
        System.out.printf("%-5s %-10s %-12s %-12s %-15s %-10s%n", "ID", "项目ID", "总次数", "剩余", "到期日期", "状态");
        for (ClientNursingSetting s : list) {
            System.out.printf("%-5d %-10d %-12d %-12d %-15s %-10s%n",
                    s.getId(), s.getNursingItemId(), s.getTotalQuantity(),
                    s.getRemainingQuantity(), s.getServiceDueDate(), s.getServiceStatus());
        }
    }

    private void purchaseService() {
        try {
            System.out.print("老人ID: "); Long cid = Long.parseLong(readLine());
            System.out.print("护理项目ID: "); Long nid = Long.parseLong(readLine());
            System.out.print("总次数: "); int total = Integer.parseInt(readLine());
            System.out.print("到期日期(yyyy-MM-dd): "); LocalDate due = LocalDate.parse(readLine());

            ClientNursingSetting s = new ClientNursingSetting();
            s.setClientId(cid);
            s.setNursingItemId(nid);
            s.setTotalQuantity(total);
            s.setRemainingQuantity(total);
            s.setServiceDueDate(due);
            s.setPurchaseDate(LocalDate.now());
            s.setServiceStatus("ACTIVE");
            clientNursingSettingRepository.save(s);
            System.out.println("购买成功");
        } catch (Exception e) {
            System.out.println("失败: " + e.getMessage());
        }
    }

    private void consumeService() {
        System.out.print("服务记录ID: "); Long id = Long.parseLong(readLine());
        System.out.print("消费数量: "); int qty = Integer.parseInt(readLine());
        Optional<ClientNursingSetting> opt = clientNursingSettingRepository.findById(id);
        if (opt.isPresent()) {
            ClientNursingSetting s = opt.get();
            if (s.getRemainingQuantity() < qty) {
                System.out.println("剩余次数不足（剩余" + s.getRemainingQuantity() + "）");
                return;
            }
            s.setRemainingQuantity(s.getRemainingQuantity() - qty);
            clientNursingSettingRepository.save(s);
            System.out.println("扣次成功，剩余 " + s.getRemainingQuantity() + " 次");
        }
    }

    private void renewService() {
        System.out.print("服务记录ID: "); Long id = Long.parseLong(readLine());
        System.out.print("追加次数: "); int add = Integer.parseInt(readLine());
        System.out.print("新到期日期(yyyy-MM-dd): "); LocalDate due = LocalDate.parse(readLine());
        Optional<ClientNursingSetting> opt = clientNursingSettingRepository.findById(id);
        if (opt.isPresent()) {
            ClientNursingSetting s = opt.get();
            s.setTotalQuantity(s.getTotalQuantity() + add);
            s.setRemainingQuantity(s.getRemainingQuantity() + add);
            s.setServiceDueDate(due);
            s.setServiceStatus("ACTIVE");
            clientNursingSettingRepository.save(s);
            System.out.println("续费成功");
        }
    }

    private void checkExpiry() {
        List<ClientNursingSetting> all = clientNursingSettingRepository.findByIsDeletedFalse();
        LocalDate now = LocalDate.now();
        int updated = 0;
        for (ClientNursingSetting s : all) {
            if (s.getServiceDueDate() != null && s.getServiceDueDate().isBefore(now)
                    && "ACTIVE".equals(s.getServiceStatus())) {
                s.setServiceStatus("EXPIRED");
                clientNursingSettingRepository.save(s);
                updated++;
            }
        }
        System.out.println("到期检查完成，已更新 " + updated + " 条记录");
    }

    // ==================== 6. 护理记录 ====================

    private void nursingRecordMenu() {
        System.out.println("\n--- 护理记录 ---");
        System.out.println("1. 查看记录  2. 新增记录  0. 返回");
        System.out.print("请选择: ");
        String input = readLine().trim();
        switch (input) {
            case "1": listNursingRecords(); break;
            case "2": addNursingRecord(); break;
            case "0": menu(); return;
            default: System.out.println("无效选项"); break;
        }
        nursingRecordMenu();
    }

    private void listNursingRecords() {
        List<NursingRecord> list = nursingRecordRepository.findAll().stream()
                .filter(r -> !r.getIsDeleted()).collect(Collectors.toList());
        System.out.println("\n===== 护理记录列表 =====");
        System.out.printf("%-5s %-10s %-10s %-10s %-20s %-10s%n", "ID", "老人ID", "项目ID", "护理员", "护理时间", "次数");
        for (NursingRecord r : list) {
            String time = r.getNursingTime() != null ? r.getNursingTime().format(DTF) : "-";
            System.out.printf("%-5d %-10d %-10d %-10d %-20s %-10d%n",
                    r.getId(), r.getClientId(), r.getNursingItemId(),
                    r.getHealthAssistantId(), time, r.getExecQuantity());
        }
    }

    private void addNursingRecord() {
        try {
            System.out.print("老人ID: "); Long cid = Long.parseLong(readLine());
            System.out.print("护理项目ID: "); Long nid = Long.parseLong(readLine());
            System.out.print("护理员ID: "); Long hid = Long.parseLong(readLine());
            System.out.print("护理时间(yyyy-MM-dd HH:mm): ");
            LocalDateTime time = LocalDateTime.parse(readLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            System.out.print("执行次数: "); int qty = Integer.parseInt(readLine());
            System.out.print("备注: "); String remark = readLine();

            NursingRecord r = new NursingRecord();
            r.setClientId(cid);
            r.setNursingItemId(nid);
            r.setHealthAssistantId(hid);
            r.setNursingTime(time);
            r.setExecQuantity(qty);
            r.setRemarks(remark);
            nursingRecordRepository.save(r);
            System.out.println("新增成功");
        } catch (Exception e) {
            System.out.println("失败: " + e.getMessage());
        }
    }

    // ==================== 7. 退住申请 ====================

    private void checkOutMenu() {
        System.out.println("\n--- 退住申请 ---");
        System.out.println("1. 查看列表  2. 新增  3. 审核  0. 返回");
        System.out.print("请选择: ");
        String input = readLine().trim();
        switch (input) {
            case "1": listCheckOuts(); break;
            case "2": addCheckOut(); break;
            case "3": auditCheckOut(); break;
            case "0": menu(); return;
            default: System.out.println("无效选项"); break;
        }
        checkOutMenu();
    }

    private void listCheckOuts() {
        List<CheckOutApplication> list = checkOutApplicationRepository.findByDeletedFalse();
        System.out.println("\n===== 退住申请列表 =====");
        System.out.printf("%-15s %-10s %-10s %-20s %-10s %-10s%n", "编号", "老人", "类型", "申请时间", "状态", "审核人");
        for (CheckOutApplication a : list) {
            String time = a.getTime() != null ? a.getTime().format(DTF) : "-";
            System.out.printf("%-15s %-10s %-10s %-20s %-10s %-10s%n",
                    a.getId(), a.getClientName(), a.getType(), time, a.getStatus(), a.getAuditor());
        }
    }

    private void addCheckOut() {
        try {
            CheckOutApplication app = new CheckOutApplication();
            System.out.print("老人ID: "); app.setClientId(Integer.parseInt(readLine()));
            System.out.print("老人姓名: "); app.setClientName(readLine());
            System.out.print("退住类型(主动退住/违规劝退/转院): "); app.setType(readLine());
            System.out.print("原因: "); app.setReason(readLine());
            app.setTime(LocalDateTime.now());
            app.setId("CO-" + System.currentTimeMillis());
            app.setStatus("已提交");
            checkOutApplicationRepository.save(app);
            System.out.println("申请提交成功，编号: " + app.getId());
        } catch (Exception e) {
            System.out.println("失败: " + e.getMessage());
        }
    }

    private void auditCheckOut() {
        System.out.print("申请编号: "); String id = readLine().trim();
        Optional<CheckOutApplication> opt = checkOutApplicationRepository.findById(id);
        if (!opt.isPresent()) return;
        System.out.print("审核人: "); String auditor = readLine().trim();
        System.out.print("是否同意(y/n): ");
        boolean approve = "y".equalsIgnoreCase(readLine().trim());
        CheckOutApplication app = opt.get();
        app.setStatus(approve ? "已通过" : "已拒绝");
        app.setAuditor(auditor);
        app.setAuditTime(LocalDateTime.now());
        checkOutApplicationRepository.save(app);
        System.out.println("审核完成");
    }

    // ==================== 8. 请假管理 ====================

    private void leaveMenu() {
        System.out.println("\n--- 请假管理 ---");
        System.out.println("1. 查看列表  2. 新增  3. 审核  4. 归院登记  0. 返回");
        System.out.print("请选择: ");
        String input = readLine().trim();
        switch (input) {
            case "1": listLeaves(); break;
            case "2": addLeave(); break;
            case "3": auditLeave(); break;
            case "4": returnLeave(); break;
            case "0": menu(); return;
            default: System.out.println("无效选项"); break;
        }
        leaveMenu();
    }

    private void listLeaves() {
        List<LeaveApplication> list = leaveApplicationRepository.findByDeletedFalse();
        System.out.println("\n===== 请假列表 =====");
        System.out.printf("%-15s %-10s %-20s %-10s %-10s%n", "编号", "老人", "申请时间", "状态", "审核人");
        for (LeaveApplication a : list) {
            String time = a.getLeaveTime() != null ? a.getLeaveTime().format(DTF) : "-";
            System.out.printf("%-15s %-10s %-20s %-10s %-10s%n",
                    a.getId(), a.getClientName(), time, a.getStatus(), a.getAuditor());
        }
    }

    private void addLeave() {
        try {
            LeaveApplication app = new LeaveApplication();
            System.out.print("老人ID: "); app.setClientId(Integer.parseInt(readLine()));
            System.out.print("老人姓名: "); app.setClientName(readLine());
            System.out.print("请假原因: "); app.setReason(readLine());
            System.out.print("请假时间(yyyy-MM-dd HH:mm): ");
            app.setLeaveTime(LocalDateTime.parse(readLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            System.out.print("预计结束(yyyy-MM-dd HH:mm): ");
            app.setPredictedEnd(LocalDateTime.parse(readLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            app.setId("LV-" + System.currentTimeMillis());
            app.setStatus("已提交");
            leaveApplicationRepository.save(app);
            System.out.println("请假提交成功，编号: " + app.getId());
        } catch (Exception e) {
            System.out.println("失败: " + e.getMessage());
        }
    }

    private void auditLeave() {
        System.out.print("申请编号: "); String id = readLine().trim();
        Optional<LeaveApplication> opt = leaveApplicationRepository.findById(id);
        if (!opt.isPresent()) return;
        System.out.print("审核人: "); String auditor = readLine().trim();
        System.out.print("是否同意(y/n): ");
        boolean approve = "y".equalsIgnoreCase(readLine().trim());
        LeaveApplication app = opt.get();
        app.setStatus(approve ? "已通过" : "已拒绝");
        app.setAuditor(auditor);
        app.setAuditTime(LocalDateTime.now());
        leaveApplicationRepository.save(app);
        System.out.println("审核完成");
    }

    private void returnLeave() {
        System.out.print("申请编号: "); String id = readLine().trim();
        Optional<LeaveApplication> opt = leaveApplicationRepository.findById(id);
        if (opt.isPresent()) {
            LeaveApplication app = opt.get();
            app.setActualReturnTime(LocalDateTime.now());
            leaveApplicationRepository.save(app);
            System.out.println("归院登记成功");
        }
    }

    // ==================== 9. 操作员管理 ====================

    private void operatorMenu() {
        System.out.println("\n--- 操作员管理 ---");
        System.out.println("1. 查看列表  2. 新增  3. 修改  4. 删除  0. 返回");
        System.out.print("请选择: ");
        String input = readLine().trim();
        switch (input) {
            case "1": listOperators(); break;
            case "2": addOperator(); break;
            case "3": updateOperator(); break;
            case "4": deleteOperator(); break;
            case "0": menu(); return;
            default: System.out.println("无效选项"); break;
        }
        operatorMenu();
    }

    private void listOperators() {
        List<Operator> list = operatorRepository.findAll().stream()
                .filter(o -> !o.getDeleted()).collect(Collectors.toList());
        System.out.println("\n===== 操作员列表 =====");
        System.out.printf("%-20s %-15s %-10s%n", "登录账号", "姓名", "类型");
        for (Operator o : list) {
            System.out.printf("%-20s %-15s %-10s%n", o.getLoginCode(), o.getRealName(), o.getOperatorType());
        }
    }

    private void addOperator() {
        try {
            Operator op = new Operator();
            System.out.print("登录账号: "); op.setLoginCode(readLine());
            System.out.print("密码: "); op.setPassword(readLine());
            System.out.print("真实姓名: "); op.setRealName(readLine());
            System.out.print("类型(ADMIN/STAFF): "); op.setOperatorType(readLine());
            operatorRepository.save(op);
            System.out.println("新增成功");
        } catch (Exception e) {
            System.out.println("失败: " + e.getMessage());
        }
    }

    private void updateOperator() {
        System.out.print("登录账号: "); String code = readLine().trim();
        Optional<Operator> opt = operatorRepository.findById(code);
        if (!opt.isPresent() || opt.get().getDeleted()) return;
        Operator op = opt.get();
        System.out.print("姓名(" + op.getRealName() + "): ");
        String v = readLine().trim();
        if (!v.isEmpty()) op.setRealName(v);
        System.out.print("密码(不修改直接回车): ");
        v = readLine().trim();
        if (!v.isEmpty()) op.setPassword(v);
        System.out.print("类型(" + op.getOperatorType() + "): ");
        v = readLine().trim();
        if (!v.isEmpty()) op.setOperatorType(v);
        operatorRepository.save(op);
        System.out.println("修改成功");
    }

    private void deleteOperator() {
        System.out.print("登录账号: ");
        String code = readLine().trim();
        Optional<Operator> opt = operatorRepository.findById(code);
        if (opt.isPresent()) {
            Operator op = opt.get();
            op.setDeleted(true);
            operatorRepository.save(op);
            System.out.println("删除成功");
        }
    }
}