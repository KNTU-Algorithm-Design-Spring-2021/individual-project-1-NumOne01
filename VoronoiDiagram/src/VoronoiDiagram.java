import java.util.Scanner;

public class VoronoiDiagram {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int rows = in.nextInt(), cols = in.nextInt(), n = in.nextInt();
        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            points[i] = new Point(in.nextInt(), in.nextInt());
        }

        VoronoiDiagram voronoiDiagram = new VoronoiDiagram();

        int[][] answer = voronoiDiagram.solve(points, rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                System.out.print(answer[j][i] + " ");
            System.out.println();
        }
    }

    private double dist(Point a, Point b) {
        return Math.hypot(a.getX() - b.getX(), a.getY() - b.getY());
    }

    private Point minDistFromCorner(Point[] points, Point corner) {
        double distance, minDistance = Double.MAX_VALUE;
        int minIndex = 0;


        for (int i = 0; i < points.length; i++) {
            distance = dist(points[i], corner);
            if (distance < minDistance) {
                minIndex = i;
                minDistance = distance;
            }
        }

        return points[minIndex];
    }

    private Point hasSameDistances(Point[] points, Box box) {
        Point[] minDistances = new Point[4];

        minDistances[0] = minDistFromCorner(points, new Point(box.getX1(), box.getY1()));
        minDistances[1] = minDistFromCorner(points, new Point(box.getX1(), box.getY2()));
        minDistances[2] = minDistFromCorner(points, new Point(box.getX2(), box.getY1()));
        minDistances[3] = minDistFromCorner(points, new Point(box.getX2(), box.getY2()));

        for (int i = 0; i < minDistances.length - 1; i++) {
            if (minDistances[i] != minDistances[i + 1])
                return null;
        }
        return minDistances[0];
    }

    private void fillPlane(int[][] plane, int fillItem, Box box) {
        for (int i = box.getX1(); i <= box.getX2(); i++) {
            for (int j = box.getY1(); j <= box.getY2(); j++) {
                plane[i][j] = fillItem;
            }
        }
    }

    private void voronoi(Point[] points, Box box, int[][] plane) {
        Point point = hasSameDistances(points, box);
        if (point != null) {
            fillPlane(plane, point.getX() * 10 + point.getY(), box);
            return;
        }

        int midX = box.getX1() + (box.getX2() - box.getX1()) / 2,
                midY = box.getY1() + (box.getY2() - box.getY1()) / 2;

        Box box1 = new Box(box.getX1(), midX, box.getY1(), midY),
                box2 = new Box(midX + 1, box.getX2(), box.getY1(), midY),
                box3 = new Box(box.getX1(), midX, midY + 1, box.getY2()),
                box4 = new Box(midX + 1, box.getX2(), midY + 1, box.getY2());

        voronoi(points, box1, plane);
        voronoi(points, box2, plane);
        voronoi(points, box3, plane);
        voronoi(points, box4, plane);
    }

    private int[][] solve(Point[] points, int rows, int cols) {
        int[][] plane = new int[rows][cols];
        voronoi(points, new Box(0, rows - 1, 0, cols - 1), plane);
        return plane;
    }
}

class Point {
    private final int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}


class Box {
    private final int x1, x2, y1, y2;

    Box(int x1, int x2, int y1, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }
}
