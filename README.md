**Example for using a module with an HATEOAS  approach.**

**Technologies that were used:**

* Spring Data

* Spring Data Rest

* Configuring Jackson Moduls(for spring data rest)

* Spring Data HAL (HATEOAS impl)

**The servies was designed with an DDD aproach with two bounded contexts:**

* Order

* Payment


Example of response:
```
{
  "amount": "RUB200",
  "creditCard": {
    "version": 0,
    "number": "7777777777778",
    "cardHolderName": "Sergey Dayneko",
    "expirationDate": "2020-02-02"
  },
  "_links": {
    "shop:order": {
      "href": "http://localhost:8787/orders/1{?projection}",
      "templated": true
    },
    "curies": [
      {
        "href": "http://localhost:8787/docs/{rel}.html",
        "name": "shop",
        "templated": true
      }
    ]
  }
}