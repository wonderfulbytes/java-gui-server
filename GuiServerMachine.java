import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GuiServerMachine extends JFrame {

	JLabel label1, label2, label3;
	JTextField textField1, textField2;
	JButton button1, button2;
	JPanel panel2;
	private int port;
	private String messageToSend;
	private Font globalFont;
	ServerSocket serverSocket;
	Socket conSocket;
	PrintWriter printWriter;

	public static void main(String[] args) {
		new GuiServerMachine("Java Server Machine");
	}

	GuiServerMachine(String title) {
		this.setTitle(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		globalFont = new Font("Arial", Font.PLAIN, 14);
		this.setFont(globalFont);
		this.setSize(380, 200);

		label1 = new JLabel("Port #");
		label1.setFont(globalFont);
		textField1 = new JTextField(5);
		textField1.setFont(globalFont);
		button1 = new JButton("Start Server");
		label2 = new JLabel("Listening for connections...");
		label2.setVisible(false);
		button1.addActionListener(new Button1Listener());
		JPanel panel1 = new JPanel();

		label3 = new JLabel("Messsage");
		label3.setFont(globalFont);
		textField2 = new JTextField(15);
		textField2.setFont(globalFont);
		button2 = new JButton("Send");
		button2.addActionListener(new Button1Listener());
		panel2 = new JPanel();
		panel2.add(label3);
		panel2.add(textField2);
		panel2.add(button2);
		panel2.setVisible(false);
		panel1.add(label1);
		panel1.add(textField1);
		panel1.add(button1);
		panel1.add(label2);
		this.add(BorderLayout.CENTER, panel1);
		this.add(BorderLayout.SOUTH, panel2);

		this.setVisible(true);
	}

	public class Button1Listener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if (e.getSource().equals(button1)) {

				switch (button1.getText()) {

					case "Start Server":		port = Integer.valueOf(textField1.getText());
												button1.setEnabled(false);
												textField1.setEditable(false);
												label2.setVisible(true);

												Runnable netWorkingJob = new NetWorking();
												Thread netThread = new Thread(netWorkingJob);
												netThread.start();
												break;
					
					case "Break Connection": 	try {
													serverSocket.close();
													conSocket.close();
												} catch (IOException ex) {
													ex.printStackTrace();
												}
												System.exit(0);
												break;
				}
			} else if (e.getSource().equals(button2)) {
				System.out.println("Sending message...");
				try {
					printWriter = new PrintWriter(conSocket.getOutputStream());
					messageToSend = textField2.getText();
					printWriter.println(messageToSend);
					printWriter.flush();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public class NetWorking implements Runnable {

		public void run() {

			try {
				serverSocket = new ServerSocket(port);
				System.out.println(port);
				conSocket = serverSocket.accept();
				System.out.println("CONNECTION ESTABLISHED");
				label2.setText("Connected to " + conSocket.getInetAddress());
				button1.setText("Break Connection");
				button1.setEnabled(true);
				panel2.setVisible(true);
				textField2.requestFocus();
				System.out.println("focus requested");
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			try {
				serverSocket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}