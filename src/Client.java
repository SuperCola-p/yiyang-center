import java.time.LocalDate;
import java.time.LocalDateTime;

public class Client {
    private Long id;
    private String name; // 客户姓名
    private Integer gender; // 性别 0:女，1:男
    private String idCard; // 身份证号
    private LocalDate birthday; // 出生日期（年龄自动计算）
    private String bloodType; // 血型
    private String contactPerson; // 家属
    private String contactPhone; // 联系电话
    private Long buildingId; // 楼栋ID (文档附录：固定606)
    private Long roomId; // 房间号
    private Long bedId; // 床位号
    private LocalDate checkInDate; // 入住时间
    private LocalDate contractDueDate; // 合同到期时间
    private Integer elderType; // 老人类型 0:自理老人， 1:护理老人
    private Long nursingLevelId; // 护理级别ID
    private Long healthStewardId; // 健康管家ID1
    private Integer isDeleted; // 逻辑删除标志
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}