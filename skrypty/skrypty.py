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
        print('1. Wstaw admina do bazy')
        print('2. Wstaw przykładowe lekcje do bazy')
        print('q. Wyjdź')
        print()
        choice = input("Wybór: ")

        if choice == '1':
            insert_admin()
        elif choice == '2':
            test_lessons()
        elif choice == 'q':
            exit(0)
        else:
            print("Nieprawidłowy wybór.")

        print()


def insert_admin():
    subprocess.run(['docker', 'exec', '-it', 'englishsubtitledbackend_db_1',
                    'psql', '-h', 'localhost', '-p', '5432', '-U', 'es_admin', 'english_subtitled',
                    '-c', 'INSERT INTO users (user_id, admin, facebook_user_id) VALUES (1, TRUE, {});'.format(FACEBOOK_USER_ID)],
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


if __name__ == '__main__':
    main()
