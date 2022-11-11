package exercises;

public class EnergyAccount {
    //account number
    private final String account;
    //Gas used
    private final double gasUsed;
    //Electricity used
    private final double elecUsed;


    public EnergyAccount(String account, double gas, double elec) {
        this.gasUsed = gas;
        this.elecUsed =elec;
        this.account =account;
    }
    public double getGas(){
        return gasUsed;
    }
    public double getElec(){
        return elecUsed;
    }
}
