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

    if(isset($_POST['mailU']) && isset($_POST['pseudoU']) && isset($_POST['mdpU'])){
            require_once "validate.php";

            $mailU = validate($_POST['mailU']);
            $pseudoU = validate($_POST['pseudoU']);
            $mdpU = validate($_POST['mdpU']);

            $reponse=$connexion->prepare("INSERT INTO utilisateur VALUES (NULL,'$mailU','". md5($mdpU) ."','$pseudoU', default);");
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