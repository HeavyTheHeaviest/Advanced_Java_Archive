import java.util.Arrays;
import java.util.Random;
import javax.swing.JOptionPane;

public class Sorter {
    public Double[] a;
    public Double[] b;
    public Boolean order;

    public native Double[] sort01(Double[] a, Boolean order);
    public native Double[] sort02(Double[] a);
    public native void     sort03();

    public void sort04() {
        if (a == null || order == null) {
            throw new IllegalStateException("Fields 'a' and 'order' must be set before calling sort04()");
        }
        b = a.clone();
        if (order) {
            Arrays.sort(b);
        } else {
            Arrays.sort(b, java.util.Collections.reverseOrder());
        }
    }

    static {
        System.loadLibrary("sorter");
    }

    private static Double[] randomArray(int size) {
        Random rnd = new Random(12345);
        Double[] arr = new Double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rnd.nextDouble();
        }
        return arr;
    }

    public static void main(String[] args) {
        final int N = 200_000;
        Sorter s = new Sorter();
        Double[] data = randomArray(N);

        Double[] in1 = data.clone();
        long t0 = System.nanoTime();
        Double[] out1 = s.sort01(in1, true);
        long t1 = System.nanoTime();
        System.out.printf("sort01 (native, order=true): %.2f ms%n", (t1 - t0) / 1e6);

        Double[] in2 = data.clone();
        s.order = false;
        t0 = System.nanoTime();
        Double[] out2 = s.sort02(in2);
        t1 = System.nanoTime();
        System.out.printf("sort02 (native, this.order=false): %.2f ms%n", (t1 - t0) / 1e6);

        Double[] in4 = data.clone();
        s.a = in4;
        s.order = true;
        t0 = System.nanoTime();
        s.sort04();
        long t4 = System.nanoTime();
        System.out.printf("sort04 (pure Java, order=true): %.2f ms%n", (t4 - t0) / 1e6);

        System.out.println("\nInteraktywnie: wprowadź mały zbiór w okienkach");
        s.sort03();
        t0 = System.nanoTime();
        s.sort04();
        long t5 = System.nanoTime();
        System.out.printf("sort03()→sort04(): %.2f ms%n", (t5 - t0) / 1e6);
    }
}
