package com.sstrzelka.merapar.interview.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BomSkipper {

    private static final char LE_BOM = '\ufeff';
    private static final char BE_BOM = '\ufffe';

    public static void skip(BufferedReader reader) throws IOException {
        reader.mark(1);
        char[] possibleBOM = new char[1];
        reader.read(possibleBOM);

        if (!isBOM(possibleBOM)) {
            reader.reset();
        }
    }
    private static boolean isBOM(char[] chars) {
        return chars[0] == LE_BOM || chars[0] == BE_BOM;
    }
}
