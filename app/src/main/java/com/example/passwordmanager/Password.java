package com.example.passwordmanager;

import java.util.Locale;
import java.util.Random;

public class Password {
    //  Password fields with default values
    private String pass;    // For alternate implementation, with password as a persistent field, if deciding to do so.
    private int length;
    private boolean allowMixedCase = true;
    private boolean allowNumbers = true;
    private boolean allowSpecialChars = true;
    private boolean allowExtraSpecialChars = true;

    //  Initialised password charsets fields
    private String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private String numbers = "1234567890";
    private String specialChars = "!£$%^&*()-_=+";
    private String extraSpecialChars = "[{]};:'@#~,<.>/?";

    //  Random object to allow for character generation
    Random ran = new Random();


    public Password(int length, boolean allowMixedCase, boolean allowNumbers, boolean allowSpecialChars, boolean allowExtraSpecialChars) {
        this.length = length;
        this.allowMixedCase = allowMixedCase;
        this.allowNumbers = allowNumbers;
        this.allowSpecialChars = allowSpecialChars;
        this.allowExtraSpecialChars = allowExtraSpecialChars;
    }


    public String generatePassword() {
        String components = generateComponents();
        String password = "";
        int counter = 0;

        while(counter < length){
            password += generateChar(components);
            counter++;
        }

        return password;
    }

    private String generateComponents() {
        String components = alphabet;

        // Add only characters from categories which have been approved.
        if(allowMixedCase){components += alphabet.toUpperCase(Locale.ROOT);}
        if(allowNumbers){components += numbers;}
        if(allowSpecialChars){components += specialChars;}
        if(allowExtraSpecialChars){components += extraSpecialChars;}

        return components;
    }


    private char generateChar(String charSeq) {
        int limit = charSeq.length() - 1;
        int index = ran.nextInt(limit+1);

        return charSeq.charAt(index);
    }



}
