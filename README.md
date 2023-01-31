# Jeu de Grundy (suite)

Ce projet a été réalisé dans le cadre d'une mise en Situation d'Apprentissage et d'Évaluation (SAE). Il a pour objectif principal de placer les étudiants dans une situation similaire à celle qu'ils pourraient rencontrer dans leur future profession, c’est à dire maximiser l’efficacité des algorithmes. Les étudiants doivent donc travailler sur cinq algorithmes différents afin de comprendre l'importance de coder efficacement.

### Mise en situation :   

Dans cette SAE1.02, nous allons étudier l'efficacité de la recherche du meilleur coup à jouer dans une partie du jeu de Grundy en utilisant une méthode récursive “estPerdante()” qui implémente une théorie garantissant de choisir toujours la solution gagnante si elle existe. Si elle n'existe pas, la machine choisira une décomposition au hasard. 
	
Un bon nombre de méthodes nous ont été données dans un document.  
La documentation que nous écrirons devra être impérativement écrite en anglais.  
Toute action doit être commentée.  

### Règle du jeu : 

Le jeu se joue seul ou à 2.  
Il commence sur un plateau de n (vous décidez) allumettes alignées sur la même rangée.  
Le premier joueur peut alors décider de séparer un certain nombre d'allumettes de cette première rangée.  
/!\ Il faut au minimum déplacer 1 allumettes et il doit au minimum en rester 1 sur la rangée initiale.  
/!\ De plus, le joueur ne peut pas séparer un tas en 2 tas de tailles exactement similaires.  
Les allumettes séparées forment une nouvelle rangée qui pourra être séparée par la suite.  
Vous l'aurez donc compris, un tas de 2 allumettes ne pourra plus être séparé car il enfreindrait la règle N°2.  
Lorsqu'il ne reste que des rangées de 1 ou 2 allumettes, le jeu est terminé.  
Celui qui ne peut plus jouer à perdu.  


###  Code source :   
Nom du fichier source qui nous a été donné Grundy2RecBrute.java  

### Version 0 : 
Nom du fichier source : Grundy2RecBruteEff.java  
Ajout de la boucle du jeu  

### Version 1 :
nom du fichier source : Grundy2RecPerdantes.java  
Ajout de la sauvegardes des dispositions du jeu perdantes pour ne pas les recalculer  

### Version 2 : 
nom du fichier source : Grundy2RecPerdEtGagn.java    
Ajout de la sauvegardes des dispositions du jeu gagnantes pour ne pas les recalculer  

### Version 3 : 
nom du fichier source : Grundy2RecPerdantNeutre.java  
Ajout de la suppression des dispositions perdantes et des doublons car ils n'influent pas sur la nature de la disposition  

### Version 4 : 
nom du fichier source : Grundy2RecGplusGequalsP.java  
Ajout de la suppressions des tas gagnants de même types   



### Théorèmes utilisés : 
L'ensemble des règles utilisées peuvent êtres retrouvées ici :   
http://mathenjeans.free.fr/amej/edition/9903grun/grundy2.html  


### Pour jouer : 
```bash
git clone https://github.com/Ne0re0/Grundy2_SAE1.02.git
cd Grundy2_SAE1.02
java -cp class/ Grundy2RecGplusGequalsP 
```


