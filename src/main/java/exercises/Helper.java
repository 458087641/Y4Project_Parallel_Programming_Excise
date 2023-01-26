package exercises;

public class Helper {
    public static int getChunkStartInclusive(final int chunk,
                                              final int nChunks, final int nElements) {
        final int chunkSize = (nElements + nChunks - 1) / nChunks;
        return chunk * chunkSize;
    }

    public static int getChunkEndExclusive(final int chunk, final int nChunks,
                                            final int nElements) {
        final int chunkSize = (nElements + nChunks - 1) / nChunks;
        final int end = (chunk + 1) * chunkSize;
        if (end > nElements) {
            return nElements;
        } else {
            return end;
        }
    }
}
