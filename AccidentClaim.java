/*
* Java Course 4, Module 3
* 
* Capstone
*
* @author Aljay Pascual
*/
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.sql.*;
import java.util.*;
// This class is for filing of claims and display for claims
public class AccidentClaim extends RatingEngine {
    private String dateAccident;
    private String addressOfAccident;
    private String descriptionOfAccident;
    private String claimNo;
    private String descriptionOfDamage;
    private double estimatedCostOfRepair;
    private int inputPolicy;
    

    // For creating accident claim
    public void createAccidentClaim() {
      
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Random rand = new Random();
        int generateNum = rand.nextInt(999999);
        claimNo = String.format("C%06d", generateNum); //generate random number for claim number

        try (
            Connection conn = DriverManager.getConnection(getScheme_URL(), getUserName(), getPassword())) {
            Statement stmt = conn.createStatement();
            System.out.println("Enter Policy number: ");
            inputPolicy = (int) Math.round(inputValidator(inputPolicy));
            ResultSet rs = stmt
                    .executeQuery("select claimNumber, policyNumber from file_accident_claim where policyNumber =  '"
                            + getInputPolicy() + "'");
            if (rs.next()) { // process below line if inputPolicy number has existing claim number
                System.out.println("This policy is already  claimed.\nClaim Number: " + rs.getString("claimNumber"));

            }

            else { // process below line if inputPolicy number has not existing claim number

                ResultSet rs2 = stmt.executeQuery(
                        "SELECT expDate, effectiveDate, policyNumber from new_policy where policyNumber =  '"
                                + getInputPolicy() + "'");
                if (rs2.next()) {
                    String expDate = rs2.getString("expDate");
                    String effectiveDate = (rs2.getString("effectiveDate"));
                    Calendar cal = Calendar.getInstance();
                    java.util.Date date = cal.getTime();
                    String todaysdate = dateFormat.format(date);
                    LocalDate date1 = LocalDate.parse(expDate, dtf);
                    LocalDate date2 = LocalDate.parse(todaysdate, dtf);

                    if (date1.isEqual(date2) || (date1.isBefore(date2))) { // checking if policy is already expired
                                                                           // based on expiration date
                        System.out.println("We cannot process claim as the policy is expired");
                        System.out.println("Policy number: " + getInputPolicy());
                        System.out.println("Effective date: " + effectiveDate);
                        System.out.println("Expiration date: " + expDate);
                    }
                    else { // creating new claim
                        ResultSet rs1 = stmt.executeQuery(
                                "SELECT * from new_policy where policyNumber =  '" + getInputPolicy() + "'");
                        while (rs1.next()) {
                            claimLoad();
                            PreparedStatement stmt1 = conn.prepareStatement(
                                    "INSERT INTO file_accident_claim(claimNumber,dateOfAccident,addressOfAccident,descriptionOfAccident,descriptionOfDamage,estimatedCostofRepair,policyNumber)"
                                            + " values('" + claimNo + "', '" + getDateAccident() + "' , '"
                                            + getAddressOfAccident() + "', '" + getDescriptionOfAccident() + "', '"
                                            + getDescriptionOfDamage() + "', '" + getEstimatedCostOfRepair() + "', '"
                                            + getInputPolicy() + "')");
                            stmt1.executeUpdate();
                            System.out.println("Your claim number is: " + claimNo);
                        }
                    }
                }
            }
            System.out.println("Enter any key to go back to menu ");
            scan.nextLine();
            scan.nextLine();
        }

        catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void claimLoad() { // load details for claims
        System.out.println("Date of accident: (YYYY-MM-DD) ");
        inputDateOfAccident();
        System.out.println("Address where accident happened: ");
        String addressOfAccident = scan.nextLine();
        System.out.println("Description of accident: ");
        String descriptionOfAccident = scan.nextLine();
        System.out.println("Description of damage to vehicle: ");
        String descriptionOfDamage = scan.nextLine();
        System.out.println("Estimated cost of repairs : ");
        double estimatedCostOfRepair = (int) Math.round(doubleValidator(estimatedCostOfRepair = 0));
        this.addressOfAccident = addressOfAccident;
        this.descriptionOfAccident = descriptionOfAccident;
        this.descriptionOfDamage = descriptionOfDamage;
        this.estimatedCostOfRepair = estimatedCostOfRepair;

    }

    public void searchClaim() { // searching for existing claim
        try (
            Connection conn = DriverManager.getConnection(getScheme_URL(), getUserName(), getPassword())) {
            Statement stmt = conn.createStatement();
            System.out.println("Enter claim number: ");
            String claimNumber = scan.next();
            ResultSet rs = stmt
                    .executeQuery("SELECT * from file_accident_claim where claimNumber LIKE  '" + claimNumber + "'");
            if (rs.next()) { // if input claimNumber is true, process below lines
                System.out.format(
                        "Claim Number: %s\nDate of Accident: %s\nAddress of Accident: %s\nDescription of Accident: %s\nDescription of Damage: %s \nEstimated Cost of Repair: %s\nPolicy Number: %s",
                        claimNumber, rs.getString("dateOfAccident"), rs.getString("addressOfAccident"),
                        rs.getString("descriptionOfAccident"), rs.getString("descriptionOfDamage"),
                        rs.getString("estimatedCostofRepair"), rs.getString("policyNumber"));
                System.out.println(" \n--- Enter any key to go back to menu --- ");
                scan.nextLine();
                scan.nextLine();
            } else // if input claimNumber is not true, process below lines
            {
                System.out.println("No existing claim number such as: " + claimNumber);
                System.out.println(" \n--- Enter any key to go back to menu --- ");
                scan.nextLine();
                scan.nextLine();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void inputDateOfAccident() { // getting date of accident, and validating if input is valid from format
                                        // YYYY-MM-DD
        boolean inputValid = false;
        while (!inputValid) {

            String dateAccident = scan.nextLine();
            this.dateAccident = dateAccident;
            try {
                if (dateAccident.matches("^[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$")) {
                    inputValid = true;
                    // if the code made it here, we have valid input
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    public String getDateAccident() {
        return dateAccident;
    }

    /**
     * @return String return the addressOfAccident
     */
    public String getAddressOfAccident() {
        return addressOfAccident;
    }

    /**
     * @return String return the descriptionOfAccident
     */
    public String getDescriptionOfAccident() {
        return descriptionOfAccident;
    }

    /**
     * @return String return the descriptionOfDamage
     */
    public String getDescriptionOfDamage() {
        return descriptionOfDamage;
    }

    /**
     * @return double return the estimatedCostOfRepair
     */
    public double getEstimatedCostOfRepair() {
        return estimatedCostOfRepair;
    }

    /**
     * @param dateAccident the dateAccident to set
     */
    public void setDateAccident(String dateAccident) {
        this.dateAccident = dateAccident;
    }

    /**
     * @param addressOfAccident the addressOfAccident to set
     */
    public void setAddressOfAccident(String addressOfAccident) {
        this.addressOfAccident = addressOfAccident;
    }

    /**
     * @param descriptionOfAccident the descriptionOfAccident to set
     */
    public void setDescriptionOfAccident(String descriptionOfAccident) {
        this.descriptionOfAccident = descriptionOfAccident;
    }

    /**
     * @param descriptionOfDamage the descriptionOfDamage to set
     */
    public void setDescriptionOfDamage(String descriptionOfDamage) {
        this.descriptionOfDamage = descriptionOfDamage;
    }

    /**
     * @param estimatedCostOfRepair the estimatedCostOfRepair to set
     */
    public void setEstimatedCostOfRepair(double estimatedCostOfRepair) {
        this.estimatedCostOfRepair = estimatedCostOfRepair;
    }

    /**
     * @return int return the inputPolicy
     */
    public int getInputPolicy() {
        return inputPolicy;
    }

    /**
     * @param inputPolicy the inputPolicy to set
     */
    public void setInputPolicy(int inputPolicy) {
        this.inputPolicy = inputPolicy;
    }

}
