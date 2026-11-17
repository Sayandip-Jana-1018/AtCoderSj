# ABC414 D · Transmission Mission

**Platform:** AtCoder (ABC414, Mirrativ Programming Contest 2025) · **Points:** 400 · **Difficulty:** ~841 · **Tags:** greedy, sorting · **Solved:** 17 Nov 2026 · [Original problem](https://atcoder.jp/contests/abc414/tasks/abc414_d)

---

## 1. The problem in simple words

`N` houses sit on a number line at integer positions (several houses may share a spot).
You may build **`M` base stations** anywhere (even at non-integer points). Each station gets a non-negative integer strength `x` and covers every house within distance `x / 2`. In other words, it covers **a segment of length `x`** centred on the station.

Every house must be covered by at least one station. Minimise the **sum of strengths**.

| Limit | Value |
|---|---|
| `M ≤ N` | up to 5·10⁵ |
| `X[i]` | 1 to 10¹² |

---

## 2. Intuition: it's about gaps

A station of strength `x` = a segment of length `x`. So the question becomes: **cover all points with at most `M` segments, minimising the total segment length.**

- With **one** segment, you must stretch from the leftmost house to the rightmost: cost `X[max] − X[min]`.
- With **two** segments, you can leave one stretch of the line uncovered. The best stretch to skip is the **largest gap between two neighbouring houses**.
- With `M` segments, you skip the **`M − 1` largest gaps**.

So: `answer = (X[max] − X[min]) − (sum of the M − 1 largest neighbour gaps)`.

Why is this optimal? In sorted order, each segment covers a run of consecutive houses, and the uncovered parts are exactly the gaps between runs. The total length is the full span minus the gaps you didn't cover. Using `M` runs lets you skip `M − 1` gaps, so you skip the biggest ones.

---

## 3. Brute force (and why it fails)

DP over sorted houses: `dp[i][k]` = minimum cost to cover the first `i` houses with `k` segments, trying every start for the last segment.
That's `O(N² · M)`, hopeless for `N = 5·10⁵`. (It's still perfect for **testing** the greedy, and that's how this solution was checked.)

---

## 4. Optimal approach

1. Sort `X`.
2. Compute the `N − 1` gaps `X[i+1] − X[i]`.
3. Sort the gaps.
4. Start from `X[N−1] − X[0]` and subtract the `M − 1` largest gaps.

---

## 5. Dry run

Sample 1: `N = 7, M = 3`, houses `5 10 15 20 8 14 15`.

| Step | Values |
|---|---|
| Sorted | 5, 8, 10, 14, 15, 15, 20 |
| Gaps | 3, 2, 4, 1, 0, 5 |
| Full span | 20 − 5 = **15** |
| `M − 1 = 2` largest gaps | 5 and 4 |
| Answer | 15 − 5 − 4 = **6** ✓ |

The three segments are `[5, 10]` (length 5), `[14, 15]` (length 1) and `[20, 20]` (length 0). Total 6, matching the sample explanation.

`M = 7` skips all 6 gaps (answer 0). `M = 1` skips none (answer 15).

---

## 6. Java code

```java
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

    // shuffleSort(): shuffle, then Arrays.sort (guards against worst-case quicksort)
    // nextLong(): fast reader. See Main.java.
}
```

Full file: [`Main.java`](./Main.java).

---

## 7. Complexity

| | Value | Why |
|---|---|---|
| Time | `O(N log N)` | Two sorts dominate |
| Space | `O(N)` | Positions and gaps |

---

## 8. Edge cases

- **`N = 1`:** no gaps, and the answer is 0. The gap array is empty and `M − 1 = 0`.
- **`M = N`:** every house gets its own station of strength 0, so the answer is 0.
- **Duplicate positions:** they create gaps of 0, which is harmless.
- **Big coordinates (10¹²):** use `long`.

---

## 9. Common mistakes

- Thinking strength `x` covers a segment of length `2x`. It covers distance `x/2` on each side, so the length is `x`.
- Subtracting `M` gaps instead of `M − 1`.
- Forgetting to sort before computing gaps.

---

## 10. Key takeaway

> "Cover points with K segments, minimise total length" = full span minus the K − 1 biggest gaps. Think about what you **skip**, not what you cover.

---

## Submission

**AC** · 461 ms · 51 056 KiB · Java24 (OpenJDK 24.0.2) · 1st attempt
