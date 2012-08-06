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
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;
import remoteclient.Remote;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * This List displays the available commands for a specified application
 * @author Nikos Fotiou
 *
 */
public class CommandsList extends List implements CommandListener{
    private Command back;
    private Remote remote;
    private int app;
    /**
     * It creates a new instance of the Commandlist class
     * @param remote The core Remote class
     * @param app The index of the selected application in the hashtable
     */
    public CommandsList(Remote remote, int app){
        super((String)(remote.commandsTable.getCommandsTable())[app].get("name"),Choice.IMPLICIT);
        this.remote = remote;
        this.app = app;
        //show the available commands for that application
        //sorted by name
        java.util.Enumeration en = (remote.commandsTable.getCommandsTable())[app].keys();
        int x = 0;
        while(en.hasMoreElements()){
            String keyValue = (String)en.nextElement();
            if(!keyValue.equalsIgnoreCase("name")){
                if ( x > 0 ){
                    if(keyValue.compareTo(this.getString(x-1))>0)
                        this.append(keyValue,null);
                    else{
                        for (int y = 0 ;y <x ; y++)
                            if(keyValue.compareTo(this.getString(y))<0){
                            this.insert(y,keyValue,null);
                            y = x;
                            }
                    }
                }else
                    this.append(keyValue,null);
                x++;
            }
        }
        back = new Command("Back",Command.BACK,1);
        this.addCommand(back);
        this.setCommandListener(this);
        this.setSelectedIndex(0,true);
    }
    
    /**
     * Handler for the list selected and back
     */
    public void commandAction(Command com, Displayable dis) {
        if (com == back)
            remote.backCommandPressed(Remote.COMMANDLIST);
        if (com == List.SELECT_COMMAND){
            try {
                String[] keys = (String[]) (remote.commandsTable.getCommandsTable())[app].get(this.getString(this.getSelectedIndex()));
                for (int x = 0; x < keys.length; x++) {
                    remote.bluetooth.SendData("K" + keys[x]);
                }
                for (int x = 0; x < keys.length; x++) {
                    remote.bluetooth.SendData("SK" + keys[x]);
                }
            } catch (IOException ex) {
                remote.doAlert("Error in sending command", -1, this);
            }
            
        }
    }
}
