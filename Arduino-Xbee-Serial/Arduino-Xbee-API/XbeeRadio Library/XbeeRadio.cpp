﻿/*
XBeeRadio.h - Library for communicating with heterogenous 802.15.4 networks.
Created by Vasileios Georgitzikis, November 23, 2010.
*/

#include "XbeeRadio.h"

//XBeeRadioResponse::XBeeRadioResponse() : XBeeResponse(){}
uint8_t XBeeRadioResponse::validPacket(uint8_t lp1,uint8_t lp2,uint8_t port)
{
	Rx16Response response = Rx16Response();
	uint8_t ApiId = XBeeResponse::getApiId();
	if(ApiId == RX_16_RESPONSE)
	{
		getRx16Response(response);
		//uint8_t myData = response.getData(0);
		if(response.getData(0) == lp1 && response.getData(1) == lp2)
			return response.getData(2);
		return false;
	}
}

uint8_t XBeeRadioResponse::getDataLength()
{
	Rx16Response response = Rx16Response();
	getRx16Response(response);
	return response.getDataLength()-3;
}
uint8_t XBeeRadioResponse::getData(int index)
{
	Rx16Response response = Rx16Response();
	getRx16Response(response);
	return response.getData(index+3);
}
uint8_t* XBeeRadioResponse::getData()
{
	Rx16Response response = Rx16Response();
	getRx16Response(response);
	return response.getData()+3;
}
// uint8_t XBeeRadioResponse::validPacket()
// {
// 	return validPacket(LP1, LP2, DEFAULT_PORT);
// }
// uint8_t XBeeRadioResponse::validPacket(uint8_t valid_port)
// {
// 	return validPacket(LP1, LP2, valid_port);
// }

bool XBeeRadio::checkForData(void)
{
	return checkForData(LP1, LP2, DEFAULT_PORT);
}
bool XBeeRadio::checkForData(uint8_t valid_port)
{
	checkForData(LP1, LP2, valid_port);
}

bool XBeeRadio::checkForData(uint8_t lp1,uint8_t lp2,uint8_t port)
{
	this->readPacket();
	if(this->getResponse().isAvailable())
	{
		Rx16Response response = Rx16Response();
		if(this->getResponse().getApiId() == RX_16_RESPONSE)
		{
			this->getResponse().getRx16Response(response);
			//uint8_t myData = response.getData(0);
			return true;
		} else 
		{
			return false;
		}
		//Rx16Response rx16 = Rx16Response();
		//Rx64Response rx64 = Rx64Response();
		//if( this->getResponse().getApiId() == RX_16_RESPONSE 
		//	|| this->getResponse().getApiId() == RX_64_RESPONSE) {

		//		if (this->getResponse().getApiId() == RX_16_RESPONSE) {
		//			this->getResponse().getRx16Response(rx16);
		//			//option = rx16.getOption();
		//			//data = rx16.getData(0);
		//			return true;
		//		} else {
		//			this->getResponse().getRx64Response(rx64);
		//			//option = rx64.getOption();
		//			//data = rx64.getData(0);
		//			return true;
		//		}
		//		
		//}
		/*uint8_t port_validity = this->getResponse().validPacket(lp1, lp2, port);
		if(port_validity == lp2)
		return true;
		else if(port_validity == PROGRAMMING_PORT)
		{
		pinMode(LED_PIN, OUTPUT);
		digitalWrite(LED_PIN, HIGH);
		delay(200);
		digitalWrite(LED_PIN, LOW);
		delay(200);
		digitalWrite(LED_PIN, HIGH);
		delay(200);
		digitalWrite(LED_PIN, LOW);
		delay(200);
		digitalWrite(LED_PIN, HIGH);
		delay(200);
		digitalWrite(LED_PIN, LOW);
		delay(200);

		Rx16Response rx = Rx16Response();
		this->getResponse().getRx16Response(rx);
		uint16_t sender = rx.getRemoteAddress16();
		getReadyForProgramming(sender);
		}*/

	} else {
		return false;
	}
	return false;

}

uint8_t XBeeRadioResponse::getRssi()
{
	return getFrameData()[2];
}

//XBeeRadio::XBeeRadio() : XBee(){}
void 	XBeeRadio::getResponse(XBeeRadioResponse &response)
{
	XBee::getResponse(response);
}
XBeeRadioResponse& XBeeRadio::getResponse()
{
	return (XBeeRadioResponse&) XBee::getResponse();
}
void XBeeRadio::send(Tx16Request &request)
{
	send(request, DEFAULT_PORT);
}

void XBeeRadio::send(Tx16Request &request, uint8_t port)
{
	uint8_t *temp_payload = request.getPayload();
	uint8_t payloadLength = request.getPayloadLength();
	uint8_t *new_payload = (uint8_t*) malloc(sizeof(uint8_t) * (payloadLength+3));

	for(int i=0;i<payloadLength;i++)
	{
		new_payload[i+3] = temp_payload[i];
	}
	new_payload[0] = LP1;
	new_payload[1] = LP2;
	new_payload[2] = port;

	request.setPayload(new_payload);
	request.setPayloadLength(payloadLength+3);

	XBee::send(request);

	delay(10);	

	request.setPayload(temp_payload);
	request.setPayloadLength(payloadLength);

	free(new_payload);
}

void XBeeRadio::send(AtCommandRequest request)
{
	XBee::send(request);
}

bool XBeeRadio::sendAndCheck(Tx16Request &request)
{
	return sendAndCheck(request, DEFAULT_PORT);
}

bool XBeeRadio::sendAndCheck(Tx16Request &request, uint8_t port)
{
	bool retVal = false;
	send(request, port);
	TxStatusResponse txStatus = TxStatusResponse();

	// after sending a tx request, we expect a status response
	// wait up to 5 seconds for the status response
	if (readPacket(5000))
	{
		// got a response! should be a znet tx status            	
		if (getResponse().getApiId() == TX_STATUS_RESPONSE)
		{
			getResponse().getZBTxStatusResponse(txStatus);

			// get the delivery status, the fifth byte
			if (txStatus.getStatus() == SUCCESS)
			{
				// success.  time to celebrate
				retVal = true;
			} 
			else
			{
				// the remote XBee did not receive our packet. is it powered on?
			}
		}      
	}
	else
	{
		// local XBee did not provide a timely TX Status Response -- should not happen
	}
}

uint8_t XBeeRadio::init(/*NewSoftSerial Serial*/void)
{
	pinMode(LED_PIN, OUTPUT);
	digitalWrite(LED_PIN, HIGH);

	delay(1000);

	//XBeeRadio temp_xbee = XBeeRadio();
	// serial low
	uint8_t slCmd[] = {'S','L'};

	uint8_t chCmd[] = {'C','H'};
	uint8_t chValue[] = {0x0C};

	uint8_t pidCmd[] = {'I','D'};
	uint8_t pidValue[] = {0x01};

	uint8_t apCmd[] = {'A','P'};
	uint8_t apValue[] = {0x02};

	uint8_t mmCmd[] = {'M','M'};
	uint8_t mmValue[] = {0x02};

	uint8_t myCmd[] = {'M','Y'};
	uint8_t myValue[] = {0x00, 0x01};

	uint8_t acCmd[] = {'A', 'C'};
	uint8_t acValue[] = {};


	// get SL
	uint8_t buffer[] = {0x00, 0x0B};
	sendAtCommand(slCmd, buffer);

	//set MY	
	buffer[0] &= 0x0f;

	myAddress = buffer[1];
	myAddress <<= 8;
	myAddress += buffer[0];

	myValue[0] = buffer[0];
	myValue[1] = buffer[1];

	// Serial.print("My address should be: ");
	// Serial.print(myValue[0], HEX);
	// Serial.println(myValue[1], HEX);

	sendAtCommand(myCmd, myValue, sizeof(myValue));
	// Serial.println("Set MY");

	sendAtCommand(chCmd, chValue, sizeof(chValue));
	// Serial.println("Set CH");

	sendAtCommand(pidCmd, pidValue, sizeof(pidValue));
	// Serial.println("Set PID");

	sendAtCommand(apCmd, apValue, sizeof(apValue));
	// Serial.println("Set AP");

	sendAtCommand(mmCmd, mmValue, sizeof(mmValue));
	// Serial.println("Set MM");

	// set AC (Enable the values we sent)
	sendAtCommand(acCmd, buffer);	
	// Serial.println("Set AC");
	digitalWrite(LED_PIN, LOW);
}
uint8_t XBeeRadio::trySendingCommand(uint8_t buffer[2], AtCommandRequest atRequest,AtCommandResponse atResponse)
{
	uint8_t error=0;

	// send the command
	this->send(atRequest);

	// wait up to 5 seconds for the status response
	if (this->readPacket(10000))
	{
		//Serial.println("Error");
		// should be an AT command response
		if (this->getResponse().getApiId() == AT_COMMAND_RESPONSE)
		{
			this->getResponse().getAtCommandResponse(atResponse);

			if (atResponse.isOk())
			{
				Serial.print("Command [");
				Serial.print(atResponse.getCommand()[0]);
				Serial.print(atResponse.getCommand()[1]);
				Serial.println("] was successful!");

				if (atResponse.getValueLength() > 0)
				{

					Serial.print("Command value length is ");
					Serial.println(atResponse.getValueLength(), DEC);

					Serial.print("Command value: ");

					buffer[0] = atResponse.getValue()[2];
					buffer[1] = atResponse.getValue()[3];


					for (int i = 0; i < atResponse.getValueLength(); i++) {
						Serial.print(atResponse.getValue()[i], HEX);
						Serial.println(" ");
					}

				}
			} 
			else
			{
				Serial.println("Error");
				error=1;
			}
		} 
		else
		{
			Serial.println("Error");
			error=1;
		}   
	} 
	else
	{
		Serial.println("Error");
		error=1;
	}

	return error;
}
uint16_t XBeeRadio::getMyAddress()
{
	return this->myAddress;
}

uint8_t XBeeRadio::init(uint8_t channel)
{
	uint8_t return_val = init();
	setChannel(channel);
	return return_val;
}

bool XBeeRadio::setChannel(uint8_t channel)
{
	digitalWrite(LED_PIN, HIGH);
	delay(100);
	if(channel<0x0b || channel>0x17)
	{
		digitalWrite(LED_PIN, LOW);
		return false;		
	}

	uint8_t chCmd[] = {'C','H'};
	uint8_t chValue[] = {channel};
	AtCommandRequest atRequest = AtCommandRequest(chCmd);
	atRequest.setCommandValue(chValue);
	atRequest.setCommandValueLength(sizeof(chValue));

	AtCommandResponse atResponse = AtCommandResponse();

	uint8_t buffer[] = {0x00, 0x0B};

	// set CH
	trySendingCommand(buffer, atRequest, atResponse);
	atRequest.clearCommandValue();	
	digitalWrite(LED_PIN, LOW);
	return true;
}

void XBeeRadio::getReadyForProgramming(uint16_t programmer_address)
{
	pinMode(LED_PIN, OUTPUT);

	uint8_t reCmd[] = {'R','E'};

	uint8_t slCmd[] = {'S','L'};

	uint8_t myCmd[] = {'M','Y'};
	uint8_t myValue[] = {0x00, 0x01};

	uint8_t dhCmd[] = {'D','H'};
	uint8_t dhValue[] = {0x00, 0x00};

	uint8_t dlCmd[] = {'D','L'};

	uint8_t chCmd[] = {'C','H'};
	uint8_t chValue[] = {0x0C};

	uint8_t pidCmd[] = {'I','D'};
	uint8_t pidValue[] = {0x00, 0x01};

	uint8_t apCmd[] = {'A','P'};
	uint8_t apValue[] = {0x02};

	uint8_t mmCmd[] = {'M','M'};
	uint8_t mmValue[] = {0x00};

	uint8_t bdCmd[] = {'B','D'};
	uint8_t bdValue[] = {0x06};

	uint8_t rrCmd[] = {'R','R'};
	uint8_t rrValue[] = {0x00};

	uint8_t roCmd[] = {'R','O'};
	uint8_t roValue[] = {0x03};

	uint8_t d3Cmd[] = {'D','3'};
	uint8_t d3Value[] = {0x00};

	uint8_t iuCmd[] = {'I','U'};
	uint8_t iuValue[] = {0x01};

	uint8_t iaCmd[] = {'I','A'};
	uint8_t iaValue[] = {0xFF, 0xFF};

	uint8_t wrCmd[] = {'W','R'};

	uint8_t acCmd[] = {'A', 'C'};

	// get SL
	uint8_t buffer[] = {0x00, 0x0B};
	sendAtCommand(slCmd, buffer);

	//set MY	
	buffer[0] &= 0x0f;
	myAddress = buffer[1];
	myAddress <<= 8;
	myAddress += buffer[0];

	myValue[0] = buffer[0];
	myValue[1] = buffer[1];

	// Serial.print("My address should be: ");
	// Serial.print(myValue[0], HEX);
	// Serial.println(myValue[1], HEX);

	sendAtCommand(myCmd, myValue, sizeof(myValue));
	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);

	// Serial.println("Set MY");

	sendAtCommand(dhCmd, dhValue, sizeof(dhValue));
	// Serial.println("Set DH");

	/***** THIS MUST GO AWAY!!!! ******/
	// programmer_address = 0x664f;
	uint8_t dlValue[2];
	dlValue[0] = programmer_address >> 8;
	dlValue[1] = (uint8_t) (programmer_address % 0x100);
	sendAtCommand(dlCmd, dlValue, sizeof(dlValue));
	// Serial.println("Set DL");

	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);

	sendAtCommand(chCmd, chValue, sizeof(chValue));
	// Serial.println("Set CH");

	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);

	sendAtCommand(pidCmd, pidValue, sizeof(pidValue));
	// Serial.println("Set PID");

	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);

	sendAtCommand(mmCmd, mmValue, sizeof(mmValue));
	// Serial.println("Set MM");

	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);

	sendAtCommand(rrCmd, rrValue, sizeof(rrValue));
	// Serial.println("Set RR");

	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);

	sendAtCommand(roCmd, roValue, sizeof(roValue));
	// Serial.println("Set RO");

	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);

	sendAtCommand(d3Cmd, d3Value, sizeof(d3Value));
	// Serial.println("Set D3");
	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);

	sendAtCommand(iuCmd, iuValue, sizeof(iuValue));
	// Serial.println("Set IU");
	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);

	sendAtCommand(iaCmd, iaValue, sizeof(iaValue));
	// Serial.println("Set IA");
	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);

	// sendAtCommand(wrCmd, buffer);
	sendAtCommand(acCmd, buffer);
	// Serial.println("Set wr");
	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);

	sendAtCommand(bdCmd, bdValue, sizeof(bdValue));
	// Serial.println("Set BD");
	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);

	Serial.begin(9600);

	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);

	// sendAtCommand(wrCmd, buffer);
	sendAtCommand(acCmd, buffer);
	// Serial.println("Set wr");
	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);

	sendAtCommand(apCmd, apValue, sizeof(apValue));
	// Serial.println("Set AP");
	digitalWrite(LED_PIN, HIGH);
	delay(200);
	digitalWrite(LED_PIN, LOW);
	delay(500);


	// set AC (Enable the values we sent)
	// sendAtCommand(acCmd, buffer);	
	// Serial.println("Set AC");



	unsigned long timestamp = millis();
	while(millis() - timestamp < 30000)
	{
		digitalWrite(LED_PIN, HIGH);
		delay(200);
		digitalWrite(LED_PIN, LOW);
		delay(200);
	}
}

void XBeeRadio::sendAtCommand(uint8_t command[], uint8_t reply[])
{
	AtCommandRequest atRequest = AtCommandRequest(command);
	AtCommandResponse atResponse = AtCommandResponse();
	while(true)
	{
		// Serial.println("Getting SL");
		if(trySendingCommand(reply, atRequest, atResponse) != 1)
			break;
	}	
}
void XBeeRadio::sendAtCommand(uint8_t command[], uint8_t value[], uint8_t length)
{
	AtCommandRequest atRequest = AtCommandRequest(command);
	AtCommandResponse atResponse = AtCommandResponse();
	atRequest.setCommandValue(value);
	atRequest.setCommandValueLength(length);

	uint8_t buffer[2];
	while(true)
	{
		// Serial.println("Setting MY");
		if(trySendingCommand(buffer, atRequest, atResponse) != 1)
			break;
	}
	atRequest.clearCommandValue();
}


void XBeeRadio::initialize_xbee_module(Stream &serial)
{
	pinMode(13, OUTPUT);
	unsigned long timestamp = millis();
	while(millis() - timestamp < 10000)
	{
		digitalWrite(13, HIGH);
		delay(100);
		digitalWrite(13, LOW);
		delay(100);
	}
	digitalWrite(13, HIGH);

	uint8_t atvr[] = {'V', 'R'};
	AtCommandRequest atRequest = AtCommandRequest(atvr);
	AtCommandResponse atResponse = AtCommandResponse();

	uint8_t buffer[2];
	begin(serial);

	// NewSoftSerial Serial(4, 5);
	Serial.begin(9600);
	Serial.println("Starting!");
	uint8_t api_mode = trySendingCommand(buffer, atRequest, atResponse);
	//Serial.end();

	if(api_mode != 1)
	{
		// Serial.println("ALREADY IN API MODE");
		return;
	}
	// Serial.println("Trying to make everything right");

	long cur_baud = setup_baudrate();
	DBG(Serial.print("Baudrate: ");)
		DBG(Serial.println(cur_baud);)
		setup_command("ATAP2");
	setup_command("ATBD5");
	setup_command("ATAC");
	digitalWrite(13, LOW);
}


//Send an AT command
bool XBeeRadio::setup_command(char* command)
{
	char bla[10];
	strcpy(bla, command);
	int cmd_length = strlen(bla);
	bla[cmd_length++] = '\r';
	bla[cmd_length] = '\0';
	//  = "ATMM2\r";
	bool ret_val;
	for(int i = 0; i < 5; i++)
	{
		for(int k = 0; k < 6; k++)
		{
			Serial.print(bla[k]);
			delay(200);
		}

		bool response = wait_for_response(3000);
		DBG(
			if(response)
				Serial.println("Got response");
			else
				Serial.println("No response");
		)

			ret_val = check_for_response();
		if(ret_val) break;
	}
	return ret_val;
}


long XBeeRadio::setup_baudrate(void)
{
	long ret_val = setup_baudrate(9600);
	return ret_val;
}

//find the correct baudrate to talk to the XBee
long XBeeRadio::setup_baudrate(long starting_baud)
{
	long bla[] = {9600, 19200, 38400, 57600, 115200};
	int k;
	for(int i = 0 ; i < 5; i++)
		if(bla[i] == starting_baud)
			k = i;

	while(true)
	{
		DBG(Serial.print("Starting Baudrate: ");)
			DBG(Serial.println(bla[k]);)
			bool ret_val = setup_xbee(bla[k++]);
		//we found our baudrate
		if(ret_val) break;
		if(k == 5)
		{
			//wait a while before starting from 9600 baudrate
			delay(3000);
			k = 0;
		}
	}
	return bla[k-1];
}

//Ping the XBee 
bool XBeeRadio::setup_xbee(long baudrate)
{
	Serial.begin(baudrate);
	//for some reason, the first message doesn't go to
	//the XBee, so we sent some dummy data first.
	Serial.print("X");
	delay(1100);
	//Go into API mode
	for(int k = 0; k < 3; k++)
	{
		Serial.print("+");
		delay(200);
	}

	//Wait for a respsonse (OK) from the XBee.
	bool response = wait_for_response(2000);
	DBG(
		if(response)
			Serial.println("Got response");
		else
			Serial.println("No response");
	)

		bool ret_val = check_for_response();
	if(!ret_val)
	{
	}
	return ret_val;
}


//Wait for a response by the XBee after sending a command
bool XBeeRadio::wait_for_response(unsigned long milliseconds)
{
	unsigned long timestamp = millis(); 
	bool return_value = false; 
	while(millis() - timestamp < milliseconds)
	{
		if(Serial.available() > 2)
		{
			return_value = true;
			break;
		}
	}
	return return_value;
}

//Check if the response was 'OK'
bool XBeeRadio::check_for_response(void)
{
#define BUFFER_SIZE 10
	char buffer[BUFFER_SIZE];
	for(int i = 0 ; i< BUFFER_SIZE; i++)
		buffer[i] = 0;

	int j = 0;
	while(Serial.available() && j < (BUFFER_SIZE-1))
	{
		int tmpChar = Serial.read();
		buffer[j++] = (char) tmpChar;
		buffer[j]='\0';
		//      Serial.println(tmpChar, BYTE);
	}

	DBG(Serial.print(buffer);)
		buffer[2] = '\0';
	bool return_value = false;
	if(!strcmp(buffer,"OK"))
		return_value = true;

	DBG(
		if(return_value)
			Serial.println("YAY");
		else
			Serial.println("NAY");
	)

		return return_value;
}