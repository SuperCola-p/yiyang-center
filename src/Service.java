import java.util.ArrayList;

public class Service {
    public class healthManager{
        private String name;
        private String password;
        ArrayList<Customer> customer=new ArrayList<>();
        void addCustomer(Customer customer){
            this.customer.add(customer);
        }
        void removeCusomer(Customer customer){
            this.customer.remove(customer);
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

    }
}
