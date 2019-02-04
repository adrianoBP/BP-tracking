<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// External functions and values
require '/var/www/html/projects/BloodPressureMonitoring/loadDefaults.php';
require '/var/www/html/projects/BloodPressureMonitoring/BackEnd/Helper/DBHelper.php';

// Reading parameters
$params = (file_get_contents('php://input'));
$request = json_decode($params, true);

$response = array();
$response["Error"] = null;

if(sizeof($request) == 0){
    $response["Error"] = "Missing parameters.";
}else{
    $authorizationToken = $request["token"];

    if(ValidateToken($authorizationToken)){

        $systole = $request["systole"];
        $diastole = $request["diastole"];
        $bpm = $request["bpm"];

        $db = NewDBConnection();
        $statement = $db->prepare("
            INSERT INTO measurement (sys, dia, bpm) VALUES (?, ?, ?)
        ");
        $statement->bind_param('sss', $systole, $diastole, $bpm);
        if(!$statement->execute()){
            $response["Error"] = "Unable to insert values.";
        }
    }else{
        $response["Error"] = "Unable to validate the token.";
    }
}
echo(json_encode($response));

 ?>
