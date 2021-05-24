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

$content = mysqli_query($con , "SELECT `name`, `father_name`, Student.id, MAX(Test.date) AS `time_of_last_test` , COUNT(DISTINCT Test.id) AS `num_of_tests` FROM `Student` JOIN `Test` ON Student.id=Test.student_id WHERE Student.teacher_id=$id");
$output = array();

while($row = mysqli_fetch_array($content)){
    $record  = array();
    $record['name'] = $row['name'];
    $record['father_name'] = $row['father_name'];
    $record['id'] = $row['id'];
    $record['time_of_last_test'] = $row['time_of_last_test'];
    $record['num_of_tests'] = $row['num_of_tests'];
    $output[] = $record;
}

echo json_encode($output);