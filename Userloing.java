package USERLOGin;
import java.util.Scanner;




public class Userloing{

    public static void main (String[] args){
        Scanner sc =new Scanner(System.in);


        User u =new StudentUser("flame","student","1234");

        try{
            System.out.print("username:");
            String id=sc.nextLine();

            System.out.print("password:");
            String pw=sc.nextLine();
            if(u.getUsername().equals(id) && u.checkPassword(pw)){
                System.out.println("登入成功");
                u.showRole();
            } else {
                System.out.println("登入失敗");
            }
        }catch(Exception e){
            System.out.println("發生錯誤: " + e.getMessage());
        } finally {
            sc.close();
            System.out.println("程式結束");
        }
    }
}

class Person{
    protected String name;
    public Person(String name){
        this.name=name;
    }
}

class User extends Person{
    private String username;
    private String password;

    public User(String name, String username,String password){
        super(name);
        this.username=username;

        this.password=password;
    }

public String getUsername(){
    return username;
}
public boolean checkPassword(String pw){
    return password.equals(pw);
}

public void showRole(){
    System.out.println("使用者"+name);

}
}


class StudentUser extends User{
    public StudentUser(String name,String username, String password){
        super(name,name,password);
    }

    @Override
    public void showRole() {
        System.out.println("student"+name);
    }
}