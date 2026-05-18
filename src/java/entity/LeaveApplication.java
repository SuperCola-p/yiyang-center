package java.entity;

import java.io.Serializable;

public class LeaveApplication implements Serializable {
    private String reason;
    private String time;
    private String predictedEnd;
    private String statue;
    private String end;
    public LeaveApplication(String reason, String time, String predictedEnd){
        this.time=time;
        this.reason=reason;
        this.predictedEnd=predictedEnd;
        this.statue="false";
    }
    void setEnd(String end){
        this.end=end;
    }
    String show(){
        return this.reason+"_"+this.time+"_"+this.predictedEnd+"_"+this.statue;
    }
}
