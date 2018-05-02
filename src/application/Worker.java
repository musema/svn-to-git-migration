package application;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.scene.control.TextArea;

public class Worker implements Runnable {
	private InputStream inputStream;
	private TextArea outputTextArea;
	
	public Worker(InputStream inputStream, TextArea processOutputTextArea) {
		this.inputStream = inputStream;
		outputTextArea=processOutputTextArea;
	}

	@Override
	public void run() {
		BufferedReader br=   new BufferedReader(new InputStreamReader(inputStream));
		br.lines().forEach(line->outputTextArea.appendText(line+"\n"));
	}
}