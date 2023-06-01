import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dot extends Circle {
    private boolean covered = true;
    private String name;
    private boolean changed;

    
    
    public Dot(double x, double y, String name){
        super(x,y,10, Color.BLUE);
        this.name = name;        
        relocate(x-10, y-10);
        paintCovered();
    }
    
    public boolean getChanged(){
        return changed;
    }
    
    public void setChanged(boolean changed){
        this.changed = changed;
    }


    public void paintCovered(){

        setFill(Color.BLUE);
       
    }

    public void setCovered(boolean on){
        covered = on;
        if (covered)
            paintCovered();
        else
            paintUncovered();
    }
    
    public String getName() {
    	return name;
    }
    
    public void paintUncovered(){
        
        setFill(Color.RED);
    }
    
    @Override
    public String toString() {
		return name;
    	
    }
    
    
}
