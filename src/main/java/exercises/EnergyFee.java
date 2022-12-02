package exercises;

import java.util.ArrayList;

public class EnergyFee {
    /**
     * for Gas and electricity fee, 0-50 part pay 1 per unit
     * 50-100 part, pay 1.5 per unit
     * Part higher than 100 pay 2 per unit
     */
    public double calFeeSeq(EnergyAccount account){
        double elecUsed = account.getElec();
        double gasUsed = account.getGas();
        double gasFee = feeCalculatorSeq(gasUsed);
        double elecFee = feeCalculatorSeq(elecUsed);
        return gasFee+elecFee;
    }
    private double feeCalculatorSeq(double usage){
        double fee = 0;
        if (usage>=0 && usage<=50){
            fee += usage *1;
        }
        else if(usage>=50 && usage<=100){
            fee += 50;
            fee +=(usage-50)*1.5;
        }
        else if(usage>100){
            fee += 125;
            fee +=(usage-100)*2;
        }
        return fee;
    }
    public static class calFeeTaskParallelThread extends Thread{
        private final ArrayList<EnergyAccount> accountList;
        private ArrayList<Integer> costList = new ArrayList<>();

        public calFeeTaskParallelThread(java.util.ArrayList<EnergyAccount> accountList) {
            this.accountList = accountList;
        }
        public void run(){

        }

    }

}
