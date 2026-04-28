import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.imageio.ImageIO;

public class test {
    static final DecimalFormat HEIGHT_FORMAT = new DecimalFormat("0.0");

    public static void main(String[] args) throws IOException {
        List<PhotoMeasurement> photos = List.of(
            PhotoMeasurement.pic1(),
            PhotoMeasurement.pic3()
        );

        for (PhotoMeasurement photo : photos) {
            MeasurementResult result = photo.measure();
            System.out.println(photo.photoName + " measurement");
            System.out.println("  Reference student (JUST DO IT): 180.0 cm");
            for (StudentMeasurement student : result.students()) {
                System.out.println("  " + student.name + ": " + HEIGHT_FORMAT.format(student.heightCm) + " cm");
            }
            System.out.println("  vanishing line: " + result.horizonLine());
            System.out.println("  vertical vanishing point: " + result.verticalVanishingPoint());
            System.out.println();

            photo.writeAnnotatedImage(result);
        }
    }
}

class PhotoMeasurement {
    final String photoName;
    final String inputPath;
    final String outputPath;
    final double referenceHeightCm;
    final Segment2 referencePerson;
    final List<Segment2> familyOneLines;
    final List<Segment2> familyTwoLines;
    final List<Segment2> verticalLines;
    final List<StudentTarget> targets;

    PhotoMeasurement(
        String photoName,
        String inputPath,
        String outputPath,
        double referenceHeightCm,
        Segment2 referencePerson,
        List<Segment2> familyOneLines,
        List<Segment2> familyTwoLines,
        List<Segment2> verticalLines,
        List<StudentTarget> targets
    ) {
        this.photoName = photoName;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.referenceHeightCm = referenceHeightCm;
        this.referencePerson = referencePerson;
        this.familyOneLines = familyOneLines;
        this.familyTwoLines = familyTwoLines;
        this.verticalLines = verticalLines;
        this.targets = targets;
    }

    MeasurementResult measure() {
        Point2 vanishA = Geometry.averageIntersection(familyOneLines);
        Point2 vanishB = Geometry.averageIntersection(familyTwoLines);
        Line2 horizon = Line2.fromPoints(vanishA, vanishB);
        Point2 verticalVanishingPoint = Geometry.averageIntersection(verticalLines);

        StudentMeasurement reference = new StudentMeasurement("Reference", referenceHeightCm, referencePerson);
        List<StudentMeasurement> measuredTargets = targets.stream()
            .map(target -> {
                Point2 auxiliaryVanishing = Geometry.intersection(
                    Line2.fromPoints(referencePerson.base(), target.segment().base()),
                    horizon
                );
                Point2 transferredTop = Geometry.intersection(
                    Line2.fromPoints(auxiliaryVanishing, referencePerson.top()),
                    Line2.fromPoints(target.segment().base(), verticalVanishingPoint)
                );
                double projectedReference = Geometry.distance(target.segment().base(), transferredTop);
                double observedTarget = Geometry.distance(target.segment().base(), target.segment().top());
                double height = referenceHeightCm * observedTarget / projectedReference;
                return new StudentMeasurement(target.name(), height, target.segment(), transferredTop, auxiliaryVanishing);
            })
            .toList();

        return new MeasurementResult(reference, measuredTargets, vanishA, vanishB, horizon, verticalVanishingPoint);
    }

    void writeAnnotatedImage(MeasurementResult result) throws IOException {
        BufferedImage image = ImageIO.read(new File(inputPath));
        BufferedImage annotated = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = annotated.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(255, 180, 0));
        g.setStroke(new BasicStroke(3f));
        drawSegment(g, result.reference().segment);
        drawLabel(g, result.reference().segment.top(), "180.0 cm");

        g.setColor(new Color(0, 220, 255));
        g.setStroke(new BasicStroke(2.5f));
        for (StudentMeasurement student : result.students()) {
            drawSegment(g, student.segment);
            drawLabel(g, student.segment.top(), student.name + " " + test.HEIGHT_FORMAT.format(student.heightCm) + " cm");

            g.setColor(new Color(255, 100, 100));
            drawLine(g, student.segment.base(), result.verticalVanishingPoint());
            g.setColor(new Color(100, 255, 120));
            drawLine(g, student.auxiliaryVanishing, result.reference().segment.top());
            g.setColor(new Color(255, 240, 120));
            drawSegment(g, new Segment2(student.segment.base(), student.transferredTop));
            g.setColor(new Color(0, 220, 255));
        }

        g.setColor(new Color(255, 0, 255));
        g.setStroke(new BasicStroke(2f));
        drawInfiniteLine(g, result.horizonLine(), image.getWidth());
        drawPoint(g, result.vanishA(), Color.YELLOW);
        drawPoint(g, result.vanishB(), Color.YELLOW);

        g.dispose();
        ImageIO.write(annotated, "jpg", new File(outputPath));
    }

    private static void drawSegment(Graphics2D g, Segment2 segment) {
        drawLine(g, segment.base(), segment.top());
    }

    private static void drawLine(Graphics2D g, Point2 a, Point2 b) {
        g.draw(new Line2D.Double(a.x(), a.y(), b.x(), b.y()));
    }

    private static void drawLabel(Graphics2D g, Point2 anchor, String text) {
        g.setColor(Color.BLACK);
        g.fillRect((int) anchor.x() - 8, (int) anchor.y() - 26, text.length() * 8 + 10, 20);
        g.setColor(Color.WHITE);
        g.drawString(text, (int) anchor.x() - 4, (int) anchor.y() - 11);
    }

    private static void drawPoint(Graphics2D g, Point2 point, Color color) {
        g.setColor(color);
        g.fillOval((int) point.x() - 6, (int) point.y() - 6, 12, 12);
    }

    private static void drawInfiniteLine(Graphics2D g, Line2 line, int width) {
        Point2 p1 = line.pointAtX(-width);
        Point2 p2 = line.pointAtX(width * 2.0);
        g.draw(new Line2D.Double(p1.x(), p1.y(), p2.x(), p2.y()));
    }

    static PhotoMeasurement pic1() {
        return new PhotoMeasurement(
            "pic1 (1).jpg",
            "pic1 (1).jpg",
            "pic1_annotated.jpg",
            180.0,
            new Segment2(new Point2(932, 1766), new Point2(926, 1058)),
            List.of(
                new Segment2(new Point2(2800, 1150), new Point2(692, 1491)),
                new Segment2(new Point2(2800, 1150), new Point2(924, 1712))
            ),
            List.of(
                new Segment2(new Point2(-2200, 1160), new Point2(1017, 999)),
                new Segment2(new Point2(-2200, 1160), new Point2(945, 1326))
            ),
            List.of(
                new Segment2(new Point2(900, -9000), new Point2(932, 1766)),
                new Segment2(new Point2(900, -9000), new Point2(577, 2100)),
                new Segment2(new Point2(900, -9000), new Point2(213, 2044)),
                new Segment2(new Point2(900, -9000), new Point2(1252, 1368))
            ),
            List.of(
                new StudentTarget("Gray-shirt student", new Segment2(new Point2(577, 2100), new Point2(571, 1110)))
            )
        );
    }

    static PhotoMeasurement pic3() {
        return new PhotoMeasurement(
            "pic3.jpg",
            "pic3.jpg",
            "pic3_annotated.jpg",
            180.0,
            new Segment2(new Point2(943, 1488), new Point2(938, 983)),
            List.of(
                new Segment2(new Point2(2800, 1150), new Point2(708, 1496)),
                new Segment2(new Point2(2800, 1150), new Point2(881, 1692))
            ),
            List.of(
                new Segment2(new Point2(-2200, 1160), new Point2(1013, 998)),
                new Segment2(new Point2(-2200, 1160), new Point2(969, 1306))
            ),
            List.of(
                new Segment2(new Point2(900, -9000), new Point2(943, 1488)),
                new Segment2(new Point2(900, -9000), new Point2(670, 1754)),
                new Segment2(new Point2(900, -9000), new Point2(209, 2042)),
                new Segment2(new Point2(900, -9000), new Point2(1252, 1369))
            ),
            List.of(
                new StudentTarget("Front black-shirt student", new Segment2(new Point2(670, 1654), new Point2(662, 941)))
            )
        );
    }
}

record MeasurementResult(
    StudentMeasurement reference,
    List<StudentMeasurement> students,
    Point2 vanishA,
    Point2 vanishB,
    Line2 horizonLine,
    Point2 verticalVanishingPoint
) {
}

class StudentMeasurement {
    final String name;
    final double heightCm;
    final Segment2 segment;
    final Point2 transferredTop;
    final Point2 auxiliaryVanishing;

    StudentMeasurement(String name, double heightCm, Segment2 segment) {
        this(name, heightCm, segment, segment.top(), segment.base());
    }

    StudentMeasurement(String name, double heightCm, Segment2 segment, Point2 transferredTop, Point2 auxiliaryVanishing) {
        this.name = name;
        this.heightCm = heightCm;
        this.segment = segment;
        this.transferredTop = transferredTop;
        this.auxiliaryVanishing = auxiliaryVanishing;
    }
}

record StudentTarget(String name, Segment2 segment) {
}

record Segment2(Point2 base, Point2 top) {
}

record Point2(double x, double y) {
    @Override
    public String toString() {
        return "(" + HEIGHT_FORMATTER.format(x) + ", " + HEIGHT_FORMATTER.format(y) + ")";
    }

    private static final DecimalFormat HEIGHT_FORMATTER = new DecimalFormat("0.00");
}

class Line2 {
    final double a;
    final double b;
    final double c;

    Line2(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    static Line2 fromPoints(Point2 p1, Point2 p2) {
        double a = p1.y() - p2.y();
        double b = p2.x() - p1.x();
        double c = p1.x() * p2.y() - p2.x() * p1.y();
        return new Line2(a, b, c);
    }

    Point2 pointAtX(double x) {
        if (Math.abs(b) < 1e-9) {
            double safeX = -c / a;
            return new Point2(safeX, 0);
        }
        double y = (-a * x - c) / b;
        return new Point2(x, y);
    }

    @Override
    public String toString() {
        return String.format("%.4fx + %.4fy + %.4f = 0", a, b, c);
    }
}

class Geometry {
    static Point2 averageIntersection(List<Segment2> segments) {
        double sumX = 0;
        double sumY = 0;
        int count = 0;

        for (int i = 0; i < segments.size(); i++) {
            for (int j = i + 1; j < segments.size(); j++) {
                Point2 intersection = intersection(
                    Line2.fromPoints(segments.get(i).base(), segments.get(i).top()),
                    Line2.fromPoints(segments.get(j).base(), segments.get(j).top())
                );
                sumX += intersection.x();
                sumY += intersection.y();
                count++;
            }
        }

        return new Point2(sumX / count, sumY / count);
    }

    static Point2 intersection(Line2 l1, Line2 l2) {
        double x = l1.b * l2.c - l1.c * l2.b;
        double y = l1.c * l2.a - l1.a * l2.c;
        double z = l1.a * l2.b - l1.b * l2.a;
        return new Point2(x / z, y / z);
    }

    static double distance(Point2 p1, Point2 p2) {
        return Math.hypot(p1.x() - p2.x(), p1.y() - p2.y());
    }
}
