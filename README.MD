# [EnglishSubtitled] Back-end

Repozytorium zawiera back-end dla systemu będącego projektem inżynierskim.

## Uruchamianie
Aplikacja wymaga programów `docker` i `docker-compose` w celu uruchomienia bazy danych:

`sudo docker-compose up -d` uruchamia bazę danych w tle.

### Przykładowe lekcje

Aby dodać do bazy danych aplikacji przykładowe lekcje, należy wykonać skrypt `skrypty/testoweLekcje.sh` (wymaga programu `curl` i systemu Linux/Mac) lub wykonać zawarte w nim zapytanie w inny sposób.

## API

Pogrubione wpisy są szczególnie istotne dla klienta.

Wszystkie endpointy oprócz `lessons` [GET] wymagają nagłówka `Authorization` z wartością Facebookowego *access tokena* 
* **`lessons` [GET]** - lista wszystkich lekcji, bez tłumaczeń;
* `lessons` [POST] - tworzenie nowych lekcji na podstawie listy obiektów zawartej w treści żądania [WYMAGA UPRAWNIEŃ ADMINA];
* `lessons` [PUT] - tworzenie lub modyfikacja istniejących lekcji na podstawie listy obiektów zawartej w treści żądania [WYMAGA UPRAWNIEŃ ADMINA];
* **`lessons/{lessonId}` [GET]** - pełna reprezentacja pojedynczej lekcji (wraz z tłumaczeniami);
* `lessons/{lessonId}` [DELETE] - usuwanie lekcji o ID lessonId [WYMAGA UPRAWNIEŃ ADMINA];
* **`films` [GET]** - lista wszystkich filmów, każdy film zawiera listę wszystich związanych z nim lekcji (bez tłumaczeń) - stąd wynik zapytania zawiera wszystkie lekcje;
* **`films/{filmTitle}` [GET]** - pojedynczy film, o tytule filmTitle, z listy zwracanej przez `films` [GET].

## Autorzy
Justyna Maciąg, Paweł Taborowski, Agnieszka Warchoł
