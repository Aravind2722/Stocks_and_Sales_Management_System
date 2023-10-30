import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/indian_brothers_incorporated";
    private static final String db_user_name = "root";
    private static final String db_password = "Aravind@MySQL.com";
    private static final Scanner input = new Scanner(System.in);
    private static person_mail_id;
    public static void main(String[] args) throws SQLException, MessagingException, IOException {
        System.out.println("if you are a customer, press 1:\nif you are an employee, press2:\nif you are the chairman, press 3:");
        int whoLoggedIn = input.nextInt();
        SignInOrSignUp loginPage = new SignInOrSignUp(url, db_user_name, db_password, input);
        boolean loginSuccess = loginPage.login(whoLoggedIn == 1 ? "customers" : whoLoggedIn == 2 ? "employees" : "chairman");
        if (whoLoggedIn == 1) {
            customerOptions();
        }
    }
    public static void customerOptions() {
        ArrayList <String> options = new ArrayList <>(Arrays.asList("place order", "view orders", "view account details", "edit account details"));
    }
}