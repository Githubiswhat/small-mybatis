package org.example.mybatis.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Resources {

    public static Reader getResourceAsReader(String resource){
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(getResourceAsStream(resource));
            return inputStreamReader;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static InputStream getResourceAsStream(String resource) throws IOException {
        ClassLoader[] classLoaders = getClassLoaders();
        for (ClassLoader classLoader : classLoaders) {
            InputStream is = classLoader.getResourceAsStream(resource);
            if (is != null){
                return is;
            }
        }
        throw new IOException("could not get resource: " + resource);
    }

    private static ClassLoader[] getClassLoaders() {
        return new ClassLoader[]{
                ClassLoader.getSystemClassLoader(),
                Thread.currentThread().getContextClassLoader()
        };
    }

    public static Class<?> classForName(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

}
