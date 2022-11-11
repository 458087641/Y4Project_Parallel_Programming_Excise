package exercises;

import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Stream;

import static edu.rice.pcdp.PCDP.async;
import static edu.rice.pcdp.PCDP.finish;


public class Scrabble {
    static int sum1 = 0;
    static int sum2 = 0;
    /**
     *Scrabble game word value calculator.
     * @param word word that need its value.
     * @return value of the word.
     */
    private static int calStrValue(String word){
        int value=0;
        String mark1="AEILNORSTU";
        String mark2="DG";
        String mark3="BCMP";
        String mark4="FHVWY";
        String mark5="K";
        String mark8="JX";
        String mark10="QZ";
        for(char c:word.toUpperCase().toCharArray()){
            if (mark1.indexOf(c)!=-1) value+=1;
            if (mark2.indexOf(c)!=-1) value+=2;
            if (mark3.indexOf(c)!=-1) value+=3;
            if (mark4.indexOf(c)!=-1) value+=4;
            if (mark5.indexOf(c)!=-1) value+=5;
            if (mark8.indexOf(c)!=-1) value+=8;
            if (mark10.indexOf(c)!=-1) value+=10;
        }
        return value;
    }

    public static int calValSeq(ArrayList<String> words){
        long startTime = System.nanoTime();
        int sum1 = 0;
        int sum2 = 0;
        for (int i=0; i< words.size()/2;i++){
            sum1+=calStrValue(words.get(i));
        }
        for (int i = words.size()/2; i< words.size(); i++){
            sum2+=calStrValue(words.get(i));
        }
        long endTime = System.nanoTime();
        System.out.println("run time of sequential program is "+(endTime-startTime)+"; result is "+(sum1+sum2));
        return sum1+sum2;
    }

    public static int calValParAyc(ArrayList<String> words){
        long startTime = System.nanoTime();
        sum1 = 0;
        sum2 = 0;
        finish(()-> {
            async(() -> {
                for (int i = 0; i < words.size() / 2; i++) {
                    sum1 += calStrValue(words.get(i));
                }
            });
            for (int i = words.size() / 2; i < words.size(); i++) {
                sum2 += calStrValue(words.get(i));
            }
            });
        long endTime = System.nanoTime();
        System.out.println("run time of parallel program is "+(endTime-startTime)+"; result is "+(sum1+sum2));
        return sum1+sum2;
    }

    private static class calValForkJoin extends RecursiveAction {
        private final int startIndex;
        private final int endIndex;
        private final ArrayList<String> input;
        private int result;
        calValForkJoin(final int startIndex, final int endIndex, final ArrayList<String> input) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.input = input;
        }
        @Override
        protected void compute() {
            result = 0;
            for(int i = startIndex; i<endIndex; i++){
                result = result+ calStrValue(input.get(i));
            }
        }
        public int getResult() {
            return result;
        }
    }

    public static int calValForkJoin2Threads(ArrayList<String> words){
        calValForkJoin first =new calValForkJoin(0,words.size()/2,words);
        calValForkJoin second =new calValForkJoin(words.size()/2,words.size(),words);
        first.fork();
        second.compute();
        first.join();
        int result = first.getResult() + second.getResult();
        return result;
    }
    /**
     * TODO
     * */
    public static int calValForkJoinMultiple(ArrayList<String> words, int threadsnum ){
        int result = 0;
        calValForkJoin[] threadsArray =new calValForkJoin[threadsnum];
        for(int i =0; i<threadsnum; i++){
            threadsArray[i]=new calValForkJoin(i*words.size()/threadsnum,(i+1)*words.size()/threadsnum,words);
        }
        for (int i=0; i<threadsnum-1; i++){
            threadsArray[i].fork();
        }
        threadsArray[threadsnum-1].compute();

        for(int i=0; i<threadsnum-1; i++){
            threadsArray[i].join();
        }
        for(int i=0; i<threadsnum; i++){
            return result += threadsArray[i].getResult();
        }
        return result;
    }

    public static int numberOfStudentFailStream(ArrayList<String> words){
        int value = Stream.of(words).parallel().mapToInt(i -> calStrValue(String.valueOf(i))).sum();
        return value;
    }


    }
