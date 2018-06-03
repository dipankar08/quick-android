#!/bin/sh
# This is some secure program that uses security.
adb forward tcp:8081 tcp:8081
red=`tput setaf 1`
green=`tput setaf 2`
reset=`tput sgr0`

if [ "$1" == "start" ]; then
    echo ">>> Executing start..."
    adb shell am start -n  $2
    echo "${green}PASS${reset}"
elif [ "$1" == "stop" ]; then
    echo ">>> Executing stop..."
    adb shell am force-stop   $2
    echo "${green}PASS${reset}"
elif [ "$1" == "sleep" ]; then
    echo ">>> Executing sleep..."
    sleep $2
    echo "${green}PASS${reset}"
elif [ "$1" == "setpref" ]; then
    echo ">>> Executing setpref..."
    curl http://localhost:8081/setPref?data=$2%20$3%20$4
    echo "${green}PASS${reset}"
elif [ "$1" == "getpref" ]; then
    echo ">>> Executing getpref..."
    curl http://localhost:8081/getPref?data=$2%20$3
    echo "${green}PASS${reset}"
#Let;s have verify
elif [ "$1" == "verify" ]; then
    if [ "$2" == "pref" ]; then
        echo ">>> Executing verify pref ..."
        RESULT=$(curl  --silent http://localhost:8081/getPref?data=$3%20$4)
        echo $RESULT
        if [ "$RESULT" == "$5" ];then
            echo "${green}PASS${reset}"
        else
            echo "${red}FAIL as expected should be: $5 but actualy it returns $RESULT ${reset}"
        fi
    elif [ "$2" == "exist" ]; then
        echo ">>> Executing  verify exist for $3  ..."
        RESULT=$(curl  --silent http://localhost:8081/isExist?data=$3 )
        echo $RESULT
        if [ "$RESULT" == "$4" ];then
            echo "${green}PASS${reset}"
        else
            echo "${red}FAIL as expected should be: $4 but actualy it returns $RESULT ${reset}"
        fi
    elif [ "$2" == "visibility" ]; then
        echo ">>> Executing  verify visibility for $3 ..."
        RESULT=$(curl  --silent http://localhost:8081/isVisible?data=$3)
        echo $RESULT
        if [ "$RESULT" == "$4" ];then
            echo "${green}PASS${reset}"
        else
            echo "${red}FAIL as expected should be: $4 but actualy it returns $RESULT ${reset}"
        fi
    else
        echo "INVALID VERIFY COMMAND"
    fi
# Action commands
elif [ "$1" == "action" ]; then
    if [ "$2" == "click" ]; then
        echo ">>> Executing action click ..."
        RESULT=$(curl  --silent http://localhost:8081/doClick?data=$3)
        echo $RESULT
        if [ "$RESULT" == "DONE" ];then
            echo "${green}PASS${reset}"
        else
            echo "${red}FAIL as expected should be: DONE but actualy it returns $RESULT ${reset}"
        fi
    elif [ "$2" == "longclick" ]; then
        echo ">>> Executing  action longclick for $3  ..."
        RESULT=$(curl  --silent http://localhost:8081/doLongClick?data=$3 )
        echo $RESULT
        if [ "$RESULT" == "DONE" ];then
            echo "${green}PASS${reset}"
        else
            echo "${red}FAIL as expected should be: DONE but actualy it returns $RESULT ${reset}"
        fi
    elif [ "$2" == "randomtap" ]; then
        echo ">>> Executing  action randomtap..."
        RESULT=$(curl  --silent http://localhost:8081/doRandomTap?data=$3)
        echo $RESULT
        if [ "$RESULT" == "DONE" ];then
            echo "${green}PASS${reset}"
        else
            echo "${red}FAIL as expected should be: <DONE> but actualy it returns $RESULT ${reset}"
        fi
    else
        echo "INVALID ACTION COMMAND"
    fi
else
    echo "INVALID COMMAND"
fi
