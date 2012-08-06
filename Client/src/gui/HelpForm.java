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

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import remoteclient.Remote;

/**
 * This form is used to display the help text
 * @author Nikos Fotiou
 *
 */
public class HelpForm extends Form implements CommandListener{
    private StringItem helptxt;
    private Command back;
    private Remote remote;
    
    /**
     * It creates a new instance of HelpForm
     * @param remote The core Remote class
     */
    public HelpForm(Remote remote){
        super("Help");
        this.remote =remote;
        String mouse_help = "Use 2,8,4,6 to move the mouse Up, Down, Left, Right\n"+
                "Or the joystic\n"+
                "Use 1 for left click, 3 for right click\n"+
                "Use * to scroll up, # to scroll down, 0 to press the wheel\n"+
                "Use the call button to increase zoom, back key to reset\n"+
                "Press 9 to enable/disable autoupdate of screenshot\n"+
                "Press 7 to reset connection\n"+
                "Press back to go to the mode menu";
        String key_help   = "Use 2,8,4,6 to move the cursor Up, Down, Left, Right\n"+
                "Use 1 for enter, 3 for space bar\n"+
                "Use 7 for tab, 9 for backspace\n"+
                "Use # for escape key\n"+
                "Use * for arbitrary typing\n"+
                "Press back to go to the mode menu";
        String app_help  =  "Choose the desired application\n"+
                "and control it by using the predifined actions\n"+
                "the application must be in focus on the server\n";
        String mode_help =  "Choose Mouse mode to control your mouse\n"+
                "Choose Keyboard mode to control your keyboard\n"+
                "Choose Application mode to control a specific application";
        if (remote.mode == remoteclient.Remote.MOUSE_MODE){
            helptxt   = new StringItem("Mouse Mode",mouse_help);
        }else if (remote.mode == remoteclient.Remote.APP_MODE){
            helptxt   = new StringItem("Application Mode",app_help);
        }else if (remote.mode == remoteclient.Remote.CHOOSE_MODE){
            helptxt   = new StringItem("Choose Mode",mode_help);
        }else{
            helptxt   = new StringItem("Key Mode",key_help);
        }
        back = new Command("Back",Command.BACK,1);
        this.append(helptxt);
        this.addCommand(back);
        this.setCommandListener(this);
    }
    
    /**
     * Handler for the back command
     */
    public void commandAction(Command com, Displayable dis) {
        if (com == back)
            remote.backCommandPressed(Remote.HELPFORMID);
    }
    
}
