package exercises;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static edu.rice.pcdp.PCDP.async;
import static edu.rice.pcdp.PCDP.finish;

public class EnergyFee {
    /**
     * for Gas and electricity fee, 0-50 part pay 1 per unit
     * 50-100 part, pay 1.5 per unit
     * Part higher than 100 pay 2 per unit
     */
    public static Map<String,Double> calFeeSeq(ArrayList<EnergyAccount> accountList){
        Map<String, Double> resultMap=new HashMap<String, Double>();
        for (EnergyAccount account : accountList) {
            double elecUsed = account.getElec();
            double gasUsed = account.getGas();
            double gasFee = feeCalculatorSeq(gasUsed);
            double elecFee = feeCalculatorSeq(elecUsed);
            String accountNum = account.getAccountNum();
            resultMap.put(accountNum,gasFee+elecFee);
        }
        return resultMap;
    }
    private static double feeCalculatorSeq(double usage){
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
    public static class calFeeDataParallelThread extends Thread{
        private final ArrayList<EnergyAccount> accountList;
        private final int startIndex;
        private final int endIndex;
        private Map<String, Double> resultMap=new HashMap<String, Double>();

        public calFeeDataParallelThread(ArrayList<EnergyAccount> accountList, int startIndex, int endIndex ) {
            this.accountList = accountList;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }
        public void run(){
            for(int i = startIndex; i<endIndex; i++) {

                resultMap = calFeeSeq(accountList);
            }
        }
    }

    public static class calFeeTaskParallelThread extends Thread{
        private final ArrayList<EnergyAccount> accountList;
        private Map<String, Double> resultMap=new HashMap<String, Double>();
        private final int level;

        public calFeeTaskParallelThread(ArrayList<EnergyAccount> accountList, int level) {
            this.accountList = accountList;
            this.level = level;
        }
        public void run() {
            for (EnergyAccount account : accountList) {
                double elecUsed = account.getElec();
                double gasUsed = account.getGas();
                String accountNum = account.getAccountNum();
                if (level == 1) {
                    double fee =0;
                    if(elecUsed+gasUsed>=50){
                        fee = 50;
                    }else{
                        fee= elecUsed+gasUsed;
                    }
                    resultMap.put(accountNum,fee);
                }
                if(level==2){
                    double fee =0;
                    if(50<elecUsed+gasUsed &&elecUsed+gasUsed<=100 ){
                        fee = 75;
                    }else{
                        fee= (elecUsed+gasUsed-50)*1.5;
                    }
                    resultMap.put(accountNum,fee);
                }
                if(level==3){
                    double fee =0;
                    if(100<elecUsed+gasUsed ){
                        fee = (elecUsed+gasUsed-100)*2;
                    }
                    resultMap.put(accountNum,fee);
                }
            }
        }
    }
    public static Map<String,Double> calFeeTaskParallelAsyc(ArrayList<EnergyAccount> accountList){
        Map<String, Double> resultMap=new HashMap<String, Double>();
        for (EnergyAccount account : accountList) {
            double elecUsed = account.getElec();
            double gasUsed = account.getGas();
            String accountNum=account.getAccountNum();
            final double[] fee = {0};
            finish(()-> {
                async(() -> {
                    double fee1 =0;
                    if(elecUsed+gasUsed>=50){
                        fee1 = 50;
                    }else{
                        fee1= elecUsed+gasUsed;
                    }
                    fee[0]+=fee1;
                });
                async(() ->{
                    double fee2 =0;
                    if(50<elecUsed+gasUsed &&elecUsed+gasUsed<=100 ){
                        fee2 = 75;
                    }else{
                        fee2= (elecUsed+gasUsed-50)*1.5;
                    }
                    fee[0] +=fee2;
                });
                double fee3 =0;
                if(100<elecUsed+gasUsed ){
                    fee3 = (elecUsed+gasUsed-100)*2;
                }
                fee[0] +=fee3;
            });
            resultMap.put(accountNum,fee[0]);
        }
        return resultMap;
    }

}
