curl -XPOST -H "Content-type: application/json" -d '[
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
]' 'localhost:8080/lessons'