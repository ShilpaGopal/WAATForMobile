**WAAT FOR Mobile**
WAAT- WebAnalytic Automation Testing

Mobile analytics is generally split between mobile web and mobile apps. Mobile web refers to when individuals use their smartphones or tablets to view online content via a mobile browser. 
This project helps to capture and verify analytics data for Android WebApp(Chrome) using BrowserMobProxy .

How to use WAATForMobile.

Perquisites :   
Appium should be installed. 
  Install Appium 
      1.Using npm modules 
		    $ npm install -g appium
      2.Directly from git repository 
		    git clone https://github.com/appium/appium.git
  Download Android SDK.
	    https://developer.android.com/sdk/index.html
	set the ANDROID_HOME and download Android build and Platform tools.
	    For OS X:
		    echo "export ANDROID_HOME=/usr/local/adt/sdk" >> ~/.bash_profile
	    For Windows :
		    set ANDROID_HOME=C:\ installation location \android-sdk
		    set PATH=%PATH%;%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools

Download or clone the project 
  git clone https://github.com/ShilpaGopal/WAATForMobile.git

Connect your Android real device or any Emulator.

Set up the Proxy for device connected
Click Settings.
In Settings, click Wi-Fi.
Click and hold WiredSSID until a box pops up.
Click on "Modify network".
Check the "Show advanced options" box and select Manual from the Proxy Settings menu.
Enter your host machine's IP address in the "Proxy hostname" field, and 5555 in the "proxy port" 
Then click Save.

Install Chrome browser on the device

Place your Tests under src/test/java –

Start capturing the Web analytics data by executing 

Gradle build 
Gradle test
