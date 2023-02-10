import junit.framework.TestCase;
import exercises.MaxMinAvg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MaxMinAvgTest extends TestCase{

    public ArrayList<Double> generateArray(int size){
        ArrayList<Double> array = new ArrayList<>();
        Random r = new Random();
        for (int s = 0; s < size; s++) {
            final double num = r.nextInt();
            array.add(num);
        }
        return array;
    }
    public void testSeqCorrectness(){
        ArrayList<Double> input = generateArray(10000);
        double[] expected =new double[3];
        MaxMinAvg mma = new MaxMinAvg();
        double[] result = mma.maxMinAvgSeq(input);
        expected[0]=Collections.max(input);
        expected[1]=Collections.min(input);
        expected[2]=input.stream().mapToDouble(a->a).sum();
        for(int i =0; i<3; i++){
            assertEquals((int) expected[i], (int) result[i]);
        }
    }

    public void testTaskParCorrectness() throws InterruptedException {
        ArrayList<Double> input = generateArray(10000);
        double[] expected =new double[3];
        MaxMinAvg mma = new MaxMinAvg();
        double[] result = mma.maxMinSumTaskParallelThread(input);
        expected[0]= Collections.max(input);
        expected[1]=Collections.min(input);
        expected[2]=input.stream().mapToDouble(a->a).sum();
        for(int i =0; i<3; i++){
            assertEquals((int) expected[i], (int) result[i]);
        }
    }

    public void testDataParCorrectness() throws InterruptedException {
        ArrayList<Double> input = generateArray(13);
        double[] expected =new double[3];
        MaxMinAvg mma = new MaxMinAvg();
        double[] result = mma.maxMinAvgDataParallelThread(input,3);
        expected[0]= Collections.max(input);
        expected[1]=Collections.min(input);
        expected[2]=input.stream().mapToDouble(a->a).sum();
        for(int i =0; i<3; i++){
            assertEquals((int) expected[i], (int) result[i]);
        }
    }

    public void testTaskDataParCorrectness() throws InterruptedException {
        ArrayList<Double> input = generateArray(10000);
        double[] expected =new double[3];
        MaxMinAvg mma = new MaxMinAvg();
        double[] result = mma.maxMinAvgDataTaskParallelThread(input);
        expected[0]= Collections.max(input);
        expected[1]=Collections.min(input);
        expected[2]=input.stream().mapToDouble(a->a).sum();
        for(int i =0; i<3; i++){
            assertEquals((int) expected[i], (int) result[i]);
        }
    }

    public void testTaskParSpeedup() throws InterruptedException {
        MaxMinAvg mma = new MaxMinAvg();
        double[] resultSeq =new double[3];
        double[] resultPar =new double[3];
        ArrayList<Double> input = null;
        for (int j=1; j<8;j++) {
            input = generateArray((int) Math.pow(10, j));

            for (int l = 0; l < 100; l++) {
                resultSeq = mma.maxMinAvgSeq(input);
            }
            long seqStartTime = System.nanoTime();
            for (int l = 0; l < 100; l++) {
                resultSeq = mma.maxMinAvgSeq(input);
            }
            long seqEndTime = System.nanoTime();
            long parStartTime = System.nanoTime();
            for (int l = 0; l < 100; l++) {
                resultPar = mma.maxMinSumTaskParallelThread(input);
            }
            long parEndTime = System.nanoTime();
            for(int i =0; i<3; i++){
                assertEquals((int) resultSeq[i], (int) resultPar[i]);
            }
            long seqTime = (seqEndTime - seqStartTime);
            long parTime = (parEndTime - parStartTime);
            System.out.println("data amount " + Math.pow(10, j) + " words " + "speedup " + (double) seqTime / (double) parTime + " ");
        }
    }

    public void testDataParSpeedup() throws InterruptedException {
        MaxMinAvg mma = new MaxMinAvg();
        double[] resultSeq =new double[3];
        double[] resultPar =new double[3];
        ArrayList<Double> input = null;
        for (int j=1; j<7;j++) {
            input = generateArray((int) Math.pow(10, j));

            for (int l = 0; l < 100; l++) {
                resultSeq = mma.maxMinAvgSeq(input);
            }
            long seqStartTime = System.nanoTime();
            for (int l = 0; l < 100; l++) {
                resultSeq = mma.maxMinAvgSeq(input);
            }
            long seqEndTime = System.nanoTime();
            long parStartTime = System.nanoTime();
            for (int l = 0; l < 100; l++) {
                resultPar = mma.maxMinAvgDataParallelThread(input,3);
            }
            long parEndTime = System.nanoTime();
            double expected =input.stream().mapToDouble(a->a).average().getAsDouble();
            for(int i =0; i<3; i++){
                assertEquals((int) resultSeq[i], (int) resultPar[i]);
            }
            long seqTime = (seqEndTime - seqStartTime);
            long parTime = (parEndTime - parStartTime);
            System.out.println("data amount " + Math.pow(10, j) + " words " + "speedup " + (double) seqTime / (double) parTime + " ");
        }
    }
    public void testDataParSpeedupMulti() throws InterruptedException {
        MaxMinAvg mma = new MaxMinAvg();
        double[] resultSeq = new double[3];
        double[] resultPar = new double[3];
        ArrayList<Double> input = null;
        for (int i = 1; i <= 8; i++) {
            System.out.println("Thread class Test with Threadnum " + i);
            for (int j = 1; j < 7; j++) {
                input = generateArray((int) Math.pow(10, j));

                for (int l = 0; l < 100; l++) {
                    resultSeq = mma.maxMinAvgSeq(input);
                }
                long seqStartTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    resultSeq = mma.maxMinAvgSeq(input);
                }
                long seqEndTime = System.nanoTime();
                long parStartTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    resultPar = mma.maxMinAvgDataParallelThread(input, i);
                }
                long parEndTime = System.nanoTime();
                double expected = input.stream().mapToDouble(a -> a).average().getAsDouble();
                for (int k = 0; k < 3; k++) {
                    assertEquals((int) resultSeq[k], (int) resultPar[k]);
                }
                long seqTime = (seqEndTime - seqStartTime);
                long parTime = (parEndTime - parStartTime);
                System.out.println("data amount " + Math.pow(10, j) + " words " + "speedup " + (double) seqTime / (double) parTime + " ");
            }
        }
    }
    public void testTaskDataParSpeedup() throws InterruptedException {
        MaxMinAvg mma = new MaxMinAvg();
        double[] resultSeq =new double[3];
        double[] resultPar =new double[3];
        ArrayList<Double> input = null;
        for (int j=1; j<8;j++) {
            input = generateArray((int) Math.pow(10, j));

            for (int l = 0; l < 100; l++) {
                resultSeq = mma.maxMinAvgSeq(input);
            }
            long seqStartTime = System.nanoTime();
            for (int l = 0; l < 100; l++) {
                resultSeq = mma.maxMinAvgSeq(input);
            }
            long seqEndTime = System.nanoTime();
            long parStartTime = System.nanoTime();
            for (int l = 0; l < 100; l++) {
                resultPar = mma.maxMinAvgDataTaskParallelThread(input);
            }
            long parEndTime = System.nanoTime();
            for(int i =0; i<3; i++){
                assertEquals((int) resultSeq[i], (int) resultPar[i]);
            }
            long seqTime = (seqEndTime - seqStartTime);
            long parTime = (parEndTime - parStartTime);
            System.out.println("data amount " + Math.pow(10, j) + " words " + "speedup " + (double) seqTime / (double) parTime + " ");
        }
    }
}
