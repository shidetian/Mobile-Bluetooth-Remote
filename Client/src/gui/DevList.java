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
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.rms.RecordStore;
import javax.microedition.lcdui.Image;
import remoteclient.Remote;

/**
 * This List displays the available Bluetooth Remote Control
 * servers
 * @author Nikos Fotiou
 *
 */
public class DevList extends List implements CommandListener{
    
    private Command back;
    private Remote remote;
    
    /**
     * It creates a new instance of the DevList class
     * @param remote The core Remote class
     * @param devices A table containing the names of the available servers
     */
    public DevList(Remote remote,String[] devices){
        super("Select Device",Choice.IMPLICIT);
       
            back = new Command("Back", Command.BACK, 1);
            for (int x = 0; x < devices.length; x++) {
                try {
                    this.append(devices[x], Image.createImage("/img/server.png"));
                } catch (IOException ex) {}
            }
            this.addCommand(back);
            this.remote = remote;
            this.setCommandListener(this);
        
    }
    
    /**
     * Handler for the back and List.Select commands
     */
    public void commandAction(Command com, Displayable dis) {
        if (com == back)
            remote.backCommandPressed(Remote.DEVLISTID);
        if (com == List.SELECT_COMMAND){
            try {
                //connect to the device
                remote.showSplashScreen("Connecting...", gui.SplashScreen.INFO);
                String url =remote.bluetooth.connetToServer(this.getSelectedIndex());
                //store the device in the RMS
                RecordStore store = RecordStore.openRecordStore("mystore", true);
                java.io.ByteArrayOutputStream sbos = new java.io.ByteArrayOutputStream( );
                java.io.DataOutputStream sdos 	   = new java.io.DataOutputStream(sbos);
                sdos.writeUTF(this.getString(this.getSelectedIndex()));
                sdos.close();
                byte[] data = sbos.toByteArray( );
                store.setRecord(1,data, 0, data.length);
                sbos = new java.io.ByteArrayOutputStream( );
                sdos = new java.io.DataOutputStream(sbos);
                sdos.writeUTF(url);
                sdos.close( );
                data = sbos.toByteArray( );
                store.setRecord(2,data, 0, data.length);
                store.closeRecordStore();
                remote.connectionEstablished();
            } catch (Exception e) {
                remote.doAlert("Error connecting to the specified server " + e.toString() ,-1,this);
            }
        }
        
    }
}

