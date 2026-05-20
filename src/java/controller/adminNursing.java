package java.controller;

import java.entity.ClientNursingSetting;
import java.entity.NursingItem;
import java.entity.NursingLevel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class adminNursing {
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        int option = 0;
        do {
            displayMainMenu();
            System.out.print("请选择操作: ");
            option = input.nextInt();
            input.nextLine(); // 清除换行符

            switch (option) {
                case 1:
                    manageNursingLevel(); // 护理级别管理
                    break;
                case 2:
                    manageNursingItem(); // 护理项目管理
                    break;
                case 3:
                    manageClientNursingSetting(); // 客户护理设置
                    break;
                case 4:
                    manageNursingRecord(); // 护理记录管理
                    break;
                case 0:
                    System.out.println("退出护理管理模块。");
                    break;
                default:
                    System.out.println("无效选项，请重新选择。");
            }
        } while (option != 0);
    }

    /**
     * 显示主菜单
     * 企划书5.1节“主菜单：护理管理”包含以下四个子功能。
     */
    private static void displayMainMenu() {
        System.out.println("\n===== 护理管理模块 =====");
        System.out.println("1. 护理级别管理");
        System.out.println("2. 护理项目管理");
        System.out.println("3. 客户护理设置");
        System.out.println("4. 护理记录管理");
        System.out.println("0. 退出");
        System.out.println("=======================");
    }

    /**
     * 功能1：护理级别管理
     * 对应企划书功能：查询、修改、添加护理级别，配置护理项目。
     */
    private static void manageNursingLevel() {
        int subOption = 0;
        do {
            System.out.println("\n--- 护理级别管理 ---");
            System.out.println("1. 查询护理级别列表");
            System.out.println("2. 修改护理级别状态");
            System.out.println("3. 添加护理级别");
            System.out.println("4. 配置护理项目");
            System.out.println("0. 返回上级菜单");
            System.out.print("请选择: ");
            subOption = input.nextInt();
            input.nextLine();

            switch (subOption) {
                case 1:
                    queryNursingLevelList(); // 查询护理级别信息列表
                    break;
                case 2:
                    updateNursingLevelStatus(); // 修改护理级别状态
                    break;
                case 3:
                    addNursingLevel(); // 添加护理级别
                    break;
                case 4:
                    configureNursingItemsForLevel(); // 为某个护理级别配置护理项目
                    break;
                case 0:
                    break;
                default:
                    System.out.println("无效选项。");
            }
        } while (subOption != 0);
    }

    private static void queryNursingLevelList() {
        // 企划书：查询护理级别信息列表(默认查询启用状态)
        // 实现：从数据库或模拟数据中查询NursingLevel列表，可根据状态过滤。
        System.out.println("执行：查询所有护理级别。");
    }

    private static void updateNursingLevelStatus() {
        // 企划书：修改护理级别:只能修改其状态
        System.out.print("请输入要修改的护理级别ID: ");
        Long levelId = input.nextLong();
        input.nextLine();
        System.out.print("请输入新的状态(启用/停用): ");
        String newStatus = input.nextLine();
        System.out.println("执行：将ID为" + levelId + "的护理级别状态修改为：" + newStatus);
    }

    private static void addNursingLevel() {
        // 企划书：添加护理级别:添加信息包含护理级别名称和状态
        NursingLevel newLevel = new NursingLevel();
        System.out.print("请输入护理级别名称: ");
        newLevel.setLevelName(input.nextLine());
        System.out.print("请输入状态(启用/停用): ");
        newLevel.setStatus(input.nextLine());
        // 设置id等操作...
        System.out.println("执行：添加护理级别 - " + newLevel.getLevelName());
    }

    private static void configureNursingItemsForLevel() {
        // 企划书：护理项目配置:管理员可以选中某个护理级别对其配置对应的护理项目
        System.out.print("请输入要配置的护理级别ID: ");
        Long levelId = input.nextLong();
        input.nextLine();
        System.out.println("1. 查询并管理当前级别已配置的项目");
        System.out.println("2. 添加新项目到当前级别");
        int choice = input.nextInt();
        input.nextLine();
        if (choice == 1) {
            // 查询并可从当前级别的NursingItemArray中移除项目
            System.out.println("执行：管理级别" + levelId + "的已有护理项目。");
        } else if (choice == 2) {
            // 查询所有启用的护理项目，将选中的加入当前级别的NursingItemArray
            System.out.println("执行：为级别" + levelId + "添加新的护理项目。");
        }
    }

    /**
     * 功能2：护理项目管理
     * 对应企划书功能：查询、添加、修改、删除（逻辑删除）护理项目。
     */
    private static void manageNursingItem() {
        int subOption = 0;
        do {
            System.out.println("\n--- 护理项目管理 ---");
            System.out.println("1. 查询护理项目列表");
            System.out.println("2. 添加护理项目");
            System.out.println("3. 修改护理项目");
            System.out.println("4. 删除护理项目");
            System.out.println("0. 返回上级菜单");
            System.out.print("请选择: ");
            subOption = input.nextInt();
            input.nextLine();

            switch (subOption) {
                case 1:
                    queryNursingItemList();
                    break;
                case 2:
                    addNursingItem();
                    break;
                case 3:
                    updateNursingItem();
                    break;
                case 4:
                    deleteNursingItem();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("无效选项。");
            }
        } while (subOption != 0);
    }

    private static void queryNursingItemList() {
        // 企划书：根据护理项目状态、名称(模糊)多条件组合查询
        System.out.println("执行：按条件查询护理项目列表。");
    }

    private static void addNursingItem() {
        // 企划书：添加信息包括编号、名称、价格、状态、执行周期、执行次数、描述
        NursingItem item = new NursingItem();
        System.out.print("请输入项目编号: ");
        item.setCode(input.nextLine());
        System.out.print("请输入项目名称: ");
        item.setName(input.nextLine());
        System.out.print("请输入价格: ");
        item.setPrice(new BigDecimal(input.nextDouble()));
        input.nextLine();
        System.out.print("请输入状态(启用/停用): ");
        item.setStatus(input.nextLine());
        System.out.print("请输入执行周期(如：每日、每周): ");
        item.setExecPeriod(input.nextLine());
        System.out.print("请输入执行次数: ");
        item.setExecTimes(input.nextInt());
        input.nextLine();
        System.out.print("请输入描述: ");
        item.setDescription(input.nextLine());
        System.out.println("执行：添加护理项目 - " + item.getName());
    }

    private static void updateNursingItem() {
        // 企划书：修改护理项目信息，若状态改为停用需从所有护理级别中剔除
        System.out.print("请输入要修改的护理项目ID: ");
        Long itemId = input.nextLong();
        input.nextLine();
        System.out.println("执行：修改ID为" + itemId + "的护理项目。若状态改为停用，将自动从所有护理级别中移除。");
    }

    private static void deleteNursingItem() {
        // 企划书：逻辑删除前需检查是否在护理级别项目中，是则需要剔除
        System.out.print("请输入要删除(逻辑)的护理项目ID: ");
        Long itemId = input.nextLong();
        input.nextLine();
        System.out.println("执行：逻辑删除ID为" + itemId + "的护理项目，并检查关联级别。");
    }

    /**
     * 功能3：客户护理设置
     * 对应企划书功能：为客户设置/移除护理级别，批量添加护理项目。
     */
    private static void manageClientNursingSetting() {
        int subOption = 0;
        do {
            System.out.println("\n--- 客户护理设置 ---");
            System.out.println("1. 查询客户列表");
            System.out.println("2. 设置/修改护理级别");
            System.out.println("3. 移除护理级别");
            System.out.println("4. 为客户购买/续费护理项目");
            System.out.println("0. 返回上级菜单");
            System.out.print("请选择: ");
            subOption = input.nextInt();
            input.nextLine();

            switch (subOption) {
                case 1:
                    queryClientList();
                    break;
                case 2:
                    setNursingLevelForClient(); // 设置或修改客户护理级别
                    break;
                case 3:
                    removeNursingLevelFromClient(); // 移除客户护理级别
                    break;
                case 4:
                    purchaseNursingItemForClient(); // 为客户购买或续费单个护理项目
                    break;
                case 0:
                    break;
                default:
                    System.out.println("无效选项。");
            }
        } while (subOption != 0);
    }

    private static void queryClientList() {
        // 企划书：根据客户名字(模糊)查询客户信息列表
        System.out.print("请输入客户姓名(模糊): ");
        String name = input.nextLine();
        System.out.println("执行：查询客户列表，姓名包含: " + name);
    }

    private static void setNursingLevelForClient() {
        // 企划书：1.下拉选择护理级别 2.修改客户的护理级别 3.批量给用户添加护理项目
        System.out.print("请输入客户ID: ");
        Long clientId = input.nextLong();
        input.nextLine();
        System.out.print("请输入要设置的护理级别ID: ");
        Long levelId = input.nextLong();
        input.nextLine();

        ClientNursingSetting setting = new ClientNursingSetting();
        setting.setClientId(clientId);
        setting.setNursingLevelId(levelId);
        // 批量添加护理项目逻辑...
        System.out.println("执行：为客户" + clientId + "设置护理级别" + levelId + "，并批量添加关联的护理项目。");
    }

    private static void removeNursingLevelFromClient() {
        // 企划书：移除客户护理级别级联移除用户当前级别的护理项目
        System.out.print("请输入客户ID: ");
        Long clientId = input.nextLong();
        input.nextLine();
        System.out.println("警告：此操作将移除该客户的所有护理项目！确认移除？(Y/N)");
        if (input.nextLine().equalsIgnoreCase("Y")) {
            System.out.println("执行：移除客户" + clientId + "的护理级别及所有相关项目。");
        }
    }

    private static void purchaseNursingItemForClient() {
        // 企划书 5.1 “服务关注”功能的一部分：购买护理项目、续费
        System.out.print("请输入客户ID: ");
        Long clientId = input.nextLong();
        input.nextLine();
        System.out.print("请输入护理项目ID: ");
        Long itemId = input.nextLong();
        input.nextLine();
        System.out.print("请输入购买/续费数量: ");
        Integer quantity = input.nextInt();
        input.nextLine();

        ClientNursingSetting setting = new ClientNursingSetting();
        setting.setClientId(clientId);
        setting.setNursingItemId(itemId);
        setting.setPurchaseDate(LocalDate.now());
        setting.setTotalQuantity(quantity);
        setting.setRemainingQuantity(quantity);
        setting.setServiceDueDate(LocalDate.now().plusMonths(3)); // 默认3个月到期
        setting.setServiceStatus("正常");
        System.out.println("执行：为客户" + clientId + "的护理项目" + itemId + "增加数量" + quantity);
    }

    /**
     * 功能4：护理记录管理
     * 对应企划书功能：查询客户护理记录，并可移除（逻辑删除）。
     */
    private static void manageNursingRecord() {
        int subOption = 0;
        do {
            System.out.println("\n--- 护理记录管理 ---");
            System.out.println("1. 查询客户列表");
            System.out.println("2. 查看客户护理记录");
            System.out.println("3. 移除护理记录");
            System.out.println("0. 返回上级菜单");
            System.out.print("请选择: ");
            subOption = input.nextInt();
            input.nextLine();

            switch (subOption) {
                case 1:
                    queryClientList(); // 复用查询客户列表功能
                    break;
                case 2:
                    queryNursingRecordByClient();
                    break;
                case 3:
                    deleteNursingRecord();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("无效选项。");
            }
        } while (subOption != 0);
    }

    private static void queryNursingRecordByClient() {
        System.out.print("请输入客户ID: ");
        Long clientId = input.nextLong();
        input.nextLine();
        System.out.println("执行：查询客户" + clientId + "的所有护理记录。");
    }

    private static void deleteNursingRecord() {
        // 企划书：管理员可对护理记录进行移除(隐藏) -> 逻辑删除
        System.out.print("请输入要移除的护理记录ID: ");
        Long recordId = input.nextLong();
        input.nextLine();
        System.out.println("执行：逻辑删除护理记录ID: " + recordId);
    }
}