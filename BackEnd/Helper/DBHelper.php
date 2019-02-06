<?php

function UserExists($UserId){
    $db = NewDBConnection();
    $statement = $db->prepare("
        SELECT email FROM User WHERE id = ?
    ");
    $statement->bind_param('s', $UserId);
    if(!$statement->execute()){
        return false;
    }
    $statement->store_result();
    if($statement->num_rows != 1){
        return false;
    }
    return true;
}

function ValidateAuthorization($authorizationToken){
    $db = NewDBConnection();
    $statement = $db->prepare("
        SELECT userId FROM Session WHERE token = ?
    ");
    $statement->bind_param('s', $authorizationToken);
    if(!$statement->execute()){
        return null;
    }
    $statement->store_result();
    if(!$statement->num_rows == 1){
        return null;
    }
    $statement->bind_result($userId);
    $statement->fetch();
    return $userId;
}

function CreateSession($UserId){
    // Create new session
    $db = NewDBConnection();
    $newToken = GUID();
    $statement = $db->prepare("
        INSERT INTO Session (token, userId) VALUES (?, ?)
    ");
    $statement->bind_param('ss', $newToken, $UserId);
    if(!$statement->execute()){
        return null;
    }
    if($db->affected_rows != 1){
        return null;
    }

    return $newToken;
}

function ClearSessions($UserId){
    if(!UserExists($UserId)){
        return false;
    }
    $db = NewDBConnection();
    $statement = $db->prepare("
        DELETE FROM Session WHERE userId = ?
    ");
    $statement->bind_param('s', $UserId);
    if(!$statement->execute()){
        return false;
    }
    return true;
}



 ?>
