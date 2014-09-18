package pack;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class main {
	static ArrayList<PrintWriter> out = new ArrayList<PrintWriter>();
	static String status = "";
	static String[] tokens = new String[200];
	static String date = "3";
	boolean loop = true;
	public static void main(String[] args){
		new main().run();//start
	}
	static synchronized void masterOutput(String a){//broadcast's message
		/*for(int i = 0; i < tokens.length; i++ ){
			if(tokens[i] != null) status = status + tokens[i]; 
		}*/
		//return status;
		for(PrintWriter writ: out){
			writ.println(a);
			writ.flush();
		}
	}
	public void run(){// accepts new clients  
		System.out.println("Server booting version "+ date);
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
		Thread newClient = new Thread(new Client(inputBuffer,i));
		out.add(outputBuffer);
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
		try{
		String input = in.readLine();
		System.out.println(input);
		if (input.equals(main.date)){System.out.println("Authentification granted");}
		else{loop = false; System.out.println("Error authenticating");}
		}catch (Exception death){loop = false; System.out.println("Error authenticating");}
		while(loop){
			try{
				String input = in.readLine();
				if(input.contains("ERROR")){in.close(); out.close(); //deleat or fix this
				System.out.println("player" + tag + "disconected");
				}
				//out.println(main.masterOutput(input, tag));
				//out.flush();
				main.masterOutput(input);
				Thread.sleep(50);
				}catch(Exception e){System.out.println("we have a comunication error");
				try{in.close();
				out.close();}catch(Exception a){}
				main.tokens[tag] = null;
				System.out.println("thread closed"); loop = false;}
		}
		
	}
	
	public Client(BufferedReader a, int c){
		tag = c;
		in = a;
		//out = b;
		//out.print('3');
		//out.flush();
	}
	
}
