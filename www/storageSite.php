<?php

function runSql() {
    $db = mysql_connect("localhost", "root", "");
    mysql_select_db("minichan", $db);
    mysql_query("SET names 'utf8'", $db);
    return $db;
}

function sendtable() {
    $db = runSql();
    
    $query = "SELECT * from storage";
    $result = mysql_query($query, $db);
    
    $table = "<table border=\"1\" cellpadding=\"5\" id=\"goodsTable\">
                <tr><th width=\"250\">Название</th><th width=\"150\">Цена</th>
                <th>Заказать</th></tr>";
    
    
    while($row = mysql_fetch_array($result))
        $table .= "<tr><td width=\"250\">".$row['name']."<td width=\"150\">"
            .$row['price']."</td><td><input type=\"number\" min=\"0\" value=\"0\"></td></tr>";
    $table .= "</table>";
    echo $table;
    
    mysql_close($db);
}

function getForm() {
    $db = runSql();
    
    $query = "SELECT * from storage";
    $result = mysql_query($query, $db);
    $sum = 0;
    
    $page = "<form action=\"index.php\" method=\"post\" name=\"form\" onsubmit=\"SentSuccessfully();\"";
    
    $page .= "<p align=\"center\"><span style=\"color: red; font-size:36px;\">
            Вы заказали следующее</span></p>";
        
    $page .= "<table border=\"1\" cellpadding=\"5\" id=\"goodsTable\">
             <tr><th width=\"300\">Название</th><th width=\"150\">Цена</th><th width=\"150\">Количество</th></tr>";
    
   $ordersList = "";
    
    next($_GET);
    while($row = mysql_fetch_array($result)) {
        if (current($_GET) != 0) {
            $page .= "<tr><td width=\"250\">".$row['name']."<td width=\"150\">"
                .$row['price']."</td><td>".current($_GET)."</td></tr>";
            $ordersList .= $row['name'].":".current($_GET)."!";
            $sum += $row['price'] * current($_GET);
            
        }
        next($_GET);
    }
    $page .= "</table>";
    
    $page .= "<p>Сумма заказа: ".$sum."</p>";
    
    $page .= "<p>Для подтверждения заказа введите ниже ваше ФИО адрес и мобильный телефон</p>";
    $page .= "<pre>ФИО:     <input name=\"fio\" type=\"text\" required></pre>";
    $page .= "<pre>Телефон: <input name=\"telephone\" type=\"text\" required></pre>";
    $page .= "<input type=\"hidden\" name = \"goods\" value=\"".$ordersList."\">";
    $page .= "<input type=\"hidden\" name = \"sum\" value=\"".$sum."\">";
    $page .= "<pre>Адрес:   <input name=\"address\" type=\"text\" required></pre>";
    
    $page .= "<p><input name=\"order\" value=\"Подтвердить заказ\" type=\"submit\"></p></form>";
    
    echo $page;
    
    mysql_close($db);
}

function sendCont() {
    $page = "<p style=\"margin-left: 20px;\">   По вопросам доставки обращаться к старшему интенданту Гарьеву Михаилу Дмитриевичу</p>";
    
    $page .= "<p style=\"margin-left: 20px;\">   Телефон горячей линии: 8-800-355-35-35</p>";
    
    echo $page;
}

function sendMainPage() {
    $page = "<p align=\"center\" style=\"color: red; font-size: 40px; font-weight: 200\">
            Добро пожаловать!</p>";
    
    $page .= "<p align=\"center\">В нашем онлайн магазине вы найдёте широкий ассортимент самых разннобразных
                канцелярских принадлежностей по доступным ценам, которые мы доставим вам в кратчайшие сроки! 
                Милости просим!</p>";
        
    echo $page;
}

function writeOrder() {
    $db = runSql();
    
    $query = "INSERT INTO Orders(fio, telephone, goods, sum, address, date)
             VALUES(\"".$_POST['fio']."\",\"".$_POST['telephone']."\",\"".$_POST['goods']."\",".$_POST['sum'].",\"".$_POST['address']
             ."\",\"".date("Y-m-d H:i:s")."\")";
    
    mysql_query($query, $db);
    
    $updatedGoods = explode("!", $_POST['goods']);
    foreach ($updatedGoods as $element) {
        $params = explode(":", $element);
        $query = "UPDATE storage SET amount = amount - ".$params[1]." WHERE name = \"".$params[0]."\"";
        if ($params[0] == '')
            break;
        mysql_query($query, $db);
    }
    
    mysql_close($db);
    
    sendMainPage();
}

function fillForm() {
    if ($_GET['page'] == "1")
        sendTable();
    else if ($_GET['page'] == "2")
        getForm();
    else if ($_GET['page'] == "3")
        sendCont();
    else if ($_POST['fio'])
        writeOrder();
    else
        sendMainPage();
}


?>