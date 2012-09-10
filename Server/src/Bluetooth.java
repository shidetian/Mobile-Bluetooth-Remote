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


import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class handles all bluetooth activity
 * @author Nikos Fotiou
 */
public class Bluetooth implements Runnable {

    final UUID uuid = new UUID( //the uid of the service, it has to be unique,
            "27012f0c68af4fbf8dbe6bbaf7aa432a", false); //it can be generated randomly
    final String name = "Echo Server";                       //the name of the service
    final String url = "btspp://localhost:" + uuid //the service url
            + ";name=" + name + ";authenticate=false;encrypt=false;";
    DataOutputStream dout;
    DataInputStream din;
    LocalDevice local = null;
    StreamConnectionNotifier server = null;
    StreamConnection conn = null;
    RemoteServer rs;
    TimerTask keepAlive;
    boolean remoteReady = true; //it holds whether client is ready to receive data; TODO may need lock

    public Bluetooth(RemoteServer rs) {
        try {
            this.rs = rs;
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);
            server = (StreamConnectionNotifier) Connector.open(url);
            remoteReady = true;
        } catch (Exception e) {
        	e.printStackTrace();
            rs.displayMessage("Exception Occured, \nMake sure bluetooth adapter is connected, \nPlease restart\nException decription:\n" + e.toString());
        }
    }

    /**
     *It listens for incoming connections 
     */
    public void listen() {
        try {
            Thread t = new Thread(this);
            t.start();
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
    }

    /**
     *It closes the bluetooth connection 
     */
    public void close() {
        try {
            conn.close();
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
    }

    /**
     * It listens for incoming connections and commands 
     */
    public void run() {
        try {
            boolean listening = true;
            boolean firstTime = true;
            String cmd = "";
            Timer timer = new Timer();
            while (true) {
                try {
                    if (listening) {
                        conn = server.acceptAndOpen();
                        /*--------- Client Connected waiting for incoming data --*/
                        din = new DataInputStream(conn.openInputStream());
                        dout = new DataOutputStream(conn.openOutputStream());
                        rs.displayMessage("Client Connected...");
                        listening = false;
                        firstTime = false;
                        keepAlive= new TimerTask(){  //TODO may need synchronization

							public void run() {
								try {
						            if(remoteReady){
						            	System.out.println("Sending beat");
						                dout.writeUTF("NOOP");
						                dout.flush();
						            }
						        } catch (Exception e) {
						            //rs.displayMessage("Exception while sending data " + e.toString());
						        }
							}
                        	
                        };
                        timer.scheduleAtFixedRate(keepAlive, 0, 1000);
                    } else {
                        cmd = din.readUTF();
                        System.out.println("Received: " + cmd);
                        if (cmd.equals("ACK")){
                            remoteReady = true;
                            System.out.println("Remote is ready");
                        }else{
                        	rs.cmdReceived(cmd);
                        }
                    }
                } catch(ArrayIndexOutOfBoundsException e){} //Ignore error caused by moving mouse while UAC prompt
                catch (Exception e) {
                	keepAlive.cancel();
                	timer.cancel();
                    e.printStackTrace();
                    rs.displayMessage("Client Disconnected due to error. Waiting for new connection");
                    close();
                    if (firstTime) {
                        rs.displayMessage("Exception Occured, \nMake sure bluetooth adapter is connected, \nPlease restart\nException decription:\n" + e.toString());
                        return;
                    }
                    listening = true;
                }
            }
        } catch (Exception e) {
            rs.displayMessage("Exception Occured, \nMake sure bluetooth adapter is connected, \nPlease restart\nException decription:\n" + e.toString());

        }
    }

    /**
     * It sends a String of data
     * @param data the data to send
     */
    public void SendData(String data) {
        try {
            dout.writeUTF(data);
            dout.flush();
        } catch (Exception e) {
            rs.displayMessage("Exception while sending data " + e.toString());
        }
    }
    /**
     * It sends a String of data only if remote client is ready
     * @param data the data to send
     * @return true if data was send successfylly else false
     */
    public boolean SendDataIfReady(String data) {
        boolean result = false;
        try {
            if(remoteReady){
            	System.out.println("Send(UTF) in progress");
                remoteReady = false;
                dout.writeUTF(data);
                dout.flush();
                result = true;
            }
        } catch (Exception e) {
            rs.displayMessage("Exception while sending data " + e.toString());
        }
         return result;
    }

    /**
     * It sends a byte[] of data
     * @param data the data to send
     */
    public void SendData(byte[] data) {
        try {
        	System.out.println("Send(Binary) in progress");
            dout.write(data, 0, data.length);
            dout.flush();
            System.out.println("Send(data) complete");
        } catch (Exception e) {
            rs.displayMessage("Exception while sending data " + e.toString());
        }
    }

    /**
     * It sends a byte[] of data only if remote client is ready
     * @param data the data to send
     * @return true if data was send successfylly else false
     */
    public boolean SendDataIfReady(byte[] data) {
        boolean result = false;
        try {
            if (remoteReady) {                
                remoteReady = false;
                dout.write(data, 0, data.length);
                dout.flush();
                result = true;
            }
            
        } catch (Exception e) {
            rs.displayMessage("Exception while sending data " + e.toString());
        }
        return result;
    }
}
