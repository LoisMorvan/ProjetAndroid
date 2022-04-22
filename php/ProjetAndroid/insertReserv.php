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

    if(isset($_POST['idResto']) && isset($_POST['idUtil']) && isset($_POST['dateResa'])){

            $idResto = $_POST['idResto'];
            $idUtil = $_POST['idUtil'];
            if ($idUtil == "") {
                $idUtil = 'NULL';
            }
            $dateResa = $_POST['dateResa'];

            $reponse=$connexion->prepare("INSERT INTO reservation VALUES (NULL,$idResto,$idUtil,'$dateResa');");
            $reponse->execute();

        if($reponse) {
            print("success"); 
        } else {
            print("failure");
        }
    }
} catch (Exception $e) {
    die('Erreur : ' . $e->getMessage());
}
?>