#!/bin/bash
# 6LoWPAN-Border-Router setup
# written by nanite 2017-06-17

NC='\033[0m' # no color
GREEN='\033[0;32m'
CYAN='\033[0;36m'
YELLOW='\033[1;33m'

echo -e ${CYAN}-------------- `basename "$0"` -------------${NC}

sudo route -A inet6 add aaaa::/64 gw bbbb::100
sudo brctl addbr br1
sudo brctl addif br1 tap0 eth0
sudo brctl show

echo
echo -e "${YELLOW}Watch ${GREEN}/var/log/6lbr.log${YELLOW} for more.${NC}"
echo

echo -e ${CYAN}---------- end of `basename "$0"` ----------${NC}
