package java.entity;

public class checkOutApplication {
    private String type;
    private String reason;
    private String time;
    private String statue;
    public checkOutApplication(String time,String type,String reason,String statue) {
        this.time = time;
        this.type = type;
        this.reason = reason;
        this.statue = statue;
    }
}
