import java.awt.Color;
import java.util.ArrayList;

public class Circle {

    static ArrayList<Circle> circles = new ArrayList<Circle>();
    private int circleNum;
    static Circle lastCircle;
    private float unitCircleRadius;
    private float[] unitCircleCenter;
    private float[] unitCircleLocation;
    private float x = 0;
    private float xStep;
    private ArrayList<Float> yHistory = new ArrayList<Float>();
    private Color color;

    /**
     * y = a sin(bx + c) + d
     * <br>
     * c and d are irrelevant in this case.
     *
     * @param a Amplitude or Radius of Unit Circle
     * @param b speed of revolution/circle/arm or frames per revolution when divided by Fourier.accuracy or horizontal stretch/shrink.
     */
    public Circle(double a, double b) {
        circles.add(this);
        lastCircle = this;
        this.circleNum = circles.size()-1;
        this.unitCircleRadius = (float) (a*Fourier.unitCircleScale);
        this.xStep = (float) (Math.PI*2/(b*Fourier.accuracy));

        //calculate or re-calculate position of first circle using the total radius when a new circle is added
        float totalAmplitude = 0; //equal to all radiuses added together
        for(Circle c : circles) {
            totalAmplitude += c.getUnitCircleRadius();
        }
        circles.get(0).unitCircleCenter = new float[] {Fourier.width/20f + totalAmplitude, Fourier.height/2f};

        int bottomColorBoundary = 150;
        int topColorBoundary = 20;
        this.color = new Color( (int) ((Math.random()*(255 - bottomColorBoundary - topColorBoundary)) + bottomColorBoundary),
                (int) ((Math.random()*(255 - bottomColorBoundary - topColorBoundary)) + bottomColorBoundary),
                (int) ((Math.random()*(255 - bottomColorBoundary - topColorBoundary)) + bottomColorBoundary));

        this.calculate();
    }

    public float[] getUnitCircleCenter() {
        return this.unitCircleCenter;
    }
    public float[] getUnitCircleLocation() {
        return this.unitCircleLocation;
    }
    public float getUnitCircleRadius() {
        return this.unitCircleRadius;
    }
    public ArrayList<Float> getYHistory() {
        return this.yHistory;
    }
    public Color getColor() {
        return this.color;
    }

    public float calculate() {
        float y = (float) -Math.sin(this.x); //why does sin have to be negative for it to move in the right direction?
        yHistory.add(y);
        update();
        return y;
    }
    private void update() {
        if(circles.get(0) != this) {
            this.unitCircleCenter = new float[] {circles.get(this.circleNum-1).getUnitCircleLocation()[0], circles.get(this.circleNum-1).getUnitCircleLocation()[1]};
        }
        this.unitCircleLocation = new float[] {(float) (Math.cos(this.x) * this.unitCircleRadius + this.unitCircleCenter[0]), (float) (this.yHistory.get(this.yHistory.size()-1) * this.unitCircleRadius + this.unitCircleCenter[1])};
    }
    public void next() {
        this.x += this.xStep;
    }
}
