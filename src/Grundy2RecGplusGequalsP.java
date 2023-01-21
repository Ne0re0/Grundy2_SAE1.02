import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Grundy 2 version 4
 * 
 * Implemented in previous versions : 
 * This version contains a method playAgainstAI()
 * This method allows a user to play Grundy Game VS the computer
 * This version implements the saving of losing dispositions as well as the 
 * method isLosingDecomposition which compares the different dispositions with 
 * the known losing dispositions
 * 
 * Implements  ArrayList<Integer> occurrenceTable(ArrayList<Integer> gameboard)
 * This method return the occurrence table of the given gameboard because the order
 * of lines isn't changing. It's used to compare gameboards.
 * 
 * Implements ArrayList<Integer> isFoundInLosingArrayList(ArrayList<Integer> gameboard, ArrayList<ArrayList<Integer>> losingArray)
 * that test if the occurrenceTable of the gameboard is stored in losingArray
 * 
 * Implements ArrayList<Integer> isFoundInWinningArrayList(ArrayList<Integer> gameboard, ArrayList<ArrayList<Integer>> winningArray)
 * that test if the occurrenceTable of the gameboard is stored in winningArray
 * 
 * Implements the deletion of losing situations with 
 * void deleteLosingKnownElements(ArrayList<Integer> gameboard, ArrayList<ArrayList<Integer>> losingArray)
 * 
 * 
 * In this version : 
 * Implement rule 4.1 : Elements of sitations can be represented by their type
 * Two winning element of same type are losing
 * but two winning element of different type are winning
 * With this rule, the method deleteLosingKnownElements become time wasting
 * We can just apply this rule and remove couples
 * 
 * Contains a cpt variable to test effectivness
 * Contains a class variable named perdantes which will store losing arrangments
 * Contains a class variable named gagnantes which will store winning arrangments
 * Contains a basic int array named "type" which has elements's types
 */

class Grundy2RecGplusGequalsP {

    double cpt;
    ArrayList<ArrayList<Integer>> perdantes = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> gagnantes = new ArrayList<ArrayList<Integer>>();
    int[] type = {0,0,0,1,0,2,1,0,2,1,   0,2,1,3,2,1,3,2,4,3,  0,4,3,0,4,3,0,4,1,2,   3,1,2,4,1,2,4,1,2,4,   1,5,4,1,5,4,1,5,4,1,   0 };

    /**
     * Méthode principal du programme
     */
    void principal() {
        
        playAgainstAI();
        // testIsFoundInLosingArrayList();
        // testIsFoundInWinningArrayList();
        // testJouerGagnantEff();
        // testDisplay();
        // testJouerGagnant();
        // testOccurenceTable();
        // testDeleteLosingKnownElements();
        // testDeleteWinningElementCouples();
    }



	
	/**
     * Méthode RECURSIVE qui indique si la configuration (du jeu actuel ou jeu d'essai) est perdante
     * 
     * @param jeu plateau de jeu actuel (l'état du jeu à un certain moment au cours de la partie)
     * @return vrai si la configuration (du jeu) est perdante, faux sinon
     */
    boolean estPerdante(ArrayList<Integer> jeu) {
        cpt++;
        boolean ret = true; // par défaut la configuration est perdante
		
        if (jeu == null) {
            System.err.println("estPerdante(): le paramètre jeu est null");
        }
		
		else {

            // Add rule 3.4 : The nature of a situation does not change if one adds or removes a losing situation
            // Add rule 3.5 : A double is a losing situation
            // Create a deepcopy of the  current gameboard
            ArrayList<Integer> narrowedGameboard = new ArrayList<>();
            for (int i = 0; i < jeu.size(); i++){
                narrowedGameboard.add(jeu.get(i));
            }

            // Editing the copy with the new "type" rule and the narrowed version
            deleteWinningElementCouples(narrowedGameboard);

            // if the new narrowedGamemboard is empty, it's because all have been removed, so i'ts a losing situation
            if (narrowedGameboard.size() == 0){
                ret = true;
            }

			// si il n'y a plus que des tas de 1 ou 2 allumettes dans le plateau de jeu
			// alors la situation est forcément perdante (ret=true) = FIN de la récursivité
            if ( !estPossible(narrowedGameboard) ) {
                ret = true;
            }
			
			else {
                
				// création d'un jeu d'essais qui va examiner toutes les décompositions
				// possibles à partir de jeu
                ArrayList<Integer> essai = new ArrayList<Integer>(); // size = 0
				
                ArrayList<Integer> occurrenceEssai; // Store occurrenceTable if this version of essai is in "perdantes", null if not
				// toute première décomposition : enlever 1 allumette au premier tas qui possède
                
                
                // au moins 3 allumettes, ligne = -1 signifie qu'il n'y a plus de tas d'au moins 3 allumettes
                // Currently searching for narrowed situations, because if the nature of the narrow version is the
                // same as the full version
                int ligne = premier(narrowedGameboard, essai);
                while ( (ligne != -1) && ret) {
					// mise en oeuvre de la règle numéro1
					// Une situation (ou position) est dite perdante pour la machine (ou le joueur, peu importe) si et seulement si TOUTES 
					// ses décompositions possibles (c-à-d TOUTES les actions qui consistent à décomposer un tas en 2 tas inégaux) sont 
					// TOUTES gagnantes pour l’adversaire.
					// Si UNE SEULE décomposition (à partir du jeu) est perdante (pour l'adversaire) alors la configuration n'EST PAS perdante.
					// Ici l'appel à "estPerdante" est RECURSIF.

                    // Testing if this version of "essai" is already found and stored in "perdantes"
                    occurrenceEssai = isFoundInLosingArrayList(essai, perdantes);
                    if (occurrenceEssai != null){
                        ret = false;
                    }

                    
                    else{

                        // Testing if this version of essai is already stored in the winningArray
                        occurrenceEssai = isFoundInWinningArrayList(essai, gagnantes);
                        if (occurrenceEssai != null) {
                            ligne = suivant(narrowedGameboard, essai, ligne);
                        }
                        else {

                            // If the version of essai has never been found, acting as normal
                            if (estPerdante(essai) == true) {

                                // If the version is a loosing one, add it to perdantes
                                perdantes.add(occurrenceTable(essai));
                                ret = false;
                                
                            } else {
                                // génère la configuration d'essai suivante (c'est-à-dire UNE décomposition possible)
                                // à partir du jeu, si ligne = -1 il n'y a plus de décomposition possible
                                
                                // if the version is a winning one, then adding it to gagnantes
                                gagnantes.add(occurrenceTable(essai));
                                ligne = suivant(narrowedGameboard, essai, ligne);
                                
                            }
                        }       
                    }
                }
            }
        }
        return ret;
    }
	






    /**
     * This method remove couples i.e. {5,5} become {0,0} but {5,5,5} become {0,0,5}
     * This method also remove elements with their type equal to 0 because they are losing elements
     * This method also remove couples with the same type
     * Based on class variable int[] types
     * @param gameboard the gameboard
     */
    void deleteWinningElementCouples(ArrayList<Integer> gameboard){
        // Avoid errors
        if (gameboard == null){
            return;
        }

        int element1;
        int element2;

        // For every element in gameboard
        for (int i = 0; i < gameboard.size(); i++){
            element1 = gameboard.get(i);
            try {
                // If type = 0 then remove it
                if (element1 <= 51 && type[element1] == 0){
                    gameboard.set(i,0);
    
                } else {

                    // For every other elements of gameboard
                    for (int j = i+1; j < gameboard.size() ; j ++){
                        element2 = gameboard.get(j);

                        // If both  elements are equals, then set both to 0 because of rule 3.5
                        if (element1 == element2){
                            gameboard.set(i,0);
                            gameboard.set(j,0);
                            break;
                        }
                        try {
                            // If both elements have the same type, then remove them
                            if (type[element1] == type[element2]){
                                gameboard.set(i,0);
                                gameboard.set(j,0);
                            }
                        } catch (Exception e) {
                            // Value superior to 51
                        }
                        
                    }
                }
            } catch (Exception e){
                // Error
            }
            
        }
    }



    /**
     * @param gameboard
     * @param expected
     */
    void testCaseDeleteWinningElementCouples(ArrayList<Integer> gameboard, ArrayList<Integer> expected){
        System.out.println(" *** Testing ...");
        System.out.println("Gameboard : " + gameboard);
        System.out.println("Expected result : " + expected);
        // Call method
        deleteWinningElementCouples(gameboard);
        System.out.println("Real result : " + gameboard);
        if (expected.equals(gameboard)){
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
        }
    }


    void testDeleteWinningElementCouples(){
        // gameboard = {1,2,3,4,5}
        // expected = {0,0,3,0,5}
        // 1,2 and 4 are type 0 | 3 and 5 have different types
        ArrayList<Integer> gameboard = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(0,0,3,0,5));
        testCaseDeleteWinningElementCouples(gameboard, expected );

        // gameboard2 = {150,150,150,3,6}
        // expected2 = {0,0,150,0,0}
        // 150 appears 3 times, it should now appear only one time
        // 3 and 6 have the same type so we remove them
        ArrayList<Integer> gameboard2 = new ArrayList<>(Arrays.asList(150,150,150,3,6));
        ArrayList<Integer> expected2 = new ArrayList<>(Arrays.asList(0,0,150,0,0));
        testCaseDeleteWinningElementCouples(gameboard2, expected2 );
    }



















    // << ----------------------- Following methods aren't edited from v2 ----------------------------------------------- >> //
























    /**
     * This method set every element to 0 if it's known as a losing one because the nature doesn't change if we add or remove a losing situation
     * This method also set to 0 duplicates because x + x is always a losing situation
     * @param gameboard the gameboard
     * @param losingArray the losingArrayList
     */
    void deleteLosingKnownElements(ArrayList<Integer> gameboard, ArrayList<ArrayList<Integer>> losingArray){
        
        // Check integrity
        if (gameboard == null){
            return;
        }
        if (gameboard.size() == 0){
            return;
        }
        
        ArrayList<Integer> tmp = new ArrayList<>();
        ArrayList<Integer> tmpOccurrence = new ArrayList<>();
        ArrayList<Integer> intersect = new ArrayList<>();
        
      
        // Remove duplicates
        ArrayList<Integer> gameboardOccurrence = occurrenceTable(gameboard);
        int index;
        for (int i = 0; i < gameboardOccurrence.size(); i++){
            // If an item is in an even quantity : set all appearance to 0
            if (gameboardOccurrence.get(i)%2 == 0){
                for (int l = 0; l < gameboardOccurrence.get(i); l++){

                    index = gameboard.indexOf(Integer.valueOf(i));
                    gameboard.set(index,0);
                }

            // If an item is in an odd quantity: set all appearances to 0 but leave one appearance
            } else {
                for (int l = 0; l < gameboardOccurrence.get(i) -1; l++){
                    index = gameboard.indexOf(Integer.valueOf(i));
                    gameboard.set(index,0);
                }
            }
        }

        // Remove losing known situations
        for (int i = 0 ; i < gameboard.size() ; i++){
            // Get elements one by one 
            tmp.clear();
            tmp.add(gameboard.get(i));

            // Create it's occurrenceTable to compare
            tmpOccurrence = occurrenceTable(tmp);

            // Compare with known losing arrangements
            for (int j = 0; j < losingArray.size(); j++){

                // If the element is a losing one, then add it to intersect
                if (tmpOccurrence.equals(losingArray.get(j))){

                    intersect.add(gameboard.get(i)); 
                } 
            }
        }

        // Set 0 to every gameboard element if it's present in intersect
        for (int k = 0; k < gameboard.size(); k++){
            for (int m = 0; m < intersect.size(); m++){
                index = gameboard.indexOf((intersect.get(m)));

                // Avoid out of bound errors and set 0
                if (index != -1){
                    gameboard.set(index,0);
                }
                
            }
        }
    }


    /**
     * This method is used to test a given case of the deleteLosingKnownElements function
     * @param gameboard the gameboard
     * @param losingArray the ArrayList of losing arrangments (by occurrence : see occurrenceTable())
     * @param expected Expected result
     */
    void testCaseDeleteLosingKnownElements(ArrayList<Integer> gameboard, ArrayList<ArrayList<Integer>> losingArray,ArrayList<Integer>expected ){
        
        // Display infos
        System.out.println(" *** Testing ...");
        System.out.println("Gameboard : " + gameboard);
        System.out.println("LosingArray : " + losingArray);
        System.out.println("Expected : " + expected);
        
        // Execute method
        deleteLosingKnownElements(gameboard, losingArray);

        // Display output
        System.out.println("New gameboard: " + gameboard);

        // comparing with expected result
        if (expected.equals(gameboard)){
            System.out.println("OK !");
        } else {
            System.out.println("ERROR");
        }
        

    }

    /**
     * This method is used to test the deleteLosingKnownElements function
     */
    void testDeleteLosingKnownElements(){
        // normal case
        ArrayList<Integer> gameboard = new ArrayList<>(Arrays.asList(1, 2,3,3,3, 4, 5, 6, 7, 8, 9));
        ArrayList<Integer> losingElt1 = new ArrayList<>(Arrays.asList(0,0,0));
        ArrayList<ArrayList<Integer>> losingArray = new ArrayList<>();
        losingArray.add(losingElt1);
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(0,0,0,0,3,4,5, 6, 7, 8, 9));
        testCaseDeleteLosingKnownElements(gameboard, losingArray, expected);


        // losingArray is empty case
        ArrayList<Integer> gameboard2 = new ArrayList<>(Arrays.asList(0,1,2,3,3,3, 4, 5, 6, 7, 8, 9));
        ArrayList<ArrayList<Integer>> losingArray2 = new ArrayList<>();
        ArrayList<Integer> expected2 = new ArrayList<>(Arrays.asList(0,1,2,0,0,3, 4, 5, 6, 7, 8, 9));

        testCaseDeleteLosingKnownElements(gameboard2, losingArray2, expected2);

        // losingArray as some elements case
        ArrayList<Integer> gameboard3 = new ArrayList<>(Arrays.asList(0,1,2,3,3,3, 4, 5, 6, 7, 8, 9,10,10,10));
        ArrayList<ArrayList<Integer>> losingArray3 = new ArrayList<>();
        ArrayList<Integer> losingElt2 = new ArrayList<>(Arrays.asList(0,0,0));
        ArrayList<Integer> losingElt4 = new ArrayList<>(Arrays.asList(0,0,0,0,1));
        ArrayList<Integer> losingElt7 = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,1));
        losingArray3.add(losingElt2);
        losingArray3.add(losingElt4);
        losingArray3.add(losingElt7);
        ArrayList<Integer> expected3 = new ArrayList<>(Arrays.asList(0,0,0,0,0,3, 0, 5, 6, 0, 8, 9,0,0,10));
        testCaseDeleteLosingKnownElements(gameboard3, losingArray3, expected3);
    }


    /**
     * Returns an array of integers representing the number of occurrences
     * of each line size in the gameboard.
     * The first 3 index are equals to 0, because lines with 0 stick doesn't exists
     * line with 1 et 2 sticks are useless 
     * @param gameboard the gameboard
     * @return an array of integers representing the number of occurrences of each line size in the gameboard
     */
    ArrayList<Integer> occurrenceTable(ArrayList<Integer> gameboard) {
        // null check
        if (gameboard == null) {
            return null;
        }
        // Getting max value (I observed that that max value of ArrayList([1]) is 2, i don't know why)
        int max = Collections.max(gameboard);
        ArrayList<Integer> occurrences = new ArrayList<>();

        // avoid out of bound errors
        if (max < 2){
            max = 2;
        }
        // Pre-sizing occurrence table
        for (int i = 0; i < max + 1; i++) {
            occurrences.add(0);
        }
        // Incrementing occurrence table
        for (int i = 0; i < gameboard.size(); i++) {
            occurrences.set(gameboard.get(i), occurrences.get(gameboard.get(i)) + 1);
        }
        // Erasing index 1,2 because 2 stick lines doesn't affect anything
        for (int i = 0; i < 3; i++) {
            occurrences.set(i, 0);
        }
        return occurrences;
    }



    /**
     * Test effectiveness of jouerGagnant() method from counter and time
     * Implemented in v0
     * Add print(perdantes.size()) in v1
     * Add print(gagnantes.size()) in v2
     */
    void testJouerGagnantEff() {
        System.out.println(" *** Testing Effectiveness of jouerGagnant() v1 method");
        ArrayList<Integer> gameboard = new ArrayList<Integer>();   // The Gameboard
        double startTime;               // Current time before calling jouerGagnant()
        double endTime;                 // Current time after calling jouerGagnant()
        double totalTime;               // endTime - startTime
        boolean result;                 // Is a playable move found or not
        int stickNB = 3;                
        while (true){
            // Variable initialization before calling jouerGagnant()
            gameboard.clear();;
            gameboard.add(stickNB);
            cpt = 0;
            System.out.println("Size : " + stickNB);
            // Get time
            startTime = System.currentTimeMillis();
            // Call method
            result = jouerGagnant(gameboard);
            // Get time
            endTime = System.currentTimeMillis();
            // Get total time
            totalTime = endTime - startTime;
            
            // Display informations
            if (result == true){
                System.out.println("Winnable move found");
            } else {
                System.out.println("Not any winnable move found");
            }
            System.out.println("Counter : " + (int)cpt);
            System.out.println("Total Time : " +(int)totalTime + "ms");
            System.out.println("Perdantes size : " + perdantes.size());
            System.out.println("Gagnantes size : " + gagnantes.size());
            System.out.println();
            // Add 1 to gameboard size
            stickNB++;

        }
    }



     /**
     * This method is use to compare of the given arrangement of the gameboard is already
     * store in the winning ArrayList. If the arrangement is found, it returns the occurrence table
     * else, it returns null
     * @param gameboard
     * @param winningArray i.e. ArrayList perdantes
     * @return 
     */
    ArrayList<Integer> isFoundInWinningArrayList(ArrayList<Integer> gameboard, ArrayList<ArrayList<Integer>> winningArray){
        ArrayList<Integer> result = null;
        ArrayList<Integer> occurrence = occurrenceTable(gameboard);
        for (int i = 0 ; i < winningArray.size() && result == null ; i++ ){
            if (winningArray.get(i).equals(occurrence)){
                result = occurrence;
            }
        }
        return result;
    }

    /**
     * Testing a Unique case : IsFoundInWinningArrayList()
     * @param gameboard
     * @param winningArray
     * @param expected
     */
    void testCaseIsFoundInWinningArrayList(ArrayList<Integer> gameboard, ArrayList<ArrayList<Integer>> winningArray, ArrayList<Integer> expected){
        System.out.println(" *** Testing ...");
        System.out.println("Gameboard : " + gameboard);
        System.out.println("winningArray : " + winningArray);
        System.out.println("Expected : " + expected);
        if (expected == null){
            if ( isFoundInWinningArrayList(gameboard, winningArray) == expected){
                System.out.println("OK !");
            } else {
                System.err.println("ERROR in isFoundInWinningArrayList()");
            }
        }else{
            if (expected.equals(isFoundInWinningArrayList(gameboard, winningArray))){
                System.out.println("OK !");
            } else {
                System.err.println("ERROR in isFoundInWinningArrayList()");
            }
        }
        
    }

    /**
     * Testing IsFoundInWinningArrayList()
     */
    void testIsFoundInWinningArrayList(){
        System.out.println(" *** Testing IsFoundInWinningArrayList()");
        ArrayList<ArrayList<Integer>> winningArrayList = new ArrayList<>(); 
        ArrayList<Integer> winningArrayListElt = new ArrayList<>();
        winningArrayListElt.add(0);
        winningArrayListElt.add(0);
        winningArrayListElt.add(0);
        winningArrayListElt.add(3);
        winningArrayList.add(winningArrayListElt);
        ArrayList<Integer> gameboard = new ArrayList<>();
        gameboard.add(3);
        gameboard.add(3);
        gameboard.add(3);
        ArrayList<Integer> expected1 = occurrenceTable(gameboard);
        testCaseIsFoundInWinningArrayList(gameboard, winningArrayList, expected1);

        ArrayList<ArrayList<Integer>> winningArrayList2 = new ArrayList<>(); 
        ArrayList<Integer> gameboard2 = new ArrayList<>();
        gameboard2.add(2);
        gameboard2.add(4);
        gameboard2.add(10);
        ArrayList<Integer> expected2 = null;
        testCaseIsFoundInWinningArrayList(gameboard2, winningArrayList2, expected2);
    }









    



    /**
     * Tests the occurrenceTable method with a given gameboard and expected result.
     *
     * @param gameboard the gameboard to test
     * @param expected the expected result for the given gameboard
     */
    void testCaseOccurenceTable(ArrayList<Integer> gameboard, ArrayList<Integer> expected){
        System.out.println(" *** testCaseOccurenceTable");
        System.out.println("Gamboard :" + gameboard + "\t expected result " + expected);
        ArrayList<Integer> result = occurrenceTable(gameboard);
        System.out.println("Result : " + result);
        if (expected.equals(result)){
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
        }
    }



    /**
     * Tests the occurrenceTable method with a predefined gameboard and expected result.
     */
    void testOccurenceTable(){
        ArrayList<Integer> gameboard = new ArrayList<>();
        gameboard.add(5);
        gameboard.add(2);
        gameboard.add(5);
        gameboard.add(3);
        gameboard.add(5);
        gameboard.add(1);
        ArrayList<Integer> expectedResult1 = new ArrayList<>();
        expectedResult1.add(0);
        expectedResult1.add(0);
        expectedResult1.add(0);
        expectedResult1.add(1);
        expectedResult1.add(0);
        expectedResult1.add(3);
        testCaseOccurenceTable(gameboard, expectedResult1);
    }

    /**
     * This method is use to compare of the given arrangement of the gameboard is already
     * store in the losing ArrayList. If the arrangement is found, it returns the occurrence table
     * else, it returns null
     * @param gameboard
     * @param losingArray i.e. ArrayList perdantes
     * @return 
     */
    ArrayList<Integer> isFoundInLosingArrayList(ArrayList<Integer> gameboard, ArrayList<ArrayList<Integer>> losingArray){
        ArrayList<Integer> result = null;
        ArrayList<Integer> occurrence = occurrenceTable(gameboard);
        for (int i = 0 ; i < losingArray.size() && result == null ; i++ ){
            if (losingArray.get(i).equals(occurrence)){
                result = occurrence;
            }
        }
        return result;
    }

    /**
     * @param gameboard
     * @param losingArray
     * @param expected
     */
    void testCaseIsFoundInLosingArrayList(ArrayList<Integer> gameboard, ArrayList<ArrayList<Integer>> losingArray, ArrayList<Integer> expected){
        System.out.println(" *** Testing ...");
        System.out.println("Gameboard : " + gameboard);
        System.out.println("losingArray : " + losingArray);
        System.out.println("Expected : " + expected);
        if (expected == null){
            if ( isFoundInLosingArrayList(gameboard, losingArray) == expected){
                System.out.println("OK !");
            } else {
                System.err.println("ERROR in isFoundInLosingArrayList()");
            }
        }else{
            if (expected.equals(isFoundInLosingArrayList(gameboard, losingArray))){
                System.out.println("OK !");
            } else {
                System.err.println("ERROR in isFoundInLosingArrayList()");
            }
        }
        
    }

    /**
     * 
     */
    void testIsFoundInLosingArrayList(){
        System.out.println(" *** Testing IsFoundInLosingArrayList()");
        ArrayList<ArrayList<Integer>> losingArrayList = new ArrayList<>(); 
        ArrayList<Integer> losingArrayListElt = new ArrayList<>();
        losingArrayListElt.add(0);
        losingArrayListElt.add(0);
        losingArrayListElt.add(0);
        losingArrayListElt.add(3);
        losingArrayList.add(losingArrayListElt);
        ArrayList<Integer> gameboard = new ArrayList<>();
        gameboard.add(3);
        gameboard.add(3);
        gameboard.add(3);
        ArrayList<Integer> expected1 = occurrenceTable(gameboard);
        testCaseIsFoundInLosingArrayList(gameboard, losingArrayList, expected1);

        ArrayList<ArrayList<Integer>> losingArrayList2 = new ArrayList<>(); 
        ArrayList<Integer> gameboard2 = new ArrayList<>();
        gameboard2.add(2);
        gameboard2.add(4);
        gameboard2.add(10);
        ArrayList<Integer> expected2 = null;
        testCaseIsFoundInLosingArrayList(gameboard2, losingArrayList2, expected2);
    }









	/**
     * Indique si la configuration est gagnante.
	 * Méthode qui appelle simplement "estPerdante".
     * 
     * @param jeu plateau de jeu
     * @return vrai si la configuration est gagnante, faux sinon
     */
    boolean estGagnante(ArrayList<Integer> jeu) {
        boolean ret = false;
        if (jeu == null) {
            System.err.println("estGagnante(): le paramètre jeu est null");
        } else {
            ret = !estPerdante(jeu);
        }
        return ret;
    }











    /**
     * This method allows a player to play Grundy game against an IA
     * Implemented in version0
     */
    void playAgainstAI(){
        System.out.println();
        System.out.println(" ** Game is starting ... ** ");
        int stickQuantity;      // Store the size of the first line when starting the game
        int lineNB;             // Store line index which player want to split
        int stickNB;            // Store stick quantity which player want to split
        int divideQuantity = 0; //Store the number of active lines in the gameboard
        int current_player;     // store 0 if current player is computer and 1 if it's the player
        // Define player username
        String player1 = SimpleInput.getString("Username : ");
        // Define size of the gameboard
        do {stickQuantity = SimpleInput.getInt("Enter the number of sticks (must be superior to 3) : ");
        } while(stickQuantity < 3);
        // Create Gameboard
        ArrayList<Integer> gameboard = new ArrayList<Integer>();
        gameboard.add(stickQuantity);
        // Define who is starting
        do {current_player = SimpleInput.getInt("who is starting ? (0:Computer | 1:" + player1 + ") : ");
        } while (current_player > 2 || current_player < 0);
        // Launching the game
        while (estPossible(gameboard)){
            // Display the Gameboard
            display(gameboard);
            // Player turn's
            if (current_player == 1){
                System.out.println(player1 + "'s turn");
                // Ask for the line player want to play and verify if the line exists and is playable
                do {lineNB = SimpleInput.getInt("Enter line number : ");
                } while (lineNB < 0 || lineNB > divideQuantity || gameboard.get(lineNB) <= 2);
                // Asks for the number of sticks the player want to split and verify the possibility
                do {stickNB = SimpleInput.getInt("Enter stick number you want to split : ");
              } while (stickNB < 1 || stickNB >= gameboard.get(lineNB) || 2*stickNB == gameboard.get(lineNB) ); 
              // Split the line
              enlever(gameboard, lineNB, stickNB);
            } 
            // AI turn's
            else {
                System.out.println("Computer turn's");
                // if it's possible, play a winner move, else play randomly
                if (jouerGagnant(gameboard) == false){
                    int[] move = {0,0}; // Store the random move {LineNB, StickNB}
                    // Select a playable line
                    while (gameboard.get(move[0]) <= 2){
                        move[0]++;
                    }
                    // Select a random playable stick number in the selected line
                    while (move[1] < 1 || move[1] > gameboard.get(move[0])-1 || gameboard.get(move[0])-move[1] == move[1]){
                        move[1] = (int) (Math.random()*gameboard.get(move[0])-1);
                    }
                    enlever(gameboard, move[0], move[1]);
                }
            }
            // Changing player turn's
            current_player = 1 - current_player;
            // Adding an active line
            divideQuantity++;
        }
        // When gameboard isn't playable anymore
        System.out.println("Final gameboard : ");
        // Display gameboard
        display(gameboard);
        // Display the winner
        System.out.println();
        System.out.println("Game closed !");
        if (current_player == 0){
            System.out.println("**********\t     YOU WON       \t**********");
            System.out.println("**********\t Congrats " + player1 + " !!  \t**********");
        } else {
            System.out.println("********** YOU LOSE ! COMPUTER WON **********");
        }
    }



   


    /**
     * Play three games against the AI to test method playAgainstTheAI()
     */
    void testPlayAgainstTheAI(){
        System.out.println(" *** Testing playAgainsTheAI()");
        for (int i = 0; i < 3 ; i++){
            playAgainstAI();
        }
    }


    /**
     * Displays the gameboard from an ArrayList<Integer>
     * @param gameboard the gameboard
     */
    void display(ArrayList<Integer> gameboard){
        // Avoid errors
        if (gameboard == null || gameboard.equals(new ArrayList<Integer>())){
            System.err.println("ERROR : display() the given gameboard is empty");
        } else {

            // For every element in gameboard 
            // Print line index
            // Print "|" as much as the index value
            for( int i = 0 ; i < gameboard.size() ; i++){
                System.out.print(i +" : ");
                for ( int j = 0 ; j < gameboard.get(i); j++){
                    System.out.print("| ");
                }
            System.out.println();
            }
        }
        System.out.println();
    }

    /**
     * Concise tests of the display() method
     */
    void testDisplay(){
        System.out.println(" *** testDisplay()");
        ArrayList<Integer> gameboard = new ArrayList<>();
        gameboard.add(3);
        gameboard.add(4);
        gameboard.add(2);
        display(gameboard);
        ArrayList<Integer> gameboard2 = new ArrayList<>();
        gameboard2.add(1);
        gameboard2.add(2);
        gameboard2.add(3);
        gameboard2.add(4);
        gameboard2.add(5);
        display(gameboard2);
        System.out.println("The following should be an error statement : ");
        ArrayList<Integer> gameboard3 = new ArrayList<>();
        display(gameboard3);

    }


    /**
     * Joue le coup gagnant s'il existe
     * 
     * @param jeu plateau de jeu
     * @return vrai s'il y a un coup gagnant, faux sinon
     */
    boolean jouerGagnant(ArrayList<Integer> jeu) {
        boolean gagnant = false; 
        if (jeu == null) {
            System.err.println("suivant(): le paramètre jeu est null");
        } else {
            ArrayList<Integer> essai = new ArrayList<Integer>();
            int ligne = premier(jeu, essai);
			// mise en oeuvre de la règle numéro2
			// Une situation (ou position) est dite gagnante pour la machine (ou le joueur, peu importe), s’il existe AU MOINS UNE 
			// décomposition (c-à-d UNE action qui consiste à décomposer un tas en 2 tas inégaux) perdante pour l’adversaire.
            while (ligne != -1 && !gagnant) {
                if (estPerdante(essai)) {
                    jeu.clear();
                    gagnant = true;
                    for (int i = 0; i < essai.size(); i++) {
                        jeu.add(essai.get(i));
                    }
                } else {
                    ligne = suivant(jeu, essai, ligne);
                }
            }
        }
        return gagnant;
    }


    


   

    /**
     * Tests succincts de la méthode joueurGagnant()
     */
    void testJouerGagnant() {
        System.out.println();
        System.out.println("*** testJouerGagnant() ***");

        System.out.println("Test des cas normaux");
        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(6);
        ArrayList<Integer> resJeu1 = new ArrayList<Integer>();
        resJeu1.add(4);
        resJeu1.add(2);
        testCasJouerGagnant(jeu1, resJeu1, true);


        
    }

    /**
     * Test d'un cas de la méthode jouerGagnant()
	 *
	 * @param jeu le plateau de jeu
	 * @param resJeu le plateau de jeu après avoir joué gagnant
	 * @param res le résultat attendu par jouerGagnant
     */
    void testCasJouerGagnant(ArrayList<Integer> jeu, ArrayList<Integer> resJeu, boolean res) {
        // Arrange
        System.out.println("jouerGagnant (" + jeu.toString() + ") : ");
        System.out.println("Attentes : " + resJeu.toString());
        // Act

        boolean resExec = jouerGagnant(jeu);
        System.out.println("Sortie : " + jeu.toString());

        // Assert
        System.out.print(jeu.toString() + " " + resExec + " : ");
        if (jeu.equals(resJeu) && res == resExec) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERREUR\n");
        }
    }	

    /**
     * Divise en deux tas les alumettes d'une ligne de jeu (1 ligne = 1 tas)
     * 
     * @param jeu   tableau des alumettes par ligne
     * @param ligne ligne (tas) sur laquelle les alumettes doivent être séparées
     * @param nb    nombre d'alumettes RETIREE de la ligne après séparation
     */
    void enlever ( ArrayList<Integer> jeu, int ligne, int nb ) {
		// traitement des erreurs
        if (jeu == null) {
            System.err.println("enlever() : le paramètre jeu est null");
        } else if (ligne >= jeu.size()) {
            System.err.println("enlever() : le numéro de ligne est trop grand");
        } else if (nb >= jeu.get(ligne)) {
            System.err.println("enlever() : le nb d'allumettes à retirer est trop grand");
        } else if (nb <= 0) {
            System.err.println("enlever() : le nb d'allumettes à retirer est trop petit");
        } else if (2 * nb == jeu.get(ligne)) {
            System.err.println("enlever() : le nb d'allumettes à retirer est la moitié");
        } else {
			// nouveau tas (case) ajouté au jeu (nécessairement en fin de tableau)
			// ce nouveau tas contient le nbre d'allumettes retirées (nb) du tas à séparer			
            jeu.add(nb);
			// le tas restant avec "nb" allumettes en moins
            jeu.set(ligne, jeu.get(ligne) - nb);
        }
    }

    /**
     * Teste s'il est possible de séparer un des tas
     * 
     * @param jeu      plateau de jeu
     * @return vrai s'il existe au moins un tas de 3 allumettes ou plus, faux sinon
     */
    boolean estPossible(ArrayList<Integer> jeu) {
        boolean ret = false;
        if (jeu == null) {
            System.err.println("estPossible(): le paramètre jeu est null");
        } else {
            int i = 0;
            while (i < jeu.size() && !ret) {
                if (jeu.get(i) > 2) {
                    ret = true;
                }
                i = i + 1;
            }
        }
        return ret;
    }

    /**
     * Crée une toute première configuration d'essai à partir du jeu
     * 
     * @param jeu      plateau de jeu
     * @param jeuEssai nouvelle configuration du jeu
     * @return le numéro du tas divisé en deux ou (-1) si il n'y a pas de tas d'au moins 3 allumettes
     */
    int premier(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai) {
	
        int numTas = -1; // pas de tas à séparer par défaut
		int i;
		
        if (jeu == null) {
            System.err.println("premier(): le paramètre jeu est null");
        } else if (!estPossible((jeu)) ){
            System.err.println("premier(): aucun tas n'est divisible");
        } else if (jeuEssai == null) {
            System.err.println("estPossible(): le paramètre jeuEssai est null");
        } else {
            // avant la copie du jeu dans jeuEssai il y a un reset de jeuEssai 
            jeuEssai.clear();
            i = 0;
			
			// recopie case par case
			// jeuEssai est le même que le jeu au départ
            while (i < jeu.size()) {
                jeuEssai.add(jeu.get(i));
                i = i + 1;
            }
			
            i = 0;
			// rechercher un tas d'allumettes d'au moins 3 allumettes dans le jeu
			// sinon numTas = -1
			boolean trouve = false;
            while ( (i < jeu.size()) && !trouve) {
				
				// si on trouve un tas d'au moins 3 allumettes
				if ( jeuEssai.get(i) >= 3 ) {
					trouve = true;
					numTas = i;
				}
				
				i = i + 1;
            }
			
			// sépare le tas (case numTas) en un tas d'UNE SEULE allumette à la fin du tableau 
			// le tas en case numTas a diminué d'une allumette (retrait d'une allumette)
			// jeuEssai est le plateau de jeu qui fait apparaître cette séparation
            if ( numTas != -1 ) enlever ( jeuEssai, numTas, 1 );
        }
		
        return numTas;
    }

    /**
     * Tests succincts de la méthode premier()
     */
    void testPremier() {
        System.out.println();
        System.out.println("*** testPremier()");

        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(10);
        jeu1.add(11);
        int ligne1 = 0;
        ArrayList<Integer> res1 = new ArrayList<Integer>();
        res1.add(9);
        res1.add(11);
        res1.add(1);
        testCasPremier(jeu1, ligne1, res1);
    }

    /**
     * Test un cas de la méthode testPremier
	 * @param jeu le plateau de jeu
	 * @param ligne le numéro du tas séparé en premier
	 * @param res le plateau de jeu après une première séparation
     */
    void testCasPremier(ArrayList<Integer> jeu, int ligne, ArrayList<Integer> res) {
        // Arrange
        System.out.print("premier (" + jeu.toString() + ") : ");
        ArrayList<Integer> jeuEssai = new ArrayList<Integer>();
        // Act
        int noLigne = premier(jeu, jeuEssai);
        // Assert
        System.out.println("\nnoLigne = " + noLigne + " jeuEssai = " + jeuEssai.toString());
        if (jeuEssai.equals(res) && noLigne == ligne) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERREUR\n");
        }
    }

    /**
     * Génère la configuration d'essai suivante (c'est-à-dire UNE décomposition possible)
     * 
     * @param jeu      plateau de jeu
     * @param jeuEssai configuration d'essai du jeu après séparation
     * @param ligne    le numéro du tas qui est le dernier à avoir été séparé
     * @return le numéro du tas divisé en deux pour la nouvelle configuration, -1 si plus aucune décomposition n'est possible
     */
    int suivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne) {
	
        // System.out.println("suivant(" + jeu.toString() + ", " +jeuEssai.toString() +
        // ", " + ligne + ") = ");
		
		int numTas = -1; // par défaut il n'y a plus de décomposition possible
		
        int i = 0;
		// traitement des erreurs
        if (jeu == null) {
            System.err.println("suivant(): le paramètre jeu est null");
        } else if (jeuEssai == null) {
            System.err.println("suivant() : le paramètre jeuEssai est null");
        } else if (ligne >= jeu.size()) {
            System.err.println("estPossible(): le paramètre ligne est trop grand");
        }
		
		else {
		
			int nbAllumEnLigne = jeuEssai.get(ligne);
			int nbAllDernCase = jeuEssai.get(jeuEssai.size() - 1);
			
			// si sur la même ligne (passée en paramètre) on peut encore retirer des allumettes,
			// c-à-d si l'écart entre le nombre d'allumettes sur cette ligne et
			// le nombre d'allumettes en fin de tableau est > 2, alors on retire encore
			// 1 allumette sur cette ligne et on ajoute 1 allumette en dernière case		
            if ( (nbAllumEnLigne - nbAllDernCase) > 2 ) {
                jeuEssai.set ( ligne, (nbAllumEnLigne - 1) );
                jeuEssai.set ( jeuEssai.size() - 1, (nbAllDernCase + 1) );
                numTas = ligne;
            } 
			
			// sinon il faut examiner le tas (ligne) suivant du jeu pour éventuellement le décomposer
			// on recrée une nouvelle configuration d'essai identique au plateau de jeu
			else {
                // copie du jeu dans JeuEssai
                jeuEssai.clear();
                for (i = 0; i < jeu.size(); i++) {
                    jeuEssai.add(jeu.get(i));
                }
				
                boolean separation = false;
                i = ligne + 1; // tas suivant
				// si il y a encore un tas et qu'il contient au moins 3 allumettes
				// alors on effectue une première séparation en enlevant 1 allumette
                while ( i < jeuEssai.size() && !separation ) {
					// le tas doit faire minimum 3 allumettes
                    if ( jeu.get(i) > 2 ) {
                        separation = true;
						// on commence par enlever 1 allumette à ce tas
                        enlever(jeuEssai, i, 1);
						numTas = i;
                    } else {
                        i = i + 1;
                    }
                }				
            }
        }
		
        return numTas;
    }

    /**
     * Tests succincts de la méthode suivant()
     */
    void testSuivant() {
        System.out.println();
        System.out.println("*** testSuivant() ****");

        int ligne1 = 0;
        int resLigne1 = 0;
        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(10);
        ArrayList<Integer> jeuEssai1 = new ArrayList<Integer>();
        jeuEssai1.add(9);
        jeuEssai1.add(1);
        ArrayList<Integer> res1 = new ArrayList<Integer>();
        res1.add(8);
        res1.add(2);
        testCasSuivant(jeu1, jeuEssai1, ligne1, res1, resLigne1);

        int ligne2 = 0;
        int resLigne2 = -1;
        ArrayList<Integer> jeu2 = new ArrayList<Integer>();
        jeu2.add(10);
        ArrayList<Integer> jeuEssai2 = new ArrayList<Integer>();
        jeuEssai2.add(6);
        jeuEssai2.add(4);
        ArrayList<Integer> res2 = new ArrayList<Integer>();
        res2.add(10);
        testCasSuivant(jeu2, jeuEssai2, ligne2, res2, resLigne2);

        int ligne3 = 1;
        int resLigne3 = 1;
        ArrayList<Integer> jeu3 = new ArrayList<Integer>();
        jeu3.add(4);
        jeu3.add(6);
        jeu3.add(3);
        ArrayList<Integer> jeuEssai3 = new ArrayList<Integer>();
        jeuEssai3.add(4);
        jeuEssai3.add(5);
        jeuEssai3.add(3);
        jeuEssai3.add(1);
        ArrayList<Integer> res3 = new ArrayList<Integer>();
        res3.add(4);
        res3.add(4);
        res3.add(3);
        res3.add(2);
        testCasSuivant(jeu3, jeuEssai3, ligne3, res3, resLigne3);

    }

    /**
     * Test un cas de la méthode suivant
	 * 
	 * @param jeu le plateau de jeu
	 * @param jeuEssai le plateau de jeu obtenu après avoir séparé un tas
	 * @param ligne le numéro du tas qui est le dernier à avoir été séparé
	 * @param resJeu est le jeuEssai attendu après séparation
	 * @param resLigne est le numéro attendu du tas qui est séparé
     */
    void testCasSuivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne, ArrayList<Integer> resJeu, int resLigne) {
        // Arrange
        System.out.print("suivant (" + jeu.toString() + ", " + jeuEssai.toString() + ", " + ligne + ") : ");
        // Act
        int noLigne = suivant(jeu, jeuEssai, ligne);
        // Assert
        System.out.println("\nnoLigne = " + noLigne + " jeuEssai = " + jeuEssai.toString());
        if (jeuEssai.equals(resJeu) && noLigne == resLigne) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERREUR\n");
        }
    }

}
