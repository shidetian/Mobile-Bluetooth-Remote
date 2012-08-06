This project is a fork of The Bluetooh Remote Control Project by Nikos Fotiou at http://www.miniware.net/ which is no longer (appears to be) actively developed. The Mobilewitch Bluetooh Remote Control at http://www.mobilewitch.com/Mobilewitch-Bluetooth-Remote-Control_software_details_2.htm is most likely a fork of the above. This project has no relation to those above.

Major changes:
*Touchscreen support
*(more) Arbitrary typing
*Screenshot enhancements (zoom/autoupdate)

Dependencies
Piccolo XML Parser: http://piccolo.sourceforge.net/
BlueCove: http://bluecove.org/

Building (tested):
Build Server using JRE6/7, requires BlueCove and Piccolo libraries. Both 64 and 32 bit versions should work.
Build Client using JavaME/J2ME with CLDC 1.0 and MIDP 2.0.

Running:
Check that your computer has bluetooth. Make sure your bluetooth is turned on and you are discoverable. It doesn't hurt to try to connect to your computer via bluetooth normally (disconnect before you start though). Running the server in admin mode will allow you to control the mouse in fullscreen applications like games. If everything is ok on the server side, the status will display "Waiting for incoming connection...". Use your favorite wap site like imserba to upload the jar and download to your phone. Run the app. Search for server (may take a while). If everthing was successful, you should see your computer on the list. Enjoy your newfound control. 