#!/bin/bash
# 6LoWPAN gateway setup
# written by nanite 2017-03-04
# ref: https://developer.nordicsemi.com/nRF5_IoT_SDK/doc/0.9.0/html/a00092.html

NC='\033[0m' # no color
GREEN='\033[0;32m'
CYAN='\033[0;36m'
YELLOW='\033[1;33m'

echo -e ${CYAN}-------------- `basename "$0"` -------------${NC}

# Run RA Daemon for IPv6 stateless auto-configuration. It will distribute a global IPv6 prefix to the connected devices by sending out a RA message.
echo -e "${YELLOW}/etc/radvd.conf${NC}" # a radvd configuration file
{
echo -e 'interface bt0 {'
echo -e '\tAdvSendAdvert on;'
echo -e '\tprefix 2005::/64 {'
echo -e '\t\tAdvOnLink off;'
echo -e '\t\tAdvAutonomous on;'
echo -e '\t\tAdvRouterAddr off;'
echo -e '\t};'
echo -e '};'
echo -e 'interface eth0 {'
echo -e '\tAdvSendAdvert on;'
echo -e '\tprefix 2004::/64 {'
echo -e '\t\tAdvOnLink on;'
echo -e '\t\tAdvAutonomous on;'
echo -e '\t\tAdvRouterAddr off;'
echo -e '\t};'
echo -e '};'
echo -e 'interface wlan0 {'
echo -e '\tAdvSendAdvert on;'
echo -e '\tprefix 2003::/64 {'
echo -e '\t\tAdvOnLink on;'
echo -e '\t\tAdvAutonomous on;'
echo -e '\t\tAdvRouterAddr off;'
echo -e '\t};'
echo -e '};'
echo -e 'interface wlan1 {'
echo -e '\tAdvSendAdvert on;'
echo -e '\tprefix 2006::/64 {'
echo -e '\t\tAdvOnLink on;'
echo -e '\t\tAdvAutonomous on;'
echo -e '\t\tAdvRouterAddr off;'
echo -e '\t};'
echo -e '};'
} | sudo tee /etc/radvd.conf
echo 1 | sudo tee /proc/sys/net/ipv6/conf/all/forwarding # > /dev/null # Enable IPv6 forwarding. This machine must be configured as a Router by doing so otherwise RADVD will not start. # sudo sysctl -w net.ipv6.conf.eth0.forwarding=1
sudo service radvd restart

# Mount the debugfs file system to /sys/kernel/debug. You can ls to check the contents of the folder.
#sudo mount -t debugfs none /sys/kernel/debug
#ls /sys/kernel/debug

# Load 6LoWPAN module. You might want to lsmod to verify that the module is indeed loaded.
sudo modprobe bluetooth_6lowpan
#lsmod | grep bluetooth_6lowpan

# Enable the bluetooth 6lowpan module. The PSM value should be set to 0x23(35) to enable the module, if using Linux kernel version less than 4.0.
#echo 35 | sudo tee /sys/kernel/debug/bluetooth/6lowpan_psm
echo 1 | sudo tee /sys/kernel/debug/bluetooth/6lowpan_enable # > /dev/null

# HCI commands such as hciconfig and hcitool are used to configure Bluetooth devices.
# The device name hciX is assigned to the (Bluetooth) device installed in the system. 

# Reset HCI device (hci0 device in this case) and start advertising.
#sudo hciconfig
#sudo hciconfig hci0 lestates
sudo hciconfig hci0 reset
sudo hciconfig hci0 leadv

echo -e ${CYAN}---------- end of `basename "$0"` ----------${NC}
