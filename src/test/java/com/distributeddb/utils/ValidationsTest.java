package com.distributeddb.utils;

import org.junit.Assert;
import org.junit.rules.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

import com.distributeddb.errors.BadRequest;

public final class ValidationsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testValidateUserWithValidUser() throws BadRequest {
        final String expected = "TESTuser1235";
        final String actual = Validations.validateUser(expected);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testValidateUserWithNullUser() throws BadRequest {
        thrown.expect(BadRequest.class);
        Validations.validateUser(null);

        Assert.fail("No Bad Request was thrown");
    }

    @Test
    public void testValidateUserWithInvalidUser() throws BadRequest {
        thrown.expect(BadRequest.class);
        Validations.validateUser("!@#%%#   ");

        Assert.fail("No Bad Request was thrown");
    }

    @Test
    public void testRawSecretKeyWithValidKey() throws BadRequest {
        final String expected = "somekey 12#42";
        final String actual = Validations.validateRawSecretKey(expected);
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRawSecretKeyWithInvalidKey() throws BadRequest {
        thrown.expect(BadRequest.class);
        Validations.validateRawSecretKey(null);

        Assert.fail("No Bad Request was thrown");
    }
}
