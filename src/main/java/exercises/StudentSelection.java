package exercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Stream;

public class StudentSelection {

    public static int numberOfStudentFailSeq(Student[] studentArray){
        int count = 0;
        for (Student s : studentArray){
            if (s.getGrade() <40){
                count +=1;
            }
        }
        return count;
    }

    public static int numberOfStudentFailStream(Student[] studentArray){
        int count = Stream.of(studentArray).parallel().filter(s ->s.getGrade() <40).toArray().length;
        return count;
    }

    private static class numberOfStudentFailFork extends RecursiveAction {
        private final int startIndex;
        private final int endIndex;
        private final Student[] input;
        private int count;
        numberOfStudentFailFork(final int startIndex, final int endIndex, final Student[] studentArray) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.input = studentArray;
        }
        @Override
        protected void compute() {
            count = 0;
            for(int i = startIndex; i<endIndex; i++){
                if (input[i].getGrade()<40){
                    count +=1;
                }
            }
        }
        public int getCount() {
            return count;
        }
    }

    public static int numberOfStudentFailForkMultiple(Student[] studentArray, int threadsnum ){
        int result = 0;
        numberOfStudentFailFork[] threadsArray =new numberOfStudentFailFork[threadsnum];
        for(int i =0; i<threadsnum; i++){
            threadsArray[i]=new numberOfStudentFailFork(i*studentArray.length/threadsnum,(i+1)*studentArray.length/threadsnum,studentArray);
        }
        for (int i=0; i<threadsnum-1; i++){
            threadsArray[i].fork();
        }
        threadsArray[threadsnum-1].compute();

        for(int i=0; i<threadsnum-1; i++){
            threadsArray[i].join();
        }
        for(int i=0; i<threadsnum; i++){
            return result += threadsArray[i].getCount();
        }
        return result;
    }
    public static class studentSelectionThreadClass extends Thread{
        private int result;
        private final Student[]  input;

        public studentSelectionThreadClass(Student[] input) {
            this.result = 0;
            this.input =input;
        }
        public void run(){
            result=numberOfStudentFailSeq(input);
        }
        public int getResult(){
            return this.result;
        }
    }
    public static int scrabbleThread (Student[] students, int threadsnum) throws InterruptedException {
        int sum = 0;
        ArrayList<Student[]> chunks=splitChunks(Arrays.asList((students)),threadsnum);
        ArrayList<studentSelectionThreadClass> threadList=new ArrayList<studentSelectionThreadClass>();
        for (int i =0; i<chunks.size();i++){
            studentSelectionThreadClass thread = new studentSelectionThreadClass(chunks.get(i));
            threadList.add(thread);
            thread.start();
        }
        for (studentSelectionThreadClass t :threadList){
            t.join();
            int result = t.getResult();
            sum+=result;
        }
        return sum;
    }

    public static ArrayList<Student[]> splitChunks(List<Student> bigList, int n){
        ArrayList<Student[]> chunks = new ArrayList<Student[]>();

        for (int i = 0; i < bigList.size(); i += n) {
            Student[] chunk = (Student[])bigList.subList(i, Math.min(bigList.size(), i + n)).toArray();
            chunks.add(chunk);
        }
        return chunks;
    }
}
