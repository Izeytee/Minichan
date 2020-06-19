<?php

function runSql() {
    $db = mysql_connect("localhost", "root", "");
    mysql_select_db("minichan", $db);
    mysql_query("SET names 'utf8'", $db);
    return $db;
}

function sendTable() {
    $db = runSql();
    
    $query = "SELECT * from storage";
    $result = mysql_query($query, $db);
    
    while($row = mysql_fetch_array($result))
        $resp .= $row['id'].'!'.$row['name'].'!'.$row['amount'].'!'.$row['price'].'&';
    echo $resp;
    
    mysql_close($db);
}

function updateTable() {
    $db = runSql();
    foreach ($_GET as $value) {
        if ($value != "update") {
            $query = "UPDATE storage SET ";
            $object = explode("id", key($_GET));
            $query .= $object[0]." = ".$value;
            if ($object[0] == "amount")
                $query .= " + ".$object[0];
            $query .= " WHERE id = ".$object[1];
            mysql_query($query, $db);
        }
        next($_GET);
    }

    mysql_close($db);
}

function getOrdersTable() {
    $db = runSql();
    
    $query = "SELECT * from orders";
    $result = mysql_query($query, $db);
    
    while($row = mysql_fetch_array($result))
        $resp .= $row['id'].'~'.$row['fio'].'~'.$row['telephone'].'~'.$row['address'].'~'.$row['sum'].'~'.$row['date'].'~'.$row['goods'].'&';
    echo $resp;
        
    mysql_close($db);
}

if ($_GET['com'] == "getTable")
    sendTable();
else if ($_GET['com'] == "update") {
    updateTable();
    sendTable();
}
else if ($_GET['com'] == "getOrders")
    getOrdersTable();
?>