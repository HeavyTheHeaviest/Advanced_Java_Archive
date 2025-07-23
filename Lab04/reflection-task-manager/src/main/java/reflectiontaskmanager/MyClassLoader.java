package reflectiontaskmanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Niestandardowy ładowacz klas, który wczytuje bajty z plików .class znajdujących się w podanym katalogu.
 */
public class MyClassLoader extends ClassLoader {
    private String classesDir;

    public MyClassLoader(String classesDir, ClassLoader parent) {
        super(parent);
        this.classesDir = classesDir;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        // Zamieniamy nazwę klasy na ścieżkę do pliku .class
        String fileName = classesDir + File.separator + name.replace('.', File.separatorChar) + ".class";
        File classFile = new File(fileName);
        if (!classFile.exists()) {
            throw new ClassNotFoundException("Nie znaleziono pliku: " + fileName);
        }
        try {
            byte[] classData = Files.readAllBytes(Paths.get(fileName));
            // Definiujemy klasę przy użyciu odczytanych bajtów
            return defineClass(name, classData, 0, classData.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Błąd odczytu pliku: " + fileName, e);
        }
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // Najpierw sprawdzamy, czy klasa została już załadowana
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            try {
                // Jeśli klasa należy do pakietu "java.", delegujemy do rodzica
                if (name.startsWith("java.")) {
                    c = super.loadClass(name, false);
                } else {
                    // Próba załadowania klasy przy użyciu metody findClass (naszej implementacji)
                    c = findClass(name);
                }
            } catch (ClassNotFoundException e) {
                // W razie niepowodzenia delegujemy do rodzica
                c = super.loadClass(name, false);
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }
}

