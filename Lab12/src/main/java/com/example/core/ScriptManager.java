package com.example.core;

import org.graalvm.polyglot.*;
import java.io.IOException;
import java.nio.file.*;

/**
 * Zarządza dynamicznym ładowaniem i wyładowywaniem skryptów JS jako automatów.
 */
public class ScriptManager {
    private Context context;
    private Value automatonFunction;

    /**
     * Ładuje skrypt o podanej nazwie (bez rozszerzenia .js).
     * Wyładowuje poprzedni, jeśli istnieje.
     */
    public void load(String name) throws IOException {
        unload(); // wyczyść poprzedni kontekst
        context = Context.newBuilder("js")
                .allowAllAccess(true)
                .build();
        String path = "src/main/resources/scripts/" + name + ".js";
        String script = Files.readString(Path.of(path));
        context.eval("js", script);
        automatonFunction = context.getBindings("js").getMember(name);
    }

    /**
     * Zwraca implementację interfejsu CellularAutomaton,
     * która przekazuje tablicę do JS i odbiera wynik.
     */
    public CellularAutomaton getAutomaton() {
        if (context == null || automatonFunction == null) {
            throw new IllegalStateException("Skrypt nie został załadowany.");
        }
        return current -> {
            Value jsArray = context.asValue(current);
            Value result = automatonFunction.execute(jsArray);
            return result.as(boolean[][].class);
        };
    }

    /**
     * Wyładowuje skrypt, zamykając kontekst.
     */
    public void unload() {
        if (context != null) {
            context.close();
            context = null;
            automatonFunction = null;
        }
    }
}