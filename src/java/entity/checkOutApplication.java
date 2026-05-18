package java.entity;

import java.io.Serializable;

public class checkOutApplication implements Serializable {
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
    public String toString(){
        return this.type+"_"+this.reason+"_"+this.time+"_"+this.statue;
    }
}
