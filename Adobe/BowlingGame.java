import java.util.ArrayList;
import java.util.HashMap;


public class BowlingGame {
	
int score;
int frameNumber;
int rollNumber;
int frameScore;
int extraRolls = 0;

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
	game.roll(10);
	
	game.roll(10);
	
	game.roll(10);
	
	game.roll(10);
	
	game.roll(6);
	game.roll(2);
	
	game.roll(0);
	game.roll(10);
	
	game.roll(10);
	
	game.roll(10);
	
	game.roll(10);
	
	game.roll(1);
	
	game.roll(9);
	
	game.roll(10);

	game.getScore();
	game.printBalls();
	
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
					addValueToFrameList("Sparse",frameScore+","+knocked,true,0, true);
				}
				
				else {
					addValueToFrameList("Open",frameScore+","+knocked,true,0, true);
				}
			
			}
		}
	}
}


public boolean isFinished() {
	return true;
}


public int getScore() {
	System.out.println(FrameList.size()+ " Frames Recorded");
	return 0;
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


public void printBalls() {
	System.out.println(FrameList.toString());
}

}
