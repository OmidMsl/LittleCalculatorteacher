<?php

require_once 'config.php';

// get parameters
if(isset($_REQUEST['id']) && isset($_REQUEST['name']) && isset($_REQUEST['city']) && isset($_REQUEST['school'])) {

    $id = $_REQUEST['id'];
    $name = $_REQUEST['name'];
    $city = $_REQUEST['city'];
    $school = $_REQUEST['school'];
} else{
    return;
}

// connect to database
$con = new mysqli(DB_HOST , DB_USER , DB_PASS , DB_NAME);

// check connection
if ($con->connect_error) {
    echo "Failed to connect : " . $con->connect_error;
    return;
}
mysqli_query( $con , "SET CHARACTER SET utf8;" );

$sqlquery = "UPDATE `Teacher` SET `name`='$name', `city`='$city', `school`='$school' WHERE `id`=$id";

if ($con->query($sqlquery) === true){
    echo "record edited successfully";
} else{
    echo "Error : " . $con->error;
}