package exercises;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.RecursiveAction;
import static exercises.Helper.getChunkEndExclusive;
import static exercises.Helper.getChunkStartInclusive;

public class MorseCode {
    private static HashMap<String, String> morseDic = new HashMap<>();
    {
        // "/" will indicate end of a word
//alphabets
        morseDic.put("A", ".-");
        morseDic.put("B", "-...");
        morseDic.put("C", "-.-.");
        morseDic.put("D", "-..");
        morseDic.put("E", ".");
        morseDic.put("F", "..-.");
        morseDic.put("G", "--.");
        morseDic.put("H", "....");
        morseDic.put("I", "..");
        morseDic.put("J", ".---");
        morseDic.put("K", "-.-");
        morseDic.put("L", ".-..");
        morseDic.put("M", "--");
        morseDic.put("N", "-.");
        morseDic.put("O", "---");
        morseDic.put("P", ".--.");
        morseDic.put("Q", "--.-");
        morseDic.put("R", ".-.");
        morseDic.put("S", "...");
        morseDic.put("T", "-");
        morseDic.put("U", "..-");
        morseDic.put("V", "...-");
        morseDic.put("W", ".--");
        morseDic.put("X", "-..-");
        morseDic.put("Y", "-.--");
        morseDic.put("Z", "--..");
// numeric numbers
        morseDic.put("0", "-----");
        morseDic.put("1", ".----");
        morseDic.put("2", "..---");
        morseDic.put("3", "...--");
        morseDic.put("4", "....-");
        morseDic.put("5", ".....");
        morseDic.put("6", "-....");
        morseDic.put("7", "--...");
        morseDic.put("8", "---..");
        morseDic.put("9", "----.");
//notations
        morseDic.put(".", ".-.-.-");
        morseDic.put(",", "--..--");
        morseDic.put("?", "..--..");
        morseDic.put(":", "---...");
        morseDic.put("-", "-....-");
        morseDic.put("@", ".--.-.");
        morseDic.put("\n","\n");
        morseDic.put(" ","/");
    }

    public static String englishToMorse(String text){
        StringBuffer buffer = new StringBuffer();

        //String[] splitedText= text.split(" ");
        //for(String i :splitedText){
            for(char c : text.toCharArray()){
                if(morseDic.get(String.valueOf(c).toUpperCase())==null){
                    System.out.println(c);
                }
                buffer.append(morseDic.get(String.valueOf(c).toUpperCase()));
                buffer.append(" ");
            }
            //if(splitedText[splitedText.length-1] !=i){
                //buffer.append("/ ");
            //}
        //}
        buffer.deleteCharAt(buffer.length() -1);
        return buffer.toString();
    }

    public static String englishToMoerseSeq(String content) throws IOException {
        String result =  englishToMorse(content);
        return result;
    }

    private static class morseForkJoin extends RecursiveAction{
        private final int startIndex;
        private final int endIndex;
        private final String input;
        public String result;
        private morseForkJoin(int startIndex, int endIndex, String input) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.input = input;
        }
        @Override
        protected void compute() {
            this.result =englishToMorse(input.substring(startIndex,endIndex));

        }
    }
    public static String morseForkJoin2Threads(String input){
        morseForkJoin first =new morseForkJoin(0,input.length()/2,input);
        morseForkJoin second =new morseForkJoin(input.length()/2,input.length(),input);
        first.fork();
        second.compute();
        first.join();
        String result = first.result +" "+second.result;

        return result;
    }

    public static String morseForkJoinMultiple(String input, int threadsnum ){
        String result = "";
        morseForkJoin[] threadsArray =new morseForkJoin[threadsnum];
        for(int i =0; i<threadsnum; i++){
            threadsArray[i]=new morseForkJoin(getChunkStartInclusive(i, threadsnum,input.length()),getChunkEndExclusive(i, threadsnum,input.length()),input);
        }
        for (int i=0; i<threadsnum-1; i++){
            threadsArray[i].fork();
        }
        threadsArray[threadsnum-1].compute();
        for(int i=0; i<threadsnum-1; i++){
            threadsArray[i].join();
        }
        for (int i = 0; i<threadsnum-1; i++){
            result=result+threadsArray[i].result+" ";
        }
        result+=threadsArray[threadsnum-1].result;
        return result;
    }

    private static class morseThreadClass extends Thread{
        private final int startIndex;
        private final int endIndex;
        private String result;
        private final String input;

        public morseThreadClass(String input, int startIndex, int endIndex) {
            this.input =input;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }
        public void run(){
            this.result =englishToMorse(input.substring(startIndex,endIndex));
            //String result =  englishToMorse(input);
        }
        public String getResult(){
            return this.result;
        }
    }
    public static String morseThread (String input, int threadsnum) throws InterruptedException {
        String output="";
        ArrayList<morseThreadClass> threadList=new ArrayList<morseThreadClass>();
        for (int i =0; i<threadsnum;i++){
            morseThreadClass thread = new morseThreadClass(input,getChunkStartInclusive(i, threadsnum,input.length()),getChunkEndExclusive(i, threadsnum,input.length()));
            threadList.add(thread);
            thread.start();
        }

        for (morseThreadClass t :threadList){
            t.join();
            String result = t.getResult();
            output=output + result + " ";
        }
        output= output.substring(0,output.length()-1);
        return output;
    }



}
