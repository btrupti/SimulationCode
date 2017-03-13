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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import edu.txstate.model.ReferenceWord;

public class SetAssociativeSimulation {

	List<String> RWArray = new ArrayList<>();
	int indexBit;
	int tagBit;
	int offsetBit;
	int RWBit;
	String tag;
	String index;
	String offset;
	int totalCompusoryMiss = 0;
	int totalHit = 0;
	int totalMiss = 0;
	int totalDirtyBits =0;
	int notDirtyBits = 0;
	boolean readWriteEntry;  // If true : Write Entry
							// If false : Read Entry
	ArrayList<ReferenceWord> set = new ArrayList<ReferenceWord>(); // Set for storing blocks
	ArrayList<ArrayList<ReferenceWord>> cache = new ArrayList<ArrayList<ReferenceWord>>(); // Cache for storing sets
	
	// Start Simulation
	public void startSimulation(int n, int L, int M, int J) {
		
		CreateInputArrayList();
		initializeChache(L, J);
		TagindexOffsetCalulator(n, L, M);
		System.out.println(
				RWBit + " = Tag bits : " + tagBit + " index bits : " + indexBit + " offset bits : " + offsetBit);
		for (int i = 0; i < RWArray.size(); i++) {
			System.out.print("_______________________________________________________________ ");
			displayCache(L, J);
			checkReadWriteEntry(i);
			RWSeperator(RWArray.get(i));
			updateCache(tag, index, offset, J, readWriteEntry);
		}
		printResult(n, L, M,J);
	}

	//Check whether read or write entry
	public void checkReadWriteEntry(int i){
		if((i+1) % 2 == 0){
			System.out.print( i +") RW : '" + RWArray.get(i) + "' is a write entry....\nBinary converssion : ");
			readWriteEntry = true;
			}
			else{
				System.out.print("\n" + i+") RW : '" + RWArray.get(i) + "'is a read erntry....\nBinary converssion : ");
				readWriteEntry = false;
			}
	}
	// Reading trace.txt file and creating array List for Reference word
	public void CreateInputArrayList() {
		try {
			for (String line : Files.readAllLines(Paths.get("trace.txt"))) {
				String RW = String.valueOf(line);
				RWArray.add(RW);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Initialize cache with v = 0 (false) for all blocks
	public void initializeChache(int L, int J) {
		for (int k = 0; k < L; k++) {
			set = new ArrayList<ReferenceWord>();
			for (int i = 0; i < J; i++) {
				ReferenceWord RW = new ReferenceWord();
				RW.setBlockNumber(i);
				RW.setValid(0);
				RW.setDirtyBit(0);
				RW.setReplacementBit(i);
				RW.setTag("XX");
				RW.setOffset("XX");
				RW.setIndex("XX");
				set.add(RW);
			}
			cache.add(set);
		}		
	}

	// Calculate Tag, indexBit, Offset
	public void TagindexOffsetCalulator(int n, int L, int M) {
		RWBit = n;
		indexBit = (int) (Math.log(L) / Math.log(2));
		offsetBit = (int) (Math.log(M) / Math.log(2));
		tagBit = n - (indexBit + offsetBit);
	}

	// Divide reference word into tag ,index and offset
	public void RWSeperator(String hex) {
		String binary = hexadecimalToBinary(hex);
		System.out.println(String.format("%24s", binary).replace(" ", "0"));
		offset = binary.substring(binary.length() - offsetBit, binary.length());
		index = binary.substring((binary.length() - (offsetBit + indexBit)), binary.length() - offsetBit);
		tag = binary.substring(0, binary.length() - (offsetBit + indexBit));
	}

	// Convert Hexadecimal string to binary string
	public String hexadecimalToBinary(String hex) {
		int i = Integer.parseInt(hex, 16);
		String binary = Integer.toBinaryString(i);
		return binary;
	}

	// Update cache
	public void updateCache(String tag, String index, String offset, int J, boolean readWriteEntry) {
		int setNo = Integer.parseInt(index, 2);
		System.out.println("Looking for SET " + setNo + " .....With index = " + index + " and tag = " + tag);
		if (checkHit(setNo, J,readWriteEntry) == true) {
			totalHit++;
		} else {
			if (checkCM(setNo, J,readWriteEntry) == true) {
				totalCompusoryMiss++;
			} else {
				System.out.println("IT is miss ");
				totalMiss++;
				replaceBlock(tag, index, offset, J, setNo, readWriteEntry);
			}
		}
	}

	// Check For Hit (H)
	public boolean checkHit(int setNo, int J, boolean readWriteEntry) {
		int flag = 0;
		for (int i = 0; i < J; i++) {
			if (cache.get(setNo).get(i).getTag().equals(tag)&& cache.get(setNo).get(i).getValid()== 1) {
				System.out.println("Hit at Block : " + i);
				setReplacementBit(setNo,i,J);
		//		logger.info("Hit"+ RWArray.get(i));
				flag = 1;
				if(readWriteEntry == true)
					cache.get(setNo).get(i).setDirtyBit(1);
				break;
			}
		}
		if (flag == 1) {
			return true;

		} else
			return false;
	}

	// Check For compulsory miss (CM)
	public boolean checkCM(int setNo, int J, boolean readWriteEntry) {
		int flag = 0;
		int Block = 0;
		for (int i = 0; i < J; i++) {
			if (cache.get(setNo).get(i).getTag().equals(tag) && cache.get(setNo).get(i).getValid() == 0) {
				System.out.println("Compulsory miss ");
				flag = 1;
				Block = i;
				break;
			}
		}

		if (flag == 1) {
			addNewBlock(setNo, Block, J,readWriteEntry);
			return true;
		} else
			return false;
	}

	// Check for Miss

	// Add new block to the set
	public void addNewBlock(int setNo, int Block, int J, boolean readWriteEntry) {
		checkDirtyBit(setNo, Block);
		cache.get(setNo).get(Block).setIndex(index);
		cache.get(setNo).get(Block).setOffset(offset);
		cache.get(setNo).get(Block).setTag(tag);
		cache.get(setNo).get(Block).setValid(1);
		setReplacementBit(setNo, Block, J);
		if(readWriteEntry == true)
			cache.get(setNo).get(Block).setDirtyBit(1);
		else
			cache.get(setNo).get(Block).setDirtyBit(0);
	}

	//replace Block
	public void replaceBlock(String tag, String index, String offset, int J,int setNo, boolean readWriteEntry){
		int replacementBlock = findLRU(J, setNo);
		checkDirtyBit(setNo, replacementBlock);
		cache.get(setNo).get(replacementBlock).setIndex(index);
		cache.get(setNo).get(replacementBlock).setOffset(offset);
		cache.get(setNo).get(replacementBlock).setTag(tag);
		cache.get(setNo).get(replacementBlock).setValid(1);
		setReplacementBit(setNo, replacementBlock, J);
		if(readWriteEntry == true)
			cache.get(setNo).get(replacementBlock).setDirtyBit(1);
		else
			cache.get(setNo).get(replacementBlock).setDirtyBit(0);
	}
	//Check dirty bit 
	public void checkDirtyBit(int setNo, int blockNo){
		if(cache.get(setNo).get(blockNo).getDirtyBit()==1){
			totalDirtyBits++;
			System.out.println("Dirty bit is replaced: " + blockNo );
		}
		else{
			notDirtyBits++;
			System.out.println("Non Dirty bit is replaced: " + blockNo);
		}
	}
	//Find LRU
	public int findLRU(int J, int setNo){
		int replacementBlock = 0;
		for(int i = 0 ; i < J ; i++){
			if ( cache.get(setNo).get(i).getReplacementBit() == 0){
				replacementBlock =i;
				break;
			}
		}
		return replacementBlock;
	}
	// Set replacement bit
	public void setReplacementBit(int setNo, int Block, int J) {
			int replacedBit = cache.get(setNo).get(Block).getReplacementBit();
			cache.get(setNo).get(Block).setReplacementBit(3);
			for (int i = 0; i < J; i++) {
				if (i != Block) {
					if (cache.get(setNo).get(i).getReplacementBit() >= replacedBit
							&& cache.get(setNo).get(i).getReplacementBit() != 0) {
						int bit = cache.get(setNo).get(i).getReplacementBit();
						bit = bit - 1;
						cache.get(setNo).get(i).setReplacementBit(bit);
					}
				}
			}
	}

	// Print Result
	public void printResult(int n, int L, int M ,int J) {
		System.out.println("\n=============================================");
		System.out.println("RW(n): " + n + " bits,   Sets(L): " + L + " Sets,   Blocks per sets(J): "+ J + " blocks,   Size(M): " + M + " bytes.");
		System.out.println("Total Hits : " + totalHit);
		System.out.println("Total Compulsory miss : " + totalCompusoryMiss);
		System.out.println("Total Miss : " + totalMiss);
		System.out.println("Miss with dirty bit (Dirty Miss) : " + totalDirtyBits);
		System.out.println("Miss without Dirty Miss : " + notDirtyBits);
		 double hitRatio = calculateHitRatio();
		 double missRatio = calculateMissRatio();
		System.out.println("---------------------------------------------");
		 double CMRatio = 1 - (hitRatio + missRatio);
		 System.out.println("Hit ratio : " + hitRatio);
		 System.out.println("Miss ratio : " + missRatio);
		 System.out.println("Compulsory  Miss ratio : " + CMRatio);
		 System.out.println("Dirty miss ratio : " + calculateDirtyMissRatio());
		System.out.println("=============================================");

	}
	// Calculate hit ratio
		public double calculateHitRatio() {
			double hitRatio = (double) (totalHit / (double) (totalHit + totalCompusoryMiss + totalMiss));
			return hitRatio;
		}

		// Calculate Miss ratio
		public double calculateMissRatio() {
			double missRatio = (double) (totalMiss / (double) (totalHit + totalCompusoryMiss + totalMiss));
			return missRatio;
		}

		// Calculate CM ratio
		public double calculateCMRatio() {
			double hitRatio = (double) (totalCompusoryMiss / (double) (totalHit + totalCompusoryMiss + totalMiss));
			return hitRatio;
		}
		
		// Calculate Dirty Miss ratio
				public double calculateDirtyMissRatio() {
					double missRatio = (double) (totalDirtyBits/ (double) (totalHit + totalCompusoryMiss + totalMiss));
					return missRatio;
				}

	// Display Cache
	public void displayCache(int L, int J) {
		System.out.format("\n%5s%20s%10s%10s%5s%5s%5s", "BLOCK", "TAG", "INDEX", "OFFSET", "V", "R", "D");
		for (int k = 0; k < L; k++) {
			for (int i = 0; i < J; i++) {
				System.out.format("\n%5d%20s%10s%10s%5d%5d%5d", cache.get(k).get(i).getBlockNumber(),
						cache.get(k).get(i).getTag(), cache.get(k).get(i).getIndex(), cache.get(k).get(i).getOffset(),
						cache.get(k).get(i).getValid(), cache.get(k).get(i).getReplacementBit(),
						cache.get(k).get(i).getDirtyBit());
			}
			System.out.println("");
		}
	}
}
