import exercises.MorseCode;
import exercises.Scrabble;

import java.io.IOException;
import java.util.ArrayList;
import junit.framework.TestCase;

import static exercises.Helper.generateString;

public class ScrabbleTest extends TestCase {
    public void testSeqCorrectness() {
        Scrabble s =new Scrabble();
        ArrayList<String> input =new ArrayList<>();
        input.add("abcdefghijklmnopqrstuvwxyz");
        input.add("abcdefghijklmnopqrstuvwxyz");
        int expectResult = (10+4+12+20+5+16+20)*2;
        int result =s.calValSeq(input);
        assertEquals(expectResult,result);
    }
    public void testAsycCorrectness() {
        Scrabble s =new Scrabble();
        ArrayList<String> input =new ArrayList<>();
        input.add("abcdefghijklmnopqrstuvwxyz");
        input.add("abcdefghijklmnopqrstuvwxyz");
        int expectResult = (10+4+12+20+5+16+20)*2;
        int result =s.calValParAyc(input);
        assertEquals(expectResult,result);
    }
    public void testFork2Correctness() {
        Scrabble s =new Scrabble();
        ArrayList<String> input =new ArrayList<>();
        input.add("abcdefghijklmnopqrstuvwxyz");
        input.add("abcdefghijklmnopqrstuvwxyz");
        int expectResult = (10+4+12+20+5+16+20)*2;
        int result =s.scrabbleForkJoin2Threads(input);
        assertEquals(expectResult,result);
    }
    public void testForkMultiCorrectness() {
        Scrabble s =new Scrabble();
        ArrayList<String> input =new ArrayList<>();
        input.add("abcdefghijklmnopqrstuvwxyz");
        input.add("abcdefghijklmnopqrstuvwxyz");
        int expectResult = (10+4+12+20+5+16+20)*2;
        int result =s.scrabbleForkJoinMultiple(input,4);
        assertEquals(expectResult,result);
    }

    public void testThreadCorrectness() throws InterruptedException {
        Scrabble s =new Scrabble();
        ArrayList<String> input =new ArrayList<>();
        input.add("abcdefghijklmnopqrstuvwxyz");
        input.add("abcdefghijklmnopqrstuvwxyz");
        int expectResult = (10+4+12+20+5+16+20)*2;
        int result =s.scrabbleThread(input,1);
        assertEquals(expectResult,result);
    }

    public void testThreadSpeedUpData() throws IOException, InterruptedException {
        Scrabble s =new Scrabble();
        int resultSeq =0;
        int resultPar =0;
        for(int i =1; i<=8; i++){
            ArrayList<String> input =new ArrayList<>();
            System.out.println("Thread class Test with Threadnum " + i);
            for (int j=1; j<7;j++){
                for(int k=0;k<Math.pow(10,j);k++){
                    input.add(generateString());
                }
                for(int l=0; l<100;l++) {
                    resultSeq = s.calValSeq(input);
                }
                long seqStartTime = System.nanoTime();
                for(int l=0; l<100;l++) {
                    resultSeq = s.calValSeq(input);
                }
                long seqEndTime = System.nanoTime();
                long parStartTime = System.nanoTime();
                for(int l=0; l<100;l++) {
                    resultPar=s.scrabbleThread(input,i);
                }
                long parEndTime = System.nanoTime();
                assertEquals(resultSeq,resultPar);
                long seqTime = (seqEndTime - seqStartTime);
                long parTime = (parEndTime - parStartTime);
                System.out.println("data amount "+ Math.pow(10,j) +" words "+"speedup "+ (double)seqTime/(double)parTime +" ");
            }
        }

    }
    public void testThreadScaleData() throws IOException, InterruptedException {
        Scrabble s =new Scrabble();
        int resultSeq =0;
        int resultPar =0;
            for (int j=1; j<9;j++){
                ArrayList<String> input =new ArrayList<>();
                for(int k=0;k<j*10000;k++){
                    input.add(generateString());
                }
                for(int l=0; l<100;l++) {
                    resultSeq = s.calValSeq(input);
                }
                long seqStartTime = System.nanoTime();
                for(int l=0; l<100;l++) {
                    resultSeq = s.calValSeq(input);
                }
                long seqEndTime = System.nanoTime();
                long parStartTime = System.nanoTime();
                for(int l=0; l<100;l++) {
                    resultPar=s.scrabbleThread(input,j);
                }
                long parEndTime = System.nanoTime();
                assertEquals(resultSeq,resultPar);
                long seqTime = (seqEndTime - seqStartTime);
                long parTime = (parEndTime - parStartTime);
                System.out.println("data amount "+ j*10000 +" words "+"speedup "+ (double)seqTime/(double)parTime +" "+parTime/1000000+ "ms");
            }
        }


    public void testForkJoinSpeedUpData() throws IOException, InterruptedException {
        Scrabble s =new Scrabble();
        int resultSeq =0;
        int resultPar =0;
        for(int i =1; i<=8; i++) {
            ArrayList<String> input = new ArrayList<>();
            System.out.println("ForkJoin class Test with Threadnum " + i);
            for (int j = 1; j < 7; j++) {
                for (int k = 0; k < Math.pow(10, j); k++) {
                    input.add(generateString());
                }
                for (int l = 0; l < 100; l++) {
                    resultSeq = s.calValSeq(input);
                }
                long seqStartTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    resultSeq = s.calValSeq(input);
                }
                long seqEndTime = System.nanoTime();
                long parStartTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    resultPar = s.scrabbleForkJoinMultiple(input, i);
                }
                long parEndTime = System.nanoTime();
                assertEquals(resultSeq, resultPar);
                long seqTime = (seqEndTime - seqStartTime);
                long parTime = (parEndTime - parStartTime);
                System.out.println("data amount " + Math.pow(10, j) + " words " + "speedup " + (double) seqTime / (double) parTime + " ");
            }
        }
    }


    public void testAsycSpeedup(){
        Scrabble s =new Scrabble();
        int resultSeq =0;
        int resultPar =0;
        ArrayList<String> input =new ArrayList<>();
        for (int j=1; j<7;j++) {
            for (int k = 0; k < Math.pow(10, j); k++) {
                input.add(generateString());
            }
            for(int l=0; l<100;l++) {
                resultSeq = s.calValSeq(input);
            }
            long seqStartTime = System.nanoTime();
            for(int l=0; l<100;l++) {
                resultSeq = s.calValSeq(input);
            }
            long seqEndTime = System.nanoTime();
            long parStartTime = System.nanoTime();
            for(int l=0; l<100;l++) {
                resultPar=s.calValParAyc(input);
            }
            long parEndTime = System.nanoTime();
            assertEquals(resultSeq,resultPar);
            long seqTime = (seqEndTime - seqStartTime);
            long parTime = (parEndTime - parStartTime);
            System.out.println("data amount "+ Math.pow(10,j) +" words "+"speedup "+ (double)seqTime/(double)parTime +" ");
        }
    }
}
