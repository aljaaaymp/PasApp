/*
* Java Course 4, Module 3
* 
* Capstone
*
* @author Aljay Pascual
*/
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

// This class is for creating policy, searching for policy and cancelling policy
public class Policy extends PolicyHolder {
    private double dlx;
    private int inputPolicyNo;
    private int tempPolicyNumber, tempYear;
    private int linkedYear;
    private int currentYear; 
    private int inputPolicy;
    private int inputPolicySearch;
    private String address;
    private String license;
    private String dateOfLicenseNumber;
    private String dateOfBirth;
    private String tempPolicyId;
    private String tempoYear = Integer.toString(tempYear);
    DataBase dataBase = new DataBase();
    public void linkAccount(int accountNo, String effectiveDate, LocalDate expDate, String userName, String passWord) { // for customers that has existing policy
       
        try (
            Connection conn = DriverManager.getConnection(getScheme_URL(), userName, passWord)) {
            Statement stmt = conn.createStatement();
            Calendar calendar = Calendar.getInstance();
            currentYear = (calendar.get(Calendar.YEAR));
            System.out.println("Enter existing policy ID: ");
            inputPolicyNo = inputValidator(inputPolicyNo);

            ResultSet policyId = stmt
                    .executeQuery("SELECT * from policy_holder where policyID =  '" + getInputPolicyNo() + "'");
            if (policyId.next()) { // if input policy ID is on the new_policy table in SQL, then below lines
                                   // will process
                String fName = policyId.getString("firstName");
                String lName = policyId.getString("lastName");
                PreparedStatement stmt2 = conn.prepareStatement("insert into new_policy(fName,lName,"
                        + "effectiveDate,expDate, accountNumber,policyID)"
                        + "values('" + fName + "', '" + lName + "' , '" + effectiveDate + "' , '" + expDate
                        + "', '" + accountNo + "', '" + getInputPolicyNo() + "')");
                stmt2.execute();

                ResultSet rs2 = stmt
                        .executeQuery("select policyID, policyNumber from new_policy\r\n" + "WHERE fName = '"
                                + fName + "' AND lName = '" + lName + "' order by policyNumber desc limit 1");
                while (rs2.next()) { 
                    // getting policyNumber from new_policy table and assigning as
                    // 'tempPolicyNumber' variable
                    tempPolicyNumber = rs2.getInt("policyNumber");
                }
                ResultSet rs3 = stmt.executeQuery("SELECT dateOfLicenseNumber FROM policy_holder\r\n"
                        + "WHERE firstName = '" + fName + "' AND lastName = '" + lName + "' ");
                if (rs3.next()) {
                    Vehicle vehicle = new Vehicle();
                    tempoYear = rs3.getString("dateOfLicenseNumber"); // getting date of license number from policy holder table
                    String[] parts = tempoYear.split("-");
                    linkedYear = Integer.parseInt(parts[0]);
                    dlx = getcurrentYear() - linkedYear;
                    dlxCheck ();
                    vehicle.createVehicle(getDlx(), getTempPolicyNumber(), accountNo, userName, passWord);

                }
            } else {
                System.out.println("Policy ID not existing: " + getInputPolicyNo());
                System.out.println(" \n--- Enter any key to go back to menu --- ");
                scan.nextLine();
                scan.nextLine();
            }
        } catch (SQLException ex) {
            System.out.println("Database error occured upon saving   " + ex);
        }
    }
    // for creating new policy account
    public void newPolicyHolder(int accNo, LocalDate expDate, String effectiveDate, int Accnumber,
            String dateOfLicense, String userName, String passWord) {
             
        try (
            Connection conn = DriverManager.getConnection(getScheme_URL(), userName, passWord)) {
            Statement stmt = conn.createStatement();
            int getAccNo = accNo;

            LocalDate expDates = expDate;
            String getEffectiveDate = effectiveDate;
            int accNumbers = Accnumber;

            Calendar calendar = Calendar.getInstance();
            currentYear = (calendar.get(Calendar.YEAR));
            super.fullName();
            System.out.println("Enter your address: ");
            String address = scan.nextLine();

            inputDateBirth();
            scan.nextLine();
            System.out.println("Enter your Driver\'s license number: ");
            String license = scan.nextLine();
            inputDateLicense();
            this.license = license;
            this.address = address;

            PreparedStatement stmt1 = conn.prepareStatement(
                    "insert into policy_holder(firstName,lastName,address,dateOfBirth,licenseNumber,dateOfLicenseNumber,accountNumber)"
                            + "values('" + getFirstName() + "' , '" + getLastName() + "', '" + getAddress()
                            + "', '" + getDateOfBirth() + "', '" + getLicense() + "', '" + getDateOfLicenseNumber()
                            + "', '"
                            + getAccNo + "')");
            stmt1.execute();

            ResultSet rs2 = stmt.executeQuery(
                    "SELECT policyID FROM policy_holder\r\n" + "WHERE firstName = '" + getFirstName()
                            + "' AND lastName = '" + getLastName() + "'  order by policyID desc limit 1 ");

            while (rs2.next()) { /*
                                  * Repeatedly process each row,
                                  * getting policyNumber from new_policy table and assigning as
                                  * 'tempPolicyNumber' variable
                                  */
                System.out.println("Your policy ID is: " + rs2.getString("policyID"));
                tempPolicyId = rs2.getString("policyID");
            }
            System.out.println("==================================================");
            // inserting details from user input to new_policy table in SQL
            PreparedStatement stmt2 = conn.prepareStatement(
                    "insert into new_policy(fName,lName," + "effectiveDate,expDate, accountNumber, policyID)"
                            + "values('" + getFirstName() + "', '" + getLastName() + "' , '" + getEffectiveDate
                            + "' , '" + expDates + "', '" + accNumbers + "', '" + tempPolicyId + "')");
            stmt2.execute();
            // selecting policyNumber from new_policy table and getting it to display by
            // firstName and lastName
            ResultSet rs1 = stmt
                    .executeQuery("SELECT policyNumber FROM new_policy\r\n" + "WHERE fName = '" + getFirstName()
                            + "' AND lName = '" + getLastName() + "'  order by policyNumber desc limit 1 ");
            while (rs1.next()) { // Repeatedly process each row
                // getting policyNumber from new_policy table and assigning as
                // 'tempPolicyNumber' variable

                setTempPolicyNumber(rs1.getInt("policyNumber"));
            }
            Vehicle vehicle = new Vehicle();
            String[] parts = getDateOfLicenseNumber().split("-"); // splitting dateOfLicense and getting first index and
                                                                  // assigning as 'year' variable
            int year = Integer.parseInt(parts[0]);

            setDlx(getcurrentYear() - year);
            dlxCheck ();
            vehicle.createVehicle(getDlx(), getTempPolicyNumber(), getAccNo, userName, passWord);

        } catch (SQLException ex) {
            System.out.println("Database error occured upon saving   " + ex);
        }
    }

    public void displayPolicy() { // for displaying specific policy
        try (
            Connection conn = DriverManager.getConnection(getScheme_URL(), getUserName(), getPassword())) {
            Statement stmt = conn.createStatement();
            System.out.println("Enter Policy number: ");
            inputPolicy = inputValidator(inputPolicy);

            ResultSet rs = stmt.executeQuery("SELECT * from new_policy where policyNumber LIKE  '" + inputPolicy + "'");
            if (rs.next()) { // if input inputPolicy is true, process below lines
                System.out.printf("%15s %15s %15s %17s %15s", "Policy Number", "Effective Date", "Expiration Date",
                        "First Name", "Last Name \n");
                System.out.format("%15s %15s %15s %17s %15s", rs.getString("policyNumber"),
                        rs.getString("effectiveDate"), rs.getString("expDate"), rs.getString("fName"),
                        rs.getString("lName"));

                ResultSet rs1 = stmt.executeQuery(
                        "select policyNumber, SUM(premiumCharge) FROM vehicle_create WHERE policyNumber = '"
                                + inputPolicy + "'");

                while (rs1.next()) {
                    System.out.println("\n-------------------------------------------------------");
                    System.out.println("Total Premium Charge: " + rs1.getString("SUM(premiumCharge)"));
                    System.out.println(" \n--- Enter any key to go back to menu --- ");
                    scan.nextLine();
                    scan.nextLine();
                }

            } else // if input inputPolicy is not true, process below lines
            {
                System.out.println("Policy number not exists as: " + inputPolicy);
                System.out.println(" \n--- Enter any key to go back to menu --- ");
                scan.nextLine();
                scan.nextLine();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void policyCancel() { // this method is for canceling policy
        try (
            Connection conn = DriverManager.getConnection(getScheme_URL(), getUserName(), getPassword())) {
            Statement stmt = conn.createStatement();

            System.out.println("Enter Policy number: ");
            inputPolicySearch = inputValidator(inputPolicySearch);

            ResultSet rs = stmt
                    .executeQuery("SELECT * from new_policy where policyNumber LIKE  '" + getInputPolicySearch() + "'");
            System.out.println("-----------------------------");
            if (rs.next()) { // if input policy number is existing
                System.out.println("Do you want to delete or update your policy?");
                System.out.println("[1] Delete \n[2] No");
                int policyDelete = validateChoice(getChoiceTypeArr(), policyDelete = 0);

                if (policyDelete == 2) { // if choice is no
                    System.out.println("Policy not deleted.");
                    System.out.println(" \n--- Enter any key to go back to menu --- ");
                    scan.nextLine();
                }
                if (policyDelete == 1) { // if choice in option is 1 or delete, process each line below
                    System.out.println("Are you sure you want to delete?");
                    System.out.println("[1] Yes \n[2] No");
                    int delete = validateChoice(getChoiceTypeArr(), delete = 0);

                    if (delete == 1) { // if user choose to delete the policy
                        PreparedStatement stmt1 = conn
                                .prepareStatement(
                                        "DELETE FROM new_policy WHERE policyNumber = '" + getInputPolicySearch() + "'");
                        PreparedStatement stmt2 = conn.prepareStatement(
                                "DELETE FROM vehicle_create WHERE policyNumber = '" + getInputPolicySearch() + "'");
                        System.out.println("Deleted");
                        stmt1.executeUpdate();
                        stmt2.executeUpdate();
                        System.out.println(" \n--- Enter any key to go back to menu --- ");
                        scan.nextLine();
                        
                    }
                    if (delete == 2) { // if user did not confirm to delete the policy
                        System.out.println("Policy not deleted.");
                        System.out.println(" \n--- Enter any key to go back to menu --- ");
                        scan.nextLine();
                        
                       
                        
                    }
                }
            } else // if policy number is not existing
            {
                System.out.println("No policy existing!");
                System.out.println(" \n--- Enter any key to go back to menu --- ");
                scan.nextLine();
               
            }
            scan.nextLine();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
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

    public void inputDateBirth() { // getting birth date, and validating if input is valid from format YYYY-MM-DD
        boolean inputValid = false;
        while (!inputValid) {
            System.out.println("Enter date of birth: (YYYY-MM-DD) ");
            dateOfBirth = scan.next();
            try {
                if (dateOfBirth.matches("^[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$")) {
                    inputValid = true;
                    // if the code made it here, we have valid input
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    public void dlxCheck () { 
        if (this.dlx <= 0){
            this.dlx = 1;
        }
    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param inputPolicyNo the inputPolicyNo to set
     */
    public void setInputPolicyNo(int inputPolicyNo) {
        this.inputPolicyNo = inputPolicyNo;
    }

    public int getInputPolicyNo() {
        return inputPolicyNo;
    }

    public String getDateOfLicenseNumber() {
        return dateOfLicenseNumber;
    }

    /**
     * @return int return the tempPolicyNumber
     */
    public int getTempPolicyNumber() {
        return tempPolicyNumber;
    }

    public void setTempPolicyNumber(int tempPolicyNumber) {
        this.tempPolicyNumber = tempPolicyNumber;
    }

    public int getLinkedYear() {
        return linkedYear;
    }

    public void setLinkedyear(int linkedYear) {
        this.linkedYear = linkedYear;
    }

    public void setinputPolicySearch(int inputPolicySearch) {
        this.inputPolicySearch = inputPolicySearch;
    }

    public int getInputPolicySearch() {
        return inputPolicySearch;
    }

    public int getcurrentYear() {
        return currentYear;
    }

    public void setDlx(double dlx) {
        this.dlx = dlx;
    }

    public double getDlx() {
        return dlx;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicense() {
        return license;
    }

    public void setTempPolicyId(String tempPolicyId) {
        this.tempPolicyId = tempPolicyId;
    }

    public String getTempPolicyId() {
        return tempPolicyId;
    }

}
