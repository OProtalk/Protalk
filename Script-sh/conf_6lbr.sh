#!/bin/bash
# 6LoWPAN-Border-Router setup
# written by nanite 2017-06-17

NC='\033[0m' # no color
GREEN='\033[0;32m'
CYAN='\033[0;36m'
YELLOW='\033[1;33m'

echo -e ${CYAN}-------------- `basename "$0"` -------------${NC}

sudo service radvd restart

echo -e "${YELLOW}/etc/network/interfaces${NC}"
{
echo -e 'auto lo'
echo -e ''
echo -e 'iface lo inet loopback'
echo -e ''
echo -e '#iface eth0 inet dhcp'
echo -e 'iface eth0 inet static'
echo -e 'address 192.168.18.19'
echo -e ''
echo -e 'auto br0'
echo -e 'iface br0 inet dhcp'
echo -e '\tbridge_ports wlan1'
echo -e '\tbridge_stp off'
echo -e '\tup echo 0 > /sys/devices/virtual/net/br0/bridge/multicast_snooping'
echo -e '\tpost-up ip link set br0 address `ip link show wlan1 | grep ether | awk '\''{print $2}'\''`'
echo -e ''
echo -e 'auto wlan0'
echo -e 'allow-hotplug wlan0'
echo -e 'iface wlan0 inet manual'
echo -e 'wpa-roam /etc/wpa_supplicant/wpa_supplicant.conf'
echo -e ''
echo -e 'auto wlan1'
echo -e 'allow-hotplug wlan1'
echo -e 'iface wlan1 inet manual'
echo -e 'wpa-conf /etc/wpa_supplicant/wpa_supplicant.conf'
echo -e ''
echo -e 'iface default inet dhcp'
} | sudo tee /etc/network/interfaces
echo

echo -e "${YELLOW}/etc/6lbr/6lbr.conf${NC}"
{
echo -e '#This file contains a default configuration for Raspberry PI platform using'
echo -e '#a Telos SLIP Radio'
echo -e '#The full list of parameters and their meaning can be found in 6lbr.conf.example'
echo -e ''
echo -e 'MODE=ROUTER'
echo -e '#MODE=SMART-BRIDGE'
echo -e '#MODE=RPL-RELAY'
echo -e '#MODE=FULL-TRANSPARENT-BRIDGE'
echo -e '#MODE=NDP-ROUTER'
echo -e '#MODE=6LR'
echo -e '#MODE=RPL-ROOT'
echo -e ''
echo -e 'RAW_ETH=0'
echo -e 'BRIDGE=0'
echo -e '#DEV_BRIDGE=br0'
echo -e 'DEV_TAP=tap0'
echo -e 'DEV_ETH=wlan1'
echo -e 'RAW_ETH_FCS=0'
echo -e ''
echo -e 'DEV_RADIO=/dev/ttyACM0'
echo -e 'BAUDRATE=115200'
echo -e ''
echo -e 'LOG_LEVEL=3 #INFO and above only'
} | sudo tee /etc/6lbr/6lbr.conf
echo

echo dwc_otg.speed=1 `cat /boot/cmdline.txt` | sudo tee /boot/cmdline.txt # This enables the support for the high baudrate usb-serial SLIP connector with the slip-radio mote. (slip_radio node)
#sudo reboot
echo

sudo /usr/lib/6lbr/bin/nvm_tool --update --channel 25 /etc/6lbr/nvm.dat > /dev/null # To change to a different channel, 25, which is used by cc26xx Contiki port.
sudo /usr/lib/6lbr/bin/nvm_tool --update --wsn-prefix aaaa:: /etc/6lbr/nvm.dat > /dev/null # Set WSN (network) prefix to aaaa::
#/usr/lib/6lbr/bin/nvm_tool --print /etc/6lbr/nvm.dat # To see the configurations set above. # Or visit http://[bbbb::100]/config.html for even more info.

sudo service radvd restart

echo
echo -e "${YELLOW}Lastly you might want to edit texts in ${GREEN}/etc/wpa_supplicant/wpa_supplicant.conf${YELLOW} as well. Google ${GREEN}wpa_supplicant.conf${YELLOW} for help. Then ${GREEN}sudo reboot${YELLOW} after finshing everything you needed to start using 6lbr.${NC}"
echo

echo -e ${CYAN}---------- end of `basename "$0"` ----------${NC}
