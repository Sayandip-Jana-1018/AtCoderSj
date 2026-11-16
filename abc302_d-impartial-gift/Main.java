import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader(System.in);
        int n = in.nextInt(), m = in.nextInt();
        long d = in.nextLong();
        long[] a = new long[n], b = new long[m];
        for (int i = 0; i < n; i++) a[i] = in.nextLong();
        for (int i = 0; i < m; i++) b[i] = in.nextLong();
        shuffleSort(b);

        long best = -1;
        for (long x : a) {
            int idx = upperBound(b, x + d) - 1;      // largest b <= x + d
            if (idx >= 0 && b[idx] >= x - d) {       // and b >= x - d
                best = Math.max(best, x + b[idx]);
            }
        }
        System.out.println(best);
    }

    // first index with b[i] > key
    static int upperBound(long[] arr, long key) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (arr[mid] <= key) lo = mid + 1; else hi = mid;
        }
        return lo;
    }

    static void shuffleSort(long[] arr) {
        Random rnd = new Random();
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            long t = arr[i]; arr[i] = arr[j]; arr[j] = t;
        }
        Arrays.sort(arr);
    }

    static class FastReader {
        private final DataInputStream din; private final byte[] buf = new byte[1 << 16]; private int len = 0, ptr = 0;
        FastReader(InputStream is) { din = new DataInputStream(is); }
        private int read() throws IOException {
            if (ptr == len) { len = din.read(buf, 0, buf.length); ptr = 0; if (len <= 0) return -1; }
            return buf[ptr++];
        }
        long nextLong() throws IOException {
            long ret = 0; int b = read();
            while (b < '0' || b > '9') b = read();
            while (b >= '0' && b <= '9') { ret = ret * 10 + (b - '0'); b = read(); }
            return ret;
        }
        int nextInt() throws IOException { return (int) nextLong(); }
    }
}
