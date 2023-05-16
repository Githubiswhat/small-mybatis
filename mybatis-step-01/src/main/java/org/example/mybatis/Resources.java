package org.example.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Resources {

    public static Reader getResourceAsReader(String resource) throws IOException {
        return new InputStreamReader(getResourceAsStream(resource));
    }

    private static InputStream getResourceAsStream(String resource) throws IOException {
        ClassLoader[]  classLoaders = getClassLoaders();
        for (ClassLoader classLoader : classLoaders) {
            InputStream is = classLoader.getResourceAsStream(resource);
            if (is != null) {
                return is;
            }
        }
        throw new IOException("no resource found");
    }

    private static ClassLoader[] getClassLoaders() {
        return new ClassLoader[]{
                ClassLoader.getSystemClassLoader(),
                Thread.currentThread().getContextClassLoader()
        };
    }
}
