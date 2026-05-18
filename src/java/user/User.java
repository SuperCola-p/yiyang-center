package java.user;

public class User {
    private int id;
    private String password;
    private String username;
    private String role;//管理员或者健康管家
    public User(){}
    public User(int id,String password, String username,String role){
        this.id=id;
        this.password=password;
        this.username=username;
        this.role=role;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{" +"id=" + id +", username='" + username + '\'' +", role='" + role + '\'' +'}';
    }
}
