

 Background for application: 
In this assignment I had to simulate a set associative cache management policy. With following assumptions: 
1) The cache is a D-Cache. 
2) A trace of reference words is available in the file trace.txt. It is assumed that all the addresses of the trace are write addresses. 
3) A dirty miss (DM), is a miss on cache write where the cache line is dirty. 
4) The main memory has ??=2?? bytes 
5) Block size is ??=2?? bytes 
6) The cache contains ??=2?? sets. 
7) Each set contains ??=2?? blocks 
8) The cache has a valid bit for each line and initially all the lines are invalidated. 
9) The cache contains a dirty bit 
10) The replacement policy is LRU 

 Steps/ Instructions: 
•	Simulation program should read reference words from text file
•	For each reference word, decide whether it is a compulsory miss (CM), hit (H), dirty miss (DM), or M (a clean miss). 
•	For each of the above emulate the relevant cache management operations, except for getting the actual bytes from the memory. 
•	Record the number of CMs, DMs, Hs, and Ms. 
•	Simulate the cache under the parameters: ??=24, ??=16, ??=4, and ??=4 
•	Compute and output the hit ratio, the CM ratio, and the DM ratio. 
•	Repeat the simulation under the parameters: ??=24, ??=16, ??=8, and ??=4 
•	Compute and output the hit ratio, the CM ratio, and the DM ratio. 
