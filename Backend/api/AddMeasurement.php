<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// External functions and values
require '/var/www/html/projects/BloodPressureMonitoring/loadDefaults.php';

// Reading parameters
$params = (file_get_contents('php://input'));
$request = json_decode($params, true);

$response = array();
$response["Error"] = null;

if(sizeof($request) == 0){
    $response["Error"] = "Missing parameters.";
}else{
    $systole = $request["systole"];
    $diastole = $request["diastole"];

    $db = NewDBConnection();
    $statement = $db->prepare("
        INSERT INTO measurement (sys, dia) VALUES (?, ?)
    ");
    $statement->bind_param('ss', $systole, $diastole);
    if(!$statement->execute()){
        $response["Error"] = "Unable to insert values.";
    }
}
echo(json_encode($response));

 ?>
