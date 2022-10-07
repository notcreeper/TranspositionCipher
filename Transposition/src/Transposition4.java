
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class Transposition4{
    /**
     * Constructeur
     * @param deEncryptionKey       Représente soit la file de cryptage soit celle de décryptage
     * @param textToEncrypt         Représente soit le texte à crypter soit le texte à décrypter
     * @param answer                Réponse de l'utilisateur à la question voulez-vous crypter ou décrypter
     */
    public Transposition4(File deEncryptionKey, File textToEncrypt, String answer){
        try {
            Scanner text = new Scanner(textToEncrypt);                                // Va copier tout le texte dans un stringBuffer
            StringBuffer textBuffer= new StringBuffer();
            while (text.hasNextLine()){
                textBuffer.append(text.nextLine());
            }
            text.close();
            String newTextToEncrypt = new String(textBuffer);                         // Transforme le Stringbuffer du texte en simple String
            Scanner key = new Scanner(deEncryptionKey);                               // Va scanner le texte contenant une ou plusieurs clefs
            while (key.hasNextLine()){                                                // Pour chaque clef dans (EncryptionKey.txt ou DecryptionKey.txt) le texte va passer dans le systéme de cryptage                                      
                StringBuffer textEncrypt= UnStack(key.nextLine(), newTextToEncrypt);  // Crypte ou décrypte en fonction du type de clef
                newTextToEncrypt = new String(textEncrypt);                           // On prend le texte et on le remet dans la variable pour crypter ou décrypter
                System.out.println(newTextToEncrypt);                                 // On affiche le texte (crypter ou decrypter)
            }
            key.close();


            if (answer.equals("crypter")){                                            // Si le texte était à crypter alors le met dans le fichier textcrypter.txt
                File fileTextEncrypt = new File("./src/textEncrypt.txt");
                fileTextEncrypt.delete();
                fileTextEncrypt.createNewFile();
                PrintWriter writerTextEncrypt = new PrintWriter("./src/textEncrypt.txt", "UTF-8");
                System.out.println("Rentre le text crypter dans le fichier textEncrypt.txt");
                writerTextEncrypt.println(newTextToEncrypt);
                writerTextEncrypt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param key   Contient la clé de décryptage ou de cryptage
     * @param text  Contient le text
     * @return      Texte lineaire soit crypter ou soit decrypter
     */
    public StringBuffer UnStack(String key, String text){
        int lineLength = Integer.parseInt(key.substring(0,1));          // Prends la longueur qui est toujours en première position dans la clef de cryptage
        String columnChange = key.substring(2);                         // Prends ce qui suit derrière la longueur (les changements de colonnes)
        String[] textHash = stringHash(text,lineLength);                // Crée le hachage du texte en fonction de la longueur des lignes 
        textHash=changeColumn(textHash,lineLength,columnChange);        // Interverti les colonnes de chaque ligne. (sauf la derniere si elle est incomplete (texte < longueurdeligne))
        StringBuffer linearText = affichage(textHash);                  // Convertie le StringHacher en un seul String en ligne
        return linearText;
    }



    /**
     * Convertie le string en un tableau de string avec une longueur de string definit (paramètre longueur)
     * @param strText       Un String du Texte
     * @param lineLength    La longueur de ligne
     * @return              Retourne un tableau de string
     */
    String[] stringHash(String text, int lineLength){                                                    // Decoupe le string en plusieurs lignes (en fonction de la longueur de ligne)
        String [] textHash = new String[(int) Math.ceil((double) text.length()/(double) lineLength)];    // Créer un tableau de string avec une longueur correspondant à la (longueurdutext/longueurdelignevoulue) arrondit au supérieur
        int startInLine=0;                                                                                  // Début du decoupage                     
        for(int stringNumber=0; stringNumber < Math.ceil((double) text.length()/(double) lineLength) ; stringNumber++)
        {
            if ((startInLine+lineLength)>text.length()){                                                 // Si le début de découpage + longueur sont supérieur a la longueur du texte
                textHash[stringNumber]= text.substring(startInLine, text.length());                   // Prends la dernière ligne avec sa longueur actuelle (même si elle a moins de lettre que la ligne voulue (exemple: "montext" longueur voulue 3 "mon" "tex" "t" )
            }
            else{                                                                                           // Sinon  
                textHash[stringNumber] = text.substring(startInLine, startInLine+lineLength) ;           // Effectue coupe de la bonne longueur 
                startInLine=startInLine+lineLength;                                                         // Incrémente le début
            }
            //System.out.println(textHash[etage]);
        }
        return textHash;
    }

    /**
     * Interverti les colonnes entre elles
     * @param StringHacher      Un tableau de String (Le texte sous forme de colonnes)
     * @param lineLength        La longueur de ligne par colonnes
     * @param columnChange      Les changements de colonnes 
     * @return                  Le texte sous forme de tableau (les colonnes du tableau sont inversés) 
     */
    public String[] changeColumn(String[] stringHash,int lineLength, String columnChange) {        
        for (int i=0 ; i<stringHash.length;i++) {                                                                   // Pour chaque ligne
            StringBuffer changedColumn= new StringBuffer();                                 
            if (stringHash[i].length()==lineLength){                                                                // Si la longueur de la ligne correspond a la longueur du cryptage
                for (int numerocharactere = 0; numerocharactere < stringHash[i].length(); numerocharactere++) {     // Pour chaque element de la ligne intervertissent les colonnes
                    int interverti = Integer.parseInt(columnChange.substring(numerocharactere,numerocharactere+1)); // Interverti en prenant le premier caractère des changements
                    changedColumn.append(stringHash[i].substring(interverti-1,interverti));                         // Interverti le premier caractère et ainsi de suite pour chaque caractère de la ligne
                }
            }
            else {                                                                                                  // Sinon n'intervertit rien, incrémente juste
                changedColumn.append(stringHash[i]);                                      
            }
            //System.out.println(colonechanger);
            stringHash[i]= new String(changedColumn);
        }
        return stringHash;
    }

    /**
     * Convertie un tableau en texte en ligne
     * @param StringHacher  Un tableau de string
     * @return              Sort un Stringbuffer (le texte en ligne)
     */
    public StringBuffer affichage(String[] StringHacher){                                        
        StringBuffer linearText= new StringBuffer();
        for (int i = 0; i < StringHacher.length; i++) {
            linearText.append(StringHacher[i]);
        }
        return linearText;
    }




    public static void main(String[] args) {
        try {
            Scanner encryptionOrDecryption = new Scanner(System.in);                    // Demande a l'utilisateur si il veut crypter ou décrypter
            System.out.println("voulez vous crypter ou decrypter (par defaut)");
            String answer = encryptionOrDecryption.nextLine();
            File deEncryptionKey;
            File textToEncrypt;
            if (answer.equals("crypter")){
                deEncryptionKey = new File("./src/EncryptionKey.txt");       // Clef de cryptage
                textToEncrypt = new File("./src/text.txt");                  // Texte a crypter
            }
            else {
                deEncryptionKey = new File("./src/DecryptionKey.txt");       // Clef de decryptage
                textToEncrypt = new File("./src/textEncrypt.txt");           // Texte a décrypter
            }
            encryptionOrDecryption.close();
            new Transposition4(deEncryptionKey, textToEncrypt, answer);                 // Decrypte ou crypte le texte

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
