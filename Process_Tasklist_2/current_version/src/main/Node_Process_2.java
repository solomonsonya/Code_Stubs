/**
 * Node and Interrupt Class.  The purpose of this class is to interrupt and pull a listing if processes currently running.
 * 
 * Each process is analyzed, and then tree_process is built over time to contain all processes that have been analyzed
 * by the system. 
 * 
 * @author Solomon Sonya
 */

package main;

import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Timer;

public class Node_Process_2 extends Thread implements Runnable, ActionListener
{
	public static Timer tmr = null;
	public static int interrupt_milliseconds = 1000;
	public volatile static boolean continue_run = true;
	
	
	public volatile boolean process_interrupt = true;
	
	public Node_Process_2(double interrupt_seconds)
	{
		if(interrupt_seconds > 0)
			interrupt_milliseconds = (int)(interrupt_seconds*1000);
			
		
		this.start();
	}
	
	public void run()
	{
		continue_run = true;
		process_interrupt = true;
		
		//init timer
		tmr = new Timer(interrupt_milliseconds, this);
		
		//start timer
		tmr.start();
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == tmr && process_interrupt)
			process_interrupt();
	}
	
	public boolean process_interrupt()
	{
		try
		{
			//determine if time to halt
			if(!continue_run)
			{
				//halt future interrupts
				this.tmr.stop();
				
				//leave lock on semaphore
				return true;
			}
			
			//determine if still working from previous interrupt
			if(!process_interrupt)
				return false;
			
			//not busy, lock semaphore until return to ready state
			process_interrupt = false;
			
			//
			//Execute work here!
			//
			ProcessHandle.allProcesses().forEach(process -> processDetails(process));
			
			//release semaphore
			process_interrupt = true;
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Exception caught");
		}
		
		//exception occurred, however, release semaphore, hopefully error might not persist upon next interrupt
		process_interrupt = true;
		return false;
	}
	
	private static String processDetails(ProcessHandle process) 
	{
				sop("TO STRING: " + process.toString() ); 
				sop("\tPID: " + process.pid()  ); 
				//sop("INFO: " + process.info()  ); //EVERYTHING WE NEED FROM THIS INSTANCE IS PROVIDED BELOW 
				sop("\tIS ALIVE: " + process.isAlive()  ); 
				sop("\tSUPPORTS NORMAL TERMINATION: " + process.supportsNormalTermination()  ); 
				sop("\tARGUMENTS: " + process.info().arguments()  );
				sop("\tCOMMAND: " + process.info().command()  ); 
				sop("\tCOMMAND LINE: " + process.info().commandLine().orElse(" - ")  );
				sop("\tSTART INSTANT: " + process.info().startInstant()  );				
				sop("\tCPU DURATION: " + process.info().totalCpuDuration()  );
				sop("\tUSER: " + process.info().user()  );  
				sop("\tPARENT PID: " + process.parent().toString());		
				sop("");
				
				return "";
		
	}
	
	public static void sop_dummy(String nop) {}
	
	public static boolean sop(String out)
	{
		try
		{			
			System.out.println(out);
		}
		catch(Exception e)
		{
			System.out.println("OPPS!");
		}
		
		return true;
	}

}
