package exercises;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static exercises.Helper.getChunkEndExclusive;
import static exercises.Helper.getChunkStartInclusive;
public class Matrix
{
    private static Random rnd = new Random();

    private int rows, cols;
    private int[][] data;


    public Matrix(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;
        this.data = new int[rows][cols];
    }



    public static Matrix createRandomized(int rows, int cols)
    {
        Matrix m = new Matrix(rows, cols);

        for (int i = 0; i < m.rows; i++)
            for (int j = 0; j < m.cols; j++)
                m.data[i][j] = rnd.nextInt(100);

        return m;
    }


    public static int scalarProduct(Matrix a, int aRow, Matrix b, int bRow)
    {
        int scalar = 0;
        for (int k = 0; k < b.getCols(); k++)
            scalar += a.data[aRow][k] * b.data[bRow][k];

        return scalar;
    }

    public static Matrix multSerial(Matrix a, Matrix b)
    {
        Matrix c = new Matrix(a.rows, b.cols);
        for (int i = 0; i < c.rows; i++)
            for (int j = 0; j < c.cols; j++)
                for (int k = 0; k < b.rows; k++)
                    c.data[i][j] += a.data[i][k] * b.data[k][j];

        return c;
    }

    public static class MatrixMultParallel4 extends Thread
    {
        private int iStart, iEnd;
        private Matrix a, r, c;

        public MatrixMultParallel4(int iStart, int iEnd, Matrix a, Matrix r, Matrix c)
        {
            this.iStart = iStart;
            this.iEnd = iEnd;
            this.a = a;
            this.r = r;
            this.c = c;
        }

        @Override
        public void run()
        {
            for (int i = iStart; i < iEnd; i++)
                for (int j = 0; j < c.getCols(); j++)
                    c.getData()[i][j] = Matrix.scalarProduct(a, i, r, j);
        }
    }
    public static Matrix multParallel4(Matrix a, Matrix b, int threadNum) throws InterruptedException {

        Matrix c = new Matrix(a.rows, b.cols);
        MatrixMultParallel4[] tList= new MatrixMultParallel4[threadNum];
        for (int x = 0; x < threadNum; x++)
        {
            MatrixMultParallel4 th = new MatrixMultParallel4(getChunkStartInclusive(x, threadNum,a.data.length),getChunkEndExclusive(x, threadNum,a.data.length), a, b, c);
            tList[x]=th;
            th.start();
        }
        for (MatrixMultParallel4 i : tList){
            i.join();
        }
        return c;
    }



    public int getRows()
    {
        return rows;
    }

    public int getCols()
    {
        return cols;
    }

    public int[][] getData()
    {
        return data;
    }

}
