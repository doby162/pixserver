package pack;

import java.io.*;
import java.net.*;

public class main {
	static String status = "";
	static String[] tokens = new String[200];
	boolean loop = true;
	public static void main(String[] args){
		new main().run();//start
	}
	static synchronized String cat(String a, int b){//broadcast's message
		tokens[b] = a;
		status = "";
		for(int i = 0; i < tokens.length; i++ ){
			if(tokens[i] != null) status = status + tokens[i]; 
		}
		return status;
	}
	public void run(){// accepts new clients  
		int i = 0;
		ServerSocket mySocket = null;
		try{mySocket = new ServerSocket(1234);}catch(Exception e){e.printStackTrace();}
		
		while(loop){
			try{Thread.sleep(1000);}catch(Exception e){}
			BufferedReader inputBuffer = null;
			PrintWriter outputBuffer = null;
			try{
				Socket clientSocket = mySocket.accept();
				inputBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				outputBuffer = new PrintWriter(clientSocket.getOutputStream());
			}catch(Exception e){System.out.println("Error connecting"); e.printStackTrace();}
		Thread newClient = new Thread(new Client(inputBuffer,outputBuffer, i));
		i++;
		System.out.println("Player number " + new Integer(i).toString() + " has been added.");
		newClient.start();
		}
	}
	

}

class Client implements Runnable{
	
	BufferedReader in = null;
	PrintWriter out = null;
	String ID = null;
	String info = null;
	int tag;
	@Override
	public void run() {
		boolean loop = true;
		while(loop){
			try{
				String input = in.readLine();
				if(input.contains("ERROR")){in.close(); out.close(); 
				System.out.println("player" + tag + "disconected");
				}
				out.println(main.cat(input, tag));
				out.flush();
				Thread.sleep(50);
				}catch(Exception e){System.out.println("we have a comunication error");
				try{in.close();
				out.close();}catch(Exception a){}
				main.tokens[tag] = null;
				System.out.println("thread closed"); loop = false;}
		}
		
	}
	
	public Client(BufferedReader a, PrintWriter b, int c){
		tag = c;
		in = a;
		out = b;
		out.print('2');
		out.flush();
	}
	
}
