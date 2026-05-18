package java.entity;

import java.util.ArrayList;
import java.util.Iterator;

public class Service extends Operator{
        private ArrayList<Customer> customers=new ArrayList<>();
        void addCustomer(Customer customer){
            this.customers.add(customer);
        }
        void removeCusomer(Customer customer){
            this.customers.remove(customer);
        }

        Customer searchCustomer(Customer customer){
            Iterator<Customer> it=customers.iterator();
            while (it.hasNext()){
                if(customer.equals(it.next())){
                    return it.next();
                }
            }
            return null;
        }
    }
}
