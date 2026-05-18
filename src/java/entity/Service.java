package java.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Service extends Operator implements Serializable {
    private ArrayList<Client> customers=new ArrayList<>();
    void addCustomer(Client customer){
        this.customers.add(customer);
    }
    void removeCusomer(Client customer){
        this.customers.remove(customer);
    }
    Client searchCustomer(Client customer){
        Iterator<Client> it=customers.iterator();
        while (it.hasNext()){
            if(customer.equals(it.next())){
                return it.next();
            }
        }
        return null;
    }
}
