import exercises.MorseCode;
import junit.framework.TestCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        String result =  translator.morseForkJoinMultiple(text,4);
        assertEquals(expectdedResult,result);
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
        String resultPar=translator.morseForkJoinMultiple(content,1);
        long parEndTime = System.nanoTime();
        long seqTime = (seqEndTime - seqStartTime);
        long parTime = (parEndTime - parStartTime);
        System.out.println((double)seqTime/(double)parTime);
    }

    public void testThreadCorrectness() throws IOException, InterruptedException {
        MorseCode translator = new MorseCode();
        Path filePath = Path.of("./morse_test.txt");
        String content = Files.readString(filePath);
        String resultSeq=translator.englishToMoerseSeq(content);
        String resultPar=translator.morseThread(content,2);
        //System.out.println(resultPar);
        assertEquals(resultSeq,resultPar);
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
        String resultPar=translator.morseThread(content,1);
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
        String resultPar=translator.morseThread(content,1);
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
            for (int j=1; j<8;j++){
                for(int k=0;k<Math.pow(10,j);k++){
                    buffer.append("abcde ");
                }
                String content =buffer.toString();

                long seqStartTime = System.nanoTime();
                for(int l=0; l<30;l++) {
                    resultSeq = translator.englishToMoerseSeq(content);
                }
                long seqEndTime = System.nanoTime();

                long parStartTime = System.nanoTime();

                for(int l=0; l<30;l++) {
                    resultPar=translator.morseThread(content,i);
                }
                long parEndTime = System.nanoTime();
                assertEquals(resultSeq,resultPar);
                long seqTime = (seqEndTime - seqStartTime);
                long parTime = (parEndTime - parStartTime);
                System.out.println("data amount "+ Math.pow(10,j) +"words "+"speedup "+ (double)seqTime/(double)parTime +" ");
            }
        }

    }
}
