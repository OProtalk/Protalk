/*****************************************************************************
*
* Copyright (C) 2016 Diwell Electronics Co.,Ltd.
* Project Name : PM1001 UART Code <softwareserial 이용>
* Version : 1.0 (2016.05.04)
* SYSTEM CLOCK : 16Mhz 
* BOARD : Arduino UNO. 5V operation 


 PORT Description

1. RX : 13           
2. TX : 11
  먼지센서 전원은 5V로 하셔야 하며 포트 연결 방법은 회로도를 참고하십시오.

 Revision history.

1. 2016.5.4  : First version is released.
****************************************************************************/

#include <SoftwareSerial.h>
SoftwareSerial mySerial(13, 11);                        // RX 13, TX 11
unsigned char Send_data[4] = {0x11,0x01,0x01,0xED};       // 읽는명령
unsigned char Receive_Buff[16];                           // data buffer
unsigned long PCS;                                        // 수량 저장 변수 
float ug;                                                 // 농도 저장 변수 
unsigned char recv_cnt = 0;

void Send_CMD(void)                                        // COMMAND
{
  unsigned char i;
  for(i=0; i<4; i++)
  {
    mySerial.write(Send_data[i]);
    delay(1);      // Don't delete this line !!
  }
}
unsigned char Checksum_cal(void)                          // CHECKSUM 
{
  unsigned char count, SUM=0;
  for(count=0; count<15; count++)
  {
     SUM += Receive_Buff[count];
  }
  return 256-SUM;
}

void setup() {
  pinMode(13,INPUT);
  pinMode(11,OUTPUT);
  Serial.begin(9600);
  while (!Serial) ;
  mySerial.begin(9600);
  while (!mySerial);
}

void loop() {
 
 
  Send_CMD();  // Send Read Command
  while(1)
  {
    if(mySerial.available())
    { 
       Receive_Buff[recv_cnt++] = mySerial.read();
      if(recv_cnt ==16){recv_cnt = 0; break;}
    }
  } 
  if(Checksum_cal() == Receive_Buff[15])  // CS 확인을 통해 통신 에러 없으면
  {
        PCS = (unsigned long)Receive_Buff[3]<<24 | (unsigned long)Receive_Buff[4]<<16 | (unsigned long)Receive_Buff[5]<<8| (unsigned long)Receive_Buff[6];  // 수량 
        ug = (float)PCS*3528/100000; // 농도 변환(이 식은 PM1001 모델만 적용됩니다.)
        Serial.write("PCS : ");
        Serial.print(PCS);
        
        Serial.write(",  ug : ");
        Serial.println(ug);
        
   }
   else
   {
     Serial.write("CHECKSUM Error");
   }
   delay(1000);       //1000ms
    
}
