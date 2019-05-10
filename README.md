# Simple REST-API 
#Catalog of customers, computer devices and store sales bills.
## Usage
* [Usage](#Usage)
    + [General view of requests](#General-view-of-requests)
    + [Get with additional options](#Get-with-additional-options)
    + [Some query examples](#Some-query-examples)
    + [Сustom device types](#Сustom-device-types)
    + [Сustom colors](#Сustom-colors)
    + [Error handling](#Error-handling)
    + [UI](#User-interface)

## General view of requests


* Getting a list of entities:
```
GET /api/<entity>?<Filter parameters><paging paremeters>
```

* Getting one entity by id:
```
GET /api/<entity>/{id}
```
* Creating a new entity:
```
POST /api/<entity> (Body: JSON)
```
### Supported entities

`customer: `

| Field Name | Type                            | Description                       |   Example  |
|:----------:|:-------------------------------:|-----------------------------------|:----------:|
|  firstName | string                          |                                   |    Петр    |
| middleName | string                          |                                   |  Петрович  |
|  lastName  | string                          |                                   |   Петров   |
|  birthdate | string, date format: dd.MM.YYYY | birth date                        | 25.04.2019 |
|     id     | Integer number                  | unique id generated upon creation |    9000    |
|

`device: `

|    Field Name   |               Type              |             Description             |                Example                |
|:---------------:|:-------------------------------:|:-----------------------------------:|:-----------------------------------:|
|    deviceType   |              string             | device type (from a predefined set) | Smartphone Laptop Smart Watch Tablet  |
|    modelName    |              string             |                                     |              Galaxy S10+              |
| manufactureDate | string, date format: dd.MM.YYYY | date of issue                       |               25.04.2019              |
|   manufacturer  |              string             |                                     |                Samsung                |
|    colorName    |              string             |                                     |                 черный                |
|     colorRGB    |          Integer number         | integer representation (rgb)        |                   0                   |
|      price      |          Integer number         | integer price                       |                12499000               |
|        id       |          Integer number         | unique id generated upon creation   |                1126970                

`bill: `

|    Field Name    |                     Type                     |             Description            |       Example       |
|:----------------:|:--------------------------------------------:|:----------------------------------:|:-------------------:|
|    customerId    |                Integer number                |                                    |        9001         |
|    totalPrice    |                Integer number                | integer price represented in penny |       31415926      |
| purchaseDateTime | String, date/time format:dd.MM.YYYY HH:mm:ss | purchase time and date             | 01.01.2019 07:50:22 |
|       items      |               List of BillItem               |                                    |                     |


`BillItem: ` (as part of `bill`)

| Field Name |      Type      |           Description           | Example |
|:----------:|:--------------:|:-------------------------------:|:-------:|
|  deviceId  | Integer number |                                 | 1004709 |
|  quantity  | Integer number | number of devices               |   100   |
|    price   | Integer number | price at the moment of purchase |   490   |

* *you should not specify `id` in POST request, otherwise it will be considered as an invalid field*
* *if you specify `colorName` and `colorRGB` for device in POST request than new color with this name and colorRGB will be added to color storage*
* *if you specify only `colorName` than this api tries to find this color name in color storage. In case of success, json will return with a corresponding colorRGB code, otherwise it will not accept the `POST` query*
* *only parameter `colorRGB` is not required*
* *api doesn't accept empty or null fields*

### Get with additional options
```
GET /api/<entity> options
```
* *`orderBy` : `String` - fields to order by separated by comma, e.g. `manufactureDate,-totalPrice`*
* *`pageItems` : `long` - number of elements on a single page*
* *`page` : `long` - page number*
* *`offset` : `long` - offset param*
* *the default is paginated output from 1 page with 10 entities per page, but if you specified the `offset` parameter and don't specify the `page` parameter than the offset output will be used*
* *anything else is considered a filter option*

* *some fields support range: `birthdate`, `price`, `manufactureDate`, `totalPrice`, `purchaseDateTime`, e.g. `priceFrom`, `priceTo` e.t.c.*
* *some fields support search by prefix: `firstName`, `middleName`, `lastName`, `modelName`, `deviceType`, `manufacturer`, e.g. `firstNamePrefix`, `modelNamePrefix`, `manufacturerPrefix` e.t.c.*
* *`-` can be used before value of orderBy to reverse the result, e.g. `orderBy=-totalPrice`*
* *for the `bill` entity filtering queries by nested items are also available:*
```
GET /api/bill?deviceId=11&quantity=30&price=499
```
### Some query examples

```
POST /api/device
body:
{
 	"price": "499",
  	"deviceType": "Smartphone",
  	"manufactureDate": "09.01.2007",
  	"colorName": "black",
  	"colorRGB" : "0",
  	"manufacturer": "Foxconn",
  	"modelName": "iPhone"
}

GET /api/device?priceFrom=100&priceTo=500&orderBy=manufacturer
```
### Сustom device types
* *you can add new device type to device type storage and see the available storage types by `GET` query*
```
GET /api/device/type (no arguments)
POST /api/device/type (Body: String)
```
#### Predefined device types

| Device type
|:----------:|
|    smartphone   |
|     laptop   |
|     smart watches   |
|     tablet   |

### Сustom colors
* *you can add new color to color storage and see the available storage colors by `GET` query*
```
GET /api/device/color (no arguments)
POST /api/device/color (Body: JSON)
```

`color: `

| Field Name |      Type      |            Description            | Example |
|:----------:|:--------------:|:---------------------------------:|:-------:|
|    colorName    |     String     | color name                        |   black   |
|     colorRGB    | Integer number | integer representation (rgb)      |    0    |


#### Predefined colors

| Color name |    rgb(r, g, b)    |
|:----------:|:------------------:|
|    черный   | rgb(0, 0, 0)       |
|    серый    | rgb(128, 128, 128) |
|     красный    | rgb(255, 0, 0)     |
|   Золотистый   | rgb(255, 215, 0)   |
|    Синий    | rgb(0, 0, 255)     |
|   Серебристый   | rgb(192, 192, 192) |
|    Белый   | rgb(255, 255, 255) |
|    Коричневый   | rgb(150, 75, 0)    |
|   Оранжевый   | rgb(255, 165, 0)   |
|    Бежевый   | rgb(245, 245, 220) |
|   Желтый   | rgb(255, 255, 0)   |
|    Зеленый   | rgb(0, 128, 0)     |
| Голубой | rgb(66, 170, 255)  |
|   Фиолетовый   | rgb(139, 0, 255)   |
|    Розовый    | rgb(252, 15, 192)  |


### Error handling

In case of error this JSON will be returned:

|   Field Name  |  Type  |  Description  |     Example     |
|:-------------:|:------:|:-------------:|:---------------:|
|   error.type  | String | error type    |  invalid query parameter format  |
| error.message | String | error description | the value '20-04-1980s' of 'birthdate' parameter does not match the date format: dd.MM.yyyy |

### User interface
*For convenience and clarity, you can also use UI, which is a simple html page with input fields. It supports all available `GET` and `POST` queries. (Coming soon)*
