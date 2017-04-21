/*
  2017-04-15
  Xbeeradio Rx 데이터를 보내면 받음.
*/


#include <XBee.h>

XBee xbee = XBee();
XBeeResponse response = XBeeResponse();
Rx16Response rx16 = Rx16Response(); //rx16은 데이터가 16비트
Rx64Response rx64 = Rx64Response(); //rx64는 데이터가 64비트

int statusLed = 11;
int errorLed = 12;
int dataLed = 10;

uint8_t option = 0;
uint8_t data = 0;

void flashLed(int pin, int times, int wait) {

  for (int i = 0; i < times; i++) {
    digitalWrite(pin, HIGH);
    delay(wait);
    digitalWrite(pin, LOW);

    if (i + 1 < times) {
      delay(wait);
    }
  }
}

void setup() {
  pinMode(statusLed, OUTPUT);
  pinMode(errorLed, OUTPUT);
  pinMode(dataLed,  OUTPUT);

  Serial.begin(9600);
  xbee.setSerial(Serial);

  flashLed(statusLed, 3, 50);
}

void loop() {

  //xbee에 패킷을 읽어옴
  xbee.readPacket();

  //만약 응답에서 데이터가 존재한다면
  if (xbee.getResponse().isAvailable()) {
    // 무언가의 데이터를 받음

    if (xbee.getResponse().getApiId() == RX_16_RESPONSE 
      || xbee.getResponse().getApiId() == RX_64_RESPONSE) {
        // rx패킷을 받음


        //그게 16비트의 응답이라면 
        if (xbee.getResponse().getApiId() == RX_16_RESPONSE) {
          //rx16에 저장
          xbee.getResponse().getRx16Response(rx16);
          //옵션 저장
          option = rx16.getOption();
          //데이터 길이만큼 Serial에 뽑아줌
          for( int i = 0; i < rx16.getDataLength(); i++) {
            Serial.print(rx16.getData(i), HEX);
            Serial.print(" ");
          }
          //64비트라면
        } else {
          xbee.getResponse().getRx64Response(rx64);
          option = rx64.getOption();
          for( int i = 0; i < rx64.getDataLength(); i++) {
            Serial.print(rx64.getData(i), HEX);
            Serial.print(" ");
          }
        }
        Serial.println();
  
        flashLed(statusLed, 1, 10);
    } else {
      flashLed(errorLed, 1, 25);
      //무언가 못받음
    }
  } else if (xbee.getResponse().isError()) {
    //Error
  } 
}
