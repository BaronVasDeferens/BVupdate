
<HTML>
<HEAD>
<TITLE>Bunn's Village Information Request</TITLE>
<link rel="stylesheet" type="text/css" href="mystyle.css" />
</HEAD>
<BODY>

<?php
//Newline character for formatting...
$newl = "\n";

//AOK is the condition flag
$aok = true;

$timestamp = time();
$error_msg[] = "The following items need to be addressed before we may proceed:";

//Strip data from the submitted form
//Customer details
$name =$_GET["NAME"];
$main_phone = $_GET["MAIN_PHONE"];
$email_1 = $_GET["EMAIL_1"];

//Bldg details
$building_select = $_GET["BUILDING_SELECT"];
$usage = $_GET["USAGE"];

//Notes
$notes = $_GET["NOTES"];






//PHONE CHECK
//Only one number need to provided. If number fields are not empty (returns false), then everything gets ANDed and comes out cool. Otherwise puke.
if (empty($main_phone) && empty($email_1))
{
	$aok = false;
	$error_msg[] = "+ Please provide either a telephone number or an email address.";
}


//END OF CHECKS



//ERRORS!!!
//Output error messages if ONE OR MORE OF THE REQUIRED FIELDS are empty
if ($aok == false)
{
	echo "<h1>One small problem...</h1>";
	
	$errors = count($error_msg);
	for ($err = 0; $err < $errors; $err++)	
	{	
		echo "<p>" . $error_msg[$err] . "</p>";
	}

	echo "<p>Please press the <b>back button</b> to return to the prior page to try again. The information you've already provided will still be there.";

}



//BEGIN OUTPUT!

//FILE WRITING and EMAIL DISPATCH
//At this point, $aok is assumed to be true and we can finally begin to move on with our lives. 

if($aok == true)
{

	//Collates the disparate data into what looks like not-shit
	$content[] = $newl . $newl . "NAME: \t \t" . $name;
	
	$content[] = $newl . "PHONE: \t" . $main_phone;
	$content[] = $newl . "EMAIL: \t \t" . $email_1;

	$content[] = $newl . $newl . "BUILDING: " . $building_select;
	$content[] = $newl . "USAGE: " . $usage;
	
	$content[] = $newl . "QUESTION: " . $notes;	


	//Create array for email ouput 
	$the_data = $newl . date("m/d/y G.i:s",$timestamp);


	//Initialize and execute writing loop
	//Writes both into the currently open file and appends data onto $the_data
	$times = count($content);
	for ($d = 0; $d < $times; $d++)
	{
		$the_data = $the_data . $content[$d];
	}

	 
	//Dispatch the email
	mail("baronvasdeferens@gmail.com", $last_name, $the_data);
	mail("bunnsvillage@gmail.com", $last_name, $the_data);
	

	//THANK YOU
	echo "<h1>Thank you!</h1>";
	echo "<p>Your information has been sent successfully. Thank you for your time and consideration.</p>"; 
	echo "<p><p><A HREF = \"http://www.bunnsvillage.com/\">Return to main site</A>";

}//$aok = true

?>



</BODY>
</HTML>
