package exercises;

import java.util.*;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static exercises.Helper.getChunkEndExclusive;
import static exercises.Helper.getChunkStartInclusive;
public class StudentSelection {


    public static String mostCommonFirstNameSeq(Student[] studentArray){
        Map<String, Integer> nameNums = new HashMap<String, Integer>();

        for (Student s : studentArray) {
            if (nameNums.containsKey(s.getFirstName())) {
                nameNums.put(s.getFirstName(),
                        nameNums.get(s.getFirstName()) + 1);
            } else {
                nameNums.put(s.getFirstName(), 1);
            }
        }

        String mostCommon = null;
        int mostCommonCount = -1;
        for (Map.Entry<String, Integer> entry : nameNums.entrySet()) {
            if (mostCommon == null || entry.getValue() > mostCommonCount) {
                mostCommon = entry.getKey();
                mostCommonCount = entry.getValue();
            }
        }

        return mostCommon;
    }
    public String mostCommonFirstNameOfStudentsParallelStream(
            final Student[] studentArray) {

        String name = Stream.of(studentArray).parallel().collect(Collectors.groupingBy(Student::getFirstName)).values().stream()
                .sorted((n1, n2)->n2.size()-n1.size()).collect(Collectors.toList()).get(0).get(0).getFirstName();
        return name;
    }



    private static class mostCommonFirstNameFork extends RecursiveAction {
        private final int startIndex;
        private final int endIndex;
        private final Student[] input;
        public Map<String, Integer> nameNums;

        mostCommonFirstNameFork(final int startIndex, final int endIndex, final Student[] input) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.input = input;
        }
        @Override
        protected void compute() {
            Map<String, Integer> nameNums = new HashMap<String, Integer>();

            for (int i = startIndex; i < endIndex; i++) {

                Student s = this.input[i];
                if (nameNums.containsKey(s.getFirstName())) {
                    nameNums.put(s.getFirstName(),
                            nameNums.get(s.getFirstName()) + 1);
                } else {
                    nameNums.put(s.getFirstName(), 1);
                }
            }
            this.nameNums=nameNums;
        }
    }

    public static String mostCommonFirstNameForkMultiple(Student[] studentArray, int threadsnum ){
        mostCommonFirstNameFork[] threadsArray =new mostCommonFirstNameFork[threadsnum];
        for(int i =0; i<threadsnum; i++){
            threadsArray[i]=new mostCommonFirstNameFork(getChunkStartInclusive(i, threadsnum,studentArray.length),getChunkEndExclusive(i, threadsnum,studentArray.length),studentArray);
        }
        for (int i=0; i<threadsnum-1; i++){
            threadsArray[i].fork();
        }
        threadsArray[threadsnum-1].compute();

        for(int i=0; i<threadsnum-1; i++){
            threadsArray[i].join();
        }
        String mostCommon = null;
        int mostCommonCount = -1;
        Map<String, Integer> nameNums = new HashMap<String, Integer>();
        for(mostCommonFirstNameFork s : threadsArray){
            Map<String, Integer> threadNameNums =s.nameNums;
            for(String key : threadNameNums.keySet()){
                if (nameNums.containsKey(key)) {
                    nameNums.put(key,
                            nameNums.get(key) + threadNameNums.get(key));
                }
                else {
                    nameNums.put(key, threadNameNums.get(key));
                }
            }
        }
        for (Map.Entry<String, Integer> entry : nameNums.entrySet()) {
            if (mostCommon == null || entry.getValue() > mostCommonCount) {
                mostCommon = entry.getKey();
                mostCommonCount = entry.getValue();
            }
        }
        return mostCommon;
    }
    public static class studentSelectionThreadClass extends Thread{
        private final int startIndex;
        private final int endIndex;
        private final Student[] input;
        public Map<String, Integer> nameNums;


        public studentSelectionThreadClass(final int startIndex, final int endIndex,Student[] input) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.input = input;
        }
        public void run() {
            Map<String, Integer> nameNums = new HashMap<String, Integer>();

            for (int i = startIndex; i < endIndex; i++) {

                Student s = this.input[i];
                if (nameNums.containsKey(s.getFirstName())) {
                    nameNums.put(s.getFirstName(),
                            nameNums.get(s.getFirstName()) + 1);
                } else {
                    nameNums.put(s.getFirstName(), 1);
                }
            }
            this.nameNums = nameNums;
        }
    }
    public static String studentSelectionThread (Student[] studentArray, int threadsnum) throws InterruptedException {
        studentSelectionThreadClass[] threadArray =new studentSelectionThreadClass[threadsnum];
        for (int i =0; i<threadsnum;i++){
            studentSelectionThreadClass thread = new studentSelectionThreadClass(getChunkStartInclusive(i, threadsnum,studentArray.length),getChunkEndExclusive(i, threadsnum,studentArray.length),studentArray);
            threadArray[i]=(thread);
            thread.start();
        }
        String mostCommon = null;
        int mostCommonCount = -1;
        Map<String, Integer> nameNums = new HashMap<String, Integer>();

        for (studentSelectionThreadClass t :threadArray){
            t.join();
            Map<String, Integer> threadNameNums =t.nameNums;
            for(String key : threadNameNums.keySet()){
                if (nameNums.containsKey(key)) {
                nameNums.put(key,
                        nameNums.get(key) + threadNameNums.get(key));
                }
                else {
                nameNums.put(key, threadNameNums.get(key));
                }
            }
        }
        for (Map.Entry<String, Integer> entry : nameNums.entrySet()) {
            if (mostCommon == null || entry.getValue() > mostCommonCount) {
                mostCommon = entry.getKey();
                mostCommonCount = entry.getValue();
            }
        }
        return mostCommon;
    }
}

