<?php
error_reporting(0);
/**
 * Created by PhpStorm.
 * User: Sunnny
 * Date: 10/10/16
 * Time: 9:51 AM
 */
$server="localhost";
$username="root";
$password="";
$database="products";
$conn = mysqli_connect($server,$username,$password,$database);

if(!$conn)
{
    die("MySql error".mysqli_connect_error());
}


if (isset($_REQUEST['mobile'])&&isset($_REQUEST['name'])&&isset($_REQUEST['password'])&&isset($_REQUEST['gender']))
{
    $mob=$_REQUEST[mobile];
    $sql ="select * from USER where MOBILE_NUMBER = '$mob' and VERIFY = TRUE";
    $_result=mysqli_query($conn,$sql);
    
    if(mysqli_num_rows($_result)==1)
    {
        $response["msg"]="Already signed in.";
        $response["status"]="failure";
        echo json_encode($response);
    }
    else
    {
         $name=$_REQUEST['name'];
         $password=$_REQUEST['password'];
         $gender=$_REQUEST['gender'];
         $sql="insert into USER (MOBILE_NUMBER,NAME, GENDER,PASSWORD,VERIFY) values('$mob','$name','$gender','$password',FALSE)";
         $_result=mysqli_query($conn,$sql);
         $data=file_get_contents("http://127.0.0.1/ProductApp/send.php?countryCode=91&mobileNumber=$mob");
         echo $data;
    }
}
else
{
    $response["msg"]="Invalid data";
    $response["status"]="failue";
    echo json_encode($response);
}

?>

