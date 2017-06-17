#!/bin/bash
# 6LoWPAN gateway setup 2
# written by nanite 2017-03-14

NC='\033[0m' # no color
GREEN='\033[0;32m'
CYAN='\033[0;36m'
YELLOW='\033[1;33m'

echo -e ${CYAN}-------------- `basename "$0"` -------------${NC}

# Start ble advertising
sudo hciconfig hci0 leadv

# Routing setup: Add the IP prefix to all the interfaces.
#route -6 # Display all existing IPv6 routes list
sudo ifconfig bt0 add 2005::1/64
sudo ifconfig eth0 add 2004::1/64
sudo ifconfig wlan0 add 2003::1/64
sudo ifconfig wlan1 add 2006::1/64

echo -e ${CYAN}---------- end of `basename "$0"` ----------${NC}
