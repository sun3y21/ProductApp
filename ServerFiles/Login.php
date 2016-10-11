<?php
/**
 * Created by PhpStorm.
 * User: Sunnny
 * Date: 11/10/16
 * Time: 10:44 AM
 */
error_reporting(0);
$server="localhost";
$username="root";
$password="";
$database="products";
$conn = mysqli_connect($server,$username,$password,$database);

if(!$conn)
{
    die("MySql error".mysqli_connect_error());
}


if (isset($_REQUEST['mobile'])&&isset($_REQUEST['password']))
{
    $mob=$_REQUEST[mobile];
    $password=$_REQUEST[password];
    $sql="select * from USER where MOBILE_NUMBER='$mob' and PASSWORD = '$password' and VERIFY = 1";
    $_result=mysqli_query($conn,$sql);

    if(mysqli_num_rows($_result)==1)
    {
        $response["status"]="Success";
        $response["msg"]="Login success";
        $row=mysqli_fetch_assoc($_result);
        $response["name"]="".$row["NAME"];
        echo json_encode($response);
    }
    else
    {
        $response["status"]="failure";
        $response["msg"]="Mobile number or password is incorrect.";
        echo json_encode($response);
    }
}
else
{
    $response["msg"]="Invalid data";
    $response["status"]="failue";
    echo json_encode($response);
}

?>