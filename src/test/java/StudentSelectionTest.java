import exercises.StudentInfo;

import java.io.IOException;
import java.util.Random;

import exercises.StudentSelection;
import junit.framework.TestCase;
public class StudentSelectionTest extends TestCase{
    private StudentInfo[] generateStudentData(int size) {
        String[] firstNames = {"Sue", "Ming", "John", "Percy", "Shams", "Max","Mahito"};
        String[] lastNames = {"Honda", "Zhang", "Smith", "Mask", "Goh", "Gates","Wang"};



        StudentInfo[] studentInfos = new StudentInfo[size];
        Random r = new Random(123);

        for (int s = 0; s < size; s++) {
            final String firstName = firstNames[r.nextInt(firstNames.length)];
            final String lastName = lastNames[r.nextInt(lastNames.length)];
            final double age = r.nextDouble() * 100.0;
            final int grade = 1 + r.nextInt(100);
            studentInfos[s] = new StudentInfo(firstName, lastName, age, grade);
        }

        return studentInfos;
    }

    public void testmostCommonFirstNameSeqCorrectness() {
        String[] firstNames = {"Sue", "Ming", "John", "Percy", "Shams", "Max","Mahito"};
        String[] lastNames = {"Honda", "Zhang", "Smith", "Mask", "Goh", "Gates","Wang"};
        StudentInfo[] studentInfos = new StudentInfo[8];
        for (int i=0; i<firstNames.length;i++){
            studentInfos[i]=new StudentInfo(firstNames[i],lastNames[i],20,100);
        }
        studentInfos[7]=new StudentInfo("Ming","Wang",20,100);
        StudentSelection s =new StudentSelection();
        String result = s.mostCommonFirstNameSeq(studentInfos);
        assertEquals("Ming",result);
    }

    public void testMostCommonFirstNameOfStudentsParallelStreamCorrectness() {
        String[] firstNames = {"Sue", "Ming", "John", "Percy", "Shams", "Max","Mahito"};
        String[] lastNames = {"Honda", "Zhang", "Smith", "Mask", "Goh", "Gates","Wang"};
        StudentInfo[] studentInfos = new StudentInfo[8];
        for (int i=0; i<firstNames.length;i++){
            studentInfos[i]=new StudentInfo(firstNames[i],lastNames[i],20,100);
        }
        studentInfos[7]=new StudentInfo("Ming","Wang",20,100);
        StudentSelection s =new StudentSelection();
        String result = s.mostCommonFirstNameOfStudentsParallelStream(studentInfos);
        assertEquals("Ming",result);
    }
    public void testMostCommonFirstNameForkMultiple() {
        String[] firstNames = {"Sue", "Ming", "John", "Percy", "Shams", "Max","Mahito"};
        String[] lastNames = {"Honda", "Zhang", "Smith", "Mask", "Goh", "Gates","Wang"};
        StudentInfo[] studentInfos = new StudentInfo[8];
        for (int i=0; i<firstNames.length;i++){
            studentInfos[i]=new StudentInfo(firstNames[i],lastNames[i],20,100);
        }
        studentInfos[7]=new StudentInfo("Ming","Wang",20,100);
        StudentSelection s =new StudentSelection();
        for(int i =1; i<8;i++) {
            String result = s.mostCommonFirstNameForkMultiple(studentInfos, i);
            assertEquals("Ming", result);
        }
    }

    public void testStudentSelectionThreadCorrectness() throws InterruptedException {
        String[] firstNames = {"Sue", "Ming", "John", "Percy", "Shams", "Max","Mahito"};
        String[] lastNames = {"Honda", "Zhang", "Smith", "Mask", "Goh", "Gates","Wang"};
        StudentInfo[] studentInfos = new StudentInfo[8];
        StudentSelection s =new StudentSelection();
        for (int i=0; i<firstNames.length;i++){
            studentInfos[i]=new StudentInfo(firstNames[i],lastNames[i],20,100);
        }
        studentInfos[7]=new StudentInfo("Ming","Wang",20,100);

        for(int i =1; i<8;i++) {
            String result = s.studentSelectionThread(studentInfos, i);
            assertEquals("Ming", result);
        }
    }

    public void testSeqParThreadEqual() throws IOException, InterruptedException {
        StudentInfo[] studentInfos =generateStudentData(100000);
        StudentSelection s =new StudentSelection();
        String resultSeq = s.mostCommonFirstNameSeq(studentInfos);
        for(int i =1; i<8;i++) {
            String resultPar = s.studentSelectionThread(studentInfos, i);
            assertEquals(resultSeq, resultPar);
        }

    }
    public void testSeqParForkEqual() throws IOException, InterruptedException {
        StudentInfo[] studentInfos =generateStudentData(100000);
        StudentSelection s =new StudentSelection();
        String resultSeq = s.mostCommonFirstNameSeq(studentInfos);
        for(int i =1; i<8;i++) {
            String resultPar = s.mostCommonFirstNameForkMultiple(studentInfos, i);
            assertEquals(resultSeq, resultPar);
        }

    }
    public void testThreadSpeedUpData() throws IOException, InterruptedException {
        StudentSelection s =new StudentSelection();
        String resultSeq =new String();
        String resultPar =new String();
        StudentInfo[] studentInfos = null;
        for(int i =1; i<=8; i++){
            System.out.println("Thread class Test with Threadnum " + i);
            for (int j=1; j<7;j++) {
                studentInfos = generateStudentData((int) Math.pow(10, j));

                for (int l = 0; l < 100; l++) {
                    resultSeq = s.mostCommonFirstNameSeq(studentInfos);
                }
                long seqStartTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    resultSeq = s.mostCommonFirstNameSeq(studentInfos);
                }
                long seqEndTime = System.nanoTime();
                long parStartTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    resultPar = s.studentSelectionThread(studentInfos, i);
                }
                long parEndTime = System.nanoTime();
                assertEquals(resultSeq, resultPar);
                long seqTime = (seqEndTime - seqStartTime);
                long parTime = (parEndTime - parStartTime);
                System.out.println("data amount " + Math.pow(10, j) + " words " + "speedup " + (double) seqTime / (double) parTime + " ");
            }
        }
    }

    public void testThreadScaleData() throws IOException, InterruptedException {
        StudentSelection s =new StudentSelection();
        String resultSeq =new String();
        String resultPar =new String();
        StudentInfo[] studentInfos = null;
        for(int i =1; i<=8; i++){
            System.out.println("Thread class Test with Threadnum " + i);
                studentInfos = generateStudentData((int) i*100000);

                for (int l = 0; l < 100; l++) {
                    resultSeq = s.mostCommonFirstNameSeq(studentInfos);
                }
                long seqStartTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    resultSeq = s.mostCommonFirstNameSeq(studentInfos);
                }
                long seqEndTime = System.nanoTime();
                long parStartTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    resultPar = s.studentSelectionThread(studentInfos, i);
                }
                long parEndTime = System.nanoTime();
                assertEquals(resultSeq, resultPar);
                long seqTime = (seqEndTime - seqStartTime);
                long parTime = (parEndTime - parStartTime);
                System.out.println("data amount " + i*10000 + " words " + "speedup " + (double) seqTime / (double) parTime + " "+parTime/1000000+ "ms" +" "+seqTime/1000000+ "ms");
            }
        }

    public void testForkSpeedUpData() throws IOException, InterruptedException {
        StudentSelection s =new StudentSelection();
        String resultSeq =new String();
        String resultPar =new String();
        StudentInfo[] studentInfos = null;
        for(int i =1; i<=8; i++){
            System.out.println("Thread class Test with Threadnum " + i);
            for (int j=1; j<7;j++) {
                studentInfos = generateStudentData((int) Math.pow(10, j));

                for (int l = 0; l < 100; l++) {
                    resultSeq = s.mostCommonFirstNameSeq(studentInfos);
                }
                long seqStartTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    resultSeq = s.mostCommonFirstNameSeq(studentInfos);
                }
                long seqEndTime = System.nanoTime();
                long parStartTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    resultPar = s.mostCommonFirstNameForkMultiple(studentInfos, i);
                }
                long parEndTime = System.nanoTime();
                assertEquals(resultSeq, resultPar);
                long seqTime = (seqEndTime - seqStartTime);
                long parTime = (parEndTime - parStartTime);
                System.out.println("data amount " + Math.pow(10, j) + " words " + "speedup " + (double) seqTime / (double) parTime + " ");
            }
        }
    }

    public void testStreamSpeedUpData(){
        StudentSelection s =new StudentSelection();
        String resultSeq =new String();
        String resultPar =new String();
        StudentInfo[] studentInfos = null;
        for (int j=1; j<7;j++) {
            studentInfos = generateStudentData((int) Math.pow(10, j));

            for (int l = 0; l < 100; l++) {
                resultSeq = s.mostCommonFirstNameSeq(studentInfos);
            }
            long seqStartTime = System.nanoTime();
            for (int l = 0; l < 100; l++) {
                resultSeq = s.mostCommonFirstNameSeq(studentInfos);
            }
            long seqEndTime = System.nanoTime();
            long parStartTime = System.nanoTime();
            for (int l = 0; l < 100; l++) {
                resultPar = s.mostCommonFirstNameOfStudentsParallelStream(studentInfos);
            }
            long parEndTime = System.nanoTime();
            assertEquals(resultSeq, resultPar);
            long seqTime = (seqEndTime - seqStartTime);
            long parTime = (parEndTime - parStartTime);
            System.out.println("data amount " + Math.pow(10, j) + " words " + "speedup " + (double) seqTime / (double) parTime + " ");
        }
    }
}
