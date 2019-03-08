package main;

import java.io.*;
import java.util.Optional;
import java.util.function.Consumer;

public class main {

	public static void main(String[] args) throws IOException 
	{
		// TODO Auto-generated method stub
		
		Node_Process_2 start = new Node_Process_2(4);
		
		System.out.println("Program Started! Solomon Sonya @Carpenter1010 version 1.000");
		
				
		BufferedReader brIn = new BufferedReader(new InputStreamReader(System.in));
		
		String line = "";
		
		while((line = brIn.readLine()) != null)
		{
			if(line.trim().equalsIgnoreCase("exit"))
			{
				System.out.println("Program Terminated");
				System.exit(0);
			}

		}
	}
	
	
	

}
