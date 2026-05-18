package entity;

import java.io.Serializable;

public class admin extends Operator implements Serializable{

    @Override
    public String toString() {
        return "Admin [getLoginCode()=" + getLoginCode() + ", getPassword()=" + getPassword() + ", getRealName()="
                + getRealName() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
                + super.toString() + "]";
    }

}
