package reflectiontaskmanager;

import reflectionapi.Processor;

/**
 * Klasa pomocnicza przechowująca informację o załadowanym procesorze,
 * jego nazwę oraz referencję do ładowacza, który został użyty.
 */
public class LoadedProcessor {
    private String className;
    private Processor processorInstance;
    private MyClassLoader classLoader;

    public LoadedProcessor(String className, Processor processorInstance, MyClassLoader classLoader) {
        this.className = className;
        this.processorInstance = processorInstance;
        this.classLoader = classLoader;
    }

    public String getClassName() {
        return className;
    }

    public Processor getProcessorInstance() {
        return processorInstance;
    }

    public MyClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public String toString() {
        return className + " - " + processorInstance.getInfo();
    }
}

