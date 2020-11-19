import java.awt.*;
import javax.swing.JFrame;


public class Fourier {
    static JFrame frame = new JFrame();
    static Canvas canvas = new Canvas();

    static int iterations = 1;

    final static int width = 1200;
    final static int height = 600;
    final static int sleep = 20;
    final static int maxGraphIterations = 1000;
    final static int accuracy = 100;
    final static double yScale = 1;
    final static double unitCircleScale = 0.5;


    public static void main(String[] args) {
        frameSetup();

        new Circle(100, 1);
        new Circle(33, .33);
        new Circle(20, .20);
        new Circle(14.3, .143);
        new Circle(11.1, .111);
        new Circle(9.1, .909);

        draw();
    }

    static void frameSetup() {
        frame.setSize(width, height);
        frame.setTitle("Fourier Series");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(canvas);
        frame.setVisible(true);
    }

    static void calculate() {
        for(Circle c : Circle.circles) {
            c.calculate();
        }
    }

    static void next() {
        try {Thread.sleep(sleep);} catch (InterruptedException e) {e.printStackTrace();}
        iterations += 1;
        for(Circle c : Circle.circles) {
            c.next();
        }
        draw();
    }

    static void draw() {
        calculate();
        canvas.repaint();
    }

    static class Canvas extends Component {
        public void paint (Graphics g) {
            Graphics2D graphics = (Graphics2D)g;
            graphics.setColor(Color.BLACK);
            graphics.setStroke(new BasicStroke(3));

            //TITLE
            graphics.setFont(graphics.getFont().deriveFont(width/20f));
            graphics.drawString("Fourier Series", width/40, height/9);


            //UNIT CIRCLES
            for(Circle c : Circle.circles) {
                graphics.setColor(c.getColor());
                //circle
                graphics.drawOval((int) (c.getUnitCircleCenter()[0]-(c.getUnitCircleRadius())), (int) (c.getUnitCircleCenter()[1]-(c.getUnitCircleRadius())), (int) c.getUnitCircleRadius()*2, (int) c.getUnitCircleRadius()*2);

                //arm
                graphics.drawLine((int) c.getUnitCircleCenter()[0], (int) c.getUnitCircleCenter()[1], (int) c.getUnitCircleLocation()[0], (int) c.getUnitCircleLocation()[1]);
            }


            //GRAPH
            //x-axis
            float totalAmplitude = 0; //equal to all radiuses added together
            for(Circle c : Circle.circles) {
                totalAmplitude += c.getUnitCircleRadius();
            }
            int graphStartingIteration = Math.min(iterations, Math.max(iterations - maxGraphIterations, 0));
            int graphIterations = Math.min(iterations, maxGraphIterations);
            float startX = (Circle.circles.get(0).getUnitCircleCenter()[0] + totalAmplitude + width/20f);
            float endX = width-(width/15f);
            int xAxisHeight = height/2;
            graphics.setColor(Color.BLACK);
            graphics.drawLine((int)startX, xAxisHeight, (int)endX, xAxisHeight);
            //graph
            double graphXStep = (endX-startX)/((double)graphIterations-1);
            int[] xPoints = new int[graphIterations];
            int[] yPoints = new int[graphIterations];
            for(int i = graphStartingIteration, k = 0; i < iterations; i++, k++) {
                xPoints[k] = (int) (endX - (k * graphXStep));
                double totalY = 0;
                for(Circle c : Circle.circles) {
                    totalY += (c.getYHistory().get(i) * (double) c.getUnitCircleRadius());
                }
                yPoints[k] = (int) ((totalY * Fourier.yScale + xAxisHeight));
            }
            graphics.drawPolyline(xPoints, yPoints, graphIterations);

            //UNIT CIRCLE AND GRAPH CONNECTION
            graphics.setColor(Color.BLUE);
            graphics.drawLine((int) Circle.lastCircle.getUnitCircleLocation()[0], (int) Circle.lastCircle.getUnitCircleLocation()[1], (int) startX, (int) Circle.lastCircle.getUnitCircleLocation()[1]);

            next();
        }
    }
}
