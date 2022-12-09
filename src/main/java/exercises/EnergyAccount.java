package exercises;

public class EnergyAccount {
    //account number
    private final String accountNum;
    //Gas used
    private final double gasUsed;
    //Electricity used
    private final double elecUsed;


    public EnergyAccount(String accountNum, double gas, double elec) {
        this.gasUsed = gas;
        this.elecUsed =elec;
        this.accountNum =accountNum;
    }
    public double getGas(){
        return gasUsed;
    }
    public double getElec(){
        return elecUsed;
    }

    public String getAccountNum() {
        return accountNum;
    }
}
