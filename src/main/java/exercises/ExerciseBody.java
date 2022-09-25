package exercises;

import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;
import static edu.rice.pcdp.PCDP.async;
import static edu.rice.pcdp.PCDP.finish;


public class ExerciseBody {
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
    public static int calValPar(ArrayList<String> words){
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
    public static void main(String[] arg){
        ArrayList<String> words = new ArrayList<>();
        for (int i =0; i<10000000; i++){
            words.add("abc");
        }
        int result = calValSeq(words);
        result = calValPar(words);
    }
}
