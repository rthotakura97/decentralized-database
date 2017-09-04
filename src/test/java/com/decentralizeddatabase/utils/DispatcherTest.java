package com.decentralizeddatabase.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExpectedException;

import com.decentralizeddatabase.errors.BadRequest;

public class DispatcherTest {

    private static final String TEST_USER = "Test User";
    private static final String TEST_SECRET_KEY = "secret key";
    private static final String TEST_FILENAME = "filename";
    private static final String TEST_FILE = "This is a file";

    private Dispatcher dispatcher;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
	this.dispatcher = new Dispatcher();
    }

    @Test
    public void testMakeCallWithBadMethod() throws BadRequest {
	DecentralizedDBRequest request = new DecentralizedDBRequest("invalid",
								    TEST_USER,
								    TEST_SECRET_KEY,
								    TEST_FILENAME,
								    TEST_FILE);
	thrown.expect(BadRequest.class);
	dispatcher.makeCall(request);

	Assert.fail("Shouldn't be here");
    }
}
