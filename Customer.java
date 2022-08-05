
/*
* Java Course 4, Module 3
* 
* Capstone
*
* @author Aljay Pascual
*/
import java.util.*;
import java.sql.*;

// This class is for creating new customer and displaying for existing customer
public class Customer extends DataBase {

    Scanner scan = new Scanner(System.in);
    private String firstName;
    private String lastName;
    private String address;
    private String accNo;

    public void load() { // load details for customer
        fullName();
        System.out.println("Enter your address: ");
        String address = scan.nextLine();
        this.address = address;
    }

    public void connectionCreds(DataBase dataBase) {
        super.setUserName(dataBase.getUserName());
        super.setPassWord(dataBase.getPassword());
    }

    public void displayCustomer() { // display for customer details after creating
        System.out.println("First name: " + getFirstName());
        System.out.println("Last name: " + getLastName());
        System.out.println("Address: " + getAddress());
        System.err.println("Account number: " + getAccNo());
    }

    public void customerToDatabase() { // saving customer detail to database

        try (
                Connection conn = DriverManager.getConnection(getScheme_URL(), getUserName(), getPassword())) {
            Statement stmt = conn.createStatement();

            load();
            ResultSet rs = stmt.executeQuery("SELECT * from create_user where firstName  = '" + getFirstName()
                    + "'  AND lastName =  '" + getLastName() + "' ");

            if (rs.next()) { // if input customer first name and last is existing in database
                System.out.println("This account name has already account number existing");
                System.out.println("Account number: " + rs.getString("accountNumber"));
            } else { // if customer first name and last name is not existing, process below and
                     // adding it to date base

                PreparedStatement saveCustomerAccount = conn
                        .prepareStatement("INSERT INTO create_user (firstName,lastName,"
                                + "address)" + " VALUES (?,?,?)");

                saveCustomerAccount.setString(1, getFirstName());
                saveCustomerAccount.setString(2, getLastName());
                saveCustomerAccount.setString(3, getAddress());
                saveCustomerAccount.execute();

                ResultSet rs2 = stmt
                        .executeQuery("SELECT accountNumber from create_user where firstName  = '" + getFirstName()
                                + "'  AND lastName =  '" + getLastName() + "' ");
                while (rs2.next()) {
                    this.accNo = rs2.getString("accountNumber");
                    System.out.println("------------------------------------------------------");
                    System.out.println("Account created!");
                    displayCustomer();
                }
            }

        } catch (SQLException ex) {
            System.out.println("Database error occured upon saving");
        }

        System.out.println("Enter any key to go back to menu ");
        scan.nextLine();
    }

    public void searchCustomer() { // searching for customer if existing
        fullName();

        try (
                Connection conn = DriverManager.getConnection(getScheme_URL(), getUserName(), getPassword())) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from create_user where firstName  = '" + getFirstName()
                    + "'  AND lastName =  '" + getLastName() + "' ");

            if (rs.next()) {
                System.out.printf("%15s %15s %15s %17s", "First Name", "Last Name", "Address", "Account Number \n");
                System.out.format("%15s %15s %15s %17s", rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("address"), rs.getString("accountNumber"));
                String accountNumber = rs.getString("accountNumber");
                ResultSet rs2 = stmt
                        .executeQuery("SELECT * from new_policy where accountNumber  = '" + accountNumber + "' ");
                System.out.println("\n \nPolicy owned");

                while (rs2.next()) {
                    System.out.println("Policy Number: " + rs2.getString("policyNumber"));
                    System.out.println("Effective Date: " + rs2.getString("effectiveDate"));
                    System.out.println("Expiration Date: " + rs2.getString("expDate") + "\n");
                }

                ResultSet rs3 = stmt
                        .executeQuery("SELECT * from policy_holder where accountNumber  = '" + accountNumber + "' ");

                System.out.println("\n \nPolicy holder associated with the account");
                while (rs3.next()) {
                    System.out.println("First Name: " + rs3.getString("firstName"));
                    System.out.println("Last Name: " + rs3.getString("lastName"));
                    System.out.println("Date of License Number: " + rs3.getString("dateOfLicenseNumber") + "\n");
                }

                ResultSet rs4 = stmt
                        .executeQuery("select SUM(premiumCharge) from vehicle_create where accountNumber  = '"
                                + accountNumber + "' ");
                System.out.println("\n \nTotal premium policy ");
                while (rs4.next()) {
                    System.out.println("Total: " + rs4.getString("SUM(premiumCharge)"));
                }
                System.out.println(" \n--- Enter any key to go back to menu --- ");
                scan.nextLine();
            } else {
                System.out.println("No account existing under name: " + getFirstName() + " " + getLastName());
                System.out.println("Enter any key to go back to menu ");
                scan.nextLine();
            }
        } catch (SQLException ex) {

            System.out.println("Database error occured upon saving   " + ex);

        }
    }

    public void fullName() { // customer full name

        System.out.println("Enter first name: ");
        String firstName = scan.nextLine();
        System.out.println("Enter last name: ");
        String lastName = scan.nextLine();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * @return String return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return String return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return String return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccNo() {
        return accNo;
    }

    /**
     * @param accNo the accNo to set
     */

    /**
     * @param accNo the accNo to set
     */
    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

}