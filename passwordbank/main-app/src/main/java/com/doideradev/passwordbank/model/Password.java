package com.doideradev.passwordbank.model;

import java.io.Serializable;
import java.util.Random;

public class Password implements Serializable{

    private static final long serialVersionUID = 100202L;
    private static final Random random = new Random();

    private String plainPass;
    private String protectedPass;
    private String salt;
    private String pepper;
    private int[] pattern;

    public Password() {}

    public Password(String plainPass) {
        setPass(plainPass);
    }



    public void setPass(String pass) {
        this.plainPass = pass;
    }
    public String getPass() {
        return this.plainPass;
    }





    private void createPattern() {
        int[] randoms = new int[plainPass.length()];
        for (int i = 0; i < plainPass.length(); i++) {
            randoms[i] = random.nextInt(1, 15);
        }
        pattern = randoms;
    }

    private void doSalting() {
        int val = pattern[0];
        char[] charSalt = new char[val];
        for (int i = 0; i < val; i++) {
            int r = random.nextInt(33, 256);
            charSalt[i] = (char) r;
        }
        String salting = String.valueOf(charSalt);
        salt = salting;
    }

    private void doPeppering() {
        int val = pattern[1];
        char[] charPepp = new char[val];
        for (int i = 0; i < val; i++) {
            int rVal = random.nextInt(33, 256);
            charPepp[i] = (char) rVal;
        }
        String peppering = String.valueOf(charPepp);
        pepper = peppering;
    }

    private void doPseudoHashing() {
        int len1 = plainPass.length();
        char[] charArray = new char[len1];
        for (int i = 0; i < len1; i++) {
            int passVal = plainPass.charAt(i) * pattern[i];
            char passChar = (char) passVal;
            charArray[i] = passChar;
        }
        String charString = String.valueOf(charArray);
        protectedPass = (salt + charString + pepper);
    }

    


    public void protectPassword() {
        createPattern();
        doSalting();
        doPeppering();
        doPseudoHashing();
        plainPass = null;
    }

    /**
     * Retrieve the original password from its protected form by reversing
     * the salting, peppering and pseudo-hashing processes.
     */
    public void retrievePass() {
        int saltLen = pattern[0];
        int peppLen = pattern[1];

        String rawPass = protectedPass.substring(saltLen, protectedPass.length()-peppLen);
        char[] rawChar = new char[rawPass.length()];

        for (int i = 0; i < rawPass.length(); i++) {
            int val = (rawPass.charAt(i) / pattern[i]);
            rawChar[i] = (char) val;
        }

        String finalPass = String.valueOf(rawChar);

        setPass(finalPass);
    }


    /**
     * Compare a candidate password with the stored password.
     * Works similarly to the equals method but is static and
     * intended for password comparison.
     * @param candidate - the candidate password to compare.
     * @param password - the stored password to compare against.
     * @return true if the passwords match, false otherwise.
     */
    public final static boolean comparePasswords(String candidate, Password password) {
        String pass = password.getPass();

        return candidate.equals(pass);
    }
}