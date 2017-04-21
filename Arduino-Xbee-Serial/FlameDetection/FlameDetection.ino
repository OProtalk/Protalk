 // 13번 핀을 LED로 지정합니다.
int ledPin = 13;
// 디지털 2번핀을 불꽃감지센서로 지정합니다.
int inputPin = 3;
// 불꽃 감지 센서의 상태를 저장합니다.(처음 상태를 LOW로 설정)
int pirState = LOW;
// 센서 값을 읽기 위해 변수를 선언합니다. 
int val = 0;  

         
void setup() {
  // put your setup code here, to run once:
  // LED 를 출력으로 설정합니다.
  pinMode(ledPin, OUTPUT);      
  // 센서를 입력으로 설정합니다.
  pinMode(inputPin, INPUT);     
  Serial.begin(9600); 
}

void loop() {
  // 센서값을 읽어들입니다.
  val = digitalRead(inputPin);  

  Serial.println(val);

//불꽃감지시 High
  if(val == LOW )
  {
    digitalWrite(ledPin, LOW);
    Serial.println("no fire");
  } else if( val == HIGH) {
    digitalWrite(ledPin, HIGH);
    Serial.println("fire");
  }
  delay(1000);
}
