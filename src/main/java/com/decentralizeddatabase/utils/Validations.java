package com.decentralizeddatabase.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.decentralizeddatabase.errors.BadRequest;

public final class Validations {

    private static final Pattern USER_PATTERN = Pattern.compile("([A-Z]*[a-z]*[0-9]*)");

    public static String validateUser(final String user) throws BadRequest {
	final Matcher matcher = USER_PATTERN.matcher(user);

	if (matcher.matches()) {
	    return user;
	}

	throw new BadRequest(String.format("%s is not a valid user string", user));
    }
}
