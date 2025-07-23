import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;

public class Benchmark {
    static {
        System.loadLibrary("sorter");
    }
    private static Double[] randomArray(int n) {
        Random r = new Random(42);
        Double[] a = new Double[n];
        for (int i = 0; i < n; i++) a[i] = r.nextDouble();
        return a;
    }

    public static void main(String[] args) throws Exception {
        Sorter s = new Sorter();
        int[] sizes = { 50_000, 100_000, 200_000, 400_000, 800_000 };
        int runs = 5;  // powtórzeń dla średniej

        try (PrintWriter out = new PrintWriter(new FileWriter("benchmark.csv"))) {
            out.println("method,N,run,time_ms");
            for (int N : sizes) {
                for (int run = 1; run <= runs; run++) {
                    Double[] base = randomArray(N);

                    // sort01
                    long t0 = System.nanoTime();
                    s.sort01(base.clone(), true);
                    long t1 = System.nanoTime();
                    out.printf("sort01,%d,%d,%.3f%n", N, run, (t1-t0)/1e6);

                    // sort02
                    s.order = false;
                    t0 = System.nanoTime();
                    s.sort02(base.clone());
                    t1 = System.nanoTime();
                    out.printf("sort02,%d,%d,%.3f%n", N, run, (t1-t0)/1e6);

                    // sort04 (pure Java)
                    s.a = base.clone();
                    s.order = true;
                    t0 = System.nanoTime();
                    s.sort04();
                    t1 = System.nanoTime();
                    out.printf("sort04,%d,%d,%.3f%n", N, run, (t1-t0)/1e6);
                }
            }
        }
        System.out.println("Done. Results in benchmark.csv");
    }
}
