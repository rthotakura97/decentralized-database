package com.distributeddb.utils;

import org.junit.Assert;
import org.junit.Test;

public class FileBlockTest {
    private static final long BLOCKORDER = 982834919L;
    private static final String DATA = "laafelisjealf2839238921j3jrl";

    private static String encoderHelper(final long order, final String data) {
        final StringBuilder orderBuilder = new StringBuilder(Long.toHexString(order));
        while (orderBuilder.length() < 16) {
            orderBuilder.insert(0, "0");
        }
        orderBuilder.append(data);

        return orderBuilder.toString();
    }

    @Test
    public void testBuildFileBlockFromEncodedData() {
        final FileBlock expected = new FileBlock(BLOCKORDER, DATA);
        final String encodedData = encoderHelper(BLOCKORDER, DATA);
        
        final FileBlock actual = new FileBlock(encodedData);

        Assert.assertEquals(expected.getBlockOrder(), actual.getBlockOrder());
        Assert.assertEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testBuildFileBLockFromEncodedDataWithNegativeLong() {
        final FileBlock expected = new FileBlock(-123456789L, DATA);
        final String encodedData = encoderHelper(-123456789L, DATA);
        
        final FileBlock actual = new FileBlock(encodedData);

        Assert.assertEquals(expected.getBlockOrder(), actual.getBlockOrder());
        Assert.assertEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testBuildFileBlockFromEncodedDataWithMaxLong() {
        final FileBlock expected = new FileBlock(Long.MAX_VALUE, DATA);
        final String encodedData = encoderHelper(Long.MAX_VALUE, DATA);
        
        final FileBlock actual = new FileBlock(encodedData);

        Assert.assertEquals(expected.getBlockOrder(), actual.getBlockOrder());
        Assert.assertEquals(expected.getData(), actual.getData());

    }

    @Test
    public void testBuildFileBlockFromEncodedDataWithMinLong() {
        final FileBlock expected = new FileBlock(Long.MIN_VALUE, DATA);
        final String encodedData = encoderHelper(Long.MIN_VALUE, DATA);
        
        final FileBlock actual = new FileBlock(encodedData);

        Assert.assertEquals(expected.getBlockOrder(), actual.getBlockOrder());
        Assert.assertEquals(expected.getData(), actual.getData());

    }

    @Test
    public void testBuildFileBlockFromEncodedDataWithZero() {
        final FileBlock expected = new FileBlock(0, DATA);
        final String encodedData = encoderHelper(0, DATA);
        
        final FileBlock actual = new FileBlock(encodedData);

        Assert.assertEquals(expected.getBlockOrder(), actual.getBlockOrder());
        Assert.assertEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testEncodeFileBlockIntoData() {
        final FileBlock fileBlock = new FileBlock(BLOCKORDER, DATA);
        final String expected = encoderHelper(BLOCKORDER, DATA);

        final String actual = fileBlock.encodeOrderIntoBlock();

        Assert.assertEquals(expected, actual);
    }
}