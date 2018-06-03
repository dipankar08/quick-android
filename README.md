# quick-android
The main purpose of the repo to store resuable componet which i have used a tons of anroid project i build in my life time. 

# Quick Tutorails:



# My Own Automation Framewrok.
I build my new automation framework called "Quick test" Which is as simple as executing some command on your laptop as you have conneted to emulator/device to usb. A very simple test case is as below:
```
#!/bin/sh
# Step1: First start the app.
./qt.sh start in.co.dipankar.quickandroidexample/in.co.dipankar.quickandroidexample.MainActivity
./qt.sh sleep 2

# Test case for perf
./qt.sh setpref I xyz 19
./qt.sh getpref I xyz
./qt.sh verify pref I xyz 19

# Test case of Existing an elemnet
./qt.sh verify exist abcde  False
./qt.sh verify exist buttonPanel True

# test case for Visiblity
./qt.sh verify visibility abcde VIEW_NOT_FOUND
./qt.sh verify visibility buttonPanel True

#Test case for click and long click.
./qt.sh verify visibility notification True
./qt.sh action click accept
./qt.sh action longclick accept
./qt.sh verify visibility notification False


./qt.sh stop in.co.dipankar.quickandroidexample
```

