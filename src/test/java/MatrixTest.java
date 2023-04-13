import exercises.Matrix;
import junit.framework.TestCase;

import java.util.Random;

public class MatrixTest extends TestCase {
    public static Matrix createRandomMatrix(int rows, int cols)
    {
        Matrix m = new Matrix(rows, cols);
        Random rand = new Random();
        for (int i = 0; i < m.getRows(); i++)
            for (int j = 0; j < m.getCols(); j++)
                m.getData()[i][j] = rand.nextInt(100);
        return m;
    }
    public void testMatrixSpeedUp(){
        for (int x = 1; x <= 8; x++) {
            System.out.println("thread number "+ x);
            System.out.println("   N       tseq       tpar   s(n)");
            for (int N = 32; N <= 1024; N *= 2) {
                Matrix a = createRandomMatrix(N, N);
                Matrix b = createRandomMatrix(N, N);

                long startPar = System.nanoTime();
                try {
                    for (int i =0; i<20;i++) {
                        Matrix cPar = Matrix.matrixMultiParallel(a, b, x);
                    }
                }catch (Exception e){}

                long endpar = System.nanoTime();

                long timeStart = System.nanoTime();
                for (int i =0; i<20;i++) {
                    Matrix cSeq = Matrix.matrixMultiSeq(a, b);
                }
                long endTime = System.nanoTime();
                double timeSerial = endTime - timeStart;
                double timeParallel = endpar - startPar;
                double speedUp = timeSerial / timeParallel;
                System.out.printf("%4d %7.1f ms %7.1f ms %6.2f\n", N, timeSerial, timeParallel, speedUp);
            }
        }
    }
    public void testMatrixscale(){
        for (int x = 1; x <= 8; x++) {
            System.out.println("thread number "+ x);
                int N = 128*x;
                Matrix a = createRandomMatrix(N, N);
                Matrix b = createRandomMatrix(N, N);

                long startPar = System.nanoTime();
                try {
                    for (int i =0; i<1;i++) {
                        Matrix cPar = Matrix.matrixMultiParallel(a, b, x);
                    }
                }catch (Exception e){}

                long endpar = System.nanoTime();

                long timeStart = System.nanoTime();
                for (int i =0; i<1;i++) {
                    Matrix cSeq = Matrix.matrixMultiSeq(a, b);
                }
                long endTime = System.nanoTime();
                double timeSerial = endTime - timeStart;
                double timeParallel = endpar - startPar;
                double speedUp = timeSerial / timeParallel;
                System.out.println(N +" "+ speedUp +" "+ timeParallel/1000000);
            }
        }
    }

