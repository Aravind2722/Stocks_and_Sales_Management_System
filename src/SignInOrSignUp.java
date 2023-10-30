import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;
import java.sql.*;
public class SignInOrSignUp {
    private static String personName;
    private static final Scanner input = new Scanner(System.in);
    private static final String url = "jdbc:mysql://localhost:3306/indian_brothers_incorporated";
    private static final String db_user_name = "root";
    private static final String db_password = "Aravind@MySQL.com";

    public static boolean login(String person) throws SQLException, MessagingException, IOException {
        if (person.equals("customers")) {
            System.out.println("Already an user?, press 1 to sign in:\nNew to VirtualSandha?, press 2 to sign up:");
            byte isNewCustomer = input.nextByte();
            if (isNewCustomer == 2) {
                boolean signUpSuccess = false;
                while (!signUpSuccess) {
                    signUpSuccess = signUp(person);
                    if (!signUpSuccess) {
                        System.out.println("To retry sign up, press 1:\nTo exit press 0: ");
                        int wishToContinue = input.nextInt();
                        if (wishToContinue == 0) {
                            System.out.println("Sign up failed!");
                            return false;
                        }
                    }
                }
                System.out.println("To login, press 1:\nTo exit, press 0: ");
                int wishToLogin = input.nextInt();
                if (wishToLogin == 0) return false;
            }
        }
        boolean signInSuccess = false;
        while (!signInSuccess) {
            signInSuccess = signIn(person);
            if (!signInSuccess) {
                System.out.println("To retry login, press: 1\nTo exit, press 0: ");
                byte wishToContinue = input.nextByte();
                if (wishToContinue == 0) {
                    System.out.println("Login failed!");
                    return false;
                }
            }
        }
        return signInSuccess;
    }
    public static boolean signUp(String person) throws SQLException, MessagingException, IOException {
        Connection con = DriverManager.getConnection(url, db_user_name, db_password);
//        Statement st = con.createStatement();
        boolean validationSuccess = false;
        String mail_id = "emailidoftheuser";
        while (!validationSuccess) {
            System.out.println("Enter your email_id: ");
            mail_id = input.next();
            validationSuccess = validate(person, mail_id, false, false, true);
            if (!validationSuccess) {
                System.out.println("To continue sign up, press 1:\nTo exit press 0: ");
                byte wishToContinue = input.nextByte();
                if (wishToContinue == 0) return false;
            }
        }
        String[] column = new String[]{"first_name", "last_name", "email", "password", "phone", "address"};
        String[] entry = new String[column.length];
        System.out.println("Please enter the following details ->\n");
        for (int i = 0; i < column.length; i++) {
            if (i == 2) entry[i] = mail_id;
            else if (i == 3) {
                System.out.println(column[i] + ": \nTo exit, press 0: ");
                String unhashedPassword = input.next();
                if (unhashedPassword.equals("0")) return false;
                entry[i] = CryptEnDecrypt.encryptPassword(unhashedPassword);
            } else {
                System.out.println(column[i] + ": \nTo exit, press 0: ");
                entry[i] = input.next();
                if (entry[i].equals("0")) return false;
            }
        }
        PreparedStatement pst = con.prepareStatement("INSERT INTO customers (first_name, last_name, email, password, phone, address) Values(?, ?, ?, ?, ?, ?)");
        for (int i = 0; i < column.length; i++) {
            pst.setString(i + 1, entry[i]);
        }
        int res = pst.executeUpdate();
        if (res == 0) {
            System.out.println("Oops! Something went wrong, please try again! ");
            return false;
        }
        System.out.println("Sign up successful!\nVirtualSandha Welcomes you Mr/Mrs. " + entry[0]);
        con.close();
        return true;
    }
    public static boolean signIn(String person) throws SQLException, MessagingException, IOException {
        Connection con = DriverManager.getConnection(url, db_user_name, db_password);
        Statement st = con.createStatement();
        System.out.println("Please enter your log in credentials!");
        int loginFailCount = 0;
        boolean validationSuccess = false;
        String mail_id = "emailidoftheuser";
        while (!validationSuccess) {
            System.out.println("Enter email_id: ");
            mail_id = input.next();
            System.out.println("validating");
            validationSuccess = validate(person, mail_id, true, true, false);
            System.out.println("validationSuccess: "+validationSuccess+" loginFailCount: "+loginFailCount);
            if (!validationSuccess) {
                loginFailCount++;
                if (loginFailCount > 0) {
                    System.out.println("Forgot Password ?, to reset password press 1: \nTo cancel, press 2: ");
                    int want_to_reset_password = input.nextInt();

                    if (want_to_reset_password == 1) {
                        boolean reset_password = forgotPassword(person);
                        if (reset_password) loginFailCount = 0;
                    }

                }
                System.out.println("To continue sign in, press 1:\nTo exit press 0: ");
                byte wishToContinue = input.nextByte();
                if (wishToContinue == 0) return false;
            }
        }
        System.out.println("login successful!");
        System.out.println("Greetings Mr/Mrs " + personName + "! VirtualSandha welcomes you!");
        con.close();
        return true;
    }
    public static boolean forgotPassword (String person) throws SQLException, MessagingException, IOException {
        boolean validationSuccess = false;
        String mail_id = "mail_id";
        while (!validationSuccess) {
            System.out.println("Enter your email_id: ");
            mail_id = input.next();
            validationSuccess = validate(person, mail_id, true, false, true);
            if (!validationSuccess) {
                System.out.println("To retry, press 1:\nTo exit press 0: ");
                byte wishToContinue = input.nextByte();
                if (wishToContinue == 0) return false;
            }
        }
        return changeNewPassword(mail_id, person);
    }
    public static boolean validate(String person, String mail_id, boolean checkEmailAvailability, boolean checkPasswordGenuinity, boolean verifyEmail) throws SQLException, MessagingException, IOException {
        boolean validEmail = true;
        if (checkEmailAvailability) {
            validEmail = checkIfEmailValid(person, mail_id);
//            if (!validEmail) System.out.println("email_id not found or invalid!");
//            allGood |= validEmail;
        }
        boolean passwordGenuinity = true;
        if (validEmail) {
            if (checkPasswordGenuinity) {

                System.out.println("Enter password");
                String passcode = input.next();
                passwordGenuinity = verifyPassword(person, mail_id, passcode);
                //            if (!passwordGenuinity) System.out.println("Incorrect Password!");
                //            allGood |= passwordGenuinity;
            }
        }
        if (!validEmail) {
            System.out.println("email_id not found or invalid!");
            return false;
        }
        if (!passwordGenuinity) {
            System.out.println("Incorrect Password! or Password did not match the user_name!");
            return false;
        }
        if (verifyEmail) {
            while (true) {
                int otp = 1234;
                String subject = "email verification";
                String body = "Use code '1234' to verify your email_id to login VirtualSandha!";
                System.out.println("A 4 digit code has been sent to your registered mail_id");
                Gmail.sendMail(mail_id, subject, body);
                int code = Integer.MAX_VALUE;
                while (true) {
                    System.out.println("Resend mail, press 1:\nTo exit, press 0: \nTo proceed, Enter code: ");
                    code = input.nextInt();
                    if (code == otp || code == 1 || code == 0) break;
                    System.out.println("Incorrect code!");
                }
                if (code == 0) return false;
                if (code == otp) break;
            }
            System.out.println("email successfully validated!");
        }
        return true;
    }
    public static boolean verifyPassword(String person, String mail_id, String passcode) throws SQLException {
        Connection con = DriverManager.getConnection(url, db_user_name, db_password);
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery("SELECT first_name, password FROM " + person + " WHERE email = '" + mail_id + "';");
        res.next();
        personName = res.getString(1);
        String fetchedPassword = res.getString(2);
        String decryptedPassword = CryptEnDecrypt.decryptPassword(fetchedPassword);
        return passcode.equals(decryptedPassword);
    }
    public static boolean checkIfEmailValid(String person, String mail_id) throws SQLException {
        Connection con = DriverManager.getConnection(url, db_user_name, db_password);
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery("SELECT * FROM " + person + ";");
        while (res.next()) {
            String fetched_mail_id = res.getString(4);
            if (mail_id.equals(fetched_mail_id)) {
                con.close();
                return true;
            }
        }
        con.close();
        return false;

    }
    public static boolean changeNewPassword(String mail_id, String person) throws SQLException {
        Connection con = DriverManager.getConnection(url, db_user_name, db_password);
        Statement st = con.createStatement();
        System.out.println("Enter new password: ");
        String newPassword = input.next();
        String encryptedNewPassword = CryptEnDecrypt.encryptPassword(newPassword);
        int res = st.executeUpdate("UPDATE " + person + " SET password = '" + encryptedNewPassword + "' WHERE email = '" + mail_id + "';");
        if (res == 0) {
            System.out.println("An error has occurred!, Please try again!");
            con.close();
            return false;
        }
        System.out.println("Password successfully changed!");
        con.close();
        return true;
    }
}