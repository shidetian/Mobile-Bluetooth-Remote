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
package util;

public class CommandsTable {

    private java.util.Hashtable[] hashtable = null;
    String finished = "Error";
    int haspos = -1;
    int totalApps = 0;
    int totalKeys = 0;
    String[] keys = null;
    int keysPos = 0;
    String cmdName = "";
    remoteclient.Remote remote;
    public void initHashTable(remoteclient.Remote remote) {
        this.remote = remote;
        //String cmd = "";
        try{
            remote.bluetooth.SendData("applist");
        }catch(Exception e){
            
        }
 
    }

    public void commandReceived(String cmd) {
        if (cmd.startsWith("APPTOTAL")) {
            totalApps = Integer.parseInt(cmd.substring(9));
            hashtable = new java.util.Hashtable[totalApps];
        } else if (cmd.startsWith("APPNAME")) {
            haspos++;
            String appName = cmd.substring(8);
            hashtable[haspos] = new java.util.Hashtable();
            hashtable[haspos].put("name", appName);
        } else if (cmd.startsWith("CMDKEYS")) {
            totalKeys = Integer.parseInt(cmd.substring(8));
            keys = new String[totalKeys];
        } else if (cmd.startsWith("CMDNAME")) {
            cmdName = cmd.substring(8);
        } else if (cmd.startsWith("KEY")) {
            keys[keysPos] = cmd.substring(4);
            keysPos++;
            if (keysPos == totalKeys) {
                keysPos = 0;
                hashtable[haspos].put(cmdName, keys);
            }
        } else if (cmd.equalsIgnoreCase("ENDCMD")) {
            totalApps--;
            if (totalApps ==0){ 
                finished = "";
                remote.initHashTableCompleted(finished);
            }
        }
    }

    public java.util.Hashtable[] getCommandsTable() {
        return hashtable;
    }
}
