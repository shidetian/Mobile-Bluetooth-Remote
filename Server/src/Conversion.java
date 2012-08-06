/**
 * @author Tate
 */

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Conversion {
    private ArrayList<Key> keys;
    
    public Conversion(){
        keys = new ArrayList<Key>();
        load();
    }
    
    public void load(){
        add(KeyEvent.VK_0,'0');
        add(KeyEvent.VK_1,'1');
        add(KeyEvent.VK_2,'2');
        add(KeyEvent.VK_3,'3');
        add(KeyEvent.VK_4,'4');
        add(KeyEvent.VK_5,'5');
        add(KeyEvent.VK_6,'6');
        add(KeyEvent.VK_7,'7');
        add(KeyEvent.VK_8,'8');
        add(KeyEvent.VK_9,'9');
        add(KeyEvent.VK_A,'A');
        add(KeyEvent.VK_B,'B');
        add(KeyEvent.VK_C,'C');
        add(KeyEvent.VK_D,'D');
        add(KeyEvent.VK_E,'E');
        add(KeyEvent.VK_F,'F');
        add(KeyEvent.VK_G,'G');
        add(KeyEvent.VK_H,'H');
        add(KeyEvent.VK_I,'I');
        add(KeyEvent.VK_J,'J');
        add(KeyEvent.VK_K,'K');
        add(KeyEvent.VK_L,'L');
        add(KeyEvent.VK_M,'M');
        add(KeyEvent.VK_N,'N');
        add(KeyEvent.VK_O,'O');
        add(KeyEvent.VK_P,'P');
        add(KeyEvent.VK_Q,'Q');
        add(KeyEvent.VK_R,'R');
        add(KeyEvent.VK_S,'S');
        add(KeyEvent.VK_T,'T');
        add(KeyEvent.VK_U,'U');
        add(KeyEvent.VK_V,'V');
        add(KeyEvent.VK_W,'W');
        add(KeyEvent.VK_X,'X');
        add(KeyEvent.VK_Y,'Y');
        add(KeyEvent.VK_Z,'Z');
        add(KeyEvent.VK_ENTER,'\n');
        add(KeyEvent.VK_BACK_SPACE,'\b');
        add(KeyEvent.VK_TAB,'\t');
        add(KeyEvent.VK_COMMA,',');
        add(KeyEvent.VK_MINUS,'-');
        add(KeyEvent.VK_PERIOD,'.');
        add(KeyEvent.VK_SLASH,'/');
        add(KeyEvent.VK_SEMICOLON,';');
        add(KeyEvent.VK_EQUALS,'=');
        add(KeyEvent.VK_OPEN_BRACKET,'[');
        add(KeyEvent.VK_BACK_SLASH,'\\');
        add(KeyEvent.VK_CLOSE_BRACKET,']');
        add(KeyEvent.VK_MULTIPLY,'*');
        add(KeyEvent.VK_ADD,'+');
        //add(KeyEvent.VK_SUBTRACT,'-');
        //add(KeyEvent.VK_AMPERSAND,"&");
        this.keys.add(new Key(KeyEvent.VK_7,"&",true));
        //add(KeyEvent.VK_QUOTEDBL,"\"");
        this.keys.add(new Key(KeyEvent.VK_QUOTE,"\"",true));
        //add(KeyEvent.VK_LESS,'<');
        this.keys.add(new Key(KeyEvent.VK_COMMA,"<",true));
        //add(KeyEvent.VK_GREATER,'>');
        this.keys.add(new Key(KeyEvent.VK_PERIOD,">",true));
        //add(KeyEvent.VK_BRACELEFT,'{');
        this.keys.add(new Key(KeyEvent.VK_OPEN_BRACKET,"{",true));
        //add(KeyEvent.VK_BRACERIGHT,'}');
        this.keys.add(new Key(KeyEvent.VK_CLOSE_BRACKET,"}",true));
        //add(KeyEvent.VK_AT,'@');
        this.keys.add(new Key(KeyEvent.VK_2,"@",true));
        //add(KeyEvent.VK_COLON,':');
        this.keys.add(new Key(KeyEvent.VK_SEMICOLON,":",true));
        //add(KeyEvent.VK_CIRCUMFLEX,'^');
        this.keys.add(new Key(KeyEvent.VK_6,"^",true));
        //add(KeyEvent.VK_DOLLAR,'$');
        this.keys.add(new Key(KeyEvent.VK_4,"$",true));
        //add(KeyEvent.VK_EXCLAMATION_MARK,'!');
        this.keys.add(new Key(KeyEvent.VK_1,"!",true));
        //add(KeyEvent.VK_LEFT_PARENTHESIS,'(');
        this.keys.add(new Key(KeyEvent.VK_9,"(",true));
        //add(KeyEvent.VK_NUMBER_SIGN,'#');
        this.keys.add(new Key(KeyEvent.VK_3,"#",true));
        add(KeyEvent.VK_PLUS,'+');
        //add(KeyEvent.VK_RIGHT_PARENTHESIS,')');
        this.keys.add(new Key(KeyEvent.VK_0,")",true));
        //add(KeyEvent.VK_UNDERSCORE,'_');
        this.keys.add(new Key(KeyEvent.VK_MINUS,"_",true));
        add(KeyEvent.VK_SPACE,' ');
        add(KeyEvent.VK_QUOTE, '\'');
        this.keys.add(new Key(KeyEvent.VK_5,"%",true));
        this.keys.add(new Key(KeyEvent.VK_SLASH,"?",true));
        this.keys.add(new Key(KeyEvent.VK_BACK_SLASH,"|",true));
        add(KeyEvent.VK_BACK_QUOTE,"`");
        this.keys.add(new Key(KeyEvent.VK_BACK_QUOTE,"~",true));
        
    }
    
    private void add(int k, char c){
        add(k, c+"");
    }
    
    private void add(int k, String c){
        this.keys.add(new Key(k,c));
    }
    
    public Key getKeyFromChar(char c){
        Key k = null;
        for(int i=0; i<this.keys.size(); i++){
            Key a = this.keys.get(i);
            if((c+"").toUpperCase().equals(a.getChar()+"")){
                k = new Key(a.getCode(),a.getChar(),a.needShift());
                if(Character.isUpperCase(c)){
                    k.setShift(true);
                }
                return k;
            }
        }
        return k;
    }
    
    public Key getKeyFromStringChar(String s){
        return getKeyFromChar(s.charAt(0));
    }
    
    public Key getKeyFromCode(int c){
        Key k = null;
        for(int i=0; i<this.keys.size(); i++){
            if(c==this.keys.get(i).getCode()){
                k = new Key(c,this.keys.get(i).getChar());
                break;
            }
        }
        return k;
    }
    
    public Key[] stringToKeys(String in){
        int size = in.length();
        Key[] keys = new Key[size];
        for(int i=0; i<size; i++){
            keys[i] = getKeyFromChar(in.charAt(i));
        }
        return keys;
    }
    
    public String keysToString(Key[] keys){
        int size = keys.length;
        String out = "";
        for(int i=0; i<size; i++){
            out = out+keys[i].getChar();
        }
        return out;
    }
    
    public Key[] getKeyList(){
        Key[] list = new Key[this.keys.size()];
        for(int i=0; i<this.keys.size(); i++){
            list[i] = this.keys.get(i);
        }
        return list;
    }
}