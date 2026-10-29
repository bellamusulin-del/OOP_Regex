# OOP_Regex
# Validator Library

A small reusable Java validation library based on the Strategy design pattern.
Each validation rule implements `Validator<T>`, so rules can be replaced or
combined without changing the form-validation code.

## Requirements

- Java 17 or newer
- Maven
- JUnit 5 for running the tests

## How it works

`Validator<T>` is a functional interface with one method:
`Eredmeny ellenoriz(T ertek)`. The `Eredmeny` record stores whether the value
is valid and a list of error messages.

The `es()` combinator runs both validators and combines their errors.
The `vagy()` combinator accepts the value if either validator accepts it.
The `uzenettel()` combinator replaces validation errors with a custom message.

`UrlapValidator` maps field names to validation strategies. It checks every
configured field and collects all errors instead of stopping at the first one.

## Usage examples

### Example 1: Validate an email address

```java
Validator<String> email = new EmailValidator();

Eredmeny eredmeny = email.ellenoriz("lili@example.com");

System.out.println(eredmeny.ervenyes());


Example 2: Combine validators
Validator<String> emailOrPhone =
        new EmailValidator().vagy(new TelefonszamValidator());

Eredmeny eredmeny = emailOrPhone.ellenoriz("+40 745 123 456");


Example 3: Validate a complete form
UrlapValidator form = new UrlapValidator()
        .mezo("email", new EmailValidator())
        .mezo("phone", new TelefonszamValidator())
        .mezo("password", new JelszoValidator());

Map<String, String> values = new HashMap<>();
values.put("email", "lili@example.com");
values.put("phone", "0745 123 456");
values.put("password", "Tulipan#2024");

UrlapEredmeny result = form.ellenoriz(values);

if (!result.ervenyes()) {
    result.osszesHiba().forEach(System.out::println);
}
