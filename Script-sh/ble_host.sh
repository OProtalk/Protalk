#!/bin/bash
# ble host setup
# written by nanite 2017-03-04
# ref: https://developer.nordicsemi.com/nRF5_IoT_SDK/doc/0.9.0/html/a00092.html

NC='\033[0m' # no color
GREEN='\033[0;32m'
CYAN='\033[0;36m'
YELLOW='\033[1;33m'

echo -e ${CYAN}-------------- `basename "$0"` -------------${NC}

# Mount the debugfs file system to /sys/kernel/debug. You can ls to check the contents of the folder.
#sudo mount -t debugfs none /sys/kernel/debug
#ls /sys/kernel/debug

# Load 6LoWPAN module. You might want to lsmod to verify that the module is indeed loaded.
sudo modprobe bluetooth_6lowpan
#lsmod | grep bluetooth_6lowpan # Enable the bluetooth 6lowpan module. The PSM value should be set to 0x23(35) to enable the module, if using Linux kernel version less than 4.0.
#echo 35 | sudo tee /sys/kernel/debug/bluetooth/6lowpan_psm
echo 1 | sudo tee /sys/kernel/debug/bluetooth/6lowpan_enable > /dev/null

# HCI commands such as hciconfig and hcitool are used to configure Bluetooth devices.
# The device name hciX is assigned to the (Bluetooth) device installed in the system. 
echo
echo -e "${YELLOW}Instructions${NC}"
echo
echo -e "To discover all advertising devices, scan using following hcitool command."
echo -e "${YELLOW}>>> ${GREEN}sudo hcitool lescan${NC}"
echo -e "Then connect to the device; an IPv6 router sending out RA messages."
echo -e "${YELLOW}>>> ${GREEN}echo \"connect ${CYAN}(Bluetooth Device Address; MAC address)${GREEN} 1\" | sudo tee /sys/kernel/debug/bluetooth/6lowpan_control${NC}"
echo -e "List all connected BLE devices after that, to see the connection to the router has successfully been made."
echo -e "${YELLOW}>>> ${GREEN}sudo hcitool con${NC}"
echo
echo -e "Check if you have established a connection. You should be able to find out your IPv6 address on bt0 interface."
echo -e "${YELLOW}>>> ${GREEN}ifconfig${NC}"
echo -e "Try to ping the device."
echo -e "${YELLOW}>>> ${GREEN}ping6 -I bt0 ${CYAN}(IPv6 address of the device)${GREEN} -c 5${NC}"
echo -e "Or you can do the multicast."
echo -e "${YELLOW}>>> ${GREEN}ping6 -I bt0 ff02::1${NC}"
echo
echo -e "To disconnect from the device."
echo -e "${YELLOW}>>> ${GREEN}echo \"disconnect ${CYAN}(Bluetooth Device Address; MAC address)${GREEN}\" | sudo tee /sys/kernel/debug/bluetooth/6lowpan_control${NC}"
echo -e "Check if there are active connections left."
echo -e "${YELLOW}>>> ${GREEN}ifconfig${NC}"
echo
echo "There you go. Good luck."
echo
echo -e ${CYAN}---------- end of `basename "$0"` ----------${NC}
