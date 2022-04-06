package com.example.passwordmanager;

import java.util.Locale;
import java.util.Random;

/**
 * A password generator which allows a user to choose if they wish their password to have simply lower case letters, or if they wish to include upper case ones, numbers and special characters.
 */
public class Password {
    //  Password fields with default values
    private String pass;    // For alternate implementation, with password as a persistent field, if deciding to do so.
    private int length;
    private boolean allowMixedCase;
    private boolean allowNumbers;
    private boolean allowSpecialChars;
    private boolean allowExtraSpecialChars;

    //  Initialised password charsets fields
    private String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private String numbers = "1234567890";
    private String specialChars = "!£$%^&*()-_=+";
    private String extraSpecialChars = "[{]};:'@#~,<.>/?";

    //  Random object to allow for character generation
    Random ran = new Random();

    /**
     * Constructor for a password object. Sets the relevant fields to the parameters passed into it.
     *
     * @param length - length of password String to generate (in characters)
     * @param allowMixedCase - whether or not to include upper case letters
     * @param allowNumbers - whether or not to include numbers
     * @param allowSpecialChars - whether or not to include special characters (special chars: !£$%^&*()-_=+)
     * @param allowExtraSpecialChars - whether or not to include extra special characters (extra special chars: [{]};:'@#~,<.>/?)
     */
    public Password(int length, boolean allowMixedCase, boolean allowNumbers, boolean allowSpecialChars, boolean allowExtraSpecialChars) {
        this.length = length;
        this.allowMixedCase = allowMixedCase;
        this.allowNumbers = allowNumbers;
        this.allowSpecialChars = allowSpecialChars;
        this.allowExtraSpecialChars = allowExtraSpecialChars;
    }

    /**
     * Generates a password based on the field values set during construction.
     *
     * @return - a randomly generated password including characters based on the Password object's field values
     */
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

    /**
     * Generates a string of all possible characters, depending on the field values of the Password object in question.
     *
     * @return - all possible character values for the password in question
     */
    private String generateComponents() {
        String components = alphabet;

        // Add only characters from categories which have been approved.
        if(allowMixedCase){components += alphabet.toUpperCase(Locale.ROOT);}
        if(allowNumbers){components += numbers;}
        if(allowSpecialChars){components += specialChars;}
        if(allowExtraSpecialChars){components += extraSpecialChars;}

        return components;
    }

    /**
     * Generate a randomly chosen character from the full list of possible character components
     *
     * @param charSeq - full list of all possible characters to choose from
     * @return - randomly selected character from list of possible characters
     */
    private char generateChar(String charSeq) {
        int limit = charSeq.length() - 1;
        int index = ran.nextInt(limit+1);

        return charSeq.charAt(index);
    }



}
