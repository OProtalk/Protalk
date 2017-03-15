#include <DHT11.h> 
int pin=3;
DHT11 dht11(pin);        
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600); //통신속도 설정
}

void loop() {
  // put your main code here, to run repeatedly:
int err;
  float temp, humi;
  if((err=dht11.read(humi, temp))==0) //온도, 습도 읽어와서 표시
  {
    Serial.print("temperature:");
    Serial.print(temp);
    Serial.print(" humidity:");
    Serial.print(humi);
    Serial.println();
  }
  else                                //에러일 경우 처리
  {
    Serial.println();
    Serial.print("Error No :");
    Serial.print(err);
    Serial.println();    
  }
  delay(1000);                        //1초마다 측정
}
