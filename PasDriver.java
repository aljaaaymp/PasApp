
/*
* Java Course 4, Module 3
* 
* Capstone
*
* @author Aljay Pascual
*/
import java.sql.SQLException;
import java.util.*;

// This class is the main and driver of Capstone
public class PasDriver {
    static int menuInput;
    public static void main(String[] args) throws SQLException {
        Scanner scan = new Scanner(System.in);
        DataBase dataBase = new DataBase();
        dataBase.createScheme();
       
        do { // creating an instance of a class
            Customer customer = new Customer();
            customer.connectionCreds(dataBase);
            Policy policy = new Policy();
            policy.connectionCreds(dataBase);
            AccidentClaim accidentClaim = new AccidentClaim();     
            accidentClaim.connectionCreds(dataBase);
            PolicyHolder policyHolder = new PolicyHolder();
            policyHolder.connectionCreds(dataBase);

            {
            menu();
            menuInput = (int) Math.round(policy.inputValidator(menuInput));
                switch (menuInput) {
                    case 1:
                        customer.customerToDatabase();
                        break;
                    case 2:
                        policyHolder.createPolicy();
                        break;
                    case 3:
                        policy.policyCancel();
                        break;
                    case 4:
                        accidentClaim.createAccidentClaim();
                        break;
                    case 5:
                        customer.searchCustomer();
                        break;
                    case 6:
                        policy.displayPolicy();
                        break;
                    case 7:
                        accidentClaim.searchClaim();
                        break;
                }
            }
        } while (menuInput != 8);
        System.out.println("Thank you for using!");
        scan.close();
    }

    public static void menu() { // Display for menu

        System.out.println(
                "Welcome to the Automobile Insurance Policy and Claims Administration System (PAS) Specification.");
        System.out.println("Press number to choose on menu");
        System.out.println("[1] Create a new Customer Account ");
        System.out.println("[2] Get a policy quote and buy the policy.");
        System.out.println("[3] Cancel a specific policy.");
        System.out.println("[4] File an accident claim against a policy.");
        System.out.println("[5] Search for a Customer account.");
        System.out.println("[6] Search for and display a specific policy.");
        System.out.println("[7] Search for and display a specific claim. ");
        System.out.println("[8] Exit the PAS System.");
    }
}
