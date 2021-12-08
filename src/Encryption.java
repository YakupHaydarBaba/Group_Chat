import java.io.Serializable;

public class Encryption implements Serializable {
    //basic Ceaser chiper encryption
    public String encrypt(String string){
        if (string != null) {
            char[] clearText = string.toCharArray();
            char[] chiper = new char[clearText.length];
            for (int i = 0; i < clearText.length; i++) {
                chiper[i] = (char) (clearText[i] + 6);
            }
            return String.valueOf(chiper);
        }
        return null;
    }

    public String decrypt(String string) {
        if (string != null) {
            char[] chiper = string.toCharArray();
            char[] clearText = new char[chiper.length];
            for (int i = 0; i < clearText.length; i++) {
                clearText[i] = (char) (chiper[i] - 6);
            }
            return String.valueOf(clearText);
        }
        return null;
    }


}