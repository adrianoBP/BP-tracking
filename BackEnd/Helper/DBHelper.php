<?php

function ValidateToken($authorizationToken){

    $db = NewDBConnection();
    $statement = $db->prepare("
        SELECT id FROM User WHERE token = ?
    ");
    $statement->bind_param('s', $authorizationToken);
    if($statement->execute()){
        $statement->store_result();
        if($statement->num_rows == 1){
            return true;
        }else{
            return false;
        }
    }else{
        return false;
    }

}



 ?>
