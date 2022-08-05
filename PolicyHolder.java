/*
* Java Course 4, Module 3
* 
* Capstone
*
* @author Aljay Pascual
*/
import java.sql.*;
import java.time.LocalDate;
import java.util.InputMismatchException;

// This class is for creating policy holder 
public class PolicyHolder extends Customer {
    private int[] choiceTypeArr = { 1, 2 };
    private int accNumber;
    private int choice;
    private String dateOfBirth;
    private String licenseNumber;
    private String dateOfLicenseNumber;
    private String effectiveDate;
    private LocalDate expDate;
    

    public void policyHolderToDatabase() { // saving policy information to data base
        try (
            Connection conn = DriverManager.getConnection(getScheme_URL(), getUserName(), getPassword())) {
            PreparedStatement savePolicy = conn.prepareStatement(
                    "INSERT INTO policy_holder (firstName,lastName,address,dateOfBirth,licenseNumber,dateOfLicenseNumber,accountNumber)"
                            + " VALUES (?,?,?,?,?,?,?)");
            savePolicy.setString(1, getFirstName());
            savePolicy.setString(2, getLastName());
            savePolicy.setString(3, getAddress());
            savePolicy.setString(4, getDateOfBirth());
            savePolicy.setString(5, getLicenseNumber());
            savePolicy.setString(6, getDateOfLicenseNumber());
            savePolicy.setInt(7, getAccNumber());
            savePolicy.execute();
        } catch (SQLException ex) {
            System.out.println("Database error occured upon saving   ");
        }
    }

    public void createPolicy() { // creating policy
       
        try (
            Connection conn = DriverManager.getConnection(getScheme_URL(), getUserName(), getPassword())) {
            Statement stmt = conn.createStatement();
            System.out.println("Enter account number: ");
            accNumber = inputValidator(accNumber);
            ResultSet accNumber = stmt
                    .executeQuery("SELECT * from create_user where accountNumber LIKE  '" + getAccNumber() + "'");
            System.out.println("-----------------------------");
            if (accNumber.next()) { // if input accountNumber is true, process each row
                inputDate();
                LocalDate date = LocalDate.parse(effectiveDate);
                expDate = date.plusMonths(6);
                System.out.println("Effective date of policy:  " + getEffectiveDate());
                System.out.println("Expiration date of policy: " + getExpDate());
                System.out.println("Do you want to link this policy to a new or existing policy holder?");
                System.out.println("[1] Link \n[2] New policy holder");
                choice = validateChoice(choiceTypeArr, choice);
                Policy policy = new Policy();
               
                if (choice == 1) { // if customer has existing policy
                 
                    policy.linkAccount(getAccNumber(), getEffectiveDate(), getExpDate(), getUserName(), getPassword());
                }
                if (choice == 2) { // if customer will create new policy
                   
                    policy.newPolicyHolder(getAccNumber(), getExpDate(), getEffectiveDate(), getAccNumber(),
                            getDateOfLicenseNumber(), getUserName(), getPassword());
                }

            } else {
                System.out.println("No account number existing as : " + getAccNumber());
                System.out.println(" \n--- Enter any key to go back to menu --- ");
                scan.nextLine();
                scan.nextLine();
            }
        } catch (SQLException ex) {
            System.out.println("Database error occured upon saving   " + ex);
        }
    }

    public double doubleValidator(double num) { // validator for double input
        boolean invalid;
        do {
            try {
                num = scan.nextDouble();
                invalid = false;
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("Invalid Input");
                invalid = true;
            }
        } while (invalid == true);
        return num;
    }

    public void inputDate() { // prompt for the effective date of policy, also validating if invalid input for
                              // date YYYY-MM-DD
        boolean inputValid = false;
        while (!inputValid) {
            System.out.println("Effective date of policy: [yyyy-mm-dd]");
            effectiveDate = scan.next();
            try {
                if (effectiveDate.matches("^[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$")) {
                    inputValid = true;
                    // if the code made it here, we have valid input
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Try again.");
            }
        }

    }

    public void inputDateOfBirth() { // getting birth date, and validating if input is valid from format YYYY-MM-DD
        boolean inputValid = false;
        while (!inputValid) {
            System.out.println("Enter date of birth: (YYYY-MM-DD) ");
            this.dateOfBirth = scan.next();
            try {
                if (this.dateOfBirth.matches("^[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$")) {
                    inputValid = true;
                    // if the code made it here, we have valid input
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    public int validateChoice(int[] numArr, int choiceInt) { // validator for choices 1 or 2
        Boolean choiceMatch = false;
        do {
            choiceInt = inputValidator(choiceInt);
            for (int index = 0; index < numArr.length; index++) {
                if (choiceInt == numArr[index]) {
                    choiceMatch = true;
                }
            }
            if (choiceMatch == false) {
                System.out.println("Input out of range. Please try again.");
            }

        } while (!choiceMatch);

        return choiceInt;
    }

    public void inputDateLicense() { // getting driver's license date issued, and validating if input is valid from
                                     // format YYYY-MM-DD
        boolean inputValid = false;
        while (!inputValid) {
            System.out.println("Date on which driver\'s license was first issued: (YYYY-MM-DD) ");
            this.dateOfLicenseNumber = scan.next();
            try {
                if (this.dateOfLicenseNumber.matches("^[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$")) {
                    inputValid = true;
                    // if the code made it here, we have valid input
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    public int inputValidator(int num) { // input validator that only accepts integer
        boolean invalid;
        do {
            try {
                num = scan.nextInt();
                invalid = false;
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("Invalid Input");
                invalid = true;
            }
        } while (invalid == true);
        return num;
    }

    public void setDateOfLicenseNumber(String dateOfLicenseNumber) {
        this.dateOfLicenseNumber = dateOfLicenseNumber;
    }

    public String getDateOfLicenseNumber() {
        return dateOfLicenseNumber;
    }

    /**
     * @return int return the dateOfBirth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return String return the licenseNumber
     */
    public String getLicenseNumber() {
        return licenseNumber;
    }

    /**
     * @param licenseNumber the licenseNumber to set
     */
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public int getAccNumber() {
        return accNumber;
    }

    /**
     * @param accNo the accNo to set
     */
    public void setAccNumber(int accNumber) {
        this.accNumber = accNumber;
    }

    /**
     * @return String return the effectiveDate
     */
    public String getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * @param effectiveDate the effectiveDate to set
     */
    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * @return LocalDate return the expDate
     */
    public LocalDate getExpDate() {
        return expDate;
    }

    /**
     * @param expDate the expDate to set
     */
    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    /**
     * @return int return the choice
     */
    public int getChoice() {
        return choice;
    }

    /**
     * @param choice the choice to set
     */
    public void setChoice(int choice) {
        this.choice = choice;
    }

    /**
     * @return int[] return the choiceTypeArr
     */
    public int[] getChoiceTypeArr() {
        return choiceTypeArr;
    }

    /**
     * @param choiceTypeArr the choiceTypeArr to set
     */
    public void setChoiceTypeArr(int[] choiceTypeArr) {
        this.choiceTypeArr = choiceTypeArr;
    }

    /**
     * @return int return the inputPolicyNo
     */

}
