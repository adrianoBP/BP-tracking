<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// External functions and values
require '/var/www/html/projects/BloodPressureMonitoring/loadDefaults.php';
require '/var/www/html/projects/BloodPressureMonitoring/BackEnd/Helper/Util.php';

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

    // Temporary code in order to retrieve the token.
    $db = NewDBConnection();
    $statement = $db->prepare("
        SELECT token FROM User WHERE ( username = ? OR email = ?) AND password = ?
    ");
    $statement->bind_param('sss', $accountUsername, $accountEmail, $accountPassword);
    if($statement->execute()){
        $statement->bind_result($token);
        $statement->fetch();
        $response["token"] = $token;
    }else{
        $response["Error"] = "Unable to retrieve the token.";
    }
    // // The code below creates a new token! DO NOT DELETE!
    // $newToken = GUID();
    //
    // $db = NewDBConnection();
    // $statement = $db->prepare("
    //     UPDATE User SET token = ? WHERE ( username = ? OR email = ?) AND password = ?
    // ");
    // $statement->bind_param('ssss', $newToken, $accountUsername, $accountEmail, $accountPassword);
    // if($statement->execute()){
    //     $affectedRows = $db->affected_rows;
    //     if($affectedRows == 1){
    //         $response['token'] = $newToken;
    //     }else if($affectedRows == 0){
    //         $response["Error"] = "Unable to update the token.";
    //     }else{
    //         $response["Error"] = "Multiple results when inserting the token.";
    //     }
    // }else{
    //     $response["Error"] = "Unable to update the token.";
    // }
}

echo(json_encode($response));

 ?>
