
/*
•	Simulation program should read reference words from text file
•	For each reference word, decide whether it is a compulsory miss (CM), hit (H), dirty miss (DM), or M (a clean miss). 
•	For each of the above emulate the relevant cache management operations, except for getting the actual bytes from the memory. 
•	Record the number of CMs, DMs, Hs, and Ms. 
•	Simulate the cache under the parameters: ??=24, ??=16, ??=4, and ??=4 
•	Compute and output the hit ratio, the CM ratio, and the DM ratio. 
•	Repeat the simulation under the parameters: ??=24, ??=16, ??=8, and ??=4 
•	Compute and output the hit ratio, the CM ratio, and the DM ratio. 

				Author: Trupti Mhaisdhune
*/ 
package edu.txstate.simulation;

import java.util.Scanner;

public class SetAssociative {

	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		SetAssociativeSimulation simulation = new SetAssociativeSimulation();
		int caseNo;

		System.out.println("Enter 1 for case1 and Ener 2 for case2");
		caseNo = reader.nextInt();
		if(caseNo ==1)
		     	simulation.startSimulation(24,4,16,4);
		else
            	simulation.startSimulation(24,8,16,4);
     	}
}
