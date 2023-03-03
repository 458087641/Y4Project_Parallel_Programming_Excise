package exercises;

import java.util.ArrayList;

import static exercises.Helper.getChunkEndExclusive;
import static exercises.Helper.getChunkStartInclusive;
public class MaxMinSum {
    enum ThreadFunction{
        Max,
        Min,
        Sum,
        All,
    }
    public static double findMax(ArrayList<Double> inputArray){
        double max =Integer.MIN_VALUE;
        for (double i :inputArray){
            if (i> max){
                max =i;
            }
        }
        return  max;
    }
    public static double findMin(ArrayList<Double> inputArray){
        double min = Integer.MAX_VALUE;
        for (double i :inputArray){
            if (i< min){
                min =i;
            }
        }
        return  min;
    }
    public static double findSum(ArrayList<Double> inputArray){
        Double sum = 0.0;
        for(Double i :inputArray){
            sum+=i;
        }

        return sum;
    }
    public static double[] maxMinSumSeq(ArrayList<Double> input){
        double[] result=new double[3];
        result[0]=findMax(input);
        result[1]=findMin(input);
        result[2]=  findSum(input);
        return result;
    }
    private static class maxMinSumParallelThreadClass extends Thread{
        private final ArrayList<Double> inputArray;
        private final ThreadFunction functionName;
        private final int startIndex;
        private final int endIndex;
        private double max =0;
        private double min =0;
        private double sum =0;
        private maxMinSumParallelThreadClass(ArrayList<Double> inputArray, ThreadFunction functionName, int startIndex, int endIndex) {
            this.inputArray = inputArray;
            this.functionName = functionName;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public void run(){
            ArrayList<Double> tempArray = new ArrayList<>();
            for(int i = startIndex; i<endIndex; i++){
                tempArray.add(inputArray.get(i));
            }
            if (functionName==ThreadFunction.All){

                max = findMax(tempArray);
                min = findMin(tempArray);
                sum = findSum(tempArray);
            }
            if(functionName==ThreadFunction.Sum){
                sum= findSum(tempArray);
            }
            if(functionName==ThreadFunction.Max){
                max= findMax(tempArray);
            }
            if(functionName==ThreadFunction.Min){
                min= findMin(tempArray);
            }
        }

        public double getSum() {
            return sum;
        }

        public double getMax() {
            return max;
        }

        public double getMin() {
            return min;
        }
    }
    public static double[] maxMinSumTaskParallelThread(ArrayList<Double> input) throws InterruptedException {
        double[] result=new double[3];
        maxMinSumParallelThreadClass threadMax = new maxMinSumParallelThreadClass(input,ThreadFunction.Max, 0, input.size());
        maxMinSumParallelThreadClass threadMin = new maxMinSumParallelThreadClass(input,ThreadFunction.Min, 0, input.size());
        maxMinSumParallelThreadClass threadAvg = new maxMinSumParallelThreadClass(input,ThreadFunction.Sum, 0, input.size());
        threadMax.start();
        threadMin.start();
        threadAvg.start();
        threadMax.join();
        threadMin.join();
        threadAvg.join();
        result[0]=threadMax.getMax();
        result[1]=threadMin.getMin();
        result[2]= threadAvg.getSum();
        return result;
    }
    //for conparison to task parallel, the number of thread used in data parallel is hardcoded to 3
    public static double[] maxMinSumDataParallelThread(ArrayList<Double> input, int threadnum) throws InterruptedException {
        double[] result=new double[3];
        ArrayList<Double> tempMax= new ArrayList<>();
        ArrayList<Double> tempMin= new ArrayList<>();
        ArrayList<Double> tempSum= new ArrayList<>();
        ArrayList<maxMinSumParallelThreadClass> threadList=new ArrayList<>();
        for(int i=0; i<threadnum; i++){
            maxMinSumParallelThreadClass thread = new maxMinSumParallelThreadClass(input,ThreadFunction.All, getChunkStartInclusive(i,threadnum,input.size()), getChunkEndExclusive(i, threadnum,input.size()));
            threadList.add(thread);
            thread.start();
        }
        double avg =0.0;
        for(maxMinSumParallelThreadClass t :threadList){
            t.join();
            tempMax.add(t.getMax());
            tempMin.add(t.getMin());
            tempSum.add(t.getSum());
        }
        result[0]=findMax(tempMax);
        result[1]=findMin(tempMin);
        result[2]=findSum(tempSum);
        return result;
    }
    private static class dataTaskParallelThreadClass extends Thread{
        private final ArrayList<Double> input;
        private double[] result;
        private final int startIndex;
        private final int endIndex;
        private dataTaskParallelThreadClass(ArrayList<Double> input, int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.input = input;
        }
        public void run(){
            try {
                ArrayList<Double> tempArray = new ArrayList<>();
                for(int i = startIndex; i<endIndex; i++) {
                    tempArray.add(input.get(i));
                }
                result = maxMinSumTaskParallelThread(tempArray);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public double[] getResult() {
            return result;
        }
    }
    public double[] maxMinSumDataTaskParallelThread(ArrayList<Double> input) throws InterruptedException {
        ArrayList<double[]> resultList= new ArrayList<>();
        double[] result=new double[3];
        ArrayList<dataTaskParallelThreadClass> threadList=new ArrayList<>();
        ArrayList<Double> tempMax= new ArrayList<>();
        ArrayList<Double> tempMin= new ArrayList<>();
        ArrayList<Double> tempSum= new ArrayList<>();
        for(int i=0; i<3;i++){
            dataTaskParallelThreadClass thread = new dataTaskParallelThreadClass(input,getChunkStartInclusive(i,3,input.size()), getChunkEndExclusive(i, 3,input.size()));
            threadList.add(thread);
            thread.start();
        }
        for(dataTaskParallelThreadClass t:threadList){
            t.join();
            resultList.add(t.getResult());
        }
        for(double[] i :resultList){
            tempMax.add(i[0]);
            tempMin.add(i[1]);
            tempSum.add(i[2]);
        }
        result[0]=findMax(tempMax);
        result[1]=findMin(tempMin);
        result[2]=findSum(tempSum);
        return result;
    }


}
