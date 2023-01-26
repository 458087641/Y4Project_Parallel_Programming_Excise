import exercises.Scrabble;

import java.util.ArrayList;
import junit.framework.TestCase;
public class ScrabbleTest extends TestCase {
    public void testSeqCorrectness() {
        Scrabble s =new Scrabble();
        ArrayList<String> input =new ArrayList<>();
        input.add("abcdefghijklmnopqrstuvwxyz");
        int expectResult = 10+4+12+20+5+16+20;
        int result =s.calValSeq(input);
        assertEquals(expectResult,result);
    }
    public void testAsycCorrectness() {
        Scrabble s =new Scrabble();
        ArrayList<String> input =new ArrayList<>();
        input.add("abcdefghijklmnopqrstuvwxyz");
        int expectResult = 10+4+12+20+5+16+20;
        int result =s.calValParAyc(input);
        assertEquals(expectResult,result);
    }
    public void testFork2Correctness() {
        Scrabble s =new Scrabble();
        ArrayList<String> input =new ArrayList<>();
        input.add("abcdefghijklmnopqrstuvwxyz");
        int expectResult = 10+4+12+20+5+16+20;
        int result =s.scrabbleForkJoin2Threads(input);
        assertEquals(expectResult,result);
    }
    public void testForkMultiCorrectness() {
        Scrabble s =new Scrabble();
        ArrayList<String> input =new ArrayList<>();
        input.add("abcdefghijklmnopqrstuvwxyz");
        int expectResult = 10+4+12+20+5+16+20;
        int result =s.scrabbleForkJoinMultiple(input,4);
        assertEquals(expectResult,result);
    }
    public void testStreamCorrectness() {
        Scrabble s =new Scrabble();
        ArrayList<String> input =new ArrayList<>();
        input.add("abcdefghijklmnopqrstuvwxyz");
        int expectResult = 10+4+12+20+5+16+20;
        int result =s.scrabbleStream(input);
        assertEquals(expectResult,result);
    }
}
