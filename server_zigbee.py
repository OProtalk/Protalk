import serial
import time

ser = serial.Serial('/dev/ttyUSB0', 9600)

while 1:
    #print ser.readline()
    ser.write("1")
    time.sleep(1000)
