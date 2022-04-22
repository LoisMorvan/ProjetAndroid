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

if ($_GET['idResto'] != NULL) {
    $reponse=$connexion->prepare("SELECT numAdrR, voieAdrR, cpR, villeR, descR, horairesR FROM resto WHERE idR = ".$_GET['idResto'].";");
    $reponse->execute();
    $datas = array();
             
    while($res=$reponse->fetch(PDO::FETCH_ASSOC))
    {
        $datas['unResto'][]=$res;
    }
                 
    echo json_encode($datas);
           
    }
}
catch (Exception $e)
{
die('Erreur : ' . $e->getMessage());
}
?>
