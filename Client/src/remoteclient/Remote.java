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

package remoteclient;

import java.io.IOException;

import gui.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
//import javax.microedition.rms.*;

/**
 * The entry point of the program. This is the midlet class
 * as well as the CommandListenet for all the guis
 * @author Nikos Fotiou
 *
 */
public class Remote extends MIDlet {
    
    public device.Bluetooth bluetooth;
    public util.CommandsTable commandsTable;
    public MainCanvas mainCanvas; 
    public Display display;
    public int mode;
    int app;
    public static final int MOUSE_MODE  = 0;
    public static final int KEY_MODE    = 1;
    public static final int APP_MODE    = 2;
    public static final int CHOOSE_MODE = 3;
    public static final int UTIL_MODE   = 4;
    public static final int DEVLISTID   = 1;
    public static final int MAINCANVASID= 2;
    public static final int HELPFORMID  = 3;
    public static final int APPLIST     = 4;
    public static final int COMMANDLIST = 5;
    public static final int RUNCOMMAND  = 6;
    public static final int MODELIST    = 7;
    public static final int SENDMSG     = 8;
    public static final int UTILLIST    = 9;
    
    public void startApp() {
        display   = Display.getDisplay(this);
        mode      = CHOOSE_MODE;
        bluetooth = new device.Bluetooth(this);
        display.setCurrent(new MainList(this));
    }
    
   /**
     * This function is called by the ModeList class whenever
     * a mode is selected
     * @param smode the index of the element selected
     */
    public void modeListModeSelected(int smode){
        if(smode ==0){
            mode = MOUSE_MODE;
            mainCanvas = new MainCanvas(this);
            display.setCurrent(mainCanvas);
            try {
				bluetooth.SendData("SUPDATE1");
			} catch (IOException e) {}
        }else if(smode ==1){
            mode = KEY_MODE;
            mainCanvas = new MainCanvas(this);
            display.setCurrent(mainCanvas);
            try {
				bluetooth.SendData("SUPDATE1");
			} catch (IOException e) {}
        }else if(smode ==2){
            mode = APP_MODE;
            display.setCurrent(new AppList(this));
        }else if(smode ==3){
            mode = UTIL_MODE;
            display.setCurrent(new UtilList(this));
        }
    }
    
    /**
     * This function is called by the CommandList class whenever
     * the run command is pressed
     */
    public void commandListRunSelected(){
        display.setCurrent(new RunCommand(this));
    }
    
    /**
     * This function is called by the CommandList class whenever
     * the send message command is pressed
     */
    public void commandListSendMessageSelected(){
        display.setCurrent(new SendMessage(this));
    }
    
    /**
     * This function is called by the AppList class whenever a
     * application has been selected
     * @param app The index of the element selected
     */
    public void appListElementSelected(int app){
        this.app = app;
        display.setCurrent(new CommandsList(this,app));
    }
    
    /**
     * This function is called whenever a help command is pressed
     * @param guiID The id of the gui class
     */
    public void helpCommandPressed(int guiID){
        display.setCurrent(new HelpForm(this));
    }
    
    /**
     * This function is called by the bluetooth class when the
     * server search has been completed
     * @param servers An array of the name of the available servers
     */
    public void searchFinished(String[] servers){
        if (servers!=null)
            display.setCurrent(new DevList(this,servers));
        else{
            doAlert("The service was not found",4000,new MainList(this));
        }
    }
    
    /**
     * This function is used to display an alert
     * @param msg The message to display
     * @param time_out Duration in ms of the Alert
     * @param next Next Displayable
     */
    public void doAlert(String msg,int time_out,Displayable next){
        if (time_out==-1){
            time_out = Alert.FOREVER;
        }
        if (display.getCurrent() instanceof Alert ){
            ((Alert)display.getCurrent()).setString(msg);
            ((Alert)display.getCurrent()).setTimeout(time_out);
        }else{
            Alert alert = new Alert("Remote Control");
            alert.setString(msg);
            alert.setTimeout(time_out);
            if (next!=null)
                display.setCurrent(alert,next);
            else
                display.setCurrent(alert);
        }
    }
    
    /**
     * It displays a splash screen
     * @param message The message to appear
     * @param type The type of the information, it can be INFO,ERROR
     */
    public void showSplashScreen(String message, int type){
        display.setCurrent(new SplashScreen(message,type));
    }
    
    /**
     * It displays a splash screen
     * @param message The message to appear
     * @param type The type of the information, it can be INFO,ERROR
     * @param cancel A CancelListener interface
     */
    public void showSplashScreen(String message, int type, util.CancelListener cancel){
        display.setCurrent(new SplashScreen(message,type,cancel));
    }
    /**
     * It displays the Main List
     */
    public void showMainList(){
        display.setCurrent(new MainList(this));
    }
    /**
     * This function is called by MainList if exit command has been pressed
     */
    public void mainListExitPressed(){
        destroyApp(false);
        notifyDestroyed();
    }
    
    /**
     * This function is called by DevList and MainList if a new connection
     * to a server is established.
     */
    public void connectionEstablished(){
        commandsTable = new util.CommandsTable();
        commandsTable.initHashTable(this);
    }
    
    /**
     * This function is called by CommandTable when hash table initialization
     * completes
     * @param result the resu;t of the initialization
     */
    public void initHashTableCompleted(String result){
         if (result.equals(""))
           display.setCurrent(new ModeList(this));
        else{
           this.doAlert("Error " + result, -1, new MainList(this));
        }
    }
    
    /**
     * This funcion is beeing called by any GUI class that has
     * the back command, whenever the back command is pressed.
     * @param guiID The id of the gui class
     */
    public void backCommandPressed(int guiID){
        switch (guiID){
            case Remote.DEVLISTID:
                display.setCurrent(new MainList(this));
                break;
            case Remote.MAINCANVASID:case Remote.APPLIST:case Remote.UTILLIST:
                display.setCurrent(new ModeList(this));
                break;
            case Remote.RUNCOMMAND:
            case Remote.SENDMSG:
                display.setCurrent(new UtilList(this));
                break;
            case Remote.MODELIST:
                display.setCurrent(new MainList(this));
                break;
            case Remote.HELPFORMID :
                if (mode == CHOOSE_MODE)
                    display.setCurrent(new ModeList(this));
                else if (mode == APP_MODE)
                    display.setCurrent(new AppList(this));
                else
                    display.setCurrent(mainCanvas);
                break;
            case Remote.COMMANDLIST:
                display.setCurrent(new AppList(this));
                break;
        }
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
