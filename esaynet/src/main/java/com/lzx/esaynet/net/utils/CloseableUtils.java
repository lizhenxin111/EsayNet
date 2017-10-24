package com.lzx.esaynet.net.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by lizhe on 2017/10/24.
 */

public class CloseableUtils {
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
