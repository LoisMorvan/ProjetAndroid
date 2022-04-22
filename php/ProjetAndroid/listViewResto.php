<?php 
try {
// Connexion à la base de données MySql
$db="resto2_lmorvan";
$dbhost="localhost";
$dbport=3308;
$dbuser="root";
$dbpasswd="";
 
$connexion = new PDO('mysql:host='.$dbhost.';port='.$dbport.';dbname='.$db.'', $dbuser, $dbpasswd);
$connexion->exec("SET CHARACTER SET utf8");
                        
$reponse=$connexion->prepare("SELECT nomR, villeR FROM resto");
$reponse->execute();
$datas = array();
         
while($res=$reponse->fetch(PDO::FETCH_ASSOC))
{
    $datas['restos'][]=$res;
}
             
echo json_encode($datas);
	   
}
catch (Exception $e)
{
die('Erreur : ' . $e->getMessage());
}

?>
