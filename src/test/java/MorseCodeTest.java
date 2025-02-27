import exercises.MorseCode;
import junit.framework.TestCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import exercises.Helper.*;

import static exercises.Helper.generateString;

public class MorseCodeTest extends TestCase {


    public void testSeqCorrectness(){
        MorseCode translator = new MorseCode();
        String text = "abcdefghigklmnopqrstuvwxyz. 1234567890";
        String expectdedResult= ".- -... -.-. -.. . ..-. --. .... .. --. -.- .-.. -- -. --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. .-.-.- / .---- ..--- ...-- ....- ..... -.... --... ---.. ----. -----";
        String result =  translator.englishToMorse(text);
        assertEquals(expectdedResult,result);
    }
    public void test2ForkJoinCorrectness(){
        MorseCode translator = new MorseCode();
        String text = "abcdefghigklmnopqrstuvwxyz. 1234567890";
        String expectdedResult= ".- -... -.-. -.. . ..-. --. .... .. --. -.- .-.. -- -. --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. .-.-.- / .---- ..--- ...-- ....- ..... -.... --... ---.. ----. -----";
        String result =  translator.morseForkJoin2Threads(text);
        assertEquals(expectdedResult,result);
    }

    public void testMultiForkJoinCorrectness(){
        MorseCode translator = new MorseCode();
        String text = "abcdefghigklmnopqrstuvwxyz. 1234567890";
        System.out.println(text.length());
        String expectdedResult= ".- -... -.-. -.. . ..-. --. .... .. --. -.- .-.. -- -. --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. .-.-.- / .---- ..--- ...-- ....- ..... -.... --... ---.. ----. -----";
        for (int i =1; i<=8;i++) {
            String result = translator.morseForkJoinMultiple(text, i);
            assertEquals(expectdedResult, result);
        }
    }

    public void testMultiThreadCorrectness() throws InterruptedException {
        MorseCode translator = new MorseCode();
        String text = "abcdefghigklmnopqrstuvwxyz. 1234567890";
        System.out.println(text.length());
        String expectdedResult= ".- -... -.-. -.. . ..-. --. .... .. --. -.- .-.. -- -. --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.. .-.-.- / .---- ..--- ...-- ....- ..... -.... --... ---.. ----. -----";
        for (int i =1; i<=8;i++) {
            String result = translator.morseThread(text, i);
            assertEquals(expectdedResult, result);
        }
    }
    public void testSeqParEqual() throws IOException {
        MorseCode translator = new MorseCode();
        Path filePath = Path.of("./morse_test.txt");
        String content = Files.readString(filePath);
        String resultSeq=translator.englishToMoerseSeq(content);
        String resultPar=translator.morseForkJoin2Threads(content);
        //System.out.println(resultPar);
        assertEquals(resultSeq,resultPar);
    }

    public void testForkJoin2ThreadsSpeedUp() throws IOException {
        MorseCode translator = new MorseCode();
        Path filePath = Path.of("./morse_test.txt");
        String content = Files.readString(filePath);
        long seqStartTime = System.nanoTime();
        String resultSeq=translator.englishToMoerseSeq(content);
        long seqEndTime = System.nanoTime();
        long parStartTime = System.nanoTime();
        String resultPar=translator.morseForkJoin2Threads(content);
        long parEndTime = System.nanoTime();
        long seqTime = (seqEndTime - seqStartTime);
        long parTime = (parEndTime - parStartTime);
        System.out.println((double)seqTime/(double)parTime);
    }
    public void testForkJoin4ThreadsSpeedUp() throws IOException {
        MorseCode translator = new MorseCode();
        Path filePath = Path.of("./morse_test.txt");
        String content = Files.readString(filePath);
        long seqStartTime = System.nanoTime();
        String resultSeq=translator.englishToMoerseSeq(content);
        long seqEndTime = System.nanoTime();
        long parStartTime = System.nanoTime();
        String resultPar=translator.morseForkJoinMultiple(content,4                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           );
        long parEndTime = System.nanoTime();
        long seqTime = (seqEndTime - seqStartTime);
        long parTime = (parEndTime - parStartTime);
        System.out.println((double)seqTime/(double)parTime);
    }

    public void testThreadCorrectness() throws IOException, InterruptedException {
        MorseCode translator = new MorseCode();
        Path filePath = Path.of("./morse_test.txt");
        String content = Files.readString(filePath);
        for (int i =1; i<=16;i++) {
            String resultSeq = translator.englishToMoerseSeq(content);
            String resultPar = translator.morseThread(content, i);
            assertEquals(resultSeq,resultPar);
        }
        //System.out.println(resultPar);

    }

    public void testThread2ThreadsSpeedUp() throws IOException, InterruptedException {
        MorseCode translator = new MorseCode();
        Path filePath = Path.of("./morse_test.txt");
        String content = Files.readString(filePath);
        StringBuffer buffer = new StringBuffer();

        long seqStartTime = System.nanoTime();
        String resultSeq=translator.englishToMoerseSeq(content);
        long seqEndTime = System.nanoTime();
        long parStartTime = System.nanoTime();
        String resultPar=translator.morseThread(content,2);
        long parEndTime = System.nanoTime();
        long seqTime = (seqEndTime - seqStartTime);
        long parTime = (parEndTime - parStartTime);
        System.out.println((double)seqTime/(double)parTime);
    }
    public void testThread4ThreadsSpeedUp() throws IOException, InterruptedException {
        MorseCode translator = new MorseCode();
        Path filePath = Path.of("./morse_test.txt");
        String content = Files.readString(filePath);
        long parStartTime = System.nanoTime();
        String resultPar=translator.morseThread(content,4);
        long parEndTime = System.nanoTime();
        long seqStartTime = System.nanoTime();
        String resultSeq=translator.englishToMoerseSeq(content);
        long seqEndTime = System.nanoTime();


        long seqTime = (seqEndTime - seqStartTime);
        long parTime = (parEndTime - parStartTime);
        assertEquals(resultSeq,resultPar);
        System.out.println((double)seqTime);
        System.out.println((double)parTime);
        System.out.println((double)seqTime/(double)parTime);
    }

    public void testThreadSpeedUpData() throws IOException, InterruptedException {
        MorseCode translator = new MorseCode();
        String resultSeq =new String();
        String resultPar =new String();
        for(int i =1; i<=8; i++){
            StringBuffer buffer = new StringBuffer();
            System.out.println("Thread class Test with Threadnum " + i);
            for (int j=2; j<7;j++){
                for(int k=0;k<Math.pow(10,j);k++){
                    buffer.append(generateString());
                }
                String content =buffer.toString();
                for(int l=0; l<100;l++) {
                    resultSeq = translator.englishToMoerseSeq(content);
                }
                long seqStartTime = System.nanoTime();
                for(int l=0; l<100;l++) {
                    resultSeq = translator.englishToMoerseSeq(content);
                }
                long seqEndTime = System.nanoTime();
                long parStartTime = System.nanoTime();
                for(int l=0; l<100;l++) {
                    resultPar=translator.morseThread(content,i);
                }
                long parEndTime = System.nanoTime();
                assertEquals(resultSeq,resultPar);
                long seqTime = (seqEndTime - seqStartTime);
                long parTime = (parEndTime - parStartTime);
                System.out.println("data amount "+ Math.pow(10,j) +" words "+"speedup "+ (double)seqTime/(double)parTime +" ");
            }
        }
    }

    public void testThreadscaleData() throws IOException, InterruptedException {
        MorseCode translator = new MorseCode();
        String resultSeq =new String();
        String resultPar =new String();
        for(int i =1; i<=8; i++){
            StringBuffer buffer = new StringBuffer();
            System.out.println("Thread class Test with Threadnum " + i);
                for(int k=0;k<10000*i;k++){
                    buffer.append(generateString());
                }
                String content =buffer.toString();
                for(int l=0; l<100;l++) {
                    resultSeq = translator.englishToMoerseSeq(content);
                }
                long seqStartTime = System.nanoTime();
                for(int l=0; l<100;l++) {
                    resultSeq = translator.englishToMoerseSeq(content);
                }
                long seqEndTime = System.nanoTime();
                long parStartTime = System.nanoTime();
                for(int l=0; l<100;l++) {
                    resultPar=translator.morseThread(content,i);
                }
                long parEndTime = System.nanoTime();
                assertEquals(resultSeq,resultPar);
                long seqTime = (seqEndTime - seqStartTime);
                long parTime = (parEndTime - parStartTime);
                System.out.println("data amount "+ 10000*i +" words "+"speedup "+ (double)seqTime/(double)parTime +" "+parTime/1000000+ "ms");
            }
        }

    public void testThreadSpeedUpData1() throws IOException, InterruptedException {
        MorseCode translator = new MorseCode();
        String resultSeq =new String();
        String resultPar =new String();
        for(int i =1; i<=8; i++){
            StringBuffer buffer = new StringBuffer();
            System.out.println("Thread class Test with Threadnum " + i);
            for (int j=1; j<7;j++){
                for(int k=0;k<j*Math.pow(10,5);k++){
                    buffer.append(generateString());
                }
                String content =buffer.toString();
                long seqStartTime = System.nanoTime();
                for(int l=0; l<100;l++) {
                    resultSeq = translator.englishToMoerseSeq(content);
                }
                long seqEndTime = System.nanoTime();
                long parStartTime = System.nanoTime();
                for(int l=0; l<100;l++) {
                    resultPar=translator.morseThread(content,i);
                }
                long parEndTime = System.nanoTime();
                assertEquals(resultSeq,resultPar);
                long seqTime = (seqEndTime - seqStartTime);
                long parTime = (parEndTime - parStartTime);
                System.out.println("data amount "+ j*Math.pow(10,5) +" words "+"speedup "+ (double)seqTime/(double)parTime +" ");
            }
        }
    }
    public void testForkSpeedUpData() throws IOException {
        MorseCode translator = new MorseCode();
        String resultSeq =new String();
        String resultPar =new String();
        for(int i =1; i<=8; i++){
            StringBuffer buffer = new StringBuffer();
            System.out.println("ForkJoin Test with Threadnum " + i);
            for (int j=1; j<7;j++){
                for(int k=0;k<Math.pow(10,j);k++){
                    buffer.append(generateString());
                }
                String content =buffer.toString();

                for(int l=0; l<100;l++) {
                    resultSeq = translator.englishToMoerseSeq(content);
                }

                long seqStartTime = System.nanoTime();
                for(int l=0; l<100;l++) {
                    resultSeq = translator.englishToMoerseSeq(content);
                }
                long seqEndTime = System.nanoTime();
                long parStartTime = System.nanoTime();
                for(int l=0; l<100;l++) {
                    resultPar=translator.morseForkJoinMultiple(content,i);
                }
                long parEndTime = System.nanoTime();
                assertEquals(resultSeq,resultPar);
                long seqTime = (seqEndTime - seqStartTime);
                long parTime = (parEndTime - parStartTime);
                System.out.println("data amount "+ Math.pow(10,j) +" words "+"speedup "+ (double)seqTime/(double)parTime +" ");
            }
        }

    }
}
