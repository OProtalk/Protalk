/*
  2017-04-14
  아두이노 Xbee API 모드
  준비물 : 아두이노 Uno, 브레드보드, XBee, LED2개
  HELLOWORLD!를 XBee에게 보내는 코드
*/

#include <XBee.h>

XBee xbee = XBee();
unsigned long start = millis();
// payload는 내가 보낼 데이터
uint8_t payload[] = { 'H', 'E', 'L', 'L', 'O', 'W', 'O', 'R', 'L', 'D', '!' };

// 16비트 주소와 64비트 주소를 사용할 수 있음.
// 16비트 주소는 상대방의 원격 Xbee주소를 사용,
// ex) Tx16Request tx = Tx16Request(0x1874, payload, sizeof(payload));
// 64비트 주소는 상대Xbee의 SH + SL 주소
XBeeAddress64 addr64 = XBeeAddress64(0x0013a200, 0x416649c0);

// 상대 XBee가 MY가 FFFF로 설정되어 있지 않으면 RX16패킷으로 수신받음
// 즉, 내가 64로보내면 상대방은 16bit로 받는다 이말.
Tx64Request tx = Tx64Request(addr64, payload, sizeof(payload));
TxStatusResponse txStatus = TxStatusResponse();
int pin5 = 0;
int statusLed = 11;
int errorLed = 12;
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
  Serial.begin(9600);
  xbee.setSerial(Serial);
}
void loop() {
  // 시작 지연후 전송, 
    if (millis() - start > 1000) {
      // 페이로드 배치
      pin5 = analogRead(5);
      pin5 = 2000;
      payload[0] = 'H';
      payload[1] = 'E';
      payload[2] = 'L';
      payload[3] = 'L';
      payload[4] = 'O';
      payload[5] = 'W';
      payload[6] = 'O';
      payload[7] = 'R';
      payload[8] = 'L';
      payload[9] = 'D';
      xbee.send(tx);
      Serial.println("Sending TX packets.");
      // LED 표시
      flashLed(statusLed, 1, 100);
    }
    // TX 요청을 보낸 후 응답상태를 대기
    // 상태 응답을 위해 최대 5초 기다림
    if (xbee.readPacket(5000)) {
      // 응답 완료!
      Serial.println("\tHaving Response.");

      if (xbee.getResponse().getApiId() == TX_STATUS_RESPONSE) {
        xbee.getResponse().getZBTxStatusResponse(txStatus);
        // 전달 상태를 얻습니다.
        Serial.println("\t\tTX STATUS");
        if (txStatus.getStatus() == SUCCESS) {
          // 성공
          flashLed(statusLed, 5, 50);
          Serial.println("\t\t\tSUCCESS");
        } else {
          // 전원이 켜져있는지 확인하십쇼 ㅎㅎ 
          flashLed(errorLed, 3, 500);
        }
      }
    } else if (xbee.getResponse().isError()) {
    } else {
      // XBee가 TX 상태 응답 제공 X, 라디오 구성 확인.
        flashLed(errorLed, 8, 50);
    }
    Serial.flush();
    delay(1000);
}
