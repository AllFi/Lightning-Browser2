package allfi.touch.primitives;

/**
 * Created by Александр on 05.08.2017.
 */
// Класс, представляющий точку касания
public class Point {
    public double X;
    public double Y;
    public double Size;
    public long Time;

    public Point(double x, double y, double size, long time){
        X = x;
        Y = y;
        Size = size;
        Time = time;
    }

    public String ToString(){
        return String.valueOf(X) + ";" + String.valueOf(Y)+ ";" + String.valueOf(Size)+ ";" + String.valueOf(Time);
    }
}