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
    $accountUsername = $request["username"];
    $accountEmail = $request["email"];
    $accountPassword = $request["password"];

    $db = NewDBConnection();
    $statement = $db->prepare("
        SELECT id FROM User WHERE ( username = ? OR email = ?) AND password = ?
    ");
    $statement->bind_param('sss', $accountUsername, $accountEmail, $accountPassword);
    if($statement->execute()){
        $statement->store_result();
        if($statement->num_rows == 1){

            $statement->bind_result($userId);
            $statement->fetch();

            $newToken = CreateSession($userId);
            if($newToken){
                $response["token"] = $newToken;
            }else{
                $response["Error"] = "Error creating the session.";
            }

        }else{
            $response["Error"] = "Error retrieving the user.";
        }

    }else{
        $response["Error"] = "Unable to retrieve the user.";
    }
}

echo(json_encode($response));


 ?>
