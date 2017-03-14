package com.protalk.serial;

import java.io.*;

import gnu.io.*;

public class Serial {
	private InputStream mIn = null;
	private OutputStream mOut = null;

	public Serial(String portName) throws NoSuchPortException, UnsupportedCommOperationException, PortInUseException, IOException {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			// 포트 개방
			CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
			if (commPort instanceof SerialPort) {
				// 포트 -> 시리얼 포트 // 통신 속도 설정
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(
						9600, 
						SerialPort.DATABITS_8, 
						SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE
						);
				// 포트 -> 스트림
				mIn = serialPort.getInputStream();
				mOut = serialPort.getOutputStream();
				System.out.println("Connected to " + portName + "");
			} else {
				System.out.println("Error: Only serial ports are handled by this.");
			} // else
		} // else
	} // func // constructor

	public InputStream getInputStream() {
		return mIn;
	}
	
	public OutputStream getOutputStream() {
		return mOut;
	}

	/*
	public void runTest() {
		if (mbRunning) return;
		mbRunning = true;
		new Thread(new SerialReader(mIn)).start();
		new Thread(new SerialWriter(mOut)).start();
	}

	private class SerialReader implements Runnable {
		InputStream in;

		public SerialReader(InputStream in) {
			this.in = in;
		}

		public void run() {
			byte[] buffer = new byte[1024];
			int len = -1;
			try {
				for (; (len = this.in.read(buffer)) > -1;) {
					System.out.print(new String(buffer, 0, len));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // func
	} // class

	private class SerialWriter implements Runnable {
		OutputStream out;

		public SerialWriter(OutputStream out) {
			this.out = out;
		}

		public void run() {
			try {
				int c = 0;
				for (; (c = System.in.read()) > -1;) {
					this.out.write(c);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // func
	} // class
	
	public static void main(String[] args) {
		try {
			new Serial("COM4").runTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
} // public class


