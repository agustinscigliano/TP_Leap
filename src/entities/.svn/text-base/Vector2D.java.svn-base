package entities;

public class Vector2D {
    private int x_pos;
    private int y_pos;
    
    public Vector2D(int x, int y){
        this.x_pos = x;
        this.y_pos = y;
    }
    
    public int x(){
        return x_pos;
    }
    
    public int y(){
        return y_pos;
    }
    
    public boolean equals(Object obj){
        if( obj == null || obj.getClass() != getClass() ){
            return false;
        }
        
        Vector2D other = (Vector2D)obj;
        
        if(this.x() == other.x() && this.y() == other.y()){
            return true;
        }
        
        return false;
    }
    public int hashCode(){
        return x() + y();
    }
}
