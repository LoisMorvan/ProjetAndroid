<?php 
try {
    // Connexion à la base de données MySql
    $db="morvan_resto_pj2";
    $dbhost="localhost";
    $dbport=3308;
    $dbuser="morvan";
    $dbpasswd="J04VT";
    
    $connexion = new PDO('mysql:host='.$dbhost.';port='.$dbport.';dbname='.$db.'', $dbuser, $dbpasswd);
    $connexion->exec("SET CHARACTER SET utf8");

    if(isset($_POST['mailU']) && isset($_POST['mdpU'])){
        require_once "validate.php";
        $mailU = validate($_POST['mailU']);
        $mdpU = validate($_POST['mdpU']);
        $reponse = $connexion->prepare("SELECT * FROM utilisateur WHERE mailU = '$mailU' and mdpU = '". md5($mdpU) ."';");
        $reponse->execute();
        if ($reponse) {
            $res = $reponse->fetch(PDO::FETCH_ASSOC);
            if ($res) {
                print "success";
            } else {
                print "failure";
            }
        }
    }
} catch (Exception $e) {
    die('Erreur : ' . $e->getMessage());
}
?>

