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
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import remoteclient.Remote;

/**
 * It displays the list where the user can choose between various modes
 * @author Nikos Fotiou
 *
 */
public class ModeList extends List implements CommandListener {
    
    private Command back;
    private Command helpcmd;
    private Remote remote;
    
    /**
     * It creates a new instance of the ModeList. Here
     * the user can choose between the available modes
     * @param remote The core Remote Class
     */
    public ModeList(Remote remote){
        super("Choose Mode",Choice.IMPLICIT);
        this.remote = remote;
        back    = new Command("Back",Command.BACK,1);
        helpcmd = new Command("Help",Command.HELP,1);
        try{
        this.append("Mouse mode",Image.createImage("/img/mouse.png"));
        this.append("Keyboard mode",Image.createImage("/img/keyboard.png"));
        this.append("Application mode",Image.createImage("/img/application.png"));
        this.append("Utilities",Image.createImage("/img/utilities.png"));
        }catch(Exception e){}
        this.addCommand(back);
        this.addCommand(helpcmd);
        this.setCommandListener(this);
    }
    
    /**
     * Handler for the list selected back and help commands
     */
    public void commandAction(Command com, Displayable dis) {
        if (com == back)
            remote.backCommandPressed(Remote.MODELIST);
        if (com == helpcmd)
            remote.helpCommandPressed(Remote.MODELIST);
        if (com == List.SELECT_COMMAND)
            remote.modeListModeSelected(this.getSelectedIndex());
    }
    
}
