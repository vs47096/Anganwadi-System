<!DOCTYPE HTML>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>Stock Bill Records</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="Free HTML5 Website Template by gettemplates.co" />
	<meta name="keywords" content="free website templates, free html5, free template, free bootstrap, free website template, html5, css3, mobile first, responsive" />
	<meta name="author" content="gettemplates.co" />

	

	<!-- Facebook and Twitter integration -->
	<meta property="og:title" content=""/>
	<meta property="og:image" content=""/>
	<meta property="og:url" content=""/>
	<meta property="og:site_name" content=""/>
	<meta property="og:description" content=""/>
	<meta name="twitter:title" content="" />
	<meta name="twitter:image" content="" />
	<meta name="twitter:url" content="" />
	<meta name="twitter:card" content="" />

	<!-- <link href='https://fonts.googleapis.com/css?family=Work+Sans:400,300,600,400italic,700' rel='stylesheet' type='text/css'> -->
	
	<!-- Animate.css -->
	<link rel="stylesheet" href="css/animate.css">
	<!-- Icomoon Icon Fonts-->
	<link rel="stylesheet" href="css/icomoon.css">
	<!-- Bootstrap  -->
	<link rel="stylesheet" href="css/bootstrap.css">
	<!-- Theme style  -->
	<link rel="stylesheet" href="css/style.css">
    <!-- Table style  -->
	<link rel="stylesheet" href="css/table.css">
	<!-- Modernizr JS -->
	<script src="js/modernizr-2.6.2.min.js"></script>
	<!-- FOR IE9 below -->
	<!--[if lt IE 9]>
	<script src="js/respond.min.js"></script>
<![endif]-->

<style>
body {font-family: Arial, Helvetica, sans-serif;}

/* Full-width input fields */
input[type=text], input[type=password] {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    box-sizing: border-box;
}

/* Set a style for all buttons */
button {
    background-color: #4CAF50;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    cursor: pointer;
    width: 100%;
}

button:hover {
    opacity: 0.8;
}

/* Extra styles for the cancel button */
.cancelbtn {
    width: auto;
    padding: 10px 18px;
    background-color: #f44336;
}

/* Center the image and position the close button */
.imgcontainer {
    text-align: center;
    margin: 24px 0 12px 0;
    position: relative;
}

img.avatar {
    width: 50%;
    height:50%;
    border-radius: 5%;
}

.container {
    padding: 16px;
}

span.psw {
    float: right;
    padding-top: 16px;
}

/* The Modal (background) */
.modal {
    display: none; /* Hidden by default */
    position: fixed; /* Stay in place */
    z-index: 1; /* Sit on top */
    left: 0;
    top: 0;
    width: 100%; /* Full width */
    height: 100%; /* Full height */
    overflow: auto; /* Enable scroll if needed */
    background-color: rgb(0,0,0); /* Fallback color */
    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
    padding-top: 60px;
}

/* Modal Content/Box */
.modal-content {
    background-color: #fefefe;
    margin: 5% auto 15% auto; /* 5% from the top, 15% from the bottom and centered */
    border: 1px solid #888;
    width: 80%; /* Could be more or less, depending on screen size */
}

/* The Close Button (x) */
.close {
    position: absolute;
    right: 25px;
    top: 0;
    color: #000;
    font-size: 35px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: red;
    cursor: pointer;
}

/* Add Zoom Animation */
.animate {
    -webkit-animation: animatezoom 0.6s;
    animation: animatezoom 0.6s
}

@-webkit-keyframes animatezoom {
    from {-webkit-transform: scale(0)} 
    to {-webkit-transform: scale(1)}
}
    
@keyframes animatezoom {
    from {transform: scale(0)} 
    to {transform: scale(1)}
}

/* Change styles for span and cancel button on extra small screens */
@media screen and (max-width: 300px) {
    span.psw {
       display: block;
       float: none;
    }
    .cancelbtn {
       width: 100%;
    }
}
</style>

</head>
<body>
	<div id="page">
		<nav class="fh5co-nav" role="navigation">
		<div class="container">
			<div class="row">
				<div class="col-xs-2">
					<div id="fh5co-logo"></div>
				</div>
				<div class="col-xs-10 text-right menu-1">
					<ul>
						<li><a href="index.php">Home</a></li>
						<li><a href="main_page.php">Modules</a></li>
					</ul>
				</div>
			</div>
			
		</div>
		</nav>
		<header  id="fh5co-header" class="fh5co-cover fh5co-cover-sm" role="banner" style="background-image:url(images/img_bg_2.jpg); height:100px;">
		
		</header>
		
		<div id="fh5co-project">
			<div class="container">
				

				<?php

				include_once('connection.php');

				$query = mysqli_query($conn,"SELECT * FROM StockBillTable");
				$count = mysqli_num_rows($query);
				if($count == "0"){
					echo  "<h2>No result found!</h2>";
				}else{
				      echo "<div class='table-users' style='width:10500px;'>
                            <div class='header' style='line-height:1.7;padding:20px'>Stock-Bill's Record</div>
   
				            <table cellspacing='0' style='table-layout:fixed; line-height:1.7;'>
					    	<tr>

    			            	<th>Bill No.</th>
    
    			            	<th style='width:150px;'>Bill Date</th>
        
    	            			<th>Bill Time</th>
								
								<th>Receiving Helper Name</th>
								
								<th>Delivery Person Name</th>
								
    				            <th>Bill Image</th>
								
								<th>Receiving Helper's Signature</th>
								
								<th>Delivery Person's Signature</th>
								
								<th> Bill Items Image 1 </th>
								
								<th> Bill Items Image 2 </th>
				            </tr>";
				            
				        $fname = "";
                        $lname = "";  
                        $mname = "";
                        $billno = "";
                        $billdate = "";
                        $billtime = "";
                        $billpicurl = "";
                        $billpicurltoken = "";
                        $billitemspic1url = "";
                        $billitemspic1urltoken ="";
                        $billitemspic2url ="";
                        $billitemspicu2rltoken ="";
                        $helper_id ="";
                        $helpersignatureurl="";
                        $helpersignatureurltoken ="";
                        $deliverypersonname ="";
                        $deliverysignurl ="";
                        $deliverysignurltoken ="";
                        
					while($row = mysqli_fetch_array($query)){
						$billno=$row['billno'];
						
						$billdate=$row['billdate'];
						
						$billtime=$row['billtime'];
						
						$billpicurl=$row['bill_pic_url'];
						
						$billpicurltoken=$row['bill_pic_url_token'];
						
						$billitemspic1url=$row['bill_items_pic1_url'];
						
						$billitemspic1urltoken=$row['bill_items_pic1_url_token'];
						
						$billitemspic2url=$row['bill_items_pic2_url'];
						
						$billitemspicu2rltoken=$row['bill_items_pic2_url_token'];
						
						$helper_id=$row['helperid_who_received'];
						
						$helpersignatureurl=$row['helper_signature_url'];
						
						$helpersignatureurltoken=$row['helper_signature_url_token'];
						
						$deliverypersonname=$row['delivery_person_name'];
						
						$deliverysignurl=$row['delivery_signature_url'];
						
						$deliverysignurltoken=$row['delivery_signature_url_token'];
					    
						//replacing billpic url with proper format
						 
					    $billpicurl =  strstr($billpicurl,"Anganwadi_Helper_images",true).str_replace("/","%2F",strstr($billpicurl,"Anganwadi_Helper_images"))."&token=".$billpicurltoken;
					    
    					//replacing billitemspic1 url with proper format
                        $billitemspic1url =  strstr($billitemspic1url,"Anganwadi_Helper_images",true).str_replace("/","%2F",strstr($billitemspic1url,"Anganwadi_Helper_images"))."&token=".$billitemspic1urltoken;
					
						//replacing billitemspic2 url with proper format
                        $billitemspic2url =  strstr($billitemspic2url,"Anganwadi_Helper_images",true).str_replace("/","%2F",strstr($billitemspic2url,"Anganwadi_Helper_images"))."&token=".$billitemspicu2rltoken;    
                        
						//replacing helpersignatureurl with proper format
						$helpersignatureurl =  strstr($helpersignatureurl,"Anganwadi_Helper_images",true).str_replace("/","%2F",strstr($helpersignatureurl,"Anganwadi_Helper_images"))."&token=".$helpersignatureurltoken;    
						
						//replacing deliverysignurl with proper format
						$deliverysignurl =  strstr($deliverysignurl,"Anganwadi_Helper_images",true).str_replace("/","%2F",strstr($deliverysignurl,"Anganwadi_Helper_images"))."&token=".$deliverysignurltoken;	
						
						//code to fetch helper's name using helper_id from helper table
						$query1 = mysqli_query($conn,"SELECT * FROM HelpersTable where unique_id='".$helper_id."'");
						$count1 = mysqli_num_rows($query1);
						if($count1 == "0"){
							$output1 = '<h2>No result found!</h2>';
						}
						else{
							while($row = mysqli_fetch_array($query1)){
								$fname=$row['first_name'];
								$mname=$row['middle_name'];
								$lname=$row['sur_name'];
							}
						}
						
						//displaying details as table 
						
						echo "<tr>";
                       
                        echo "<td>" . $billno . "</td>";
                       
                        echo "<td>" . $billdate . "</td>";

        				echo "<td>" . $billtime . "</td>";
        
        				echo "<td>" . $fname ." " . $mname . " " . $lname ."</td>";
						
						echo "<td>" . $deliverypersonname. "</td>";
				        
				        echo"<td ><img style='cursor:pointer;' src='".$billpicurl."' onclick='disblock(this)'> </td>";
         
						echo"<td><img style='cursor:pointer;' src='".$helpersignatureurl."' onclick='disblock(this)'> </td>";

						echo"<td><img style='cursor:pointer;' src='".$deliverysignurl."' onclick='disblock(this)'> </td>";

						echo"<td><img style='cursor:pointer;' src='".$billitemspic1url."' onclick='disblock(this)'></td>";

						echo"<td><img style='cursor:pointer;' src='".$billitemspic2url."' onclick='disblock(this)'></td>";
         
        				echo"</tr>";

						
					}	
						echo"</table></div>";
					
			}
?>
			</div>
		</div>	
	</div>
	
	<div class="gototop js-top">
		<a href="#" class="js-gotop"><i class="icon-arrow-up"></i></a>
	</div>
	
	<div id="id01" class="modal">
		<form class="modal-content animate" action="/action_page.php">
			

			<div class="container" style="height:500px;">
				<div class="imgcontainer">
					<span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal">&times;</span>
					<?php
					echo "<img src=". $billpicurl. " id='blockpick' alt='Image' class='avatar'>";
					?>
				</div>
			</div>

			<div class="container" style="background-color:#f1f1f1; width:1030px;">
			    <center>
			    	<button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">Close</button>
			    </center>
			</div>
		</form>
	</div>

	<script>
	//function to display
	function disblock(pic){
		document.getElementById('id01').style.display='block';
		document.getElementById('blockpick').src = pic.src;
		
	}
	
	// Get the modal
	var modal = document.getElementById('id01');

	// When the user clicks anywhere outside of the modal, close it
	window.onclick = function(event) {
		if (event.target == modal) {
			modal.style.display = "none";
		}
	}
	</script>

	
	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<!-- jQuery Easing -->
	<script src="js/jquery.easing.1.3.js"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Waypoints -->
	<script src="js/jquery.waypoints.min.js"></script>
	<!-- Main -->
	<script src="js/main.js"></script>

</body>
</html>