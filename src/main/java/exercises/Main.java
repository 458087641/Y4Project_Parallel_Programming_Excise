package exercises;

import java.util.ArrayList;

public class Main {
    public static void main(String[] arg){
        Scrabble s = new Scrabble();
        ArrayList <String> words = new ArrayList<String>();
        for (int i = 0; i<10; i++) {
            words.add("abc");
            words.add("abc");
        }
        int value1 = s.calValSeq(words);
        int value2 = s.numberOfStudentFailStream(words);
        int x =0 ;
    }
}
