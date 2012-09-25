<?php
/*
Author - Ibrahim Jumkhawala
Email Address - ijumkh01@students.poly.edu

Code Challenge Question1 : Accept a search string and the number of tweets from the user and Display the top hundred tweets if there are any!

Setup Required
On linux/Unix istributions do a sudo get-install php5-curl
This installs php5 with the appropriate Curl classes required to make the HttpRequest.
Run the program by placing the Php file in a folder - php TopTweets.php
*/


//Get contents from the keyboard
$keyboard = fopen("php://stdin","r");

//Accept Input from user-search string and number of tweets 
print ("What Tweet do you want to search for?");
$search = fgets($keyboard,80);

print ("How many tweets do you want?");

$max = fgets($keyboard,80);
//Trim search parameters
$max=trim($max);
//Url Encode string before sending it to Curl
$search=urlencode($search);

//Call function passing the two parameters
latest_tweets($max,$search);



function latest_tweets($max,$search) {
//Run Curl to get the Json List returned from the query
$curl_opener = curl_init("http://search.twitter.com/search.json?q=".$search."&rpp=".$max."&include_entities=true");

curl_setopt($curl_opener, CURLOPT_RETURNTRANSFER, true);

$output = curl_exec($curl_opener);

//Decode json into the appropriate array
$json_decode = json_decode($output);

//Count the number of results incase the number of tweets are not met
$result = count($json_decode->results);

$number=1;

//Echo each User and the url to his tweet
echo "Top ".$result." Tweets on #".$search."\n";
	for($i=0;$i<$result;$i++) {
		
		echo '============================================================================================================================================================================='."\n";
		echo "[".$number."]"." Username ==> ".$json_decode->results[$i]->from_user."\n";
		$url="Tweet Link ==> "."https://twitter.com/#!/".$json_decode->results[$i]->from_user."/status/".$json_decode->results[$i]->id_str;
		echo $url."\n";
		$number++;
	}




}
?>

