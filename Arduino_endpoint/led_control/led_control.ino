const int ledPin1=13;
const int ledPin2=12;
const int ledPin3=11;

void setup(){
  pinMode(ledPin1, OUTPUT);
  pinMode(ledPin2, OUTPUT);
pinMode(ledPin3, OUTPUT);
Serial.begin(9600);  
}

void loop() {
  if(Serial.available())
  {
    light(Serial.read() - '0');
  }
  delay(500); 
}

void light(int n){
  switch(n)
{ 
  case 1:
  digitalWrite(ledPin1, HIGH);
  // 1초간 대기합니다.
  delay(1000);        
  // LED를 OFF 합니다.
  digitalWrite(ledPin1,LOW);
  // 1초간 대기합니다.
  delay(1000);
  break;
  
  case 2:
  digitalWrite(ledPin2, HIGH);
  // 1초간 대기합니다.
  delay(1000);        
  // LED를 OFF 합니다.
  digitalWrite(ledPin2,LOW);
  // 1초간 대기합니다.
  delay(1000);
  break;

  case 3:
  digitalWrite(ledPin3, HIGH);
  // 1초간 대기합니다.
  delay(1000);        
  // LED를 OFF 합니다.
  digitalWrite(ledPin3,LOW);
  // 1초간 대기합니다.
  delay(1000);
  break;     
}
}
