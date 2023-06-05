package org.example.mybatis.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Resources {

    public static Reader getResourceAsReader(String resource){
        try {
            return new InputStreamReader(getResourceAsStream(resource));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static InputStream getResourceAsStream(String resource) throws IOException {
        ClassLoader[] classLoaders = getClassLoaders();
        for (ClassLoader classLoader : classLoaders) {
            InputStream is = classLoader.getResourceAsStream(resource);
            return is;
        }
        throw new IOException("no such resource: " + resource);
    }

    private static ClassLoader[] getClassLoaders() {
        return new ClassLoader[]{
                Thread.currentThread().getContextClassLoader(),
                ClassLoader.getSystemClassLoader()
        };
    }

    public static Class<?> classForName(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }


}
