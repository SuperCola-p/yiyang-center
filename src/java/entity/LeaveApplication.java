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
    public void setEnd(String end){
        this.end=end;
    }
    String show(){
        return this.reason+"_"+this.time+"_"+this.predictedEnd+"_"+this.statue;
    }
    public String getEnd(){
        return end;
    }
    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String getPredictedEnd() {
        return predictedEnd;
    }

    public void setPredictedEnd(String predictedEnd) {
        this.predictedEnd = predictedEnd;
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
}
