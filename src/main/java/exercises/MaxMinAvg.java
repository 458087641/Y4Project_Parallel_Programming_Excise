package exercises;

import java.util.ArrayList;
import java.util.Arrays;

public class MaxMinAvg {
    enum ThreadFunction{
        Max,
        Min,
        Avg,
        All,
    }
    public static int findMax(ArrayList<Integer> inputArray){
        int max =Integer.MIN_VALUE;
        for (int i :inputArray){
            if (i> max){
                max =i;
            }
        }
        return  max;
    }
    public static int findMin(ArrayList<Integer> inputArray){
        int min = Integer.MAX_VALUE;
        for (int i :inputArray){
            if (i< min){
                min =i;
            }
        }
        return  min;
    }
    public static int findAvg(ArrayList<Integer> inputArray){
        int avg;
        int sum = 0;
        for(int i :inputArray){
            sum+=i;
        }
        avg= sum/inputArray.size();
        return avg;
    }
    public static int[] maxMinAvgSeq(ArrayList<Integer> input){
        int[] result=new int[3];
        result[0]=findMax(input);
        result[1]=findMin(input);
        result[2]=findAvg(input);
        return result;
    }
    private static class maxMinAvgParallelThreadClass extends Thread{
        private final ArrayList<Integer> inputArray;
        private ThreadFunction functionName;
        private int max =0;
        private int min =0;
        private int avg =0;
        private maxMinAvgParallelThreadClass(ArrayList<Integer> inputArray, ThreadFunction functionName) {
            this.inputArray = inputArray;
            this.functionName = functionName;
        }

        public void run(){
            if (functionName==ThreadFunction.All){
                max = findMax(inputArray);
                min = findMin(inputArray);
                avg = findAvg(inputArray);
            }
            if(functionName==ThreadFunction.Avg){
                avg= findAvg(inputArray);
            }
            if(functionName==ThreadFunction.Max){
                max= findMax(inputArray);
            }
            if(functionName==ThreadFunction.Min){
                min= findMin(inputArray);
            }
        }

        public int getAvg() {
            return avg;
        }

        public int getMax() {
            return max;
        }

        public int getMin() {
            return min;
        }
    }
    public static int[] maxMinAvgTaskParallelThread(ArrayList<Integer> input) throws InterruptedException {
        int[] result=new int[3];
        maxMinAvgParallelThreadClass threadMax = new maxMinAvgParallelThreadClass(input,ThreadFunction.Max);
        maxMinAvgParallelThreadClass threadMin = new maxMinAvgParallelThreadClass(input,ThreadFunction.Min);
        maxMinAvgParallelThreadClass threadAvg = new maxMinAvgParallelThreadClass(input,ThreadFunction.Avg);
        threadMax.join();
        threadMin.join();
        threadAvg.join();
        result[0]=threadMax.getMax();
        result[1]=threadMin.getMin();
        result[2]=threadAvg.getAvg();
        return result;
    }
    //for conparison to task parallel, the number of thread used in data parallel is hardcoded to 3
    public static int[] maxMinAvgDataParallelThread(ArrayList<Integer> input) throws InterruptedException {
        int[] result=new int[3];
        ArrayList<Integer> tempMax= new ArrayList<>();
        ArrayList<Integer> tempMin= new ArrayList<>();
        ArrayList<Integer> tempAvg= new ArrayList<>();
        ArrayList<maxMinAvgParallelThreadClass> threadList=new ArrayList<>();
        ArrayList<Integer[]> chunks= splitChunks(input,3);
        for(int i=0; i<3; i++){
            maxMinAvgParallelThreadClass thread = new maxMinAvgParallelThreadClass(new ArrayList<>(Arrays.asList(chunks.get(i))),ThreadFunction.All);
            threadList.add(thread);
            thread.start();
        }
        for(maxMinAvgParallelThreadClass t :threadList){
            t.join();
            tempMax.add(t.getMax());
            tempMin.add(t.getMin());
            tempAvg.add((t.getAvg()));
        }
        result[0]=findMax(tempMax);
        result[1]=findMin(tempMin);
        result[2]=findAvg(tempAvg);
        return result;
    }
    private static class dataTaskParallelThreadClass extends Thread{
        private final ArrayList<Integer> input;
        private int[] result;

        private dataTaskParallelThreadClass(ArrayList<Integer> input) {
            this.input = input;
        }
        public void run(){
            try {
                result=maxMinAvgTaskParallelThread(input);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public int[] getResult() {
            return result;
        }
    }
    public int[] maxMinAvgDataTaskParallelThread(ArrayList<Integer> input) throws InterruptedException {
        ArrayList<int[]> resultList= new ArrayList<>();
        int[] result=new int[3];
        ArrayList<Integer[]> chunks= splitChunks(input,3);
        ArrayList<dataTaskParallelThreadClass> threadList=new ArrayList<>();
        ArrayList<Integer> tempMax= new ArrayList<>();
        ArrayList<Integer> tempMin= new ArrayList<>();
        ArrayList<Integer> tempAvg= new ArrayList<>();
        for(int i=0; i<3;i++){
            dataTaskParallelThreadClass thread = new dataTaskParallelThreadClass(new ArrayList<>(Arrays.asList(chunks.get(i))));
            threadList.add(thread);
            thread.start();
        }
        for(dataTaskParallelThreadClass t:threadList){
            t.join();
            resultList.add(t.getResult());
        }
        for(int[] i :resultList){
            tempMax.add(i[0]);
            tempMin.add(i[1]);
            tempAvg.add(i[2]);
        }
        result[0]=findMax(tempMax);
        result[1]=findMin(tempMin);
        result[2]=findAvg(tempAvg);
        return result;
    }

    public static ArrayList<Integer[]> splitChunks(ArrayList<Integer> bigList, int n){
        ArrayList<Integer[]> chunks = new ArrayList<>();

        for (int i = 0; i < bigList.size(); i += n) {
            Integer[] chunk = (Integer[])bigList.subList(i, Math.min(bigList.size(), i + n)).toArray();
            chunks.add(chunk);
        }
        return chunks;
    }
}
