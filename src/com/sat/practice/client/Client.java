package com.sat.practice.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.opencv.core.Core;
import org.opencv.highgui.VideoCapture;

public class Client extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	VideoCapture videoCapture;
	JMenu menuCamera;
	JMenuBar menuBar;
	CameraPanel cameraPanel;
	public Client(){
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		videoCapture=new VideoCapture(0);
		cameraPanel= new CameraPanel();
		Thread thread=new Thread(cameraPanel);
		menuCamera=new JMenu("Camera");
		menuBar=new JMenuBar();
		menuBar.add(menuCamera);
		int cameraNo=1;
		while (videoCapture.isOpened()) {
			JMenuItem cameraItem=new JMenuItem("Camera "+cameraNo);
			cameraItem.addActionListener(this);
			menuCamera.add(cameraItem);
			videoCapture.release();
			videoCapture=new VideoCapture(cameraNo);
			cameraNo++;
		}
		thread.start();
		add(cameraPanel);
		setJMenuBar(menuBar);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640,640);
		setVisible(true);
	}

	public static void main(String[] args) {
		int[] a=new int[5];
		System.out.println(a);
		/*Client client=new Client();*/
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
