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


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

/**
 * This class is used to move the mouse cursor. It starts moving the cursor 
 * until the boolean variable cont becomes false
 * @author Nikos Fotiou
 */
public class MouseControl {

    private java.awt.Robot robot;
    private RemoteServer rs;
    /**
     * Constant for the left click
     */
    public static final int LCLICK = 16;
    /**
     * Constant for the right click
     */
    public static final int RCLICK = 4;
    /**
     * Constant for the wheel click
     */
    public static final int WCLICK = 8;
  

    /**
     * It creates a new instance of the MouseMove
     */
    public MouseControl(RemoteServer rs) {
        try {
            this.rs = rs;
            robot = new java.awt.Robot();
        } catch (Exception e) {
        }
    }
    
    public java.awt.Point getCursorPos(){
        return MouseInfo.getPointerInfo().getLocation();
    } 
    
    /**
     * It performs a mouse click
     * @param button it represents to be clicked. Constants
     * LCLICK, RCLICK, WCLICK can be used 
     */
    public void mClick(int button) {
        robot.mousePress(button);
    }

    /**
     * It performs a mouse button release
     * @param button it represents to be clicked. Constants
     * LCLICK, RCLICK, WCLICK can be used 
     */
    public void mRelease(int button) {
        robot.mouseRelease(button);
        sendScreen();
    }

    /**
     * It performs a wheel movement
     * @param direction -1 for up, 1 for down 
     */
    public void mWheel(int direction) {
        robot.mouseWheel(direction);
        sendScreen();
    }

    /**
     * It controls the mouse movement.
     * In order to move the mouse the class mouse.MouseCursor is being used. This
     * class is found in the win32lib.jar and it uses the win32lib.dll, which is
     * a JNI dll that invokes native Windows API's that return the cursor's current
     * position.
     * @param x  is set to -1 to move cursor to the left, 1 to the right
     * @param y  is set to -1 to move cursor up, 1 down
     * @param step number of pixels to move the cursor
     */
    public void moveMouse(int x, int y, int step) {
        java.awt.Point point = getCursorPos();
        robot.mouseMove(point.x + x * step, point.y + y * step);
        sendScreen();
    }
    
    public void teleportMouse(int x, int y, double multiplier){
    	java.awt.Point point = getCursorPos();
    	robot.mouseMove((int)Math.round(point.x + x*multiplier),(int)Math.round(point.y + y*multiplier));
    }


    /**
     * It send to the client a screenshot of the area around the mouse
     */
    public void sendScreen() {
    	sendScreen(1.0);
    }
    
    public void sendScreen(double scale) {
    	java.awt.Point point = getCursorPos();
    	point.setLocation(point.x*scale, point.y*scale); //Calculate scaled pointer position
        int startx, starty;
        
        //Take a screenshot and apply scale
        Image screen = robot.createScreenCapture(new java.awt.Rectangle(0,0,rs.sx,rs.sy));
        int scaledX = Math.max(1, (int) (rs.sx*scale));
        int scaledY = Math.max(1, (int) (rs.sy*scale));
        BufferedImage bscreen = new BufferedImage(scaledX, scaledY,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphicsTemp = bscreen.createGraphics();
        graphicsTemp.drawImage(screen, 0,0,bscreen.getWidth(),bscreen.getHeight(),null);
    	
        //Get the inverse of the pixel where the mouse is
        int originalColor = bscreen.getRGB(point.x, point.y);
        int  red = Math.abs(((originalColor & 0x00ff0000) >> 16)-255);
        int  green = Math.abs(((originalColor & 0x0000ff00) >> 8)-255);
        int  blue = Math.abs((originalColor & 0x000000ff)-255);
        graphicsTemp.setColor(new Color(red,green,blue));
        
        //Draw the cursor
        graphicsTemp.drawLine(point.x-5, point.y-5, point.x+5, point.y+5);
        graphicsTemp.drawLine(point.x+5, point.y-5, point.x-5, point.y+5);
        graphicsTemp.dispose();
        
        //Calculate portion of screen to send
        if (point.x - (int) (rs.screenx / 2) < 0) {
            startx = 0;
        } else if (point.x + (rs.screenx / 2) > rs.sx*scale) {
            startx = (int) (rs.sx*scale - rs.screenx);
        } else {
            startx = point.x - (int) (rs.screenx / 2);
        }
        if (point.y - (int) (rs.screeny / 2) < 0) {
            starty = 0;
        } else if (point.y + (rs.screeny / 2) > rs.sy*scale) {
            starty = (int) (rs.sy*scale - rs.screeny);
        } else {
            starty = point.y - (int) (rs.screeny / 2);
        }

        //Get the portion of the screen to send to device
        try {
			bscreen = bscreen.getSubimage(startx, starty, Math.min(rs.screenx,bscreen.getWidth()-startx), Math.min(rs.screeny, bscreen.getHeight()-starty));
		} catch (RasterFormatException  e1) { //We've zoomed too much, much easier to catch it then doing if statements to check in advance
		}
        
        java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
        try {
            javax.imageio.ImageIO.write(bscreen, "jpg", bout);
            byte[] tempimage = bout.toByteArray();
            if (rs.bluetooth.SendDataIfReady("SCRC " + tempimage.length)){
                rs.bluetooth.SendData(tempimage);
                //rs.bluetooth.SendData("END");
            }
        } catch (Exception e) {
            System.out.println("error" + e.toString());
        }
    }
}
