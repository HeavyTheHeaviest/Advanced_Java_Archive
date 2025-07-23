- **Ograniczenie pamięci**: `-Xms512m -Xmx512m`  
  - **Efekt**: Przy intensywnym przełączaniu między plikami, GC częściej usuwał dane z pamięci, co wymagało ponownego ładowania z dysku.

- **Zwiększenie pamięci**: `-Xmx1024m`  
  - **Efekt**: Rzadziej pojawiała się konieczność ponownego ładowania plików, ponieważ więcej danych pozostawało w pamięci podręcznej.

- **UseSerialGC**  
  - **Efekt**: Prosty, jednowątkowy GC. Czasem zauważalne krótkie „pauzy” podczas odśmiecania; częściej ponowne ładowanie danych, gdy pamięć była ograniczona.

- **UseParallelGC**  
  - **Efekt**: Lepsza responsywność niż w przypadku SerialGC; mniej zauważalne przestoje przy czyszczeniu pamięci, choć wciąż występowało ponowne ładowanie przy dużym obciążeniu.

- **UseG1GC**  
  - **Efekt**: Krótsze, bardziej równomierne przerwy w pracy; większa szansa na utrzymanie danych w pamięci (zwłaszcza przy wyższym `-Xmx`), a więc rzadsze ponowne ładowanie plików.
