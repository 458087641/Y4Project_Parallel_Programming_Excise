package exercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Stream;
import static exercises.Helper.getChunkEndExclusive;
import static exercises.Helper.getChunkStartInclusive;
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
        int sum1 = 0;
        int sum2 = 0;
        for (int i=0; i< words.size()/2;i++){
            sum1+=calStrValue(words.get(i));
        }
        for (int i = words.size()/2; i< words.size(); i++){
            sum2+=calStrValue(words.get(i));
        }

        return sum1+sum2;
    }

    public static int calValParAyc(ArrayList<String> words){
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
        return sum1+sum2;
    }

    public static class calValForkJoin extends RecursiveTask<Integer> {
        private final int startIndex;
        private final int endIndex;
        private final ArrayList<String> input;
        calValForkJoin(final int startIndex, final int endIndex, final ArrayList<String> input) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.input = input;
        }
        @Override
        protected Integer compute() {
            Integer result = 0;
            for(int i = startIndex; i<endIndex; i++){
                result = result+ calStrValue(input.get(i));
            }
            return result;
        }

    }

    public static int scrabbleForkJoin2Threads(ArrayList<String> words){

        calValForkJoin first =new calValForkJoin(0,words.size()/2,words);
        calValForkJoin second =new calValForkJoin(words.size()/2,words.size(),words);
        first.fork();
        Integer result1 = second.compute();
        Integer result2 =first.join();
        int result = result1+result2;

        return result;
    }

    public static int scrabbleForkJoinMultiple(ArrayList<String> words, int threadsnum ){
        Integer result = 0;
        calValForkJoin[] threadsArray =new calValForkJoin[threadsnum];
        for(int i =0; i<threadsnum; i++){
            threadsArray[i]=new calValForkJoin(getChunkStartInclusive(i, threadsnum,words.size()),getChunkEndExclusive(i, threadsnum,words.size()),words);
        }
        for (int i=0; i<threadsnum-1; i++){
            threadsArray[i].fork();
        }
        result+=threadsArray[threadsnum-1].compute();

        for(int i=0; i<threadsnum-1; i++){
            result+= threadsArray[i].join();
        }

        return result;
    }

    private static class scrabbleThreadClass extends Thread{
        private int result;
        private final ArrayList<String> input;
        private final int startIndex;
        private final int endIndex;
        public scrabbleThreadClass( int startIndex, int endIndex,ArrayList<String> input) {
            this.result = 0;
            this.input =input;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }
        public void run(){
            this.result=0;
            for(int i = startIndex; i<endIndex; i++){
                this.result = result+ calStrValue(input.get(i));
            }
        }
        public int getResult(){
            return this.result;
        }
    }
    public static int scrabbleThread (ArrayList<String> words, int threadsnum) throws InterruptedException {
        int sum = 0;
        ArrayList<scrabbleThreadClass> threadList=new ArrayList<scrabbleThreadClass>();
        for (int i =0; i<threadsnum;i++){
            scrabbleThreadClass thread = new scrabbleThreadClass(getChunkStartInclusive(i, threadsnum,words.size()),getChunkEndExclusive(i, threadsnum,words.size()),words);
            threadList.add(thread);
            thread.start();
        }

        for (scrabbleThreadClass t :threadList){
            t.join();
            int result = t.getResult();
            sum+=result;
        }
        return sum;
    }

    }

