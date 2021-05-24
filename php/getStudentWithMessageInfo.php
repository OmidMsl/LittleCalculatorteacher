<?php

require_once 'config.php';
// get parameters
if (isset($_REQUEST['id'])){
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

$content = mysqli_query($con , "SELECT `name`, `father_name`, `id`, FROM `Student` WHERE teacher_id=$id");
$output = array();

while($row = mysqli_fetch_array($content)){
    $record  = array();
    $record['name'] = $row['name'];
    $record['father_name'] = $row['father_name'];

    $studentId = $row['id'];
    $record['id'] = $studentId;
    $content1 = mysqli_query($con , "SELECT COUNT(*) AS `num_of_new_messages` FROM `Message` WHERE `sender_id`=$studentId AND `is_from_teacher`=0 AND `is_observed`=0");
    $content2 = mysqli_query($con, "SELECT `content` AS `last_message`, `is_from_teacher` FROM `Message` WHERE (`sender_id`=$studentId OR `receiver_id`=$studentId) AND `date` IN (SELECT MAX(`date`) FROM `Message` WHERE `sender_id`=$studentId OR `receiver_id`=$studentId)");
    $row1 = mysqli_fetch_array($content1);
    $row2 = mysqli_fetch_array($content2);
    $record['num_of_new_messages'] = $row1['num_of_new_messages'];
    $record['last_message'] = $row2['last_message'];
    $record['if_from_teacher'] = $row2['if_from_teacher'];

    $output[] = $record;
}

echo json_encode($output);