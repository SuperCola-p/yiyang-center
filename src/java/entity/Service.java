package java.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Service extends Operator implements Serializable {
    private ArrayList<Client> clients=new ArrayList<>();
    void addCustomer(Client client){
        this.clients.add(client);
    }
    void removeCusomer(Client client){
        this.clients.remove(client);
    }
    Client searchCustomer(Client client){
        Iterator<Client> it=clients.iterator();
        while (it.hasNext()){
            if(client.equals(it.next())){
                return it.next();
            }
        }
        return null;
    }
    void AddCheckOutApplication(Client client,checkOutApplication checkOutApplication){
        client.addCheckOutApplication(checkOutApplication);
    }
}
