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
            SELECT id, sys, dia, bpm, createTime FROM measurement ORDER BY createTime DESC LIMIT 100
        ");
        if($statement->execute()){
            $statement->store_result();
            if($statement->num_rows > 0){
                $statement->bind_result($id, $systole, $diastole, $bpm, $createTime);
                $response["Data"] = array();
                while($statement->fetch()){
                    $dataRow["id"] = $id;
                    $dataRow["systole"] = $systole;
                    $dataRow["diastole"] = $diastole;
                    $dataRow["bpm"] = $bpm;
                    $dataRow["createTime"] = $createTime;

                    $response["Data"][] = $dataRow;
                }
            }else{
                $response["Error"] = "No data found.";
            }
        }else{
            $response["Error"] = "Unable to retrieve the data.";
        }
    }else{
        $response["Error"] = "Unable to validate the token.";
    }
}
echo(json_encode($response));

 ?>
