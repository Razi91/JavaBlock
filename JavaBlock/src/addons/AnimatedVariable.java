package addons;

/**
 * Variable used to animations, set new value, duration and method of approximation
 * @author razi
 */
public class AnimatedVariable{
    public AnimatedVariable(){
        timeStart=System.currentTimeMillis();
    }
    
    protected float varOld=0, varNew=0;
    protected long timeStart, timeAnim=500;
    
    private int method=2;
    public static final int METHOD_LINE=1;
    public static final int METHOD_SINE=2;
    public static final int METHOD_COS=3;
    public static final int METHOD_TAN=4;
    protected double elapsed;

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }
   

    public java.lang.Float get() {
        elapsed = ((double) System.currentTimeMillis() - timeStart) / timeAnim;
        if (elapsed > 1.0) 
            elapsed = 1.0;
        switch (method) {
            case -METHOD_LINE://line
                elapsed = elapsed * 2 - 1;
                break;
            case METHOD_SINE:
                elapsed = Math.sin(elapsed * Math.PI / 2);
                break;
            case -METHOD_SINE:
                elapsed = Math.sin(elapsed * Math.PI);
                break;
            case METHOD_TAN:
                elapsed = Math.tan(elapsed * Math.PI / 4);
                break;
            case -METHOD_TAN:
                elapsed = Math.tan(elapsed * Math.PI / 2);
                break;
        }
        return (float) (varOld * (1 - elapsed) + varNew * elapsed);
    }  
    public java.lang.Float getByMethod(int method, boolean repeat) {
        elapsed = ((double) System.currentTimeMillis() - timeStart) / timeAnim;
        if (!repeat && elapsed > 1.0) 
            elapsed = 1.0;
        switch (method) {
            case -METHOD_LINE://line
                elapsed = elapsed * 2 - 1;
                break;
            case METHOD_SINE:
                elapsed = Math.sin(elapsed * (Math.PI / 2));
                break;
            case -METHOD_SINE:
                elapsed = Math.sin(elapsed * Math.PI);
                break;
            case METHOD_COS:
                elapsed = Math.sin(elapsed * (Math.PI / 2));
                break;
            case -METHOD_COS:
                elapsed = Math.sin(elapsed * Math.PI);
                break;
            case METHOD_TAN:
                elapsed = Math.tan(elapsed * (Math.PI / 4));
                break;
            case -METHOD_TAN:
                elapsed = Math.tan(elapsed * (Math.PI / 2));
                break;
        }
        return (float) (varOld * (1 - elapsed) + varNew * elapsed);
    }  
    public void set(float var) {
        if(method>0)
            varOld=get();
        varNew = var;
        timeStart=System.currentTimeMillis();
        elapsed=0;
    }
    public void set(float var, long time) {
        if(method>0)
            varOld=get();
        varNew = var;
        timeStart=System.currentTimeMillis();
        timeAnim=time;
        elapsed=0;
    }
    public void set(float var, long time, int method) {
        if(method>0)
            varOld=get();
        varNew = var;
        timeStart=System.currentTimeMillis();
        timeAnim=time;
        elapsed=0;
        this.method=method;
    }
    public boolean isAnimating(){
        return System.currentTimeMillis()-timeStart<=timeAnim;
    }
}
