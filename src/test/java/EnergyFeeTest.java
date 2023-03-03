import exercises.EnergyAccount;
import exercises.EnergyFee;
import exercises.MaxMinSum;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static exercises.Helper.generateString;

public class EnergyFeeTest extends TestCase {
    public static ArrayList<EnergyAccount> accountGenerator(int n){
        ArrayList<EnergyAccount> result = new ArrayList<>();
        Random random = new Random();
        for (int i =0; i<n;i++){
            EnergyAccount account = new EnergyAccount(generateString(),random.nextInt(400));
            result.add(account);
        }
        return result;
    }
    public void testEnergyFeeSeqCorrectness(){
        ArrayList<EnergyAccount> input = new ArrayList<>();
        Map<String, Double> resultMap=new HashMap<String, Double>();
        input.add( new EnergyAccount("1",50));
        input.add( new EnergyAccount("2",100));
        input.add( new EnergyAccount("3",200));
        EnergyFee calculator = new EnergyFee();
        resultMap=calculator.calFeeSeq(input);
        assertEquals(resultMap.get("1"),50.0);
        assertEquals(resultMap.get("2"),125.0);
        assertEquals(resultMap.get("3"),325.0);
    }
    public void testEnergyFeeDataParThreadCorrectness() throws InterruptedException {
        ArrayList<EnergyAccount> input = new ArrayList<>();
        Map<String, Double> resultMap=new HashMap<String, Double>();
        input.add( new EnergyAccount("1",50));
        input.add( new EnergyAccount("2",100));
        input.add( new EnergyAccount("3",200));
        EnergyFee calculator = new EnergyFee();
        for (int i=1; i<8;i++) {
            resultMap = calculator.calFeeDataParallelThread(input,i);
            assertEquals(resultMap.get("1"), 50.0);
            assertEquals(resultMap.get("2"), 125.0);
            assertEquals(resultMap.get("3"), 325.0);
        }
    }

    public void testEnergyFeeTaskParThreadCorrectness() throws InterruptedException {
        ArrayList<EnergyAccount> input = new ArrayList<>();
        Map<String, Double> resultMap=new HashMap<String, Double>();
        input.add( new EnergyAccount("1",50));
        input.add( new EnergyAccount("2",100));
        input.add( new EnergyAccount("3",200));
        EnergyFee calculator = new EnergyFee();

        resultMap = calculator.calFeeTaskParallelThread(input);
        assertEquals(resultMap.get("1"), 50.0);
        assertEquals(resultMap.get("2"), 125.0);
        assertEquals(resultMap.get("3"), 325.0);
    }

    public void testDataParSpeedup() throws InterruptedException {
        EnergyFee calculator = new EnergyFee();
        Map<String, Double> resultMappar = new HashMap<String, Double>();
        Map<String, Double> resultMapseq = new HashMap<String, Double>();
        ArrayList<EnergyAccount> input;
        for (int i = 1; i <= 8; i++) {
            System.out.println("Thread class Test with Threadnum " + i);
            for (int j = 1; j < 7; j++) {
                input = accountGenerator((int) Math.pow(10, j));
                for (int l = 0; l < 100; l++) {
                    resultMapseq = calculator.calFeeSeq(input);
                }
                long seqStartTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    resultMapseq = calculator.calFeeSeq(input);
                }
                long seqEndTime = System.nanoTime();
                long parStartTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    resultMappar = calculator.calFeeDataParallelThread(input, i);
                }
                long parEndTime = System.nanoTime();
                for (EnergyAccount e : input) {
                    assertEquals(resultMapseq.get(e.getAccountNum()), resultMappar.get(e.getAccountNum()));
                }
                long seqTime = (seqEndTime - seqStartTime);
                long parTime = (parEndTime - parStartTime);
                System.out.println("data amount " + Math.pow(10, j) + " words " + "speedup " + (double) seqTime / (double) parTime + " ");
            }
        }
    }
    public void testTaskParSpeedup() throws InterruptedException {
        EnergyFee calculator = new EnergyFee();
        Map<String, Double> resultMappar = new HashMap<String, Double>();
        Map<String, Double> resultMapseq = new HashMap<String, Double>();
        ArrayList<EnergyAccount> input;
            for (int j = 1; j < 7; j++) {
                input = accountGenerator((int) Math.pow(10, j));
                for (int l = 0; l < 100; l++) {
                    resultMapseq = calculator.calFeeSeq(input);
                }
                long seqStartTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    resultMapseq = calculator.calFeeSeq(input);
                }
                long seqEndTime = System.nanoTime();
                long parStartTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    resultMappar = calculator.calFeeTaskParallelThread(input);
                }
                long parEndTime = System.nanoTime();
                for (EnergyAccount e : input) {
                    assertEquals(resultMapseq.get(e.getAccountNum()), resultMappar.get(e.getAccountNum()));
                }
                long seqTime = (seqEndTime - seqStartTime);
                long parTime = (parEndTime - parStartTime);
                System.out.println("data amount " + Math.pow(10, j) + " words " + "speedup " + (double) seqTime / (double) parTime + " ");
            }
        }

}
