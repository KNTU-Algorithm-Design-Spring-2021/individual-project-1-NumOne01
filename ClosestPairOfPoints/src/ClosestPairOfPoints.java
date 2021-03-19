import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ClosestPairOfPoints {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(in.nextDouble(), in.nextDouble(), in.nextDouble());
        }
        System.out.println(solve(points));
    }

    private static double dist(Point pointA, Point pointB) {
        final double xDiff = Math.pow(pointA.x - pointB.x, 2);
        final double yDiff = Math.pow(pointA.y - pointB.y, 2);
        final double zDiff = Math.pow(pointA.z - pointB.z, 2);

        return Math.sqrt(xDiff + yDiff + zDiff);
    }

    private static double stripClose(ArrayList<Point> py, double d) {
        double min = d, distance;
        for (int i = 0; i < py.size(); i++) {
            int j = i + 1;
            while (j < py.size() && py.get(i).y - py.get(j).y < d) {
                distance = dist(py.get(i), py.get(j));
                if (distance < min)
                    min = distance;
                j++;
            }
        }
        return Math.min(min, d);
    }

    private static double bruteForce(Point[] points, int left, int right) {
        double min = Double.MAX_VALUE;
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                double distance = dist(points[i], points[j]);
                if (distance < min)
                    min = distance;
            }
        }
        return min;
    }

    private static double findMinDistance(Point[] px, ArrayList<Point> py, ArrayList<Point> pz, int left, int right, int dimension) {
        if (right - left <= 3) {
            return bruteForce(px, left, right);
        }

        int mid = (left + right) / 2;
        Point midPoint = px[mid];

        ArrayList<Point> pyLeft = new ArrayList<>();
        ArrayList<Point> pyRight = new ArrayList<>();

        for (Point point : py) {
            if (point.x <= midPoint.x) {
                pyLeft.add(point);
            } else {
                pyRight.add(point);
            }
        }

        ArrayList<Point> pzLeft = new ArrayList<>();
        ArrayList<Point> pzRight = new ArrayList<>();

        if (dimension > 2) {
            for (Point point : pz) {
                if (point.x <= midPoint.x) {
                    pzLeft.add(point);
                } else {
                    pzRight.add(point);
                }
            }
        }

        double dl = findMinDistance(px, pyLeft, pzLeft, left, mid, dimension);
        double dr = findMinDistance(px, pyRight, pzRight, mid + 1, right, dimension);

        double d = Math.min(dl, dr);

        ArrayList<Point> stripY = new ArrayList<>();

        for (Point point : py) {
            if (Math.abs(point.x - midPoint.x) < d) {
                stripY.add(point);
            }
        }

        double strip;
        if (dimension > 2) {
            Point[] yPoints = new Point[py.size()];
            int i = 0;
            for (Point point : py) {
                yPoints[i++] = point;
            }
            strip = findMinDistance(yPoints, pz, pz, 0, py.size() - 1, dimension - 1);
        } else strip = stripClose(stripY, d);

        return Math.min(d, strip);
    }

    private static double solve(Point[] points) {
        Point[] py = Arrays.copyOf(points, points.length);
        Point[] pz = Arrays.copyOf(points, points.length);
        Arrays.sort(points, (a, b) -> (int) (a.x - b.x));
        Arrays.sort(py, (a, b) -> (int) (a.y - b.y));
        Arrays.sort(pz, (a, b) -> (int) (a.z - b.z));
        ArrayList<Point> yPoints = new ArrayList<>(Arrays.asList(py));
        ArrayList<Point> zPoints = new ArrayList<>(Arrays.asList(pz));
        return findMinDistance(points, yPoints, zPoints, 0, points.length - 1, 3);
    }
}

class Point {
    public double x, y, z;

    Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

/*
6
2 3 5
12 30 -3
40 50 -8
5 1 -10
12 10 7
3 4 10
 */
