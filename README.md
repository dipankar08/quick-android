# quick-android
The main purpose of the repo to store resuable componet which i have used a tons of anroid project i build in my life time. 

# Quick Tutorails:



# My Own Automation Framewrok.
I build my new automation framework called "Quick test" Which is as simple as executing some command on your laptop as you have conneted to emulator/device to usb. A very simple test case is as below:

My simple automation framwork can only do two operation - Actions and Verify. 
- In Actions, it can click, long click any of the exiting elemnets, it can set the input with text or do a random tap anywhere in the screen. It can also do something like setting the android pref etc. 
- In verify, as the named said, it can verify the property of any view elment and can access the internal ds or state of the application. 

- A very simple and basic example as below.

```
#!/bin/sh
# Step1: First start the app.
./qt.sh start in.co.dipankar.quickandroidexample/in.co.dipankar.quickandroidexample.MainActivity
./qt.sh sleep 2

# Test case for perf
./qt.sh setpref I xyz 19
./qt.sh getpref I xyz
./qt.sh verify pref I xyz 19

# Verify if an elemnet exising the view hiererchy.
./qt.sh verify exist abcde  False
./qt.sh verify exist buttonPanel True

# Verify if a control or layout is visisble or not!
./qt.sh verify visibility abcde VIEW_NOT_FOUND
./qt.sh verify visibility buttonPanel True

# Do some operation like click and long click and test for the visisblity.
./qt.sh verify visibility notification True
./qt.sh action click accept
./qt.sh action longclick accept
./qt.sh verify visibility notification False

# At last stop the app. 
./qt.sh stop in.co.dipankar.quickandroidexample
```

I will be keep adding new test case as it is required for my app. The main purpose of this automation to keep things simple and stupid - so i will keep avoiding compelex task unless i find a simplified solution for it. For any bug/query/ suggestion- feel free to comment here.
