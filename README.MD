# [EnglishSubtitled] Back-end

Repozytorium zawiera back-end dla systemu będącego projektem inżynierskim.

## Uruchamianie
Aplikacja wymaga programów `docker` i `docker-compose` w celu uruchomienia bazy danych:

`sudo docker-compose up -d` uruchamia bazę danych w tle.

### Przykładowe lekcje

Aby dodać do bazy danych aplikacji przykładowe lekcje, należy wykonać skrypt `skrypty/skrypty.py` (wymaga `Python 3` z biblioteką `Requests`) lub wykonać zawarte w nim zapytanie w inny sposób.

## API

Pogrubione wpisy są szczególnie istotne dla klienta.

Wszystkie endpointy oprócz `lessons` [GET] i `films` [GET] wymagają nagłówka `Authorization` z wartością Facebookowego *access tokena*.
 
* **`lessons` [GET]** - lista wszystkich lekcji, bez tłumaczeń;
* `lessons` [POST] - tworzenie nowych lekcji na podstawie listy obiektów zawartej w treści żądania *[Wymaga uprawnień admina]*;
* `lessons` [PUT] - tworzenie lub modyfikacja istniejących lekcji na podstawie listy obiektów zawartej w treści żądania *[Wymaga uprawnień admina]*;
* **`lessons/{lessonId}` [GET]** - pełna reprezentacja pojedynczej lekcji (wraz z tłumaczeniami); pojedynczy użytkownik może pobrać tylko 3 różne, nieukończone lekcje (ale może je oddawać nieukończone);
* `lessons/{lessonId}` [DELETE] - usuwanie lekcji o ID lessonId *[Wymaga uprawnień admina]*;
* **`films` [GET]** - lista wszystkich filmów, każdy film zawiera listę wszystich związanych z nim lekcji (bez tłumaczeń) - stąd wynik zapytania zawiera wszystkie lekcje;
* **`films/{filmTitle}` [GET]** - pojedynczy film, o tytule filmTitle, z listy zwracanej przez `films` [GET];
* **`bookmarks` [GET]** - lista słówek dodanych przez zautoryzowanego w tym zapytaniu użytkownika do słownika;
* **`bookmarks` [PUT]** - dodawanie tłumaczenia (umieszczonego w *body*) do słownika zautoryzowanego w tym żądaniu użytkownika;
* **`progress` [GET]** - lista lekcji obecnie dostępnych dla użytkownika, z podziałem na wypożyczone i ukończone;
* **`progress/{lessonId}` [PUT]** - oznaczanie lekcji jako ukończonej *[API tymczasowe, do czasu lepszego zdefiniowania końca lekcji]*;
* **`progress/{lessonId}` [DELETE]** - porzucenie postępu i oddanie nieukończonej pożyczonej lekcji (nie usuwa ukończonych lekcji).

## Autorzy
Justyna Maciąg, Paweł Taborowski, Agnieszka Warchoł
