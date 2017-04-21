/*
아두이노스티커 
CH C
ID 1
DH 0
DL 2
MY 1
SH 13A200
SL 40F9D5E4

C스티커
CH C
ID 1
DH 0
DL 1
MY 2
SH 13A200
SL 416649C0
*/

/*  
*   2017-04-16
*   Rx - Tx XBeeRadio 테스트버젼 메시지를 보내면 다시 그메시지를 전송
*   만약에 Receive가 안될시 지그비 5v 점퍼선을 뺏다 다시 끼세요
*   
*/


//라이브러리 포함
#include <XBee.h>
#include <XbeeRadio.h>

XBeeRadio xbee = XBeeRadio();
XBeeRadioResponse response = XBeeRadioResponse();

// 받을 rx16 객체 생성
Rx16Response rx = Rx16Response();

// tx16 개체 생성
Tx16Request tx;

void setup()
{
  Serial.begin(9600);
  xbee.begin(Serial);
  xbee.init();
}

void loop()
{
  //데이터가 들어오는지 확인함 
  if(xbee.checkForData())
  {
    //데이터가 들어오면 응답변수에 저장함.
    xbee.getResponse().getRx16Response(rx);

    //data를 저장
    uint8_t* data = rx.getData();
    //data를 보낸 상대방 주소 저장
    uint16_t sender = rx.getRemoteAddress16();
    //데이터 길이 저장
    uint8_t dataLength = rx.getDataLength(); 
    //tx변수에 저장
    tx = Tx16Request(sender, data, dataLength);
    //tx로 보낸다.
    xbee.send(tx);
  } 

}
