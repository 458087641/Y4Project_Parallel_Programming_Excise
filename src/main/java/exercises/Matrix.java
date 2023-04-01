package exercises;

import static exercises.Helper.getChunkEndExclusive;
import static exercises.Helper.getChunkStartInclusive;
public class Matrix
{
    private int rows, cols;
    private int[][] data;
    public Matrix(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;
        this.data = new int[rows][cols];
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

    public static class MatrixMultParallel extends Thread
    {
        private int iStart, iEnd;
        private Matrix a, r, c;

        public MatrixMultParallel(int iStart, int iEnd, Matrix a, Matrix r, Matrix c)
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
    public static int scalarProduct(Matrix a, int aRow, Matrix b, int bCol)
    {
        int scalar = 0;
        for (int k = 0; k < b.getCols(); k++)
            scalar += a.data[aRow][k] * b.data[k][bCol];

        return scalar;
    }
    public static Matrix multParallel(Matrix a, Matrix b, int threadNum) throws InterruptedException {

        Matrix c = new Matrix(a.rows, b.cols);
        MatrixMultParallel[] tList= new MatrixMultParallel[threadNum];
        for (int i = 0; i < threadNum; i++)
        {
            MatrixMultParallel th = new MatrixMultParallel(getChunkStartInclusive(i, threadNum,a.data.length),getChunkEndExclusive(i, threadNum,a.data.length), a, b, c);
            tList[i]=th;
            th.start();
        }
        for (MatrixMultParallel i : tList){
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
