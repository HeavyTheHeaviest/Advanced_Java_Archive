# README

## Projekt: CryptoApp Multi-Release JAR i Instalator

### 1. Multi-Release JAR

- **Źródła wspólne (JDK 8+)**:
  - `src/main/java/com/example/...`  
- **Źródła specyficzne dla JDK 9+**:
  - `src/main/java9/com/example/app/MainFrame.java` (nadpisana wersja z nowymi API)  
- **Kompilacja**:
  ```
  # pod JDK 21
  mvn clean package
  ```
  - Faza `compile` kompiluje wspólne klasy jako Java 8  
  - Faza `process-classes` kompiluje katalog `src/main/java9` jako Java 9 do `target/classes/META-INF/versions/9`  
- **MANIFEST.MF**:
  ```text
  Manifest-Version: 1.0
  Main-Class: com.example.app.MainFrame
  Multi-Release: true
  ```
- **Wynikowy JAR**:
  ```
  target/crypto-app-1.0.0.jar
    ├─ com/example/app/MainFrame.class          # Java 8
    ├─ META-INF/versions/9/com/example/app/MainFrame.class  # Java 9+
    └─ pozostałe klasy
  ```

### 2. Instalator aplikacji (jpackage)

- **Przykład natywny EXE na Windows**:
  ```bat
  "%JAVA_HOME%\bin\jpackage.exe" ^
    --type exe ^
    --name CryptoApp ^
    --app-version 1.0.0 ^
    --input target ^
    --main-jar crypto-app-1.0.0.jar ^
    --main-class com.example.app.MainFrame ^
    --win-shortcut ^
    --win-menu ^
    --vendor "Filip Opac"
  ```
- **Wynik**:
  - `target/CryptoApp-1.0.0.exe` (instalator EXE)

