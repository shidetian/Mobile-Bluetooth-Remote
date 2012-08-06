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
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import remoteclient.Remote;

/**
 * This class is used to display a text box where the user
 * can write a command and execute on the remote server
 * it simulates the start-> run windows input field
 */
public class RunCommand extends TextBox implements CommandListener {

    private Command ok;
    private Command back;
    private Remote remote;

    public RunCommand(Remote remote) {
        super("Insert Command", "", 200, TextField.ANY);
        this.remote = remote;
        ok = new Command("OK", Command.OK, 1);
        back = new Command("Back", Command.BACK, 1);
        this.addCommand(back);
        this.addCommand(ok);
        this.setCommandListener(this);
    }

    /**
     * Handler for the back and ok commands
     */
    public void commandAction(Command com, Displayable dis) {
        if (com == back) {
            remote.backCommandPressed(Remote.RUNCOMMAND);
        }
        if (com == ok) {
            if (this.getString().trim().length() > 0) {
                try {
                    remote.bluetooth.SendData("run " + this.getString());
                    this.setString("");
                } catch (IOException ex) {
                    remote.doAlert("Error in sending command", -1, this);
                }
            }
        }
    }
}
