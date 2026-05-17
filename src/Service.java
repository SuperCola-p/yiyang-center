import java.util.ArrayList;
import java.util.Iterator;

public class Service {
    public class healthManager{
        private String name;
        private String password;
        ArrayList<Customer> customers=new ArrayList<>();
        void addCustomer(Customer customer){
            this.customers.add(customer);
        }
        void removeCusomer(Customer customer){
            this.customers.remove(customer);
        }
        void setName(String name){
            this.name=name;
        }
        String getName(){
            return this.name;
        }
        void setPassword(String password){
            this.password=password;
        }
        String getPassword(){
            return this.password;
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
