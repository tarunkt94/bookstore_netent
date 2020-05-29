###Building the project

After the pulling the repository, go to the base of the repository and run

`docker build ./ -t bookstoreapp`
`docker-compose up`

This starts the SpringBoot service on port 8080 while connecting to the postgres database which runs on a different docker container

You can connect to the DB from any tool with the following information  

```
URL : jdbc:postgresql://localhost:5432/bookstore
user : postgres
password : postgres 
```

###Design of the Project

The BookStore contains two tables, `table: books` for storing information about the books
and `table: inventory` for storing inventory information of the books

```create table  if not exists public.books (
   
       id serial  primary  key  not null,
       title varchar not null,
       author varchar not null,
       isbn varchar unique not null,
       price float not null
   );
   
   create table  if not exists public.inventory (
       id serial  primary key not null,
       no_of_copies int not null,
       book_id int references public.books(id)
   );
``` 


#### REST Controllers

There are two main controller.

`Books` Controller for storing information about the Books(Title,Author,Price,ISBN, No of copies)
`Store` Controller which exposes an API to buy books


####Books Controller


`POST /books`

```
Request Body : 

public class BookAddRequest {

    private String isbn;
    private String title;
    private String author;
    private Float price;
    private Integer noOfCopies;
}
```

Adds the book into the database if it passes all the validations and checks

Sends a `400 Bad Request` with a message when

1) Given isbn already exists in the system for another book
2) ISBN/Title/Author are empty or null
3) Price is negative
4) No of copies is negative

Sends a `500 Internal Server Error` when the system faces any exception while trying to put data into the database  
or any other unforeseen error

Sends a `200  OK` with the `bookId` in the response when it successfully adds the books

Successful response: 
```
public class BookResponse {

    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private Float price;
    private Integer noOfCopies;
}
```


`GET /books/{id}`

Sends a `404 Resource Not Found` if the given `id` doesn't exist in the system

Sends a `500 Internal Server Error` if the system encounters an exception while getting data from the database or any
unforeseen error

Sends a `200 OK` with the following response body in case of success


```
public class BookResponse {

    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private Float price;
    private Integer noOfCopies;
}
```


`GET /books`

`Filer API`

```
Reuqest Params : 
String isbn : optional
String title : optional
String author : optional
```

Sends a `500 Internal Server Error` if the system encounters an exception while getting data from the database or any
unforeseen error

Sends a `200 OK` with all the books which satisfy the criteria given in the request params with the following response body

```$xslt
public class ListBookResponse {

    List<BookResponse> books;
}

public class BookResponse {

    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private Float price;
    private Integer noOfCopies;
}

```


**Future Considerations** : The API can be paginated as an enhancement when required


`PATCH /books/{id}`

 ```Request Body :

public class BookUpdateRequest {

    private String isbn;
    private String title;
    private String author;
    private Float price;
    private Integer noOfCopies;

}

 ```

All the params in the request body are optional.

Sends a `400 Bad Request` with a message in case of

1) Another book already having the `isbn` given in the update request
2) `price` is sent negative
3) `noOfCopies` is sent negative

Sends a `404 Resource Not Found` if the given `id` doesn't exist in our system

Sends a `500 Internal Server Error` if the system encounters an exception while putting data into the database or any
unforeseen error

Updates all the data in the system according to the request sent and sends `200 OK` in case of successful
request and execution


`DELETE /books/{id}`

Deletes the book and respective inventory from the database

Sends a `404 Resource Not Found` if the given `id` doesn't exist in our system

Sends a `500 Internal Server Error` if the system encounters an exception while deleting data from  the database or any
unforeseen error

Sends `200 OK` in case of successful execution

`GET /books/mediaCoverage`
`Request Param : String isbn (Mandatory)`

```Response Body
public class MediaCoverageResponse {

    boolean success;
    String msg;
    List<String> titleList;
}
```

Sends a `500 Internal Server Error` in the system faces an error while trying to get the information from 
the database or any other unforeseen error

In all other cases, the system sens a `200 OK` response with varying response body

- Case : `isbn` doesnt exist in the system

```$xslt
       success  :false,
       msg : No book exists in the system with given ISBN
```

- Case : Error while trying to reach the URL where media coverage is fethced from

```$xslt
       success  :false,
       msg : Error while getting information regarding media coverage
```

- Case : All success 

```$xslt
    success : true,
    titleList : <list of titles>
```




#### Store Controller

`POST /store/buyBook`

```Request Body

public class BuyBookRequest {

    Integer bookId;
    Integer noOfCopies; {No of copies to buy}

}
```

Sends a `500 Internal Server Error` in case the system faces exceptions while trying to get data from the database
or any other unforeseen error

Sends a `400 Bad Request` in case the the given `bookId` doesn't exist in our system

In all other cases , the service sends `200 OK` with varying response bodies

- Case : Enough books are present in the inventory

```$xslt
    success : true,
    msg : Books bought successfully

```

- Case : Not enough books present in the inventory as per request

```$xslt
    success : true,
    msg : Not enough books available at the moment

```





 