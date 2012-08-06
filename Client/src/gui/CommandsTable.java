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

public class CommandsTable {
    
    public static java.util.Hashtable[] getCommandsTable(){
        java.util.Hashtable[] hashtable;
        //Build the application hashtable
        //using the values of the constants in
        //java.awt.event.KeyEvent
        hashtable = new java.util.Hashtable[6];
//---Explorer
        hashtable[0] = new java.util.Hashtable();
        hashtable[0].put("name","Explorer");
        hashtable[0].put("1. Close Window",new String[]{"18","115"}); //Alt+F4
        hashtable[0].put("2. Copy",new String[]{"17","67"}); //Ctrl + C
        hashtable[0].put("3. Cut",new String[]{"17","88"}); //Ctrl + X
        hashtable[0].put("4. Paste",new String[]{"17","86"}); //Ctrl + V
        hashtable[0].put("5. Run Command","Run");
//---Firefox
        hashtable[1] = new java.util.Hashtable();
        hashtable[1].put("name","Firefox");
        hashtable[1].put("1. Back",new String[]{"8"});             //Backspace
        hashtable[1].put("2. Refresh",new String[]{"116"});        //F5
        hashtable[1].put("3. Next Tab",new String[]{"17","9"});    //Ctrl + Tab
        hashtable[1].put("4. Close Tab",new String[]{"17","87"});  //Ctrl + W
        hashtable[1].put("5. New Tab",new String[]{"17","84"});    //Ctrl + T
        hashtable[1].put("6. Home",new String[]{"18","36"});       //Alt + Home
        hashtable[1].put("7. Hisotry",new String[]{"17","72"});    //Ctrl + H
//---Windows Media Player
        hashtable[2] = new java.util.Hashtable();
        hashtable[2].put("name","Windows Media Player");
        hashtable[2].put("1. Play/Pause",new String[]{"17","80"});       //Ctrl + P
        hashtable[2].put("2. Stop",new String[]{"17","83"});             //Ctrl + S
        hashtable[2].put("3. Previous",new String[]{"17","66"});         //Ctrl + B
        hashtable[2].put("4. Next",new String[]{"17","70"});             //Ctrl + F
        hashtable[2].put("5. Fast Forward",new String[]{"17","16","70"});//Ctrl + Shift + F
        hashtable[2].put("6. Volume up",new String[]{"121"});            //F10
        hashtable[2].put("7. Volume down",new String[]{"120"});          //F9
        hashtable[2].put("8. Mute",new String[]{"119"});                 //F8
//---Internet Explorer
        hashtable[3] = new java.util.Hashtable();
        hashtable[3].put("name","Internet Explorer");
        hashtable[3].put("1. Back",new String[]{"8"});             //Backspace
        hashtable[3].put("2. Refresh",new String[]{"116"});        //F5
        hashtable[3].put("3. Next Tab",new String[]{"17","9"});    //Ctrl + Tab
        hashtable[3].put("4. Close Tab",new String[]{"17","87"});  //Ctrl + W
        hashtable[3].put("5. New Tab",new String[]{"17","84"});    //Ctrl + T
        hashtable[3].put("6. Home",new String[]{"18","36"});       //Alt + Home
        hashtable[3].put("7. Hisotry",new String[]{"17","72"});    //Ctrl + H
//---Winamp
        hashtable[4] = new java.util.Hashtable();
        hashtable[4].put("name","Winamp");
        hashtable[4].put("1. Play",new String[]{"88"});             //X
        hashtable[4].put("2. Pause",new String[]{"67"});            //C
        hashtable[4].put("3. Stop",new String[]{"86"});             //V
        hashtable[4].put("4. Previous",new String[]{"90"});         //Z
        hashtable[4].put("5. Next",new String[]{"66"});             //B
        hashtable[4].put("6. Volume up",new String[]{"38"});        //Up
        hashtable[4].put("7. Volume down",new String[]{"40"});      //Down
//---Powerpoint
        hashtable[5] = new java.util.Hashtable();
        hashtable[5].put("name","Power Point");
        hashtable[5].put("1. Next Slide",new String[]{"40"});       //Down
        hashtable[5].put("2. Previous Slide",new String[]{"38"});   //Up
        hashtable[5].put("3. End Show",new String[]{"27"});         //Esc
        return hashtable;
    }
}
