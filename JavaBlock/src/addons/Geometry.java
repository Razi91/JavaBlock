package addons;



public class Geometry {
    public static Point Point(double x, double y){
        return new Point(x, y);
    }
}

class Point{
    public double x=0, y=0;
    Point(double x, double y){
        this.x=x; this.y=y;
    }

    public Point add(double x, double y){
        this.x+=x;this.y+=y;
        return this;
    }
    public Point mult(double x, double y){
        this.x*=x;this.y*=y;
        return this;
    }
    public Point mult(double s){
        this.x*=s;this.y*=s;
        return this;
    }
    public double rad(){
        return Math.atan2(y, x);
    }
    public Point normalize(){
        double r=rad();
        this.x=Math.sin(r); this.y=Math.cos(r);
        return this;
    }
    public double length(){
        return Math.sqrt(x*x + y*y);
    }
    public double getX(){return x;}
    public double getY(){return y;}
    public void setX(double x){this.x=x;}
    public void setY(double y){this.y=x;}
    public Point set(double x, double y){this.x=x; this.y=y; return this;}
    @Override
    public String toString(){
        return "("+x+";"+y+")";
    }
}