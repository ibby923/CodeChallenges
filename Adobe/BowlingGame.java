import java.util.ArrayList;
import java.util.HashMap;


public class BowlingGame {
	
	private int score;
	private int frameNumber;
	private int rollNumber;
	private int frameScore;
	private int extraRolls = 0;

	/*There has to be a better way to store this...Each Index of the List is the Frame Number
	Each map in the index can have 4 keys => 
	Strike - 10 pins knocked out in one roll
	Spare - 2 rolls in one frame that add up to 10
	Open - 2 rolls in a frame that do not add up to 10
	In Progress - one roll in the frame has been bowled and the game is in progress
	Each value for these keys can be one number or two numbers stored as Strings	
	*/
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
		game.roll(5);
		game.roll(6);
		game.roll(10);
		game.roll(9);
		game.printBalls();
	}

	//Takes an integer as input, number of pins knocked out by roll and updates the data structure
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
	
		//If the last frame was Open , in the sense pins not cleared there will be no bonus shot!
		if(frameNumber ==10 ) {
			String type = FrameList.get(9).entrySet().iterator().next().getKey();
			
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
			//Two possibilities, if a (strike is rolled or not) AND (if it is 1st or 2nd roll in frame)
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
			
				else if(knocked+frameScore==10)	addValueToFrameList("Spare",frameScore+","+knocked,true,0,true);
				
				else addValueToFrameList("Open",frameScore+","+knocked,false,0,true);
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
				
				else if(knocked+frameScore == 10) addValueToFrameList("Spare",frameScore+","+knocked,true,0, true);
				
				else addValueToFrameList("Open",frameScore+","+knocked,true,0, true);
				}
			}
		}
	}

	//Checks if the game has finished
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

	//Used to get score at any point in the game, have to test it better -_-
	public int getScore() {
		score = 0;
		int FrameSize = FrameList.size();
		for(int i = 0; i < FrameSize; i++) {	
			String Type = (FrameList.get(i).entrySet().iterator().next().getKey());
			//If the hit is a strike then add the next two scores
			if(Type.equals("Strike")) {
				score += 10;
				if(i+1 < FrameSize) {
					String secondType = (FrameList.get(i+1).entrySet().iterator().next().getKey());
					//System.out.println(secondType);
					if(secondType.equals("In Progress")) {
						String secondScore = (FrameList.get(i+1).entrySet().iterator().next().getValue());
						score += Integer.parseInt(secondScore);
					}
					else if(secondType.equals("Spare") || secondType.equals("Open")) {
						String secondScore = (FrameList.get(i+1).entrySet().iterator().next().getValue());
						score += (Integer.parseInt(secondScore.split(",")[0])+Integer.parseInt(secondScore.split(",")[1]));
						continue;
					}
					else if(secondType.equals("Strike")){
						score += 10;
						if(i+2 < FrameSize) {
							String thirdScore = (FrameList.get(i+2).entrySet().iterator().next().getValue());
							if(thirdScore.equals("Strike")) score += 10;
							else score += (Integer.parseInt(thirdScore.split(",")[0]));
						}
					}
					else {
						score += (Integer.parseInt(FrameList.get(10).entrySet().iterator().next().getValue()) + Integer.parseInt(FrameList.get(11).entrySet().iterator().next().getValue()));
					}
				
				}
			}
		
			//If the frame is in progress means the game is not over yet
			else if(Type.equals("In Progress")) {
				String firstScore = (FrameList.get(i).entrySet().iterator().next().getValue());
				score+= Integer.parseInt(firstScore.split(",")[0]);
			}
		
			//If it is a spare then add the next frame first roll
			else if(Type.equals("Spare")) {
				String firstScore = (FrameList.get(i).entrySet().iterator().next().getValue());
				score += (Integer.parseInt(firstScore.split(",")[0])+Integer.parseInt(firstScore.split(",")[1]));
				if(i+1 < FrameSize) {
					String secondScore = (FrameList.get(i+1).entrySet().iterator().next().getKey());
					if(secondScore.equals("Strike")) score += 10;
					else {
						String lastNumber = (FrameList.get(i+1).entrySet().iterator().next().getValue());
						score += Integer.parseInt(lastNumber.split(",")[0]);
					}
				}
			}
		
			//If it is open just add the current frame score
			else if(Type.equals("Open")){
				String firstScore = FrameList.get(i).entrySet().iterator().next().getValue();
				score += (Integer.parseInt(firstScore.split(",")[0])+Integer.parseInt(firstScore.split(",")[1]));
			}
		
			else {
				break;
			}
		
		}
		return score;
	}

	//Print pins
	public void printBalls() {
		System.out.println(FrameList.toString());
	}

	//Used for adding Values to the FrameList
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
