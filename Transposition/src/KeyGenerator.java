
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class KeyGenerator {
    /**
     * Constructeur
     * @param encryptionNumber  Nombre de clef de cryptage et de décryptage a créé.
     */
    public KeyGenerator(int encryptionNumber){
        try {
            File fileEncryptionKey = new File("./src/EncryptionKey.txt");    // Definit la position du fichier EncryptionKey.txt
            File fileDecryptionKey = new File("./src/DecryptionKey.txt");    // Definit la position du fichier DecryptionKey.txt
            fileEncryptionKey.delete();                                                 // Le supprime s'il existe
            fileEncryptionKey.createNewFile();                                          // Crée le fichier EncryptionKey.txt
            fileDecryptionKey.delete();                                                 // Le supprime s'il existe
            fileDecryptionKey.createNewFile();                                          // Crée le fichier DecryptionKey.txt
            KeyStack(encryptionNumber);                                                 // Empile les clefs de cryptage dans les fichiers
        }catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Empiler les clefs de cryptage et de décryptage dans les fichiers
     * @param encryptionNumber  Nombre de clefs de cryptage a crée
     */
    public void KeyStack(int encryptionNumber){
        PrintWriter writerEncryption;                                                               // Créé l'élément writerEncryption permettant d'écrire dans le fichier
        PrintWriter writerDecryptionKey;                                                            // Créé l'élément writerDecryptionKey permettant d'écrire dans le fichier
        try {
            writerEncryption = new PrintWriter("./src/EncryptionKey.txt", "UTF-8");      // Definit l'emplacement du fichier 
            writerDecryptionKey = new PrintWriter("./src/DecryptionKey.txt", "UTF-8");   // Definit l'emplacement du fichier 
            String[] reverseDecryptionKeyInFile= new String[encryptionNumber];                      // Crée un tableau string pouvant contenir un certain nombre de clef
            for (int i = 0; i < encryptionNumber; i++) {                                            // Pour chaque clef a créé 
                StringBuffer encryptionKey= CreateEncryptionKey();                                  // Crée la clef de cryptage
                StringBuffer DecryptionKey= CreateDecryptionKey(encryptionKey);                     // Crée la clef de décryptage en fonction de la clef de cryptage
                writerEncryption.println(encryptionKey);                                            // Ecrit la clef dans le fichier de cryptage
                reverseDecryptionKeyInFile[i]=new String(DecryptionKey);                            // Enregistre la clef dans le tableau de clef de décryptage 
            }
            for (int i = 1; i <= reverseDecryptionKeyInFile.length; i++) {                          // Pour chaque clef dans le tableau de décryptage
                writerDecryptionKey.println(reverseDecryptionKeyInFile[reverseDecryptionKeyInFile.length-i]);   // Ecrit les clefs de décriptage en partant de la derniere dans de tableau reverseDecryptionKeyInFile
            }
            writerEncryption.close();                                                               // ferme l'élément permettant d'écrire dans le fichier
            writerDecryptionKey.close();                                                            // ferme l'élément permettant d'écrire dans le fichier
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }   
    }

    /**
     * Crée la clef de cryptage
     * @return  La clef de cryptage en stringBuffer
     */
    public StringBuffer CreateEncryptionKey(){
        int lineLength=(int) (Math.random()*7)+3;                           // Definit la longueur de ligne (un nombre entre 3 et 9)
        StringBuffer crypte= new StringBuffer();                            
        int aleatoire= (int) ((Math.random()*lineLength)+1);                // Nombre aléatoire entre 1 et la longueur de ligne              
        for (int i = 0; i < lineLength; i++) {                              // Pour chaque caractère dans la longueur de ligne, definit un changement de colonnes
            do {                                                            // Tant que le chiffre aléatoire est dans le string crypte
                Math.random();
                aleatoire= (int) ((Math.random()*lineLength)+1);            // Nombre aléatoire entre 1 et la longueur de ligne
            } while (crypte.indexOf(""+aleatoire)!=-1);             
            crypte.append(aleatoire);                                       // Ajoute aleatoire dans le string
        }
        StringBuffer crypte2= new StringBuffer(lineLength+" "+crypte);      // Ajoute la longueur de ligne avant le cryptage des colonnes 
        System.out.println("clef de cryptage= "+crypte2);                   
        return crypte2;                                                     // Retourne la clef de cryptage
    }



    /**
     * Crée la clef de décryptage en fonction de la clef de cryptage
     * @param encryptionKey     La clef de cryptage
     * @return                  La clef de décryptage en stringBuffer
     */
    public StringBuffer CreateDecryptionKey(StringBuffer encryptionKey){    
        StringBuffer decryptionKey= new StringBuffer();                         // Crée le stringBuffer
        int lineLength = Integer.parseInt(encryptionKey.substring(0,1));        // La longueur de ligne est le premier caractère de la clef de cryptage 
        decryptionKey.append(lineLength+" ");                                   // La longueur de ligne est aussi le premier caractère de la clef de décryptage  (on l'ajoute au stringBuffer)
        String encryptionKey2 = new String(encryptionKey.substring(2));         // Tout ce qui est après le deuxième caractère représente les changements de colonnes dans le texte
        for (int colonne = 1; colonne <= encryptionKey2.length(); colonne++) {  // De la première colonne jusqu'au maxcolonne
            decryptionKey.append(encryptionKey2.indexOf(""+colonne)+1);         // Ajoute la position de la colonne dans la clef de décryptage
        }  // exemple clée de cryptage 4213 = clée de décryptage 3241 [index 3 on a la colonne 1] [index 2 on a la colonne 2] [index 4 on a la colonne 3]  ect... 
        System.out.println("clef de decryptage= "+decryptionKey);                        
        return decryptionKey;                                                   // Retourne la clef de decryptage
    }




    public static void main(String[] args) {
        System.out.println("entrer le nombre de cryptage");
        int encryptionNumber=-1;
        Scanner mytxt = new Scanner(System.in);
        do{ 
            try {
                encryptionNumber = mytxt.nextInt();
                mytxt.nextLine();
            } catch (Exception e) {
                mytxt.nextLine();
            }
            if (encryptionNumber>0){
                mytxt.close();
            }
            else{
                System.out.println("merci de bien vouloir entrer un 'nombre positif' de cryptage");
            }
        }while(encryptionNumber<0);
        new KeyGenerator(encryptionNumber);
        
    }

}
