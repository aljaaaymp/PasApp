/*
* Java Course 4, Module 3
* 
* Capstone
*
* @author Aljay Pascual
*/
import java.sql.*;


// this class is for creating vehicle and displaying for premium charge on each vehicle
public class Vehicle extends RatingEngine {
    private double purchasePrice;
    private int numberOfCars;
    private int yearModel;
    private int choiceBuy;
    private String make;
    private String model;
    private String typeCar;
    private String fuel; 
    private String color;
    
    // this method is for creating vehicle
    public void createVehicle(double dlx, int policyNumber, int accountNumber, String userName, String passWord) {
        try (
            Connection conn = DriverManager.getConnection(getScheme_URL(), userName, passWord)) {
            
            double getDlx = dlx;
            System.out.print("How many cars do you want to insure? ");
            numberOfCars = inputValidator(numberOfCars);

            // creating array of objects for vehicle
            Vehicle[] vehicleArr = new Vehicle[getNumberOfCars()];
            for (int index = 0; index < getNumberOfCars(); index++) {
                vehicleArr[index] = new Vehicle();
                vehicleArr[index].vehicleLoad();
            }
            // calculating premium charge for each vehicle
            for (int i = 0; i < vehicleArr.length; i++) {

                vehicleArr[i].calculation(getDlx, vehicleArr[i].getPurchasePrice(), vehicleArr[i].getYearModel(),
                        vehicleArr[i].getMake());

            }
            System.out.println("Do you want to buy the policy? \n[1] Yes \n[2] No");

            choiceBuy = validateChoice(getChoiceTypeArr(), choiceBuy);

            // if user chose 1 or buy, insert each objects into data base
            if (choiceBuy == 1) {
                for (int o = 0; o < vehicleArr.length; o++) {
                    PreparedStatement stmt1 = conn.prepareStatement("insert into vehicle_create(make,model,"
                            + "yearModel,typeCar, fuel, purchasePrice, color, premiumCharge,policyNumber,accountNumber)"
                            + "values('" + vehicleArr[o].getMake() + "', '" + vehicleArr[o].getModel() + "' , '"
                            + vehicleArr[o].getYearModel() + "' , '" + vehicleArr[o].getTypeCar()
                            + "', '" + vehicleArr[o].getFuel() + "'" + ", '" + vehicleArr[o].getPurchasePrice() + "', '"
                            + vehicleArr[o].getColor() + "' , '"
                            + vehicleArr[o].getPremiumCharge() + "', '" + policyNumber + "', '" + accountNumber
                            + "')");
                    stmt1.executeUpdate();

                }
                System.out.println("Policy added!");
                System.out.println("Your policy number is: " + policyNumber);
                System.out.println(" \n--- Enter any key to go back to menu --- ");
                scan.nextLine();
                scan.nextLine();

            }
            // if choice is not going to buy the policy
            if (choiceBuy == 2) {
                System.out.println("Get a quote again.");
                System.out.println("Enter any key to go back to menu ");
                scan.nextLine();
                scan.nextLine();
            }

        } catch (SQLException ex) {
            System.out.println("Database error occured upon saving    ");
        }
    }
    // vehicle details
    public void vehicleLoad() {
        System.out.println("Make e.g. Toyota, Ford, BMW, e.t.c: ");
        String make = scan.nextLine();

        System.out.println("Enter model: ");
        String model = scan.nextLine();

        System.out.println("Enter year model: ");
        int yearModel = 0;
        yearModel = inputValidator(yearModel);

        System.out.println("Type – 4-door sedan, 2-door sports car, SUV, or truck");
        scan.nextLine();
        String typeCar = scan.nextLine();

        System.out.println("Fuel Type – Diesel, Electric, Petrol");
        String fuel = scan.nextLine();

        System.out.println("Purchase Price: ");
        double purchasePrice = 0;
        purchasePrice = doubleValidator(purchasePrice);

        System.out.println("Enter color: ");
        scan.nextLine();
        String color = scan.nextLine();

        this.make = make;
        this.model = model;
        this.yearModel = yearModel;
        this.typeCar = typeCar;
        this.fuel = fuel;
        this.purchasePrice = purchasePrice;
        this.color = color;
    }

    /**
     * @return int return the numberOfCars
     */
    public int getNumberOfCars() {
        return numberOfCars;
    }

    /**
     * @param numberOfCars the numberOfCars to set
     */
    public void setNumberOfCars(int numberOfCars) {
        this.numberOfCars = numberOfCars;
    }

    /**
     * @return String return the make
     */
    public String getMake() {
        return make;
    }

    /**
     * @param make the make to set
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * @return String return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return int return the yearModel
     */
    public double getYearModel() {
        return yearModel;
    }

    /**
     * @param yearModel the yearModel to set
     */
    public void setYearModel(int yearModel) {
        this.yearModel = yearModel;
    }

    /**
     * @return String return the typeCar
     */
    public String getTypeCar() {
        return typeCar;
    }

    /**
     * @param typeCar the typeCar to set
     */
    public void setTypeCar(String typeCar) {
        this.typeCar = typeCar;
    }

    /**
     * @return String return the fuel
     */
    public String getFuel() {
        return fuel;
    }

    /**
     * @param fuel the fuel to set
     */
    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    /**
     * @return double return the purchasePrice
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * @param purchasePrice the purchasePrice to set
     */
    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    /**
     * @return String return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

}
