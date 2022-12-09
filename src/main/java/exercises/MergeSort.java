package exercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MergeSort {
    public static void MergeSort(int a[],int n)
    {
        if(n<=1)
            return;
        int mid=n/2;
        int left[]=new int[mid];
        int right[]=new int[n-mid];
        for(int i=0;i<mid;i++)
            left[i]=a[i];
        for(int i=mid;i<n;i++)
            right[i-mid]=a[i];
        MergeSort(left,mid);
        MergeSort(right,n-mid);
        Merge(left,right,a);
    }
    public static void Merge(int left[],int right[],int a[])
    {int nL=left.length;
        int nR=right.length;
        int i,j,k;
        i=j=k=0;
        while(i<nL&&j<nR)
        { if(left[i]<=right[j])
            { a[k]=left[i];
                i++;
                k++; }
            else
            { a[k]=right[j];
                j++;
                k++; }
        }
        while(i<nL)
        {
            a[k]=left[i];
            i++;
            k++;
        }
        while(j<nR)
        {
            a[k]=right[j];
            j++;
            k++;
        }
    }
    public class ParallelMergeSort extends RecursiveTask<List<Integer>> {

        private List<Integer> elements;

        public ParallelMergeSort(List<Integer> elements){
            this.elements = elements;
        }

        @Override
        protected List<Integer> compute() {
            if(this.elements.size() <= 1){
                return this.elements;
            }
            else{
                final int pivot = this.elements.size() / 2;
                ParallelMergeSort leftTask = new ParallelMergeSort(this.elements.subList(0, pivot));
                ParallelMergeSort rightTask = new ParallelMergeSort(this.elements.subList(pivot, this.elements.size()));

                leftTask.fork();
                rightTask.fork();

                List<Integer> left = leftTask.join();
                List<Integer> right = rightTask.join();

                merge(left, right);
                return this.elements;
            }

        }

        private void merge(List<Integer> left, List<Integer> right) {
            int leftIndex = 0;
            int rightIndex = 0;
            while(leftIndex < left.size() ) {
                if(rightIndex == 0) {
                    if( left.get(leftIndex).compareTo(right.get(rightIndex)) > 0 ) {
                        swap(left, leftIndex++, right, rightIndex++);
                    } else {
                        leftIndex++;
                    }
                } else {
                    if(rightIndex >= right.size()) {
                        if(right.get(0).compareTo(left.get(left.size() - 1)) < 0 )
                            merge(left, right);
                        else
                            return;
                    }
                    else if( right.get(0).compareTo(right.get(rightIndex)) < 0 ) {
                        swap(left, leftIndex++, right, 0);
                    } else {
                        swap(left, leftIndex++, right, rightIndex++);
                    }
                }
            }

            if(rightIndex < right.size() && rightIndex != 0)
                merge(right.subList(0, rightIndex), right.subList(rightIndex, right.size()));
        }

        private void swap(List<Integer> left, int leftIndex, List<Integer> right, int rightIndex) {
            left.set(leftIndex, right.set(rightIndex, left.get(leftIndex)));
        }

    }
}
