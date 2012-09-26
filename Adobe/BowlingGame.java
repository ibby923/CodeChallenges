import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class BowlingGame {
	
private int score;
private int frameNumber;
private int rollNumber;
private int frameScore;
private int extraRolls = 0;

//There has to be a better way to store this!
ArrayList<HashMap<String,String>> FrameList;
//Map to store type of progress in game
HashMap<String,String> myMap;

//Constructor
public BowlingGame() {
	this.score = 0;
	this.frameNumber = 0;
	this.rollNumber = 0;
	this.frameScore = 0;
	this.extraRolls = 0;
	this.FrameList = new ArrayList<HashMap<String,String>>();
}

public static void main(String [] args) {
	BowlingGame game = new BowlingGame();
	game.roll(9);
	game.isFinished();
	game.printBalls();
	System.out.println(game.getScore());
}

public void roll(int knocked) {
	
	//Return if the roll is a invalid value
	if(knocked < 0 || knocked > 10 ) {
		System.out.println("This is not a valid ten pin roll");
		return;
	}
	
	//Frame Numbers are indexed 0-9 , the 9th index decides how many more will be added
	if(frameNumber > 10 || extraRolls == 2) {
		System.out.println("You can't roll more -_-");
		return;
	}
	
	if(frameNumber ==10 ) {
		String type = FrameList.get(9).entrySet().iterator().next().getKey();
		//If the last frame was Open , in the sense pins not cleared there will be no bonus shot!
		if(type.equals("Open")) {
			System.out.println("You can't roll more -_-");
			frameNumber++;
			return;
		}
		//If the last frame was a Strike then two bonus shots will be alloted
		else if(type.equals("Strike")){
			addValueToFrameList("Extras",""+knocked,false,0,false);
			extraRolls++;
		}
		//If the last frame was a sparse, I am not sure about this rule, add only one shot and exit
		else {
			addValueToFrameList("Extras",""+knocked,false,0,false);
			extraRolls += 2;
		}
	}
	
	else {
		//Increment roll numbers, to check number of rolls per frame
		rollNumber++;
		//Two possibilities, if a strike is rolled or not AND if it is 1st or 2nd roll
		if( knocked == 10) {
			if( rollNumber % 2 == 1) {
				addValueToFrameList("Strike",""+knocked,false,0,true);
				rollNumber--;
			}
			
			else {
				
				if(knocked+frameScore > 10) {
					System.out.println("Invalid roll!");
					rollNumber--;
					return;
				}
			
				else if(knocked+frameScore==10){
					addValueToFrameList("Spare",frameScore+","+knocked,true,0,true);
				}
				
				else {
					addValueToFrameList("Open",frameScore+","+knocked,false,0,true);
				
				}
			}
		}
		else {
			
			if(rollNumber % 2 == 1) {
				frameScore += knocked;
				addValueToFrameList("In Progress",""+knocked,false,frameScore,false);
			}
			else {
				if(knocked+frameScore > 10) {
					System.out.println("Invalid roll!");
					rollNumber--;
				}
				else if(knocked+frameScore == 10) {
					addValueToFrameList("Spare",frameScore+","+knocked,true,0, true);
				}
				
				else {
					addValueToFrameList("Open",frameScore+","+knocked,true,0, true);
				}
			
			}
		}
	}
}


public boolean isFinished() {
	if(FrameList.size() > 9) {
		String type = (FrameList.get(9).entrySet().iterator().next().getKey());
		if(type.equals("In Progress")) return false;
		else if(type.equals("Spare")) {
			if(FrameList.size() == 11) return true;
			else return false;
		}
		else {
			if(FrameList.size() == 12) return true;
			else return false;
		}
	}
	else return false;
	
}


public int getScore() {
	score = 0;
	int FrameSize = FrameList.size();
	//System.out.println(FrameSize);
	for(int i = 0; i < FrameSize; i++) {
		
		
		String Type = (FrameList.get(i).entrySet().iterator().next().getKey());
		//System.out.println(type);
	
		
		if(Type.equals("Strike")) {
			score += 10;
			if(i+1 < FrameSize) {
				String secondType = (FrameList.get(i+1).entrySet().iterator().next().getKey());
				//System.out.println(secondType);
				if(secondType.equals("In Progress")) {
					String numby = (FrameList.get(i+1).entrySet().iterator().next().getValue());
					score += Integer.parseInt(numby);
				}
				else if(secondType.equals("Spare") || secondType.equals("Open")) {
					String numby = (FrameList.get(i+1).entrySet().iterator().next().getValue());
					score += (Integer.parseInt(numby.split(",")[0])+Integer.parseInt(numby.split(",")[1]));
					continue;
				}
				else if(secondType.equals("Strike")){
					score += 10;
					if(i+2 < FrameSize) {
						String thirdType = (FrameList.get(i+2).entrySet().iterator().next().getValue());
						if(thirdType.equals("Strike")) score += 10;
						else score += (Integer.parseInt(thirdType.split(",")[0]));
					}
				}
					else {
						score += (Integer.parseInt(FrameList.get(10).entrySet().iterator().next().getValue()) + Integer.parseInt(FrameList.get(11).entrySet().iterator().next().getValue()));
					}
				
			}
		}
		
		
		else if(Type.equals("In Progress")) {
			String numby = (FrameList.get(i).entrySet().iterator().next().getValue());
			score+= Integer.parseInt(numby.split(",")[0]);
		}
		
		
		else if(Type.equals("Spare")) {
			String numby = (FrameList.get(i).entrySet().iterator().next().getValue());
			score += (Integer.parseInt(numby.split(",")[0])+Integer.parseInt(numby.split(",")[1]));
			if(i+1 < FrameSize) {
				String othy = (FrameList.get(i+1).entrySet().iterator().next().getKey());
				if(othy.equals("Strike")) score += 10;
				else {
					String lastnumby = (FrameList.get(i+1).entrySet().iterator().next().getValue());
					score += Integer.parseInt(lastnumby.split(",")[0]);
				}
			}
		}
		
		else if(Type.equals("Open")){
			String numby = FrameList.get(i).entrySet().iterator().next().getValue();
			score += (Integer.parseInt(numby.split(",")[0])+Integer.parseInt(numby.split(",")[1]));
		}
		
		else {
			break;
		}
		
	}
	return score;
}


public void printBalls() {
	System.out.println(FrameList.toString());
}


public void addValueToFrameList(String type,String score,boolean removeLast,int frameScore,boolean incrementframeNumber) {
	myMap = new HashMap<String,String>();
	myMap.put(type,score);
	if(removeLast == true) {
		FrameList.remove(FrameList.size()-1);
	}
	if(incrementframeNumber == true) {
		frameNumber++;
	}
	this.frameScore = frameScore; 
	FrameList.add(myMap);
}
}



/*
TEST MODULE
 
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0:0
4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4:80 
9 1 9 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0:28
10 9 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0:28
9 1 10 9 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0:48
10 9 1 9 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0:48
10 10 9 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0:57
10 10 10 0 0 0 0 0 0 0 0 0 0 0 0 0 0:60
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 1 9:19 
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 10 8 1:19
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 10 9 1:20 
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 1 10:20
10 10 10 10 10 10 10 10 10 10 10 10 :300
	 
	BowlingGame game = new BowlingGame();
	String input =  "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0:0  4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4:80  9 1 9 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0:28  10 9 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0:28  9 1 10 9 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0:48  10 9 1 9 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0:48  10 10 9 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0:57  10 10 10 0 0 0 0 0 0 0 0 0 0 0 0 0 0:60  0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 1 9:19  0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 10 8 1:19  0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 10 9 1:20  0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 1 10:20  10 10 10 10 10 10 10 10 10 10 10 10 :300";
	String t[] =  input.split("  ");
	int i = 0;
while(i<t.length) {	
	String Stringy[] = t[i].split(":");
	String onemore[] = Stringy[0].split(" ");
	//System.out.println("Input ==> "+Stringy[0]);
	game = new BowlingGame();
	for ( int k =0 ; k < onemore.length; k ++) {
		
		//System.out.println(Integer.parseInt(onemore[k]));
		game.roll(Integer.parseInt(onemore[k]));
	}
	//game.printBalls();
		System.out.println(game.getScore());
		i++;
		}
		
	
	
	//System.out.println(game.isFinished());
	
	//System.out.println(game.getScore());
 */
