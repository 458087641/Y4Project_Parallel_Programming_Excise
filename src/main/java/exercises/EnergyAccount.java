package exercises;

public class EnergyAccount {
    //account number
    private final String accountNum;
    //Electricity used
    private final double elecUsed;


    public EnergyAccount(String accountNum, double elec) {
        this.elecUsed =elec;
        this.accountNum =accountNum;
    }
    public double getElec(){
        return elecUsed;
    }

    public String getAccountNum() {
        return accountNum;
    }
}
