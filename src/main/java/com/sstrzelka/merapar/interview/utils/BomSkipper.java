package com.sstrzelka.merapar.interview.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.IOException;
import java.io.Reader;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BomSkipper {

    private static final char[] UTF_8_BOM = {0xEF, 0xBB, 0xBF};

    public static void skip(@NonNull Reader reader) throws IOException {
        reader.mark(1);
        char[] possibleBOM = new char[1];
        reader.read(possibleBOM);

        if (!isBOM(possibleBOM)) {
            reader.reset();
        }
    }
    private static boolean isBOM(@NonNull char[] chars) {
        return chars[0] == '\ufeff';
    }
}
