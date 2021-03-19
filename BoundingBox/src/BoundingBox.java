import java.util.Scanner;

public class BoundingBox {
    static class Pair {
        int min, max;
    }

    private static Pair findMinMax(int[] array, int left, int right) {
        Pair minmax = new Pair();

        // if there is only on element
        if (left == right) {
            minmax.min = minmax.max = array[left];
            return minmax;
        }

        // if there is only two element
        if (left + 1 == right) {
            if (array[left] > array[right]) {
                minmax.min = array[right];
                minmax.max = array[left];
            } else {
                minmax.min = array[left];
                minmax.max = array[right];
            }
            return minmax;
        }

        int mid = (left + right) / 2;

        // divide
        Pair leftMinmax = findMinMax(array, left, mid);
        Pair rightMinmax = findMinMax(array, mid + 1, right);

        // conquer
        minmax.min = Math.min(leftMinmax.min, rightMinmax.min);
        minmax.max = Math.max(leftMinmax.max, rightMinmax.max);

        return minmax;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter points count : ");

        int n = in.nextInt();
        int[] arrayX = new int[n];
        int[] arrayY = new int[n];

        System.out.println("Enter points coordinates :");

        for (int i = 0; i < n; i++) {
            arrayX[i] = in.nextInt();
            arrayY[i] = in.nextInt();
        }

        Pair minmaxX = findMinMax(arrayX, 0, arrayX.length - 1);
        Pair minmaxY = findMinMax(arrayY, 0, arrayY.length - 1);

        System.out.println("Bounding box coordinates : \n"
                + '(' + minmaxX.min + ", " + minmaxY.min + ") "
                + '(' + minmaxX.max + ", " + minmaxY.max + ") "
                + '(' + minmaxX.max + ", " + minmaxY.min + ") "
                + '(' + minmaxX.min + ", " + minmaxY.max + ")"
        );
    }
}
