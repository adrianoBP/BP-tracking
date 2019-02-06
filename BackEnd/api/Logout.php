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

    if(ValidateAuthorization($authorizationToken)){

        $db = NewDBConnection();
        $statement = $db->prepare("
            DELETE FROM Session WHERE token = ?
        ");
        $statement->bind_param('s', $authorizationToken);
        if(!$statement->execute()){
            $response["Error"] = "Unable to insert values.";
        }
        $statement->store_result();
        if($db->affected_rows != 1){
            $response["Error"] = "Unable to log out.";
        };
    }else{
        $response["Error"] = "Unable to validate the token.";
    }
}
echo(json_encode($response));

 ?>
