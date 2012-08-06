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
import remoteclient.Remote;

/**
 * This List displays the available utilites
 * @author Nikos Fotiou
 */

public class UtilList extends List implements CommandListener {
    
    private Command back;
    private Remote remote;
    
    /**
     * It creates a new instance of the UtilList class
     * @param remote The core Remote class
     */
    public UtilList(Remote remote) {
        super("Utilities",Choice.IMPLICIT);
        back = new Command("Back",Command.BACK,1);
        this.append("Run command",null);
        this.append("Send message",null);
        this.addCommand(back);
        this.remote= remote;
        this.setCommandListener(this);
    }
    
     /**
     * Handler for the back and List.Select commands
     */
    public void commandAction(Command com, Displayable dis) {
        if (com == back)
            remote.backCommandPressed(Remote.UTILLIST);
        if (com == List.SELECT_COMMAND){
            if (this.getSelectedIndex()==0)
                remote.commandListRunSelected();
            if (this.getSelectedIndex()==1)
                remote.commandListSendMessageSelected();            
        }
        
    }
}
