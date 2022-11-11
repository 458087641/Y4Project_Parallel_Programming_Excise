package exercises;

public class EnergyFee {
    /**
     * for Gas and electricity fee, 0-50 part pay 1 per unit
     * 50-100 part, pay 1.5 per unit
     * Part higher than 100 pay 2 per unit
     */
    private double calFee(EnergyAccount account){
        double elecUsed = account.getElec();
        double gasUsed = account.getGas();
        double gasFee = feeCalculator(gasUsed);
        double elecFee = feeCalculator(elecUsed);
        return gasFee+elecFee;
    }
    private double feeCalculator(double usage){
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

}
