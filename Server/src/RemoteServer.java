import java.util.Timer;
import java.util.TimerTask;

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




/**
 * The server of the remote control
 * it receives the commands from the mobile phone
 * @author Nikos Fotiou
 */
public class RemoteServer {

    
    public int screenx =1;//the width of the mobile phone screen
    public int screeny =1;//the height of the mobile phone screen
    public int sx = -1;//the server screen width
    public int sy = -1;//the server screen height
	public double multiplier = 1.5;
    MouseControl mouse;
    KeyboardControl keyboard;
    RemoteServerGUI gui;
    public Bluetooth bluetooth;
    private TimerTask screenRefresher;
    private Timer timer;    
    private boolean autoupdate = false;
    private double lastSize =1.0;

    /**
     * It creates a new GUI for the remote control server,
     * it initiates the Bluetooth stuck and it waits until
     * it receives a command. Upon the command is received
     * its being parsed and the appropriate action is being 
     * taken.
     */
    public RemoteServer() {
        try {
            gui = new RemoteServerGUI();
            mouse = new MouseControl(this);
            keyboard = new KeyboardControl();
            timer = new Timer();
            java.awt.Dimension sdimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            gui.setSize(250, 200);
            sx = sdimension.width;
            sy = sdimension.height;
            gui.setLocation(sdimension.width - 250, sdimension.height - 250);
            gui.setVisible(true);
            gui.jScrollPane1.setLocation(1, 1);
            gui.jScrollPane1.setSize(245, 150);
            gui.InfoLabel.setText("Setting device to be discoverable...");

            /*--------- Initialising the Bluetooth Stuck -----------*/
            bluetooth = new Bluetooth(this);            
            gui.InfoLabel.setText("Start advertising service...");

            /*--------- Listening for incoming connections ----------*/
            gui.InfoLabel.setText("Waiting for incoming connection...");
            bluetooth.listen();

            

        } catch (Exception e) {
        	e.printStackTrace();
            gui.InfoLabel.setText("Exception Occured, \nMake sure bluetooth adapter is connected, \nPlease restart\nException decription:\n" + e.toString());
        }
    }
    /**
     * It is used by system.Bluetooth whenever a
     * command arrives
     * @param cmd the command arrived
     */
    public void cmdReceived(final String cmd){
        /*------ Parse the command -------*/
        
    	if (cmd.startsWith("MTELE")){								//Touchscreen mouse movement
			mouse.teleportMouse(Integer.parseInt(cmd.substring(5, cmd.indexOf(';'))), Integer.parseInt(cmd.substring(cmd.indexOf(';')+1)), multiplier);
    	}
    	if (cmd.startsWith("AUPDATE")){
    		autoupdate = !autoupdate;
    		System.out.println("Autoupdate: "+autoupdate);
    		if (autoupdate){
    			screenRefresher = new TimerTask(){

    				@Override
    				public void run() {
    					if (autoupdate)
    						mouse.sendScreen(lastSize);
    				}
                	
                };
                timer.scheduleAtFixedRate(screenRefresher, 1000, 1000);
    		}else{
    			try {
					screenRefresher.cancel();
				} catch (Exception e) {} //screenRefresher might not have been initialized
    		}
    	}
    	if (cmd.startsWith("SUPDATE")){								//Update screenshot
    		lastSize = Double.parseDouble(cmd.substring(7));
    		mouse.sendScreen(lastSize);
    	}
        if (cmd.startsWith("MUP")) {
            String scode = cmd.substring(3);
            mouse.moveMouse(0, -1,Integer.parseInt(scode));          //Move mouse up
        }
        if (cmd.startsWith("MDOWN")) {
            String scode = cmd.substring(5);
            mouse.moveMouse(0, 1,Integer.parseInt(scode));		     //Move mouse down
        }
        if (cmd.startsWith("MLEFT")) {
            String scode = cmd.substring(5);
            mouse.moveMouse(-1, 0,Integer.parseInt(scode));		     //Move mouse left
        }
        if (cmd.startsWith("MRIGHT")) {
            String scode = cmd.substring(6);
            mouse.moveMouse(1, 0,Integer.parseInt(scode));			 //Move mouse right
        }
        
        if (cmd.equalsIgnoreCase("MLCLICK")) {
            mouse.mClick(MouseControl.LCLICK);  //Mouse left click pressed
        }
        if (cmd.equalsIgnoreCase("SMLCLICK")) {
            mouse.mRelease(MouseControl.LCLICK); //Mouse left click released		
        }
        if (cmd.equalsIgnoreCase("MRCLICK")) {
            mouse.mClick(MouseControl.RCLICK);	 //Mouse right click pressed
        }
        if (cmd.equalsIgnoreCase("SMRCLICK")) {
            mouse.mRelease(MouseControl.RCLICK); //Mouse right click released
        }
        if (cmd.equalsIgnoreCase("MWPRESS")) {
            mouse.mClick(MouseControl.WCLICK);	 //Mouse wheel pressed
        }
        if (cmd.equalsIgnoreCase("SMWPRESS")) {
            mouse.mRelease(MouseControl.WCLICK); //Mouse wheel released
        }
        if (cmd.equalsIgnoreCase("MWUP")) {
            mouse.mWheel(-1);					 //Move wheel up
        }
        if (cmd.equalsIgnoreCase("MWDOWN")) {
            mouse.mWheel(1);					 //Move wheel down
        }
        if(cmd.startsWith("TYPE")){
        	keyboard.typeString(cmd.substring(4));
        }
        if (cmd.startsWith("K")) {				 //Key pressed
            String scode = cmd.substring(1);
            keyboard.keyPress(Integer.parseInt(scode));
        }
        if (cmd.startsWith("SK")) {				 //Key released
            String scode = cmd.substring(2);
            keyboard.keyRelease(Integer.parseInt(scode));
        }
        if (cmd.startsWith("run")) {			//Invoke a run command
            String command = cmd.substring(4);
            try {
                String sysName = System.getProperty("os.name");
                if (sysName.indexOf("windows") != -1) {
                    Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start", command});
                }else{//Linux
                    Runtime.getRuntime().exec(command); 
                }                
            } catch (Exception e) {
            }
        }
        if (cmd.startsWith("msg")) {			//Send message has been send
            String message = cmd.substring(4);
            gui.showMsgbox(message);
        }
        if (cmd.startsWith("DIM")) {			//The screen simensions
            int ypos = cmd.indexOf("Y");
            screenx = Integer.parseInt(cmd.substring(3, ypos));
            screeny = Integer.parseInt(cmd.substring(ypos+1));
        }
        if (cmd.equalsIgnoreCase("applist")) {	      //Send the application list
           try{
                SendAppList.startSending(this);
           }catch(Exception e){
               gui.InfoLabel.setText("Exception Occured " + e.toString());
           }
        }
        if (cmd.equalsIgnoreCase("CLOSE")) {
        	screenRefresher.cancel();
        	timer.cancel();
           bluetooth.close();                           //close connection
        }
    }
    /**
     * It displays a message in the TextArea
     * @param msg the message to be displayed
     */
    public void displayMessage(String msg) {
        gui.InfoLabel.setText(msg);
    }

    /**
     * The entry point of the program
     */
    public static void main(String args[]) {
        @SuppressWarnings("unused")
		RemoteServer echoserver = new RemoteServer();
    }
}
