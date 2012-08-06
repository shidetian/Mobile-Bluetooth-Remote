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

/**
 * This is a canvas class which is meant to replace Alerts that
 * last forever and we don't want the OK command to be displayed
 * @author Nikos Fotiou
 */
public class SplashScreen extends Form implements CommandListener {
    
    private util.CancelListener cancel;
    private Command cancelCmd;
    public static int INFO = 0;
    public static int ERROR = 1;
    
    /**
     * It creates a new instance of the Splash Screen
     * @param displayText The message to be displayed
     * @param type The type of the Slash Screen, it can be
     * SplashScreen.INFO or SplashScreen.Error
     * @param cancel The class to call if cancel is pressed
     */
    public SplashScreen(String displayText, int type, util.CancelListener cancel){
      super("Remote Control"); 
        try {
            String imgFile = "";
            if (type == INFO) {
                imgFile = "info.png";
            }
            if (type == ERROR) {
                imgFile = "error.png";
            }
            ImageItem img = new ImageItem("", Image.createImage("/img/" + imgFile),
                    ImageItem.LAYOUT_CENTER | ImageItem.LAYOUT_NEWLINE_BEFORE | ImageItem.LAYOUT_NEWLINE_AFTER, "");
            this.append(img);
            StringItem msg = new StringItem("", displayText);
            msg.setLayout(StringItem.LAYOUT_CENTER);
            this.append(msg);
            if (cancel != null){
                this.cancel = cancel;
                cancelCmd = new Command("Cancel",Command.CANCEL,1);
                this.addCommand(cancelCmd);
                this.setCommandListener(this);
            }
        } catch (IOException ex) {}
    }
    /**
     * It creates a new instance of the Splash Screen
     * @param displayText The message to be displayed
     * @param type The type of the Slash Screen, it can be
     * SplashScreen.INFO or SplashScreen.Error
     */
    public SplashScreen(String displayText, int type) {
        this(displayText,type,null);
    }

    public void commandAction(Command cmd, Displayable arg1) {
        if (cmd == cancelCmd){
            cancel.cancelCommandPressed();
        }
    }
}
