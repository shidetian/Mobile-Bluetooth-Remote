/*
Copyright (C) 2008 Nikolaos Fotiou
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package gui;

import java.io.IOException;
import javax.microedition.lcdui.*;
import remoteclient.Remote;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * This is the canvas that handles the mouse and keyboard mode.
 * It has a keylistener and whenever a key is pressed or released
 * it calls  the appropriate method of the remote.Remote class.
 * @author Nikos Fotiou
 */
public class MainCanvas extends Canvas implements CommandListener, Runnable {

    private Remote remote;
    private Command back;
    private Command helpcmd;
    private int mode;
    private byte[] screen; // The server screen
    private boolean initialise = true;//It is used to store the first time paint is called
    private int width; //The screen width
    private int height; // The screen height
    private int startX; //Used to implement touchscreen mouse
    private int startY;//Used to implement touchscreen mouse
    private long tapAt;
    private boolean moved = false; //Stop clicking for swiping motion
    private int threshold = 3; //The threshold a touchscreen mouse event is sent (reduce bandwidth from shaky fingers)
    private double scale = 1; //The scale of the screenshot
    private Thread t; //Mousethread
    private boolean cont; //If true keep mouving mouse
    private int step; //number of pixels to move the cursor
    private String mouseCmd; //
    private TextBox typingScreen;
    /**
     * It creates a new instance of MainCanvas
     * @param remote The core Remote class
     */
    public MainCanvas(remoteclient.Remote remote) {
        back = new Command("Back", Command.BACK, 1);
        helpcmd = new Command("Help", Command.HELP,0);
        this.remote = remote;
        this.mode = remote.mode;
        screen = new byte[0];
        this.setFullScreenMode(true);
        width = this.getWidth();
        height = this.getHeight();
        step = 5;
        cont = false;
    }

    /**
     * It is trigerred by the Canvas whenever a key s beeing pressed.
     * It checks the mode and it sens the appropriate command using
     * remote's core class bluetooth clas.
     * @param keyCode The keycode of the key pressed
     */
    public void keyPressed(int keyCode) {
        if (getGameAction(keyCode) == Canvas.UP) {
            keyCode = 50;
        }
        else if (getGameAction(keyCode) == Canvas.DOWN) {
            keyCode = 56;
        }
        else if (getGameAction(keyCode) == Canvas.LEFT) {
            keyCode = 52;
        }
        else if (getGameAction(keyCode) == Canvas.RIGHT) {
            keyCode = 54;
        }
        else if (getGameAction(keyCode) == Canvas.FIRE) {
            keyCode = 49;
        }else if (keyCode == -6||keyCode ==-1||keyCode ==-21) {//left softkey
            this.commandAction(helpcmd, this);
        }
        else if (keyCode == -7||keyCode ==-4||keyCode ==-22) {//right softkey
            this.commandAction(back, this);
        }
        if (mode == remoteclient.Remote.MOUSE_MODE) {
            try {
                switch (keyCode) {
                    case 50:
                        if (t == null){
                            mouseCmd = "MUP";
                            cont = true;
                            t = new Thread(this);
                            t.start();
                        }
                        break;
                    case 56:
                        if (t == null){
                            mouseCmd = "MDOWN";
                            cont = true;
                            t = new Thread(this);
                            t.start();
                        }
                        break;
                    case 52:
                        if (t == null){
                            mouseCmd = "MLEFT";
                            cont = true;
                            t = new Thread(this);
                            t.start();
                        }
                        break;
                    case 54:
                        if (t == null){
                            mouseCmd = "MRIGHT";
                            cont = true;
                            t = new Thread(this);
                            t.start();
                        }
                        break;
                    case 49:
                        remote.bluetooth.SendData("MLCLICK");
                        break;
                    case 51:
                        remote.bluetooth.SendData("MRCLICK");
                        break;
                    case 42:
                        remote.bluetooth.SendData("MWUP");
                        break;
                    case 35:
                        remote.bluetooth.SendData("MWDOWN");
                        break;
                    case 48:
                        remote.bluetooth.SendData("MWPRESS");
                        break;
                    case -8:
                    	scale = 1.0;
                    	remote.bluetooth.SendData("SUPDATE"+scale);
                    	break;
                    case -10:
                    	scale-=0.1;
                    	if(scale<=0)
                    		scale = 1.0;
                    	remote.bluetooth.SendData("SUPDATE"+scale);
                    	break;
                    case 57: //9
                    	remote.bluetooth.SendData("AUPDATE");
                    	break;
                    default: //remote.bluetooth.resetInput();
                    remote.bluetooth.resetInput();
                    remote.bluetooth.SendData("ACK");//remote.bluetooth.SendData("msg "+keyCode);
                    break;
                }
            } catch (IOException ex) {
                remote.doAlert("Error in sending command", -1, this);
                remote.doAlert(ex.toString(), -1, this);
            }
        }
        if (mode == remoteclient.Remote.KEY_MODE) {
            try {
                switch (keyCode) {
                	case Canvas.KEY_STAR:
                		remote.display.setCurrent(createNewTypingScreen());
                		break;
                	case Canvas.KEY_POUND:
                		remote.bluetooth.SendData("K27"); //Escape
                		break;
                    case 50:
                        remote.bluetooth.SendData("K38");
                        break;
                    case 56:
                        remote.bluetooth.SendData("K40");
                        break;
                    case 52:
                        remote.bluetooth.SendData("K37");
                        break;
                    case 54:
                        remote.bluetooth.SendData("K39");
                        break;
                    case 49:
                        remote.bluetooth.SendData("K10");
                        break;
                    case 51:
                        remote.bluetooth.SendData("K32");
                        break;
                    case 55:
                        remote.bluetooth.SendData("K9");
                        break;
                    case 57:
                        remote.bluetooth.SendData("K8");
                        break;
                }
            } catch (IOException ex) {
                remote.doAlert("Error in sending command", -1, this);
            }
        }
    }

    /**
     * It is trigerred by the Canvas whenever a key s beeing released.
     * It checks the mode and it sens the appropriate command using
     * remote's core class bluetooth clas.
     * @param keyCode The keycode of the key released
     */
    public void keyReleased(int keyCode) {
        
        if (getGameAction(keyCode) == Canvas.UP) {
            keyCode = 50;
        }
        else if (getGameAction(keyCode) == Canvas.DOWN) {
            keyCode = 56;
        }
        else if (getGameAction(keyCode) == Canvas.LEFT) {
            keyCode = 52;
        }
        else if (getGameAction(keyCode) == Canvas.RIGHT) {
            keyCode = 54;
        }
        else if (getGameAction(keyCode) == Canvas.FIRE) {
            keyCode = 49;
        }
        if (mode == remoteclient.Remote.MOUSE_MODE) {
            try {
                switch (keyCode) {
                    case 50:
                    case 56:
                    case 52:
                    case 54:
                        cont = false;
                        break;
                    case 49:
                        remote.bluetooth.SendData("SMLCLICK");
                        break;
                    case 51:
                        remote.bluetooth.SendData("SMRCLICK");
                        break;
                    case 42:
                        remote.bluetooth.SendData("SMWUP");
                        break;
                    case 35:
                        remote.bluetooth.SendData("SMWDOWN");
                        break;
                    case 48:
                        remote.bluetooth.SendData("SMWPRESS");
                        break;
                }
            } catch (IOException ex) {
                remote.doAlert("Error in sending command", -1, this);
            }
        }
        if (mode == remoteclient.Remote.KEY_MODE) {
            try {
                switch (keyCode) {
                    case 50:
                        remote.bluetooth.SendData("SK38");
                        break;
                    case 56:
                        remote.bluetooth.SendData("SK40");
                        break;
                    case 52:
                        remote.bluetooth.SendData("SK37");
                        break;
                    case 54:
                        remote.bluetooth.SendData("SK39");
                        break;
                    case 49:
                        remote.bluetooth.SendData("SK10");
                        break;
                    case 51:
                        remote.bluetooth.SendData("SK32");
                        break;
                    case 55:
                        remote.bluetooth.SendData("SK9");
                        break;
                    case 57:
                        remote.bluetooth.SendData("SK8");
                        break;
                }
            } catch (IOException ex) {
                remote.doAlert("Error in sending command", -1, this);
            }
        }

    }

    /**
     * Handler for the back and help commands
     */
    public void commandAction(Command com, Displayable dis) {
    	if (dis==typingScreen){
    		if (com.getCommandType()==Command.OK){
    			Thread stringSend = new Thread(new Runnable(){
					public void run() {
						try {
							remote.bluetooth.SendData("TYPE"+(typingScreen.getString()));
						} catch (IOException e) {}
					}
    			});
    			stringSend.start();
    		}else if (com.getCommandType()==Command.CANCEL){
    			remote.display.setCurrent(this);
    		}
    	}else{
            if (com == back) {
                remote.backCommandPressed(Remote.MAINCANVASID);
            }
            if (com == helpcmd) {
                remote.helpCommandPressed(Remote.MAINCANVASID);
            }
    	}
    }

    /**
     * It paints the appropriate message to the screen
     * according to the mode
     */
    public void paint(Graphics g) {
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(0, 0, 0);
        g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
        int tHeight = 0;
        //Draw the help and back command
        g.drawString("Help?", 1, height , Graphics.BOTTOM|Graphics.LEFT);
        g.drawString("Back", width, height , Graphics.BOTTOM|Graphics.RIGHT);
        /*if (remote.mode == remoteclient.Remote.MOUSE_MODE) {
            tHeight = drawText(2,2,width,-1,g,"Mode:Mouse press help for instructions");
        }
        if (remote.mode == remoteclient.Remote.KEY_MODE) {
            tHeight = drawText(2,2,width,-1,g,"Mode:Keyboard press help for instructions");
        }*/
        if (initialise) {//send the dimensions of the screen remaining to the server
            int restx = width;
            int resty = height - tHeight - g.getFont().getHeight() - 2;
            try {
                remote.bluetooth.SendData("DIM" + restx + "Y" + resty);
            } catch (Exception e) {
                remote.doAlert("Error in sending command", -1, this);
            }
            initialise = false;
        }
        if (screen.length > 0) {
           g.drawImage(Image.createImage(screen, 0, screen.length), 0, tHeight, Graphics.TOP | Graphics.LEFT);
        }
        if (!remote.bluetooth.isAlive){
        	g.setColor(255, 0, 0);
        	g.drawString("!", width/2, height , Graphics.BOTTOM|Graphics.HCENTER);
        }

    }
    
    /**
     * Finger first pressed on screen
     */
     protected void pointerPressed(int x, int y) {
    	 tapAt = System.currentTimeMillis();
       startX = x;
       startY = y;
       moved = false;
     }
     
     /**
     * Finger dragging on screen
     */
     protected void pointerDragged(int x, int y) {
    	 if (Math.abs(x-startX)>threshold||Math.abs(y-startY)>threshold){
        	 try {
        		 remote.bluetooth.SendData("MTELE"+(x-startX)+";"+(y-startY));
            	 startX = x;
             	 startY = y;
             	 moved = true;
     		} catch (IOException e) {
     			remote.doAlert("Error in sending command", -1, this);
     		}
    	 }
     }
     
     protected void pointerReleased(int x, int y){
    	 try{
	    	 if (System.currentTimeMillis()-tapAt<500&&!moved){ //Tap and release less then half a second
	    		 remote.bluetooth.SendData("MLCLICK"); //Imitate click
	    		 remote.bluetooth.SendData("SMLCLICK"); //Imitate release
	    	 }
    		 remote.bluetooth.SendData("SUPDATE"+scale);
        	 startX = x;
         	 startY = y;
 		} catch (IOException e) {
 			remote.doAlert("Error in sending command", -1, this);
 		}
     }
     
     private TextBox createNewTypingScreen(){
 		typingScreen = new TextBox("Input Text", "", 5000, TextField.ANY);
		typingScreen.setCommandListener(this);
		typingScreen.addCommand(new Command("Type", Command.OK, 1));
		typingScreen.addCommand(new Command("Back", Command.CANCEL, 1));
		return typingScreen;
     }
     
    /**
     * It draws text
     * @param x the x pos of text
     * @param y the y pos the text
     * @param twidth the width of the text
     * @param theight the height of the text
     * @param g the graphics to draw at
     * @param text the text to draw
     * @return the height used
     */
    public int drawText(int x, int y,int twidth,int theight,Graphics g, String text){
        int lines = 0;
        int fheight = g.getFont().getHeight();
        int startOfWord = 0;
        int endOfWord = 0;
        int rWidth = twidth -2;
        text = text + " ";
        while (endOfWord < text.length()){
            startOfWord = endOfWord;
            endOfWord = text.indexOf(" ",startOfWord + 1);
            if (endOfWord == -1){
                endOfWord = text.length();
            }else{
                String tempWord = text.substring(startOfWord,endOfWord +1);
                int tempWordL = g.getFont().stringWidth(tempWord);
                if ( tempWordL < rWidth){
                    g.drawString(tempWord, twidth - rWidth, (lines*fheight)+2, Graphics.TOP | Graphics.LEFT);
                    rWidth = rWidth - tempWordL;
                }else{
                    lines++;
                    rWidth = twidth -2;
                    g.drawString(tempWord, twidth - rWidth, (lines*fheight)+2, Graphics.TOP | Graphics.LEFT);
                }
            }
        }
        
        return (lines+1)* fheight;
    }
    /**
     * Set the image that represents the server screen
     * @param data the bytes that hold the image
     */
    public void setScreen(byte[] data) {
        this.screen = data;
        repaint();
    }
    public void run() {
        while (cont) {
            try{
                remote.bluetooth.SendData(mouseCmd+(step++));
                Thread.sleep(100);
            }catch(Exception e){}
        }
        step = 5;
        t = null;
     }
}

