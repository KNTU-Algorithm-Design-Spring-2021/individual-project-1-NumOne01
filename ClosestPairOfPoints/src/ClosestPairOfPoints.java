import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ClosestPairOfPoints {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter number of points : ");
        int n = in.nextInt();
        Point[] points = new Point[n];
        System.out.println("Enter points coordinates : ");
        for (int i = 0; i < n; i++) {
            points[i] = new Point(in.nextDouble(), in.nextDouble(), in.nextDouble());
        }
        ClosestPairOfPoints closestPairOfPoints = new ClosestPairOfPoints();
        System.out.println("Minimum distance is : " + closestPairOfPoints.solve(points));
    }

    /**
     * calculates the distance between two points
     * based on sqrt((x1-x2)^2 + (y1-y2)^2 + (z1-z2)^2)
     *
     * @param pointA first point
     * @param pointB second point
     * @return distance between two points
     */
    private double dist(Point pointA, Point pointB) {
        final double xDiff = Math.pow(pointA.getX() - pointB.getX(), 2);
        final double yDiff = Math.pow(pointA.getY() - pointB.getY(), 2);
        final double zDiff = Math.pow(pointA.getZ() - pointB.getZ(), 2);

        return Math.sqrt(xDiff + yDiff + zDiff);
    }

    /**
     * calculates the distance for points in a strip
     * this runs in a O(6) order
     *
     * @param pz list of points ordered by z
     * @param d  upper bound
     * @return minimum of d and points in the strip
     */
    private double stripClose(ArrayList<Point> pz, double d) {
        double min = d, distance;
        for (int i = 0; i < pz.size(); i++) {
            int j = i + 1;
            while (j < pz.size() && pz.get(i).getZ() - pz.get(j).getZ() < d) {
                distance = dist(pz.get(i), pz.get(j));
                if (distance < min)
                    min = distance;
                j++;
            }
        }
        return min;
    }

    /**
     * calculates the minimum distance of points
     * with brute force
     * this runs on O(n^2)
     *
     * @param points array of points
     * @param left   left bound
     * @param right  right bound
     * @return minimum distance
     */
    private double bruteForce(Point[] points, int left, int right) {
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

    /**
     * find minimum distance of points with a divide and conquer algorithm
     * this runs on O(nlog(n)^2)
     *
     * @param firstDPoints  points ordered on first dimension
     * @param secondDPoints points ordered on second dimension
     * @param thirdDPoints  points ordered on third dimension
     * @param left          left bound
     * @param right         right bound
     * @param dimension     dimension of points
     * @return minimum distance of points
     */
    private double findMinDistance(Point[] firstDPoints, ArrayList<Point> secondDPoints, ArrayList<Point> thirdDPoints, int left, int right, int dimension) {
        if (right - left <= 3) {
            return bruteForce(firstDPoints, left, right);
        }

        int mid = (left + right) / 2;
        Point midPoint = firstDPoints[mid];

        ArrayList<Point> secondDPointsLeft = new ArrayList<>();
        ArrayList<Point> secondDPointsRight = new ArrayList<>();

        for (Point point : secondDPoints) {
            if (point.getX() <= midPoint.getX()) {
                secondDPointsLeft.add(point);
            } else {
                secondDPointsRight.add(point);
            }
        }

        ArrayList<Point> thirdDPointsLeft = new ArrayList<>();
        ArrayList<Point> thirdDPointsRight = new ArrayList<>();

        if (dimension > 2) {
            for (Point point : thirdDPoints) {
                if (point.getX() <= midPoint.getX()) {
                    thirdDPointsLeft.add(point);
                } else {
                    thirdDPointsRight.add(point);
                }
            }
        }

        double dl = findMinDistance(firstDPoints, secondDPointsLeft, thirdDPointsLeft, left, mid, dimension);
        double dr = findMinDistance(firstDPoints, secondDPointsRight, thirdDPointsRight, mid + 1, right, dimension);

        double d = Math.min(dl, dr);

        ArrayList<Point> stripY = new ArrayList<>();

        for (Point point : secondDPoints) {
            if (Math.abs(point.getY() - midPoint.getX()) < d) {
                stripY.add(point);
            }
        }

        double strip;
        if (dimension > 2) {
            Point[] points = new Point[stripY.size()];
            int i = 0;
            for (Point point : stripY) {
                points[i++] = point;
            }
            // solve problem for a lower dimension
            strip = findMinDistance(points, thirdDPoints, thirdDPoints, 0, stripY.size() - 1, dimension - 1);
        } else strip = stripClose(stripY, d);

        return Math.min(d, strip);
    }

    private double solve(Point[] points) {
        Point[] py = Arrays.copyOf(points, points.length);
        Point[] pz = Arrays.copyOf(points, points.length);
        Arrays.sort(points, (a, b) -> (int) (a.getX() - b.getX()));
        Arrays.sort(py, (a, b) -> (int) (a.getY() - b.getY()));
        Arrays.sort(pz, (a, b) -> (int) (a.getZ() - b.getZ()));
        ArrayList<Point> yPoints = new ArrayList<>(Arrays.asList(py));
        ArrayList<Point> zPoints = new ArrayList<>(Arrays.asList(pz));
        return findMinDistance(points, yPoints, zPoints, 0, points.length - 1, 3);
    }
}

class Point {
    private double x, y, z;

    Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    double getZ() {
        return z;
    }
}

