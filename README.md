This repo is an archive for all my work done for "Advanced Java" course at Wrocław University of Science and Technology. Each app was created using those instructions.

Lab01

Napisz aplikację, która pozwoli na archiwizowanie wskazanych plików/katalogów. Aplikacja ta powinna równiez umożliwiać generowanie funkcji skrótu MD5 dla stworzonych archiwów oraz weryfikowanie, czy dana funkcja skrótu zgadza się z funkcją skrótu wyliczoną dla danego archiwum. 

Proszę zastanowić się nad interfejsem użytkownika tej aplikacji (w jaki sposób definiować zadanie archiwizacji, w tym miejsca do zapisu archiwów oraz funkcji skrótu oraz miejsca do ich odczytu).

Można przyjąć różne strategie co do sposobu zapamiętywania funkcji skrótu (np. w tym samym katalogu, w którym zapisano archiwum, o tej samej nazwie co archiwum, ale z innym rozszerzeniem). 

Aplikacja powinna być napisana z wykorzystaniem modułów (wprowadzonych w Javie od jdk 9). Powstać ma przynajmniej jeden moduł biblioteki oraz moduł samej aplikacji (korzystający z modułu biblioteki). 
Powstałe moduły należy wyeksportować do plików jar.

Używając jlink należy przygotować minimalne rodowisko uruchomieniowe, do którego podpięte zostaną wymienione wyżej moduły.

Aplikację powinno dać się uruchomić z linii komend, korzystając tylko z wygenerowanego rodowiska uruchomieniowego. Sama aplikacja powinna oferować interfejs użytkownika (najlepiej graficzny, minimum - tekstowy).

Sprawę tworzenia archiwów (zipowania) niele opisano w tutorialu: https://www.baeldung.com/java-compress-and-uncompress
Można z niego skorzystać.

Lab02

Napisz aplikację, która umożliwi przeglądanie danych pomiarowych przechowywanych na dysku w plikach csv (w wersji minimum).
Zakładamy, że pliki danych będą składowane w folderach o nazwach reprezentujących daty kampanii pomiarowych (np. nazwą folderu może być 07.03.2023), za nazwy samych plików będą odpowiadały identyfikatorowi stanowiska pomiarowego oraz godzinie rozpoczęcia pomiarów (np. nazwą pliku może być 0001_10:00). 
Zawartoć plików powinna odpowiadać następującemu schematowi:

# godzina pomiaru; cinienie [hPa];  temperatura [stopnie C]; wilgotnoć [%]
10:00; 960,34; -1,2; 60;
10:02; 960,34; -1,2; 60;
....

Pliki danych można stworzyć do testów posługując się generatorami losowymi czy innymi narzędziami.

Interfejs graficzny aplikacji powinien składać się z dwóch paneli - jednego, przeznaczonego do nawigacji po folderach i plikach, oraz drugiego, służącego do wywietlania podglądu zawartoci aktualnie wybranego pliku z danymi (wywietlanych może być kilka pierwszych linijek) oraz wyników przetworzenia danych (wartoci rednich dla kolejnych kolumn, poza kolumną z godziną pomiaru).

Aplikację należy zaprojektować z wykorzystaniem słabych referencji (ang. weak references). Zakładamy, że podczas przeglądania folderów z plikami danych pomiarowych zawartoć aktualnie wybranego pliku będzie ładowana do odpowiedniej struktury oraz zostanie przetworzona (celem wyliczenia odpowiednich wartoci). Słabe referencje powinny pozwolić na ominięcie koniecznoci wielokrotnego ładowania i przetwarzania tej samej zawartoci - co może nastąpić podczas poruszanie się wprzód i wstecz po licie plików w folderach.

Aplikacja powinna wskazywać, czy zawartoć pliku została załadowana ponownie, czy też została pobrana z pamięci. Wskazanie to może być zrealizowane za pomocą jakiego znacznika prezentowanego na interfejsie.

W celu oceny poprawnoci działania aplikację należy uruchamiać przekazując wirtualnej maszynie parametry ograniczające przydzielaną jej pamięć. Na przykład -Xms512m (co oznacza minimalnie 512 MB pamięci), -Xmx1024m (co oznacza maksymalnie 1GB).
Należy też przetestować możliwoć regulowania zachowania się algorytmu odmiecania, do czego przydają się opcje -XX:+ShrinkHeapInSteps, -XX:-ShrinkHeapInSteps. Proszę przestudiować, jakie inne atrybuty można przekazać do wirtualnej maszyny, w tym selekcji algorytmu -XX:+UseSerialGC, -XX:+UseParNewGC (deprecated), -XX:+UseParallelGC, -XX:+UseG1GC.

Architektura aplikacji powinna umożliwiać dołączanie różnych algorytów przetwarzania danych, jak również algorytmów renderujących fragment zawartoci przeglądanego pliku (zakładamy, że aplikację będzie można rozbudować w celu przeglądania danych zapisanych w plikach o innym schemacie zawartoci czy też innym formacie danych).

Proszę dodać do ródeł plik readme.md z wnioskami co do stosowalnoci opcji wirtualnej maszyny.

Proszę sięgnąć do materiałów http://tomasz.kubik.staff.iiar.pwr.wroc.pl/dydaktyka/Java/UnderstandingWeakReferences.pdf

Lab03

Napisz aplikację, która pozwoli skonsumować dane bibliograficzne pozyskiwane z serwisu oferującego publiczne restowe API. Niech pierwszym wyborem będzie api serwisu Wolne Lektury, udostępnione pod adresem https://wolnelektury.pl/api/ (nie potrzeba podawać żadnych parametrów uwierzytelnienia ani klucza api). Istnieją też inne serwisy oferujące dostęp do danych bibliograficznych. Przykłady można znaleć na stronie: https://publicapis.dev/category/books.
Do realizacji zadania można więc wybrać inne api, ale poza https://openlibrary.org/developers/api(bo to api wykorzystano na laboratoriach w 2024 roku). W szczególnoci pouczające byłoby wykorzystanie api, które wymaga podania klucza. Jednak często uzyskanie klucza wiąże się z koniecznocią rejestracji w danym serwisie z wyborem jakiego planu biznesowego (tj. za opłatą).

Ciekawą listę serwisów udostępniających różne API można znaleć pod adresem:
https://rapidapi.com/collection/list-of-free-apis (wymagają klucza API), czy też https://mixedanalytics.com/blog/list-actually-free-open-no-auth-needed-apis/ (te klucza API nie wymagają). 

Bazując na wskazanym api należy zbudować aplikację z graficznym interfejsem użytkownika, pozwalającą na przeprowadzanie testów dla bibliofilów, z pytaniami dotyczącymi książek (np. tytuły, tematyka, wydania, autorzy), autorów (np. dorobek wydawniczy, współautorstwo) itp. Renderowanie zapytań i odpowiedzi powinno być tak zaimplementowane, by dało się zmianić ustawienia językowe (lokalizacji) w oparciu o tzw. bundle (definiowane w plikach i klasach - obie te opcje należy przetestować). Wspierane mają być języki: polski i angielski. 

Proszę zapoznać się z api i zaproponować kilka schematów zapytań i pól odpowiedzi. Niech zapytania będą parametryzowane wartociami pochodzącymi z list wyboru wypełnionych trecią pozyskaną z serwisu, a odpowiedzi niech będą uzupełniane wolnym tekstem lub wartociami z list wyboru (jeli "charakter" pytania jest, odpowiednio, otwarty lub zamknięty). 
Odpowiedzi podanawane przez użytkownika powinny być weryfikowane przez aplikację (aplikacja, po wysłaniu zapytania przez api powinna sprawdzić, czy wynik tego zapytania jest zgodny z odpowiedzią udzieloną przez użytkownika).

Przykłady szablonów zapytania i odpowiedzi: 
Przykład 1:
 Pole zapytania: "Kto jest autorem książki ... ?" (w miejsce kropek aplikacja powinna wstawić jaki tytuł pobrany z serwisu)
 Pole odpowiedzi: "..."  (miejsce na wpisanie autora).
 Pole weryfikacji (dla poprawnej odpowiedzi): "Tak, masz rację. Autorem książki .... jest ..." (to ma wypełnić sama aplikacja, przy czym można się zastanowić nad tym, jak ma przebiegać weryfikacja, gdy np. autorów jest więcej niż jeden).

Przykład 2:
Na przykład dla szablonu zapytania zapytania: 
 Pole zapytania: "Ilu współatorów ma książka ... ?" (w miejsce kropek aplikacja powinna wstawić jaki tytuł pobrany z serwisu)
 Pole odpowiedzi: "..."  (miejsce na wpisanie liczby).
 Pole weryfikacji (dla poprawnej odpowiedzi): "Tak, masz rację. Książka ... została napisana przez ... autorów" (to ma wypełnić sama aplikacja, przy czym trzeba obsłużyć odmianę przez liczby). 
 
Przykład 3:
 Pole zapytania: "Które z wymienionych tytułów: a) ...., b) ...., c) ....., d) ..... to książki dotyczące tematyki: ....?" (w miejsce kropek przy wyliczeniu aplikacja powinna wstawić losowe tytuły książek pobrane z serwisu, za tematyka powinna być atrybutem pasującym przynajmniej do jednego tytułu - co można sprawdzić w serwisie).
 Pole odpowiedzi: "..."  (miejsce na wpisanie literki/literek odpowiadających wybranym tytułom).
 Pole weryfikacji (dla poprawnej odpowiedzi): "Tak, masz rację. Książka (bąd książki) ... dotyczy (dotyczą) tematyki ....." (to ma wypełnić sama aplikacja, przy czym trzeba obsłużyć odmianę przez liczby). 

Proszę pamiętać o obsłudze przynajmniej dwóch języków na interfejsie. Do tego proszę zastosować wariantowe pobieranie tekstów z bundli. Do tego przyda się klasa ChoiceFormat. 

Przypominam, że podczas realizacji zadania można wykorzystać inne api niż sugerowane, jeli tylko pozwoli ono zrealizować przedstawioną wyżej koncepcję (parametryzowane szablony zapytań, do wypełnienia pola odpowiedzi, linijka weryfikacji z odmianą przez liczby/osoby - wszystko przynajmniej z obsługą dwóch języków: polski i angielski).


Lab04

Napisz aplikację, która umożliwi zlecanie wykonywania zadań instancjom klas ładowanym własnym ładowaczem klas. Do realizacji tego ćwiczenia należy użyć Java Reflection API z jdk 17.

Tworzona aplikacja powinna udostępniać graficzny interfejs, na którym będzie można:
1. zdefiniować zadanie (zakładamy, że będzie można definiować "dowolne" zadania reprezentowane przez ciąg znaków),
2. załadować klasę wykonującą zadanie (zakładamy, że będzie można załadować więcej niż jedną taką klasę),
3. zlecić wykonanie wskazanego zadania wskazanej załadowanej klasie, monitorować przebieg wykonywania zadania, wywietlić wynik zadania.
4. wyładować wybraną klasę z wczeniej załadowanych

Realizacja zadania powinna opierać się na wykorzystaniu API (klas i interfejsów) zdefiniowanych w archiwum Refleksja02.zip (http://tomasz.kubik.staff.iiar.pwr.wroc.pl/dydaktyka/Java/Refleksja02.zip - dostęp z sieci uczelnianej).

Należy dostarczyć przynajmniej 3 różne klasy implementujące interfejs Processor. Każda taka klasa po załadowaniu powinna oznajmić, poprzez wynik metody getInfo(), jakiego typu zadanie obsługuje. 

Z uwagi na wymagania GUI wynik getInfo() nie powinien być zbyt długi (ma się zmiecić w przewidziany polu). Musi jednak nieć jaką informację o zadaniu i jego parametrach. Proponuję, by ten ciąg znaków zawierał jednowyrazową nazwę zadania, a po dwukropku szablon zapytania. Jest to tylko propozycja. Podczas realizacji zadania można zastosować inny sposób informowania użytkownika na czym polega zadanie i jak je sparametryzować.
  
Przykładowe wyniki wywołania metody getInfo():	
1. "sumowanie: #1 op #2" - definiując zadanie użytkownik powinien podać ciąg znaków składający się z dwóch operandów oraz operatora, np.: "1 + 2". Oczekiwanym wynikiem będzie ciąg znaków: "3".
2. "uppercase: #1" - definiując zadanie użytkownik powinien podać ciąg znaków do przetworzenia: "ala ma kota", oczekiwanym wynikiem będzie ciąg znaków: "ALA MA KOTA".
3. "pogoda: #1, #2" - definiując zadanie użytkownik powinien podać ciąg znaków reprezentujący nazwę miasta i datę: "Wroclaw, 25.03.2025", a oczekiwanym wynikiem będzie ciąg znaków reprezentujący prognozowane warunki pogodowe: "12 stopni C, 1000 hPa".
4. "csvfilter: #1, #2" - definiując zadanie użytkownik powinien podać ciąg znaków reprezentujący URL  przetwarzanego pliku csv oraz parametry filtra, np.: "file:\\C:\in.csv, 2:3:4. Oczekiwanym wynikiem będzie ciąg znaków reprezentujący URL przetworzonego pliku (z pozostawionymi kolumnami 2, 3 i 4): "file:\\C:\in.filtered.csv".

Każda z grup laboratoryjnych otrzymała wskazówki, na czym ma polegać przetwarzanie. Proszę nie implementować sposobu przetwarzania z przykładów 1 i 2 wymienionych powyżej.

Klasy ładowane powinny być skompilowane w innym projekcie niż sama użytkowa aplikacja (podczas budowania aplikacja nie powinna mieć dostępu do tych klas). Można założyć, że kod bajtowy tych klas będzie umieszczany w katalogu, do którego aplikacja będzie miała dostęp. cieżka do tego katalogu powinna być parametrem ustawianym w aplikacji w trakcie jej działania. Wartocią domylną dla cieżki niech będzie katalog, w którym uruchomiono aplikację. Aplikacja powinna odczytać zawartoć tego katalogu i załadować własnym ładowaczem odnalezione klasy. Zakładamy, że podczas działania aplikacji będzie można "dorzucić" nowe klasy do tego katalogu (należy więc pomyleć o pewnego rodzaju "odwieżaniu").

Wybieranie klas (a tym samym algorytmów przetwarzania) powinno odbywać się poprzez listę wywietlającą nazwy załadowanych klas. Nazwom tym niech towarzyszą opisy pozyskane metodą getInfo() z utworzonych instancji tych klas.


Lab05

Zaimplementuj aplikację z graficznym interfejsem pozwalającą przeprowadzić analizę statystyczną wyników klasyfikacji zgromadzonych w tzw. tabelach niezgodnoci (ang. confusion matrix). Obliczać należy współczynnik kappa oraz przynajmniej trzy inne miary (np. miary opisane w artykule: https://arxiv.org/pdf/2008.05756.pdf).
Aplikacja powinna pozwalać na:
- wywietlanie/edytowanie danych tabelarycznych;
- wybranie algorytmu, jakim będą one przetwarzane;
- wywietlenie wyników przetwarzania (obliczonej miary).
W trakcie implementacji należy wykorzystać interfejs dostarczyciela serwisu (ang. Service Provider Interface, SPI). Dokładniej, stosując podejcie SPI należy zapewnić aplikacji możliwoć załadowania klas implementujących zadany interfejs już po zbudowaniu samej aplikacji. 
Klasy te (z zaimplementowanymi algorytmami wyliczającymi miary służące do oceny wyników klasyfikacji) mają być dostarczane w plikach jar umieszczanych w cieżce. Należy stworzyć dwie wersje projektu: standardową oraz modularną.

Aby zapoznać się z problemem proszę sięgnąć do przykładowych projektów w archiwum udostępnionym pod adresem:
    http://tomasz.kubik.staff.iiar.pwr.wroc.pl/dydaktyka/Java/WorkspaceServiceProviderInterface.zip

W implementacji należy wykorzystać klasy z pakietu ex.api dostarczanego przez bibliotekę:
 http://tomasz.kubik.staff.iiar.pwr.wroc.pl/dydaktyka/Java/analysisserviceapi-1.0-SNAPSHOT.jar
(ten modułowy jar zawiera kod bajtowy oraz kod ródłowy klas: AnalysisException, AnalysisService, DataSet).

Trochę informacji o SPI można znaleć pod adresem:
    https://www.baeldung.com/java-spi
Porównanie SPI ze SpringBoot DI zamieszczono pod adresem:
    https://itnext.io/serviceloader-the-built-in-di-framework-youve-probably-never-heard-of-1fa68a911f9b


Lab06

Napisz aplikację służącą do zarządzania usługami oferowanymi klientom firmy dostarczającej telewizję internetową. Zadanie należy zrealizować wykorzystując relacyjną bazę danych. Zalecane jest użycie h2 (z zapisem do pliku) bąd sqlite (by podczas uruchamiania nie było zależnoci od zewnętrznej usługi). 

Podczas realizacji zadania należy skorzystać z mapowania ORM (technologia JPA, do czego można użyć framework Hibernate) oraz zastosować wzorzec projektowy z użyciem serwisów i repozytoriów. Można w tym celu wykorzystać framework Spring Boot.

Zakładamy, że w bazie danych będą przechowywane następujące informacje (jest to model mocno uproszczony, można go nieco przeorganizować celem "ulepszenia"):
* Kient - imię, nazwisko, numer klienta
* Subkonta - dane uwierzytelnienia (login, hasło)
* Typ abonamentu - typ usługi z listy wyliczeniowej
* Należnoci - termin płatnoci, kwota do zapłaty
* Dokonane wpłaty - termin wpłaty, kwota wpłaty
* Cennik - typ usługi, cena 

Jeden klient może wykupić wiele abonamentów, przy czym z każdym abonamentem może być związanych kilka subkont. Należnoci i wpłaty dotyczą danego abonamentu. Należnoci mają być naliczane w trybie miesięcznym. Cennik powinien obejmować wszystkie typy usług (z uwzględnieniem historii: usługi aktywne, usługi wygaszone).

Aby dało się przetestować działanie aplikacji należy zasymulować upływ czasu.

Program powinien:
- pozwalać na ręczne zarządzanie sprzedawanymi usługami (klientami i ich abonamentami) oraz cennikiem;
- automatycznie naliczać należnoci i wysyłać monity o kolejnych płatnociach (wystarczy, że będzie pisał do pliku z logami monitów);
- automatycznie eskalować monity w przypadku braku terminowej wpłaty (wystarczy, że będzie pisał do pliku z logami ekalowanych monitów, upływ czasu należy zasymulować);
- umożliwiać ręczne rejestrowanie wpłat oraz nanoszenie korekt;
- umożliwiać przeglądanie należnoci i wpłat.
 
Program może działać w trybie konsolowym, choć dużo lepiej wyglądałoby stworzeni interfejsu graficznego. Do logowania monitów proszę użyć jaką standardową bibliotekę (np. log4j).


Lab07

Zamień aplikację napisaną podczas poprzedniego labotatorium na usługę sieciową. Niech ta usługa będzie oferowała restowy interfejs pozwalający na wykonanie wszystkich operacji związanych z obsługą klientów firmy będącej dostawcą Internetu, a jej implementacja niech będzie wykonana z pomocą frameworka Spring Boot.

Implementacja może odbywać się według podejcia top-down (czyli najpierw definicja interfejsu wykonana w Swagger Editor, a potem wygenerowanie kodu ródłowego interfejsów i ich implementacja), albo według podejcia bottom-up (czyli najpierw redagowanie kodu ródłowego interfejsów i ich implementacja, a potem wygenerowanie swaggerowego opisu).

Efektem ma być tzw. backend, którego działanie będzie można:
* przetestować za pomocą swaggerowego interfejsu (automatycznie wygenerowanej strony internetowej, dającej możliwoć sparametryzowania zapytań protokołu HTTP, ich wysłania na endpoint oraz wywietlenia wyniku);
* przetestować za pomocą zewnętrznych narzędzi jak: Postman czy SoapUI.

Reszta wymagań zgodnie z ustaleniami poczynionymi na laboratorium.

Lab08

Napisz program, w którym wykorzystane zostanie JNI. Zadanie do wykonania polegać ma na zadeklarowaniu klasy oferującej trzy metody natywne pozwalające posortować wskazane tablice obiektów typu Double. Schemat implementacji tej klasy powinien odpowiadać poniższemu wzorcowi.

class .....{
.....
public Double[] a;
public Double[] b;
public Boolean order;


public native Double[] sort01(Double[] a, Boolean order);
// zakładamy, że po stronie kodu natywnego będzie sortowana przekazana tablica a 
// (order=true oznacza rosnąco, order=false oznacza malejąco)
// metoda powinna zwrócić posortowaną tablicę
public native Double[] sort02(Double[] a); 
// zakładamy, że drugi atrybut będzie pobrany z obiektu przekazanego do metody natywnej (czyli będzie brana wartoć pole order) 
public native void sort03();
// zakładamy, że po stronie natywnej utworzone zostanie okienko pozwalające zdefiniować zawartoć tablicy do sortowania 
// oraz warunek okrelający sposób sortowania order.
// wczytana tablica powinna zostać przekazana do obiektu Javy na pole a, za warunek sortowania powinien zostać przekazany
// do pola orded
// Wynik sortowania (tablica b w obiekcie Java) powinna wyliczać metoda Javy multi04 
// (korzystająca z parametrów a i order, wstawiająca wynik do b).

public void sort04(){
 ..... // sortuje a według order, a wynik wpisuje do b
 }

}

Działanie metod należy przetestować. Podczas testowania należy wygenerować odpowiednio duży problem obliczeniowy. Testowanie powinno dać odpowied na pytanie, która z implementacji jest wydajniejsza. Stąd uruchamianiu metod powinno towarzyszyć mierzenie czasu ich wykonania (czyli co na kształt testów wydajnociowych). Sposób wykonania takich testów jest dowolny, jednak warto spróbować wykorzystać do tego np. framework Mockito.

Jako wynik zadania, oprócz kodów ródłowych Java oraz kodu zaimplementowanej natywnej biblioteki, należy dostarczyć również raport z omówieniem wyników testowania. Proszę zwrócić uwagę na to, czy podczas testów nie uwidoczniło się działanie JIT.

Lab09

Podczas laboratoriów należy przećwiczyć różne sposoby przetwarzania dokumentów XML. Aby zadanie nie było zbyt skomplikowane, a jednoczenie umożliwiało zapoznanie się z różnymi technologiami, zdefiniowano je w następujący sposób:

Napisz program pozwalający:
1. wczytać dane XML korzystając z JAXB i wyrenderować je na ekranie (tutaj należy zdefiniować klasę mapującą się do struktury dokumentu XML oraz skorzystać z serializatora/deserializatora).
2. wczytać dane XML korzystając z JAXP i wyrenderować je na ekranie (tutaj należy uruchomić parsery SaX i DOM).
3. przetworzyć dane z dokumentu XML za pomocą wybranego arkusza transformacji XSLT i wywietlić wynik tego przetwarzania. Przetwarzanie może polegać na wygenerowaniu i wyrenderowaniu htmla powstałego na skutek zaaplikowania wybranego arkusza XSLT do dokumentu XML (arkusz może "wycinać" niektóre elementy, "wyliczać" jakie wartoci, ostylowywać tekst itp.). Program powinien umożliwić wybór używanego arkusza sporód kilku arkuszy składowanych w zadanym miejscu. Chodzi tu, między innymi, o to, by dało się zmienić "działanie programu" już po jego kompilacji (poprzez podmianę/wskazanie używanego arkusza).

Dane XML do przetwarzania mają pochodzić z publicznie dostępnych repozytoriów (z licencjami pozwalającymi na ich użytkowanie przynajmniej do celów edukacyjnych). Na przykład mogą do być dane opublikowane pod linkami:
https://bip.poznan.pl/api-xml/bip/dane-o-srodowisku-i-ochronie/A/,
https://catalog.data.gov/dataset/popular-baby-names,
https://www.floatrates.com/feeds.html,
Dane mogą dotyczyć np. wyników piłkarskich rozgrywek ligowych (często dane te udostępniane są na komercyjnych zasadach, ale istnieją też darmowe przykłady do pobrania, jak: http://xmldocs.sports.gracenote.com/XML-Downloads-and-Samples_1048597.html)

Istnieje co takiego jak "xml feeds"(strumienie danych XML), w szczególnoci "atom feeds" - ale to jest temat do osobnego omówienia (patrz https://www.ibm.com/docs/en/cics-ts/5.5?topic=support-overview-atom-feeds).

Lab10

Zaimplementuj aplikację pozwalającą na szyfrowanie/deszyfrowanie plików (taka aplikacja mogłaby, na przykład pełnić, rolę narzędzia służącego do szyfrowania/odszyfrowywania załączników do e-maili). 
Na interfejsie graficznym aplikacji użytkownik powinien mieć możliwoć wskazania plików wejciowych oraz wyjciowych, jak również algorytmu szyfrowania/deszyfrowania oraz wykorzystywanych kluczy: prywatnego (do szyfrowania) i publicznego (do deszyfrowania).
Cała logika związana z szyfrowaniem/deszyfrowaniem powinna być dostarczona w osobnej bibliotece, spakowanej do podpisanego cyfrowo i wyeksportowanego pliku jar (do przedyskutowania na początku zajęć jest, czy działać w niej ma menadżer bezpieczeństwa korzystający z dostarczonego pliku polityki).
Projekt opierać ma się na technogiach należących do Java Cryptography Architecture (JCA) i/lub Java Cryptography Extension (JCE). Proszę zwrócić uwagę na ograniczenia związane z rozmiarem szyfrowanych danych narzucane przez wybrane algorytmu (zależy nam, by zaszyfrować dało się pliki o dowolnym rozmiarze).
W trakcie realizacji laboratorium będzie trzeba skorzystać z repozytoriów kluczy i certyfikatów.  Ponadto proszę zapoznać się z zasadami korzystania z narzędzia jarsigner. 
Proszę w gitowym repozytorium kodu w gałęzi sources/releases stworzyć osobne podkatalogi: na bibliotekę (biblioteka) oraz na aplikację (aplikacja).


Lab11

Bazując na kodzie stworzonym w trakcie poprzednich laboratoriów przygotuj: a) wielowydaniowy jar (projekty poszczególnych wydań mają być modułowe - Java Platform Module System (JPMS)), b) instalator aplikacji. Opis tego czym jest wielowydaniowy jar pojawi się na wykładzie. Towarzyszące mu materiały znaleć można na stronie powięconej kursowi.  Instalator aplikacji może być wykonany dowolnym narzędziem. Ponadto dla ciekawych polecane jest sprawdzenie, jak działa narzędzie jpackage. 

Proszę zauważyć, że z uwagi na wprowadzenie systemów modułów nieco kłopotliwe jest generowanie wielowydaniowego jara zawierającego kod dla JDK8 oraz JDK9 i wyżej. Dlatego często wielowydaniowe jary są generowane tylko dla nowszych wersji JDK (JDK9 i wyżej).

Oprócz ródeł kodu w gitlabowym repozytorium proszę również umiecić krótki raport z realizacji zadania.


Lab12

Celem laboratorium jest przetrenowanie możliwoci dynamicznego rozszerzania funkcji programu Java przez ładowanie i wyładowywanie skryptów JavaScript (na podobieństwo ładowania klas własnym ładowaczem). Ponadto chodzi w nim o opanowanie technik przekazywania obiektów pomiędzy wirtualną maszyną Java a silnikiem JavaScript.

Na początek proszę przeczytać parę uwag:

Rozwój silnika JavaScript, odbywający się w ramach rozwoju JDK, został w pewnym momencie zatrzymany przez Oracle. Stwierdzono bowiem, że zadanie to realizowane jest w projekcie GraalVM, a szkoda powięcać zasoby na powielanie prac. Efekt podjętej wtedy decyzji widać było już w JDK 11, gdzie odpowiedni moduł dostarczający rzeczony silnik otagowano:
    @Deprecated(since="11", forRemoval=true)
    Module jdk.scripting.nashorn
W wersji JDK 17 tego modułu już nie ma. Przy okazji usunięto też niektóre pomocnicze narzędzia, jak uruchamiane z linii komend interpreter jjs. Co więcej, dla aplikacji JavaFX skryptowanie zostało domylnie wyłączone. Aby je włączyć, należy przekazać wirtualnej maszynie odpowiednią opcję: -Djavafx.allowjs=true

Aby dało się "skryptować" w nowszych wersjach JDK, oprócz włączenia odpowiednich opcji, można:
1. umiecić we własnym projekcie zależnoć do rozwijanego niezależnie silnika nashorn (czyli do JDK dołożyć silnik nashorn)
2. umiecić we własnym projekcie zależnoć do rozwijanego niezależnie silnika graal.js (czyli do JDK dołożyć silnik graal.js - możliwe jest też przy okazji dołączenie kompilatora JIT z dystrybucji GraalVM)
2. stworzyć projekt wykorzystując GraalVM zamiast JDK

GraalVM Community Edition wersja 22.3.2 (obecnie najnowsza, patrz https://www.graalvm.org/downloads/) zbudowano na bazie OpenJDK 11.0.17 oraz OpenJDK 17.0.5. 

Niezależna implementacja silnika nashorn - patrz https://github.com/openjdk/nashorn

Archiwum z czterema projektami: skryptowanie.zip (http://tomasz.kubik.staff.iiar.pwr.wroc.pl/dydaktyka/Java/skryptowanie.zip)

Zadanie do wykonania polega na napisaniu programu w języku Java pozwalającego na wizualizację działania automatów komórkowych, przy czym logika działania tych automatów powinna być zaimplementowana za pomocą ładowanych dynamicznie skryptów JavaScript zapisanych w plikach o znanej lokalizacji. Nazwy plików ze skryptami powinny odpowiadać nazwom automatów - by było wiadomo, co robi ładowany skrypt. Załadowane skrypty powinno dać się wyładować.

Opis przykładowych automatów komórkowych opublikowano na wiki: https://pl.wikipedia.org/wiki/Automat_kom%C3%B3rkowy.
Materiały pomocnicze można znaleć pod adresami:
http://www.oracle.com/technetwork/articles/java/jf14-nashorn-2126515.html
https://www.n-k.de/riding-the-nashorn/

Proszę w gitowym repozytorium kodu w gałęzi sources umiecić wszystkie wykorzystywane artefakty (skrypty JavaScript oraz ródła kodu Java). Proszę też zamiecić instrukcję użycia programu wraz z udokumentowanym wynikiem jego działania (plik Readme.md z dołączomymi zrzutami z ekranu).


Lab13

Znając zasady korzystania ze skryptów JS w programach napisanych w języku JavaFX można pójć o krok dalej w zabawie z tymi technologiami. Zdobytą wiedzę można wykorzystać podczas budowy aplikacji z graficznym interfejsem użytkownika opierającym się na klasach JavaFX.

Zadanie polega na zaimplementowaniu aplikacji korzystającej z JavaFX w niestandardowy sposób. Chodzi o to, by całą jej logikę aplikacji zapisać w pliku fxml zamiast w kontrolerze napisanym w języku Java. W języku Java ma być zaimplementowane tylko ładowanie fxmla. Proszę spojrzeć na przykłady zamieszczone poniżej (Sample1.fxml, Sample1.java, application.css). 

Sama aplikacja ma pozwalać na generowanie złotych myli o różneh tematyce na podstawie spreparowanych wczeniej wzorców zapisanych w plikach. Wygenerowane złote myli mogą pojawiać się na interfejsie użytkownika jako tekst, który powinno dać się skopiować. 

Do przemylenia jest, w jaki sposób uzależnić kategorię wywietalnej złotej myli od kontekstu aplikacji (może poprzez jakie atrybuty lub algorytmy AI rozpoznające nastrój użytkownika).

W gitowym repozytorium w gałęzi sources należy umiecić wszystkie ródła plus plik Readme.md (z dołączonymi zrzutami i opisem, jak uruchomić aplikację), w gałęzi releases - wykonywalny plik jar (z przygotowaniem jara może być problem - trzeba sprawdzić, czy pliki fxml, css oraz szablonów będą się ładować z tego jara, ostatecznie można je dystrybuować osobno).

########### Sample1.fxml  ###########
<?xml version="1.0" encoding="UTF-8"?>
<?language javascript?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<VBox fx:id="vbox" layoutX="10.0" layoutY="10.0" prefHeight="250.0"
	prefWidth="300.0" spacing="10" xmlns:fx="http://javafx.com/fxml/1"
	xmlns="http://javafx.com/javafx/2.2">
	
	<fx:script>
	var System = Java.type("java.lang.System")
		function buttonAction(event){
		java.lang.System.out.println("finally we get to print something.");
		outputLbl.setText("Your Input please:");
		}
	</fx:script>
	<children>

		<Label fx:id="inputLbl" alignment="CENTER_LEFT" cache="true"
			cacheHint="SCALE" prefHeight="30.0" prefWidth="200.0"
			text="Please insert Your Input here:" textAlignment="LEFT" />
		<TextField fx:id="inputText" prefWidth="100.0" />
		<Button fx:id="okBtn" alignment="CENTER_RIGHT"
			contentDisplay="CENTER" mnemonicParsing="false"
			onAction="buttonAction(event);" text="OK" textAlignment="CENTER" />

		<Label fx:id="outputLbl" alignment="CENTER_LEFT" cache="true"
			cacheHint="SCALE" prefHeight="30.0" prefWidth="200.0"
			text="Your Input:" textAlignment="LEFT" />
		<TextArea fx:id="outputText" prefHeight="100.0"
			prefWidth="200.0" wrapText="true" />
	</children>
</VBox>

########### Sample1.java  ###########
package application;

import java.io.FileInputStream;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Sample1 extends Application {
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		String fxmlDocPath = "src/Sample1.fxml";
		FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);

		VBox root = (VBox) loader.load(fxmlStream);

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("A FXML Example without any Controller");
		stage.show();

	}
}
########### application.css  ###########
/* JavaFX CSS - Leave this comment until you have at least create one rule which uses -fx-Property */
.pane {
    -fx-background-color: #1d1d1d;
}

.root {
		-fx-padding: 10;
		-fx-border-style: solid inside;
		-fx-border-width: 2;
		-fx-border-insets: 5;
		-fx-border-radius: 5;
		-fx-border-color: #2e8b57;
		-fx-background-color: #1d1d1d;
}
.label {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: white;
    -fx-opacity: 0.6;
}

.label-bright {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.label-header {
    -fx-font-size: 32pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.text-field {
    -fx-font-size: 12pt;
    -fx-font-family: "Segoe UI Semibold";
}

.button {
    -fx-padding: 5 22 5 22;   
    -fx-border-color: #e2e2e2;
    -fx-border-width: 2;
    -fx-background-radius: 0;
    -fx-background-color: #1d1d1d;
    -fx-font-family: "Segoe UI", Helvetica, Arial, sans-serif;
    -fx-font-size: 11pt;
    -fx-text-fill: #d8d8d8;
    -fx-background-insets: 0 0 0 0, 0, 1, 2;
}

.button:hover {
    -fx-background-color: #3a3a3a;
}

.button:pressed, .button:default:hover:pressed {
  -fx-background-color: white;
  -fx-text-fill: #1d1d1d;
}

.button:focused {
    -fx-border-color: white, white;
    -fx-border-width: 1, 1;
    -fx-border-style: solid, segments(1, 1);
    -fx-border-radius: 0, 0;
    -fx-border-insets: 1 1 1 1, 0;
}

.button:disabled, .button:default:disabled {
    -fx-opacity: 0.4;
    -fx-background-color: #1d1d1d;
    -fx-text-fill: white;
}

.button:default {
    -fx-background-color: -fx-focus-color;
    -fx-text-fill: #ffffff;
}

.button:default:hover {
    -fx-background-color: derive(-fx-focus-color,30%);
}


Lab14

Napisz program, który będzie symulował działanie systemu biletowego (kolejkowego) stosowanego w obsłudze klientów.

Systemy biletowe można spotkać w służbie zdrowia, administracji publicznej itp. W systemach tych klienci wybierają na panelu biletomatu kategorię sprawy, a następnie otrzymują bilet, który uprawnia ich do obsługi. Na bilecie wypisany jest identyfikator: symbol odpowiadający kategorii sprawy oraz kolejny numer. Obsługa klientów odbywa się na numerowanych stanowiskach. O tym, kto w danej chwili może być obsłużony oraz jaka jest lista oczekujących można przeczytać na tablicach informacyjnych (wywietlane na nich są numery stanowisk oraz listy oczekujących - z podaniem identyfikatora biletu).

Program powinien wywietlać interfejsy: biletomatów (gdzie można wybrać kategorię sprawy i pobrać bilet), stanowisk (gdzie wywietla się identyfikator aktualnie obsługiwanego biletu i gdzie można zatwierdzić obsługę sprawy, by dało się przejć do następej sprawy w kolejce) oraz tablic informacyjnych (na których widać listy kolejkowe). 

Lista kategorii spraw ma być parametrem programu.
Kategorie spraw powinny mieć przypisane priorytety wpływające na kolejnoć obsługi (czyli wpływ na kolejnoć pobrania kolejnego biletu do obsługi na stanowisku mają wpływ numery biletów oraz priorytety kategorii spraw).
Kategorie spraw obsługiwanych na danym stanowisku powinny być konfigurowale.

Zanurz w programie agenta obsługującego ziarenko, które pozwali na zmianę parametrów programu (listy kategorii spraw oraz priorytetów, listę kategorii przypisanych do obsługi na danym stanowisku). Ziarenko to powinno również generować notyfikacje przy każdej zmianie parametrów inicjowanej przez użytkownika aplikacji (nie powinno tych notyfikacji wysyłać, jeli zmiana parametrów odbywać się będzie przez nie same).

Podsumowując, ziarenko powinno posiadać/obsługiwać:
- właciwoć (pozwalającą na ustawianie/odczytywanie kategorii spraw),
- metodę zmieniającą priorytety spraw, metodę zmieniającą przypisanie kategorii obsługiwanych spraw na danym stanowisku,
- notyfikację.

Komunikacja z agentem odbywać się ma za pomocą aplikacji klienckiej (jconsole lub jmc).