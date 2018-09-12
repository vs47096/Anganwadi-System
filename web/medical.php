<!DOCTYPE HTML>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>Medical Records</title>
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

				$query = mysqli_query($conn,"SELECT * FROM Dr_Reg");
				$count = mysqli_num_rows($query);
				if($count == "0"){
					$output = '<h2>No result found!</h2>';
				}else{
				      echo "<div class='table-users' style='width:1000px;'>
                            <div class='header' style='line-height:1.7; padding:20px;'>Medical Record Details</div>
   
				            <table cellspacing='0'  style='line-height:1.7;'>
					    	<tr>

    			            	<th>name</th>
    
    			            	<th>Organisation</th>
        
    	            			<th>Date</th>
    				
    				            <th>Patient Data </th>
				
				            </tr>";
					while($row = mysqli_fetch_array($query)){
					    echo "<tr>";
                       
                        echo "<td>" . $row['name'] . "</td>";
                       
                        echo "<td>" . $row['org_name'] . "</td>";

        				echo "<td>" . $row['date'] . "</td>";
        
        				echo "<td>" . $row['child_data'] . "</td>";
        
        				echo "</tr>";

						
					}	
						echo "</table></div>";
			}

				?>
			</div>
		</div>	
	</div>
	
	<div class="gototop js-top">
		<a href="#" class="js-gotop"><i class="icon-arrow-up"></i></a>
	</div>
	
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