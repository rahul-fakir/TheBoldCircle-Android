package com.rahulfakir.theboldcircle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rahulfakir on 6/13/16.
 */

public class DataManipulation {
    private String unprocessedString;
    private String statusMessage;

    public String getUnprocessedString() {
        return unprocessedString;
    }

    public void setUnprocessedString(String unprocessedString) {
        this.unprocessedString = unprocessedString;
    }

    public boolean validateForStringNotEmpty(String field, String unprocessedString) {
        if (unprocessedString.length() == 0 || unprocessedString == null) {
            statusMessage = field + "cannot be blank or empty";
            return false;
        }
        return true;
    }

    public boolean validateForEmailAddress(String unprocessedString) {
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(unprocessedString);
        if (!matcher.matches()) {
            statusMessage = "E-mail address is invalid";
            return false;
        }
        return true;
    }

    public boolean validateForPhoneNumber(String unprocessedString) {
        Pattern pattern = Pattern.compile("(\\d{3}){2}\\d{4}");
        Matcher matcher = pattern.matcher(unprocessedString);
        if (!matcher.matches()) {
            statusMessage = "Phone number is invalid";
            return false;
        }
        return true;
    }

    public boolean validateForEquality(String field, String first, String second) {
        if (!first.equals(second)) {
            statusMessage = field + "s do not match";
            return false;
        }
        return true;
    }

    public boolean validateForPassword(String unprocessedString) {

        Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})");
        Matcher matcher = pattern.matcher(unprocessedString);
        if (!matcher.matches()) {
            statusMessage = "Password must contain letters from both cases and at least one number";
            return false;
        }
        return true;

    }

    public String encodeEmailToUsername(String email) {
        email = email.replace(".", "0");
        email = email.replace("#", "1");
        email = email.replace("$", "2");
        email = email.replace("[", "3");
        email = email.replace("]", "4");
        return email;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
