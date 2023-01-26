package exercises;

import java.util.ArrayList;

public class Main {
    public static void main(String[] arg) throws InterruptedException {
        ArrayList<String>input =new ArrayList<>();
        for(int i=0;i<2000000;i++){
            input.add("abcde");
        }
        Scrabble s =new Scrabble();
        long parStartTime = System.currentTimeMillis();
        int ayc=s.scrabbleForkJoin2Threads(input);
        //int ayc = s.scrabbleForkJoinMultiple(input,8);
        long parEndTime = System.currentTimeMillis();
        long seqStartTime = System.currentTimeMillis();
        int seq=s.calValSeq(input);
        long seqEndTime = System.currentTimeMillis();

        long seqTime = (seqEndTime - seqStartTime);
        long parTime = (parEndTime - parStartTime);
        System.out.println((double)seqTime/(double)parTime);
    }
}
