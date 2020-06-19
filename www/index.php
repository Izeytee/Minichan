<html>
<head>
	<title>MiniChan online market</title>
	<meta http-equiv="Content-Type" content="text/html" charset="UTF-8"/> 
	<link href="MiniChan.css" rel="stylesheet" type="text/css"/>
</head>

<?php 
include ('storageSite.php');
?>

<script> 
function SentSuccessfully(){
	alert("Спасибо за заказ! Вскоре мы отправим вам посылку.");
}

function getInput() {
	 if (document.getElementById("page").href.indexOf("page=2") != -1)
		 return; 
	
	 var inputs = document.getElementsByTagName('input');

	 var hasNonZero = false;
	 var ref = "index.php?page=2&";
	 for (var i = 0; i < inputs.length; ++i) {
		ref += "pos" + i + "=" + inputs[i].value + "&";
		if (inputs[i].value != "0")
			hasNonZero = true;
	 }
	if (ref.length < 18 || !hasNonZero)
		alert("Вы ничего не заказали! Для выбора перейдите во вкладку \"Товары\" и выберете хотя бы один желаемый продукт");
	else
		document.getElementById("page").href = ref;
}
</script>

<body>
	<center>
		<table border="0" cellspacing="0" width = "1024">

			<tr valign="top">
				<td valign="top" width="1024">
					<a href="index.php" target="_self">
						<img src="image/head.jpg" border="0" width="1024" height="150">
					</a>
				</td>
			</tr>

			<tr valign="top">
				<td valign="top" width="1024">
					<table border="1" cellpadding="0" width="1024" id="tableCenter">
						<tr valign="top">

							<td valign="top" width="200">
								<div id ="menuLeft">
									<ul>
										<li><a href="index.php" target="_self"><button>Главная</button></a></li>
										<li><a href="index.php?page=1" target="_self"><button>Товары</button></a></li>
										<li><a href="" target="_self" id = "page"><button onClick="getInput();">Заказать</button></a></li>
										<li><a href="index.php?page=3" target="_self"><button>Контакты</button></a></li>
									</ul>
								</div>

							</td>
							<td valign="top" width="924">
								<?php 
									fillForm();
								?>
							</td>
							<td valign="top" width="200" height="200" style="background-color: 11caff">
								<div style="width: 200; 
									padding-top: 10px; 
									padding-bottom: 10px;">
									<p style="text-align: center;"><img src="image/adv.gif" alt="image/jpg" border="0" width="200" height="200"/></p>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</center>
	<div style="position:absolute; bottom: 0px; color: white;">
	Minichan, разработано Трусовым Кириллом. Email для связи: I-zey@yandex.ru.
	</div>
</body>
</html>