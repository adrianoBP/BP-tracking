<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// External functions and values
require '/var/www/html/projects/BloodPressureMonitoring/loadDefaults.php';
require '/var/www/html/projects/BloodPressureMonitoring/BackEnd/Helper/Util.php';
require '/var/www/html/projects/BloodPressureMonitoring/BackEnd/Helper/DBHelper.php';

// Reading parameters
$params = (file_get_contents('php://input'));
$request = json_decode($params, true);

$response = array();
$response["Error"] = null;

if(sizeof($request) == 0){
    $response["Error"] = "Missing parameters.";
}else{
    $userId = $request["userId"];
    if($userId){
        if(!ClearSessions($userId)){
            $response["Error"] = "Unable to remove the sessions.";
        }
    }else{
        $response["Error"] = "Provided user ID is not valid.";
    }
}

echo(json_encode($response));


 ?>
