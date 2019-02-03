<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

$settings = json_decode(file_get_contents('/var/www/html/projects/BloodPressureMonitoring/settings.json'), true);

$GLOBALS['username'] = $settings["database"]["username"];
$GLOBALS['password'] = $settings["database"]["password"];
$GLOBALS['server'] = $settings["database"]["server"];
$GLOBALS['port'] = $settings["database"]["port"];
$GLOBALS['name'] = $settings["database"]["name"];

function NewDBConnection(){
  $db = new mysqli($GLOBALS['server'].":".$GLOBALS['port'], $GLOBALS['username'], $GLOBALS['password'], $GLOBALS['name']);
  if($db->connect_errno){
    echo $db->connect_error;
    //die('A problem occurred.');
  }
  return $db;
}

 ?>
