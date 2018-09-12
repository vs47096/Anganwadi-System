<?php
$conn =  mysqli_connect("localhost","id","password","dbname");
if($conn->connect_error){
	die( "Not Connected".$conn->connect_error);
}
else{
	echo "";
}
?>