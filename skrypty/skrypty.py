#!/usr/bin/python3
import subprocess
import requests

FACEBOOK_USER_ID = 108078020102724
# Valid access token of the user above
FACEBOOK_TOKEN = 'EAAKbRPy4D7EBAKs5MmOQPE6dx22XHBalDiAOrpIgqQ2o5cPFfwWTwN3dMOMTvEKLzirJvplTn9YJV1YFZAEU0ygWu0tWZC3JimmjU5FxlrORbUROo2sGxF45Moowtxw1ZA5groLft3h0a2sTQRK9ld63T87XZBD0xAvyPaFmPF2uSXB2JbN61MQhZBirHTeWHRyWKqHvaWQqZCS1pBUzF8qnlKLtqwFuED9VgEIcQTURYPdQcNth5U'


def main():
    print("Skrypt należy uruchamiać jako administrator.")
    print("Pamiętaj, aby ustawić obie zmienne w pierwszych liniach tego skryptu przed jego użyciem.")
    print()
    menu()


def menu():
    while True:
        print('0. Init (odpowiednik 1+2)')
        print('1. Wstaw admina do bazy')
        print('2. Wstaw przykładowe lekcje do bazy (wymaga [1])')
        print('3. Zmodyfikuj przykładowe lekcje wstawione [2] (test /lessons PUT)')
        print('4. Dodaj zakładkę adminowi (wymaga [2], test /bookmarks PUT)')
        print('q. Wyjdź')
        print()
        choice = input("Wybór: ")

        if choice == '0':
            insert_admin()
            test_lessons()
        elif choice == '1':
            insert_admin()
        elif choice == '2':
            test_lessons()
        elif choice == '3':
            modify_lessons()
        elif choice == '4':
            add_bookmark()
        elif choice == 'q':
            exit(0)
        else:
            print("Nieprawidłowy wybór.")

        print()


def insert_admin():
    r = requests.get(url="http://localhost:8080/progress", headers={'Authorization': FACEBOOK_TOKEN})
    print("Rejestrowanie użytkownika poprzez wykonanie zapytania 'progress' [GET]: " + str(r.status_code) + " " + str(r.content))
    print("Ustawianie uprawnień admina: ")
    subprocess.run(['docker', 'exec', '-it', 'englishsubtitledbackend_db_1',
                    'psql', '-h', 'localhost', '-p', '5432', '-U', 'es_admin', 'english_subtitled',
                    '-c', 'UPDATE users SET admin=TRUE WHERE facebook_user_id=\'{}\';'.format(FACEBOOK_USER_ID)],
                   timeout=10)


def test_lessons():
    r = requests.post(url="http://localhost:8080/lessons", json=[
        {
            "lessonTitle": "Big Buck Bunny - full transcription",
            "filmTitle": "Big Buck Bunny",
            "translations": []
        },
        {
            "lessonTitle": "Mock Movie with words by Justyna",
            "filmTitle": "Mock Movie",
            "translations": [
                {
                    "engWord": "home",
                    "plWord": "dom"
                },
                {
                    "engWord": "bag",
                    "plWord": "torba"
                },
                {
                    "engWord": "computer",
                    "plWord": "komputer"
                },
                {
                    "engWord": "bike",
                    "plWord": "rower"
                },
                {
                    "engWord": "dog",
                    "plWord": "pies"
                },
                {
                    "engWord": "cat",
                    "plWord": "kot"
                },
                {
                    "engWord": "frog",
                    "plWord": "żaba"
                },
                {
                    "engWord": "bed",
                    "plWord": "łóżko"
                },
                {
                    "engWord": "leg",
                    "plWord": "noga"
                },
                {
                    "engWord": "sleep",
                    "plWord": "spać"
                },
                {
                    "engWord": "nose",
                    "plWord": "nos"
                },
                {
                    "engWord": "eat",
                    "plWord": "jeść"
                },
                {
                    "engWord": "talk",
                    "plWord": "mówić"
                },
                {
                    "engWord": "live",
                    "plWord": "żyć"
                }
            ]
        }
    ], headers={'Authorization': FACEBOOK_TOKEN})
    print(str(r.status_code) + " " + str(r.content))


def modify_lessons():
    r = requests.put(url="http://localhost:8080/lessons", json=[
        {
            "lessonTitle": "Big Buck Bunny - full transcription",
            "filmTitle": "Big Buck Bunny",
            "translations": [
                {
                    "engWord": "big",
                    "plWord": "wielki"
                },
                {
                    "engWord": "buck",
                    "plWord": "bryknięcie"
                },
                {
                    "engWord": "bunny",
                    "plWord": "królik"
                }
            ]
        },
        {
            "lessonTitle": "Mock Movie with words by Justyna",
            "filmTitle": "Mock Movie",
            "translations": [
                {
                    "engWord": "home",
                    "plWord": "ojczyzna"
                },
                {
                    "engWord": "bag",
                    "plWord": "torba"
                },
                {
                    "engWord": "computer",
                    "plWord": "komputer"
                },
                {
                    "engWord": "bike",
                    "plWord": "rower"
                }
            ]
        }
    ], headers={'Authorization': FACEBOOK_TOKEN})
    print(str(r.status_code) + " " + str(r.content))


def add_bookmark():
    r = requests.put(url="http://localhost:8080/bookmarks", json={"engWord": "bag", "plWord": "torba"},
                     headers={'Authorization': FACEBOOK_TOKEN})
    print(str(r.status_code) + " " + str(r.content))


if __name__ == '__main__':
    main()
