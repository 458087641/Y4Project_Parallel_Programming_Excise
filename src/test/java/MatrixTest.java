import exercises.Matrix;
import junit.framework.TestCase;

public class MatrixTest extends TestCase {
    public void testMatrixSpeedUp(){
        for (int x = 1; x <= 8; x++) {
            System.out.println("thread number "+ x);
            System.out.println("   N       tseq       tpar   s(n)");
            for (int N = 128; N <= 4096; N *= 2) {
                Matrix a = Matrix.createRandomized(N, N);
                Matrix b = Matrix.createRandomized(N, N);
                long timeStart = System.nanoTime();
                Matrix cSerial = Matrix.multSerial(a, b);
                long endTime = System.nanoTime();
                double timeSerial = endTime - timeStart;

                long startPar = System.nanoTime();
                try {
                    Matrix c = Matrix.multParallel4(a, b, x);
                }catch (Exception e){}

                long endpar = System.nanoTime();
                double timeParallel = endpar - startPar;

                double speedUp = timeSerial / timeParallel;

                System.out.printf("%4d %7.1f ms %7.1f ms %6.2f\n", N, timeSerial, timeParallel, speedUp);
            }
        }
    }
}
