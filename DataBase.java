import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DataBase { // connecting to database
    private final String Scheme_URL = "jdbc:mysql://localhost:3306/PasApplication";
    private final String DB_URL = "jdbc:mysql://localhost/";
    private String userName = null;
    private String password = null;
    Scanner scan = new Scanner(System.in);

    public void createScheme() throws SQLException { // login for sql connection
        {
            Boolean creds = false;
            do {
                askForUsernamePassword();
                try (
                        Connection conn = DriverManager.getConnection(DB_URL, getUserName(), getPassword());
                        Statement stmt = conn.createStatement();) {
                    creds = true;
                    String sql = "CREATE DATABASE IF NOT EXISTS PasApplication";
                    stmt.executeUpdate(sql);
                    System.out.println("Database created successfully...");
                    JdbcTable();

                } catch (SQLException e) {
                    System.out.println("Invalid credentials");
                }
            } while (creds == false);

        }
    }

    private void askForUsernamePassword() { // username and password of sql connection

        System.out.println("Enter your username: ");
        String userName = scan.nextLine();
        System.out.println("Enter your password: ");
        String password = scan.nextLine();
        this.userName = userName;
        this.password = password;

    }

    public void JdbcTable() { // creating table on mysql
        {
            try (
                    Connection conn = DriverManager.getConnection(getScheme_URL(), getUserName(), getPassword());
                    Statement stmt = conn.createStatement();) {

                String create_user = "CREATE TABLE IF NOT EXISTS create_user " +
                        "(accountNumber int (4) auto_increment primary key, " +
                        " firstName nvarchar (50) not null, " +
                        " lastName nvarchar (50) not null, " +
                        " address nvarchar (100) not null) " +
                        " AUTO_INCREMENT = 1000";
                stmt.executeUpdate(create_user);
                System.out.println("Created create_user table in given database...");

                String policy_holder = "CREATE TABLE IF NOT EXISTS policy_holder " +
                        "(firstName nvarchar (50) not null, " +
                        " lastName nvarchar (50) not null, " +
                        " address nvarchar (50) not null, " +
                        " dateOfBirth nvarchar (100) not null, " +
                        " licenseNumber nvarchar(50), " +
                        " dateOfLicenseNumber date, " +
                        " accountNumber nvarchar (4), " +
                        " policyID int auto_increment primary key)";

                stmt.executeUpdate(policy_holder);
                System.out.println("Created policy_holder table in given database...");

                String new_policy = "CREATE TABLE IF NOT EXISTS new_policy " +
                        "(policyNumber int (6) auto_increment  primary key, " +
                        " effectiveDate date, " +
                        " expDate date, " +
                        " fName nvarchar (50), " +
                        " lName nvarchar (50), " +
                        " accountNumber int (4), " +
                        " policyID int, " +
                        " foreign key (policyID) references policy_holder (policyID) )AUTO_INCREMENT = 100000";

                stmt.executeUpdate(new_policy);
                System.out.println("Created new_policy table in given database...");

                String vehicle_create = "CREATE TABLE IF NOT EXISTS vehicle_create " +
                        "(make nvarchar (50) not null, " +
                        " model nvarchar (50) not null, " +
                        " yearModel int not null, " +
                        " typeCar nvarchar (50) not null, " +
                        " fuel nvarchar (20) not null, " +
                        " color nvarchar (20) not null, " +
                        " purchasePrice double not null, " +
                        " premiumCharge double ," +
                        " policyNumber int (6), " +
                        " accountNumber int (4))";

                stmt.executeUpdate(vehicle_create);
                System.out.println("Created vehicle_create table in given database...");

                String file_accident_claim = "CREATE TABLE IF NOT EXISTS file_accident_claim " +
                        "(claimNumber nvarchar (7), " +
                        " dateOfAccident date, " +
                        " addressOfAccident nvarchar (100) not null, " +
                        " descriptionOfAccident nvarchar (1000) not null, " +
                        " descriptionOfDamage nvarchar (1000) not null, " +
                        " estimatedCostofRepair int not null, " +
                        " policyNumber int (6))";

                stmt.executeUpdate(file_accident_claim);
                System.out.println("Created file_accident_claim table in given database...");

            }

            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getScheme_URL() {
        return this.Scheme_URL;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String password) {
        this.password = password;
    }
}
