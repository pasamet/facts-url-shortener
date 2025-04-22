# facts-url-shortener

An URL shortener API

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
quarkus dev
```

Type `r` to run unit tests

## API

### Shorten fact end point

`curl -X POST http://localhost:8080/facts`

Fetches a random fact from the Useless Facts API and provides a short link

Response:

```json
{
  "original_fact": "string",
  "shortened_url": "string"
}
```

### List facts end point

Lists previously cached facts

`curl http://localhost:8080/facts`

Response:

```json
 [
  {
    "fact": "string",
    "original_permalink": "string"
  },
  ...
]
```

### List facts end point

Lists previously cached facts

`curl http://localhost:8080/facts`

Response:

```json
 [
  {
    "fact": "string",
    "original_permalink": "string"
  },
  ...
]
```

### Single fact end point

Return the fact associated with given `<shortened_url>`

`curl http://localhost:8080/facts/<shortened_url>`

Response:

```json
{
  "fact": "string",
  "original_permalink": "string"
}
```

### Redirect to fact end point

Redirects to the original permalink of the given `<shortened_url>`

`curl http://localhost:8080/facts/<shortened_url>/redirect`

### Access statistics

Lists access counts for each short link

`curl http://localhost:8080/admin/statistics`

Response:

```json
 [
  {
    "shortened_url": "string",
    "access_count": "integer"
  },
  ...
]
```
