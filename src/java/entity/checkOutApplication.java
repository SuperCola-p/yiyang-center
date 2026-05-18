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

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString(){
        return this.type+"_"+this.reason+"_"+this.time+"_"+this.statue;
    }
}
