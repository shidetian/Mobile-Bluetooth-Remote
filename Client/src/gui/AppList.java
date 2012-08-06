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
 * The list that displays the applications on the server that can be controled
 * by the client
 * @author Nikos Fotiou
 */
public class AppList extends List implements CommandListener{
    
    private Command back;
    private Command helpcmd;
    private Remote remote;
    
    /**
     * It creates a new instance of Applist
     * @param remote The core Remote class
     */
    public AppList(Remote remote){
        super("Select Application",Choice.IMPLICIT);
        this.remote = remote;
        back = new Command("Back",Command.BACK,1);
        helpcmd = new Command("Help",Command.HELP,1);
        for (int x = 0; x < remote.commandsTable.getCommandsTable().length; x++){
            try{
                String name = (String)remote.commandsTable.getCommandsTable()[x].get("name");
            this.append(name,null);
            }catch (Exception e){
                this.append(e.toString(), null);
            }
        }
        
        this.addCommand(back);
        this.addCommand(helpcmd);
        this.setCommandListener(this);
    }
    
    /**
     * Handler for the list selected back and help commands
     */
    public void commandAction(Command com, Displayable dis) {
        if (com == back)
            remote.backCommandPressed(Remote.APPLIST);
        if (com == helpcmd)
            remote.helpCommandPressed(Remote.APPLIST);
        if (com == List.SELECT_COMMAND)
            remote.appListElementSelected(this.getSelectedIndex());
    }
    
}
