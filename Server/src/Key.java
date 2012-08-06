/**
 * @author Tate
 */

public class Key {
    
    private int tCode;
    private String tChar;
    private boolean shift;
    
    public Key(int k, char c){
        this.tCode=k;
        this.tChar=c+"";
        this.shift=false;
    }
    
    public Key(int k, String c){
        this.tCode=k;
        this.tChar=c;
        this.shift=false;
    }
    
    public Key(int k, String c, boolean shift){
        this.tCode=k;
        this.tChar=c;
        this.shift=shift;
    }
    
    public void setCode(int k){
        this.tCode=k;
    }
    
    public int getCode(){
        return this.tCode;
    }
    
    public void setChar(char c){
        this.tChar=c+"";
    }
    
    public String getChar(){
        if(this.needShift()){
            return this.tChar.toUpperCase();
        }else{
            return this.tChar;
        }
    }
    
    public void setShift(boolean shift){
        this.shift=shift;
    }
    
    public boolean needShift(){
        return this.shift;
    }
}