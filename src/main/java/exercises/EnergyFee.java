package exercises;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import static exercises.Helper.getChunkEndExclusive;
import static exercises.Helper.getChunkStartInclusive;
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
            double elecFee = feeCalculatorSeq(elecUsed);
            String accountNum = account.getAccountNum();
            resultMap.put(accountNum,elecFee);
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
                resultMap.put(accountList.get(i).getAccountNum(),feeCalculatorSeq(accountList.get(i).getElec()));
            }
        }
    }

    public static class calFeeTaskParallelThreadClass extends Thread{
        private final ArrayList<EnergyAccount> accountList;
        private Map<String, Double> resultMap=new HashMap<String, Double>();
        private final int level;

        public calFeeTaskParallelThreadClass(ArrayList<EnergyAccount> accountList, int level) {
            this.accountList = accountList;
            this.level = level;
        }
        public void run() {
            for (EnergyAccount account : accountList) {
                double elecUsed = account.getElec();
                String accountNum = account.getAccountNum();
                if (level == 1) {
                    double fee =0;
                    if(elecUsed>=50){
                        fee = 50;
                    }else{
                        fee= elecUsed;
                    }
                    resultMap.put(accountNum,fee);
                }
                if(level==2){
                    double fee =0;
                    if(elecUsed>100 ){
                        fee = 75;
                    }
                    else if(elecUsed>50){
                        fee= (elecUsed-50)*1.5;
                    }
                    else{
                        fee = 0;
                    }
                    resultMap.put(accountNum,fee);
                }
                if(level==3){
                    double fee =0;
                    if(100<elecUsed){
                        fee = (elecUsed-100)*2;
                    }
                    resultMap.put(accountNum,fee);
                }
            }
        }
    }
    public static Map<String, Double> calFeeTaskParallelThread (ArrayList<EnergyAccount> accountList) throws InterruptedException {
        Map<String, Double> resultMap=new HashMap<String, Double>();
        calFeeTaskParallelThreadClass threadLevel1= new calFeeTaskParallelThreadClass(accountList,1);
        calFeeTaskParallelThreadClass threadLevel2= new calFeeTaskParallelThreadClass(accountList,2);
        calFeeTaskParallelThreadClass threadLevel3= new calFeeTaskParallelThreadClass(accountList,3);
        threadLevel1.start();
        threadLevel2.start();
        threadLevel3.start();
        threadLevel1.join();
        threadLevel2.join();
        threadLevel3.join();
        for (EnergyAccount e :accountList){
            double result = threadLevel1.resultMap.get(e.getAccountNum()) + threadLevel2.resultMap.get(e.getAccountNum()) + threadLevel3.resultMap.get(e.getAccountNum());
            resultMap.put(e.getAccountNum(),result);
        }
        return resultMap;
    }

    public static Map<String, Double> calFeeDataParallelThread (ArrayList<EnergyAccount> accountList,int threadNum) throws InterruptedException {
        calFeeDataParallelThread[] threadArray = new calFeeDataParallelThread[threadNum];
        Map<String, Double> resultMap=new HashMap<String, Double>();
        for (int i =0; i<threadNum; i++){
            threadArray[i]=new calFeeDataParallelThread(accountList,getChunkStartInclusive(i, threadNum,accountList.size()),getChunkEndExclusive(i, threadNum,accountList.size()));
            threadArray[i].start();
        }
        for (calFeeDataParallelThread t : threadArray){
            t.join();
            resultMap.putAll(t.resultMap);
        }
        return resultMap;
    }
}
