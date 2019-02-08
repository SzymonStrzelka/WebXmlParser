package com.sstrzelka.merapar.interview.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.util.Arrays;

public class BomSkipperTest {

    private static final String UTF_8_BOM = "\uefbb\00bf";
    private static final String UTF_16_LE_BOM = "\ufeff";
    private static final String UTF_16_BE_BOM = "\ufffe";
    private static final String UTF_32_BOM = "\ufffe\u0000";

    private BufferedReader reader;
    private char[] actual = new char[1];

    @Test
    public void testSkipUTF8() throws Exception {
        reader = new BufferedReader(new CharArrayReader(UTF_16_LE_BOM.toCharArray()));
        BomSkipper.skip(reader);
        reader.read(actual);
        Assert.assertFalse(Arrays.equals(actual, UTF_16_LE_BOM.toCharArray()));
    }

}