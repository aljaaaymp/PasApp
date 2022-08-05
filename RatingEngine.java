/*
* Java Course 4, Module 3
* 
* Capstone
*
* @author Aljay Pascual
*/
import java.util.Calendar;
import java.text.*;

// this class is for calculation of premium charge
public class RatingEngine extends Policy {
    private double premiumCharge;
    private double yearModel;
    private double currentYear;
    private double purchase;
    NumberFormat formatter = new DecimalFormat("#0.00");
    // formula for getting premium charge
    public void calculation(double dlx, double purchasePrice, double getYearModel, String getMake) {
        this.yearModel = getYearModel;
        this.premiumCharge = ((purchasePrice * getVehiclePriceFactor()) + ((purchasePrice / 100) / dlx));
        System.out.println("====================================================");
        System.out.println("Premium Charge: " + formatter.format(getPremiumCharge()) + "\nMake: " + getMake);
        

    }
    // getting rate of vehicle price factor from age of vehicle
    public double getVehiclePriceFactor() {
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        double vehiclePriceFactor = 0;
        double vehicleAge = getCurrentYear() - getYearModel();

        if (vehicleAge < 1) {
            vehiclePriceFactor = 0.01;
        } else if (vehicleAge < 3) {
            vehiclePriceFactor = 0.008;
        } else if (vehicleAge < 5) {
            vehiclePriceFactor = 0.007;
        } else if (vehicleAge < 10) {
            vehiclePriceFactor = 0.006;
        } else if (vehicleAge < 15) {
            vehiclePriceFactor = 0.004;
        } else if (vehicleAge < 20) {
            vehiclePriceFactor = 0.002;
        } else {
            vehiclePriceFactor = 0.001;
        }
        return vehiclePriceFactor;
    }

    /**
     * @return double return the premiumCharge
     */
    public double getPremiumCharge() {
        return premiumCharge;
    }

    /**
     * @param premiumCharge the premiumCharge to set
     */
  

    /**
     * @return int return the currentYear
     */
    public double getCurrentYear() {
        return currentYear;
    }

    public void setYearModel(int yearModel) {
        this.yearModel = yearModel;
    }

    public double getYearModel() {
        return yearModel;
    }

    /**
     * @param currentYear the currentYear to set
     */
    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    /**
     * @return double return the purchase
     */
    public double getPurchase() {
        return purchase;
    }

    /**
     * @param purchase the purchase to set
     */
    public void setPurchase(double purchase) {
        this.purchase = purchase;
    }

}
