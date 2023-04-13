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

    public static Matrix matrixMultiSeq(Matrix a, Matrix b)
    {
        Matrix c = new Matrix(a.rows, b.cols);
        for (int i = 0; i < c.rows; i++)
            for (int j = 0; j < c.cols; j++)
                for (int k = 0; k < b.rows; k++)
                    c.data[i][j] += a.data[i][k] * b.data[k][j];
        return c;
    }

    public static class MatrixMultiParallelThreadClass extends Thread
    {
        private int startIndex, endIndex;
        private Matrix a, b, c;

        public MatrixMultiParallelThreadClass(int startIndex, int endIndex, Matrix a, Matrix b, Matrix c)
        {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public void run()
        {
            for (int i = startIndex; i < endIndex; i++)
                for (int j = 0; j < c.getCols(); j++)
                    c.getData()[i][j] = Matrix.scalarProduct(a, i, b, j);
        }
    }
    public static int scalarProduct(Matrix a, int aRow, Matrix b, int bCol)
    {
        int scalar = 0;
        for (int k = 0; k < b.getCols(); k++)
            scalar += a.data[aRow][k] * b.data[k][bCol];

        return scalar;
    }
    public static Matrix matrixMultiParallel(Matrix a, Matrix b, int threadNum) throws InterruptedException {

        Matrix c = new Matrix(a.rows, b.cols);
        MatrixMultiParallelThreadClass[] tList= new MatrixMultiParallelThreadClass[threadNum];
        for (int i = 0; i < threadNum; i++)
        {
            MatrixMultiParallelThreadClass th = new MatrixMultiParallelThreadClass(getChunkStartInclusive(i, threadNum,a.data.length),getChunkEndExclusive(i, threadNum,a.data.length), a, b, c);
            tList[i]=th;
            th.start();
        }
        for (MatrixMultiParallelThreadClass i : tList){
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
