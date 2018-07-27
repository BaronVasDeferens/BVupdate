
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
$name = $_GET["NAME"];
$home_address = $_GET["HOME_ADDRESS"];
$city = $_GET["CITY"];
$state = $_GET["STATE"];
$zip = $_GET["ZIP"];
$home_phone = $_GET["HOME_PHONE"];
$cell_phone = $_GET["CELL_PHONE"];
$email = $_GET["EMAIL"];

$business_name = $_GET["BUSINESS_NAME"];
$business_type = $_GET["BUSINESS_TYPE"];
$business_nature = $_GET["BUSINESS_NATURE"];
$years_in_business = $_GET["YEARS_IN_BUSINESS"];
$employees = $_GET["EMPLOYEES"];
$vehicles = $_GET["VEHICLES"];

$ref_1_name = $_GET["REF_1_NAME"];
$ref_1_contact = $_GET["REF_1_CONTACT"];
$ref_1_phone = $_GET["REF_1_PHONE"];

$ref_2_name = $_GET["REF_2_NAME"];
$ref_2_contact = $_GET["REF_2_CONTACT"];
$ref_2_phone = $_GET["REF_2_PHONE"];

$ref_3_name = $_GET["REF_3_NAME"];
$ref_3_contact = $_GET["REF_3_CONTACT"];
$ref_3_phone = $_GET["REF_3_PHONE"];

$insurance_company = $_GET["INSURANCE_COMPANY"];
$insurance_phone = $_GET["INSURANCE_PHONE"];

//Bldg details
$building_select = $_GET["BUILDING_SELECT"];
$lease_type = $_GET["LEASE_TYPE"];
$other_lease = $_GET["OTHER_LEASE"];
$renewal_option = $_GET["RENEWAL_OPTION"];


//Notes
$notes = $_GET["NOTES"];

//CAPTCHA
$captcha = $_GET["CAPTCHA"];


//CAPTCHA CHECK
//The characters must match, otherwise we know that this is another stinking script
if ($captcha != "7G8Yb")
{
	$aok = false;
	$error_msg[] = "+ Retype the sequence of numbers and letters at the Security Check section. Please be aware of capitalization.";
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

            echo "<p>Please press the <b>back button</b> to return to the prior page to try again. The information you've already provided will still be there.</p>";

    }



//BEGIN OUTPUT!

//FILE WRITING and EMAIL DISPATCH
//At this point, $aok is assumed to be true and we can finally begin to move on with our lives. 

//if($aok == true)
    else
    {

            //Collates the disparate data into what looks like not-shit
            $content[] = $newl . $newl . "APPLICANT:";

            $content[] = $newl . $name;
            $content[] = $newl . $home_address;
            $content[] = $newl . $city . ", " . $state . "  " . $zip;
            $content[] = $newl . "HOME: \t" . $main_phone;
            $content[] = $newl . "CELL: \t" . $cell_phone;
            $content[] = $newl . "EMAIL: \t \t" . $email;

            $content[] = $newl . "BUSINESS NAME: \t" . $business_name;
            $content[] = $newl . "BUSINESS TYPE: \t" . $business_type;
            $content[] = $newl . "BUSINESS NATURE: \t" . $business_nature;
            $content[] = $newl . "YEARS: \t" . $years_in_business;
            $content[] = $newl . "NUM EMPLOYESS: \t" . $employees;
            $content[] = $newl . "NUM VEHICLES: \t" . $vehicles;

            $content[] = $newl . $newl . "BUSINESS REFERENCE 1:";
            $content[] = $newl . $ref_1_name;
            $content[] = $newl . $ref_1_contact;
            $content[] = $newl . $ref_1_phone;

            $content[] = $newl . $newl . "BUSINESS REFERENCE 2:";
            $content[] = $newl . $ref_2_name;
            $content[] = $newl . $ref_2_contact;
            $content[] = $newl . $ref_2_phone;

            $content[] = $newl . $newl . "BUSINESS REFERENCE 3:";
            $content[] = $newl . $ref_3_name;
            $content[] = $newl . $ref_3_contact;
            $content[] = $newl . $ref_3_phone;

            $content[] = $newl .$newl ."INSURANCE INFO:";
            $content[] = $newl . $insurance_company;
            $content[] = $newl . $insurance_phone;

            $content[] = $newl . $newl . "BUILDING: \t" . $building_select;
            $content[] = $newl . "LEASE TYPE: \t" . $lease_type;
            $content[] = $newl . "OTHER: \t" . $other_lease;
            $content[] = $newl . "RENEWAL: \t" . $renewal_option;

            $content[] = $newl . "NOTES: \t" . $notes;	


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
            mail("baronvasdeferens@gmail.com", ("Rental application (" . $building_select . "):" . $name ), $the_data);
            mail("bunnsvillage@gmail.com", ("Rental application (" . $building_select . "):" . $name ), $the_data);
            mail("lgwest@wildblue.net", ("Rental application (" . $building_select . "):" . $name ), $the_data);


            //THANK YOU
            echo "<h1>Thank You!</h1>";
            echo "<p>Your application was submitted successfully. Thank you for your interest in Bunn's Village.</p>"; 
            echo "<p><p><A HREF = \"http://www.bunnsvillage.com/\">Return to main site</A>";

    }//$aok = true

?>



</BODY>
</HTML>
