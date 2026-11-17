import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        DataInputStream in = new DataInputStream(new BufferedInputStream(System.in, 1 << 16));
        int n = (int) nextLong(in), m = (int) nextLong(in);
        long[] x = new long[n];
        for (int i = 0; i < n; i++) x[i] = nextLong(in);
        shuffleSort(x);

        // gaps between neighbouring houses after sorting
        long[] gap = new long[n - 1];
        for (int i = 0; i + 1 < n; i++) gap[i] = x[i + 1] - x[i];
        shuffleSort(gap);

        // one station covering everything costs x[n-1] - x[0];
        // each extra station lets us "cut out" one gap, so drop the m-1 largest gaps
        long ans = x[n - 1] - x[0];
        for (int i = 0; i < m - 1; i++) ans -= gap[n - 2 - i];
        System.out.println(ans);
    }

    private static void shuffleSort(long[] a) {
        Random r = new Random(1414);
        for (int i = a.length - 1; i > 0; i--) {
            int j = r.nextInt(i + 1);
            long t = a[i]; a[i] = a[j]; a[j] = t;
        }
        Arrays.sort(a);
    }

    private static long nextLong(DataInputStream in) throws IOException {
        int b = in.read();
        while (b < '0' || b > '9') b = in.read();
        long r = 0;
        while (b >= '0' && b <= '9') { r = r * 10 + (b - '0'); b = in.read(); }
        return r;
    }
}
