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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import javax.bluetooth.BluetoothStateException;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.*;
import remoteclient.Remote;

/**
 * It displays the initial screen of the application
 * @author Nikos Fotiou
 *
 */
public class MainList extends List implements CommandListener,util.CancelListener {

    private Command exit;
    private Remote remote;

    /**
     * It creates a new instance of the MainList. Initially it
     * checks whether there is any previous computer address
     * stored in a specified recordstore. If there is, it displays
     * the computer name before the &quot;Search for devices&quot; option
     * @param remote The core Remote Class
     */
    public MainList(Remote remote) {
        super("Welcome", Choice.IMPLICIT);
        exit = new Command("Exit", Command.EXIT, 1);
        this.remote = remote;
        try {
            RecordStore store = RecordStore.openRecordStore("mystore", true);
            if (store.getNumRecords() > 0) {
                byte[] data = store.getRecord(1);
                if (data.length > 2) {
                    DataInputStream sdis = new DataInputStream(new ByteArrayInputStream(data));
                    String sdevname = sdis.readUTF();
                    sdis.close();// store.closeRecordStore();
                    this.append(sdevname, Image.createImage("/img/server.png"));
                }
            } else {
                //create two foo records
                byte[] foo = new byte[1];
                foo[0] = 0;
                store.addRecord(foo, 0, 1);
                store.addRecord(foo, 0, 1);
            }
            store.closeRecordStore();
            this.append("Search for servers", Image.createImage("/img/search.png"));
            this.addCommand(exit);
            this.setCommandListener(this);
        } catch (Exception e) {
            remote.doAlert("Exception in initialising main list " + e.toString(), -1, null);
        }
    }

    /**
     * Handler for the exit and List.Select Commands
     */
    public void commandAction(Command com, Displayable dis) {
        if (com == exit) {
            remote.mainListExitPressed();
        }
        //search for a device or connecte to a stored devie has been pressed
        if (com == List.SELECT_COMMAND) {
            if (this.getString(this.getSelectedIndex()).startsWith("Search for")) {
                try {
                    remote.showSplashScreen("Searching for servers...", gui.SplashScreen.INFO,this);
                    remote.bluetooth.FindDevices();
                } catch (BluetoothStateException e) {
                    remote.doAlert("Error in searching for devices. Make sure bluetooth is enabled and there is no" +
                            " other active connection" + e.toString() , -1, this);
                }
            } else {
                //Read the stored url and connect to it
                try {
                    remote.showSplashScreen("Connecting...", gui.SplashScreen.INFO);
                    RecordStore store = RecordStore.openRecordStore("mystore", true);
                    byte[] data = store.getRecord(2);
                    java.io.DataInputStream sdis = new java.io.DataInputStream(new java.io.ByteArrayInputStream(data));
                    String url = sdis.readUTF();
                    store.closeRecordStore();
                    sdis.close();
                    remote.bluetooth.connetToServer(url);
                    remote.connectionEstablished();
                } catch (Exception e) {
                    remote.doAlert("Error connecting to the specified server " + e.toString(), -1, this);
                }

            }
        }
    }

    public void cancelCommandPressed() {
        remote.bluetooth.CancelQuery();
        remote.showMainList();
    }
}
