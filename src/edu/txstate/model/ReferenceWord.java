package edu.txstate.model;

public class ReferenceWord {
	String tag;
	String index;
	String offset;
	int valid ;
	int blockNumber;
	int replacementBit;
	int dirtyBit;
	
	public void setTag(String Strtag){
		tag = Strtag;
	}
	public String getTag(){
		return tag;
	}
	
	public void setIndex(String Strindex){
		index = Strindex;
	}
	public String getIndex(){
		return index;
	}
	
	public void setOffset(String Stroffset){
		offset = Stroffset;
	}
	public String getOffset(){
		return offset;
	}
	
	public void setValid(int bitV){
		valid = bitV;
	}
	public int getValid(){
		return valid;
	}
	public void setBlockNumber(int block){
		blockNumber = block;
	}
	public int getBlockNumber(){
		return blockNumber;
	}
	public void setReplacementBit(int bit){
		replacementBit = bit;
	}
	public int getReplacementBit(){
		return replacementBit;
	}
	public void setDirtyBit(int bit){
		dirtyBit = bit;
	}
	public int getDirtyBit(){
		return dirtyBit;
	}

}
