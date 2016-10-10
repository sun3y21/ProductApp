<?php
error_reporting(0);
/**
 * Created by PhpStorm.
 * User: Sunnny
 * Date: 11/10/16
 * Time: 12:27 AM
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


if (isset($_REQUEST['mobileNumber'])&&isset($_REQUEST['countryCode'])&&isset($_REQUEST['oneTimePassword']))
{
    $mob=$_REQUEST[mobileNumber];
    $otp=$_REQUEST[oneTimePassword];
    $data=file_get_contents("http://127.0.0.1/ProductApp/verify.php?countryCode=91&mobileNumber=$mob&oneTimePassword=$otp");
    $decodedData=json_decode($data);

    if($decodedData!=null)
    {
        $sql="update USER set VERIFY=true where MOBILE_NUMBER='$mob'";
        $result=mysqli_query($conn,$sql);
        $response["status"]="success";
        $response["msg"]="Verification done";
        echo json_encode($response);
    }
    else
    {
        $response["status"]="failure";
        $response["msg"]="Wrong OTP";
        echo json_encode($response);
    }
}
else
{
    $response["msg"]="Invalid data";
    $response["status"]="failue";
    echo json_encode($response);
    $sql ="select MOBILE from USER where MOBILE_NUMBER = '$mob' and VERIFY = FALSE ";
    $_result=mysqli_query($conn,$sql);
}

?>

