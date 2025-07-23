# Raport z testów wydajnościowych sortowania

## 1. Środowisko testowe

* **CPU**: AMD Ryzen 5 5600X
* **OS**: Windows 10 64-bit
* **Java**: OpenJDK 21
* **Kompilator natywny**: gcc (MSYS2)

## 2. Metodologia

1. Testowano trzy metody sortowania:

   * `sort01(Double[] a, Boolean order)` – metoda natywna z przekazaniem parametru `Boolean`.
   * `sort02(Double[] a)` – metoda natywna odczytująca wartość `order` z pola obiektu.
   * `sort04()` – implementacja czysto w Javie, korzystająca z `java.util.Arrays.sort`.
2. Pomiary wykonano dla różnych rozmiarów tablic: 50 000, 100 000, 200 000, 400 000 i 800 000 elementów.
3. Każdy pomiar powtórzono pięć razy, a czas sortowania mierzono przy pomocy `System.nanoTime()`.
4. Dane zebrano w pliku **`benchmark.csv`**, zawierającym metodę, wielkość instancji, numer powtórzenia i czas wykonania.

> **Wyniki**: szczegółowe wartości czasów sortowania oraz wykresy znajdują się w pliku `benchmark.csv`.

## 4. Dyskusja

* Metody natywne (`sort01`, `sort02`) generują dodatkowy narzut JNI (konwersja danych, wywołanie metod C), co wpływa na wydłużenie czasu w porównaniu z czystą Javą.
* Między `sort01` a `sort02` występują niewielkie różnice: odczytywanie `order` z pola (`sort02`) jest odrobinę szybsze, ponieważ unika przekazywania obiektu `Boolean` podczas wywołania JNI.
* Przy małych rozmiarach tablic (50 000) narzut JNI jest relatywnie największy, ale wraz ze wzrostem N do 800 000 czas sortowania rośnie typowo O(N log N), co zmniejsza względną różnicę między metodami.

## 5. Wnioski

1. Dla sortowania prostych tablic `Double[]` w Javie zdecydowanie lepsza jest implementacja czysto w Javie (`sort04`).
2. Jeżeli konieczne jest użycie kodu natywnego, lepiej odczytywać parametry z pól obiektu (`sort02`) niż przekazywać nowy obiekt `Boolean` za każdym wywołaniem JNI.
3. JNI warto stosować wyłącznie wtedy, gdy dostępny jest zoptymalizowany algorytm w C/C++ o znaczącej przewadze nad bibliotekami Javy.
