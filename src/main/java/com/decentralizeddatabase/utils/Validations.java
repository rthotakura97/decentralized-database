package com.decentralizeddatabase.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;

import com.decentralizeddatabase.errors.BadRequest;

public final class Validations {

    private static final Pattern USER_PATTERN = Pattern.compile("([A-Z]*[a-z]*[0-9]*)");

    public static String validateUser(final String user) throws BadRequest {
		if (Strings.isNullOrEmpty(user)) {
			throw new BadRequest("No user provided");
		}

		final Matcher matcher = USER_PATTERN.matcher(user);

		if (!matcher.matches()) {
			throw new BadRequest(String.format("%s is not a valid user string", user));
		}

		return user;
    }

    public static String validateRawSecretKey(final String rawKey) throws BadRequest {
		if (Strings.isNullOrEmpty(rawKey)) {
			throw new BadRequest("Secret key is empty!");
		}

		return rawKey;
    }
}
