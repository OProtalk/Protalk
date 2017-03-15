///===============================================================
/// The load resistance on the board
#define RLOAD 10.0

/// Calibration resistance at atmospheric CO2 level

#define RZERO 76.63

/// Parameters for calculating ppm of CO2 from sensor resistance
#define PARA 116.6020682
#define PARB 2.769034857
 
 void setup() {
 Serial.begin(9600);
}
 
void loop() {

 int val = analogRead(0);
 val = (1023./(float)val) * 5. - 1.* RLOAD;
 float Resistance;
 Resistance = val;
//
 float PPM;
 PPM = PARA * pow((Resistance/RZERO), -PARB);
//
 Serial.println(PPM,1);
 
  delay(1000);
}
///===============================================================

