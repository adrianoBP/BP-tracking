<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// External functions and values
require '/var/www/html/projects/BloodPressureMonitoring/loadDefaults.php';

$response = array();
$response["Error"] = null;

$db = NewDBConnection();
$statement = $db->prepare("
    SELECT id, sys, dia, createTime FROM measurement ORDER BY createTime DESC LIMIT 100
");
if($statement->execute()){
    $statement->store_result();
    if($statement->num_rows > 0){
        $statement->bind_result($id, $systole, $diastole, $createTime);
        $response["Data"] = array();
        while($statement->fetch()){
            $dataRow["id"] = $id;
            $dataRow["systole"] = $systole;
            $dataRow["diastole"] = $diastole;
            $dataRow["createTime"] = $createTime;

            $response["Data"][] = $dataRow;
        }
    }else{
        $response["Error"] = "No data found.";
    }
}else{
    $response["Error"] = "Unable to retrieve the data.";
}

echo(json_encode($response));

 ?>
