package com.sat.practice.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

public class CameraPanel extends JPanel implements Runnable,ActionListener{

	BufferedImage image;
	VideoCapture capture;
	JButton screenshot;
	CascadeClassifier faceDetector;
	MatOfRect faceDetections;
	

	public CameraPanel() {
		faceDetector=new CascadeClassifier(CameraPanel.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
		faceDetections=new MatOfRect();
		screenshot=new JButton("Screenshot");
		screenshot.addActionListener(this);
		add(screenshot);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		saveScreenshot();
	}
	
	public void saveScreenshot(){
		File output=new File("C:\\Users\\vanaik\\Desktop\\screenshots\\screenshot1.png");
		int i=0;
		while(output.exists()){
			i++;
			output=new File("C:\\Users\\vanaik\\Desktop\\screenshots\\screenshot"+i+".png");
		}
		try{
			ImageIO.write(image, "png", output);
		}catch(IOException e1){
			e1.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		capture=new VideoCapture(0);
		Mat webImage=new Mat();
		if(capture.isOpened()){
			while (true) {
				capture.read(webImage);
				if(!webImage.empty()){
					JFrame topFrame=(JFrame)SwingUtilities.getWindowAncestor(this);
					topFrame.setSize(webImage.width()+40,webImage.height()+110);
					matToBufferedImage(webImage);
					faceDetector.detectMultiScale(webImage, faceDetections);
					repaint();
				}
			}
		}
	}
	
	private void matToBufferedImage(Mat webImage) {
		int width=webImage.width();
		int height=webImage.height();
		int channels=webImage.channels();
		byte[] source=new byte[width * height * channels];
		webImage.get(0, 0,source);
		image=new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		final byte[] target=((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(source, 0, target, 0, source.length);
		
		
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(image==null) return;
		g.drawImage(image, 10, 40, image.getWidth(), image.getHeight(), null);
		g.setColor(Color.GREEN);
		for (Rect rect: faceDetections.toArray()) {
			g.drawRect(rect.x+10,rect.y+40,rect.width,rect.height);
			saveScreenshot();
		}
	}

}
