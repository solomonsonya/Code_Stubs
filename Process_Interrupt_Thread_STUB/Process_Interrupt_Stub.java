/**
 * Timer Interrupt Starter class stub - to help start a new class that has interrupt timers 
 * 
 * @author Solomon Sonya
 */

package main;

import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Timer;

public class Start extends Thread implements Runnable, ActionListener
{
	public Timer tmr = null;
	public int interrupt_milliseconds = 1000;
	public volatile static boolean continue_run = true;	
	
	public volatile boolean process_interrupt = true;
	
	public Start(int interrupt_seconds)
	{
		if(interrupt_seconds > 0)
			interrupt_milliseconds = interrupt_seconds*1000;			
		
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
			System.out.println("Ready to execute work => " + (new SimpleDateFormat("yyyy" + "-" + "MM" + "-" + "dd" + "-" + "HH" + "-" + "mm" + "-" + "ss").format(new Date())));
			
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

}
