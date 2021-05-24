<?php

require_once 'config.php';

// get parameters
if(isset($_REQUEST['id']) ) {
    $id = $_REQUEST['id'];
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

$content = mysqli_query($con , "SELECT * FROM `Teacher` WHERE `id`=$id");
$output = array();

while($row = mysqli_fetch_array($content)){
    $record  = array();
    $record['id'] = $row['id'];
    $record['name'] = $row['name'];
    $record['city'] = $row['city'];
    $record['school'] = $row['school'];
    $output[] = $record;
}

echo json_encode($output);
