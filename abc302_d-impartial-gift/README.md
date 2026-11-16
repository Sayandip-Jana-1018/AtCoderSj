# ABC302 D · Impartial Gift

**Platform:** AtCoder (ABC302) · **Difficulty:** ~680 (AtCoder Problems estimate) · **Points:** 400 · **Tags:** sorting, binary search · **Solved:** 16 Nov 2026 · [Original problem](https://atcoder.jp/contests/abc302/tasks/abc302_d)

---

## 1. The problem in simple words

There are two lists of gift values:
- `A` has `N` gifts for Aoki.
- `B` has `M` gifts for Snuke.

Pick **one gift from each list** so the two values differ by **at most `D`** (`|a − b| ≤ D`). Among all valid pairs, print the **largest possible sum `a + b`**. If no valid pair exists, print `-1`.

| Limit | Value |
|---|---|
| `N`, `M` | 1 to 2 × 10^5 |
| Values `A[i]`, `B[i]` | 1 to 10^18 |
| `D` | 0 to 10^18 |
| Time limit | 2 seconds |

---

## 2. Intuition

Fix one gift `a` from list A. Which `b` should you pair it with?

- The sum is `a + b`, so you want `b` **as big as possible**.
- `b` can't be too big: it must satisfy `b ≤ a + D`.
- So the best candidate is **the largest `b` that is still ≤ `a + D`**.
- Then check the other side: is that `b ≥ a − D`? If it isn't, **every smaller `b` fails too**, so this `a` has no partner at all.

Finding "the largest `b` ≤ some value" is a job for **sorting + binary search**.

---

## 3. Brute force (and why it fails)

Try every pair:

```java
long best = -1;
for (long a : A)
    for (long b : B)
        if (Math.abs(a - b) <= D) best = Math.max(best, a + b);
```

That's `N × M` = up to 4 × 10^10 pairs, far over the time limit. It's still useful as a **checker**: I compared it with the fast version on 300 small random inputs.

---

## 4. Optimal approach: sort B and binary search

1. Sort `B` ascending (shuffle first, so Java's quicksort can't be forced into its worst case).
2. For each `a` in `A`:
   - Use binary search to find `idx` = the position of the **largest `b ≤ a + D`** (the upper bound of `a + D`, minus 1).
   - If `idx ≥ 0` and `B[idx] ≥ a − D`, then `a + B[idx]` is a candidate answer.
3. Print the largest candidate, or `-1` if there were none.

---

## 5. Dry run

**Input:** `N = 2, M = 3, D = 2`, `A = [3, 10]`, `B = [2, 5, 15]`

Sorted `B = [2, 5, 15]`

| `a` | `a + D` | largest `b ≤ a + D` | `a − D` | valid? | sum |
|---|---|---|---|---|---|
| 3 | 5 | 5 | 1 | 5 ≥ 1 ✅ | **8** |
| 10 | 12 | 5 | 8 | 5 ≥ 8 ❌ | – |

Answer = **8**

**Input:** `D = 0`, `A = [1, 3, 3]`, `B = [6, 2, 7]` → sorted `B = [2, 6, 7]`
- `a = 1`: no `b ≤ 1` → skip
- `a = 3`: largest `b ≤ 3` is 2, but 2 ≥ 3 is false → skip

Answer = **−1**

---

## 6. Java code

```java
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

    // first index with arr[i] > key
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

    static class FastReader { /* fast byte reader, see Main.java */ }
}
```

The complete file, including the fast input reader, is [`Main.java`](./Main.java).

---

## 7. Complexity

| | Cost | Why |
|---|---|---|
| Sorting `B` | O(M log M) | |
| Binary search for each `a` | O(N log M) | |
| **Total time** | **O((N + M) log M)** | Ran in 232 ms on the judge |
| **Space** | **O(N + M)** | The two arrays |

---

## 8. Edge cases

- `D = 0`: only equal values can pair up.
- No valid pair at all: print `-1`.
- Every value is 10^18: the answer is 2 × 10^18. That still fits in a `long` (max ≈ 9.22 × 10^18), but **not** in an `int`.
- `a − D` can go negative. That's fine with `long`.

## 9. Common mistakes

- Using `int`. Values go up to 10^18, so use `long` everywhere.
- Taking the **smallest** valid `b` instead of the largest. You want a maximum sum.
- Off-by-one in the binary search. You need "largest ≤", which is upper bound minus 1.
- Reading 4 × 10^5 numbers with `Scanner` is slow. Use a fast reader.

## 10. Key takeaway

> When you need the best partner from another list under a range condition, **sort that list and binary search** the boundary. It turns O(N·M) into O(N log M).

---

## Submission

**AC** · 400 points · 232 ms · 44 MiB · Java 24 (OpenJDK 24.0.2) · 1 attempt
