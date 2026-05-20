package java.entity;

import java.io.Serializable;
import java.util.Date;

public class admin extends Operator implements Serializable{
    public admin(String loginCode, String password, String realName) {
        super(loginCode, password, realName);
    }
    @Override
    public String toString() {
        return "Admin [getLoginCode()=" + getLoginCode() + ", getPassword()=" + getPassword() + ", getRealName()="
                + getRealName() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
                + super.toString() + "]";
    }
}
