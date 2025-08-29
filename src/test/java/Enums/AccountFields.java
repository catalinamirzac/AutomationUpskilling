package Enums;

import com.github.javafaker.Faker;

public enum AccountFields {
    NAME("name"),
    FIRSTNAME("firstname"),
    LASTNAME("lastname"),
    EMAIL("email"),
    PASSWORD("password"),
    TITLE("title"),
    BIRTH_DATE("birth_date"),
    BIRTH_MONTH("birth_month"),
    BIRTH_YEAR("birth_year"),
    COMPANY("company"),
    ADDRESS1("address1"),
    ADDRESS2("address2"),
    COUNTRY("country"),
    STATE("state"),
    CITY("city"),
    ZIPCODE("zipcode"),
    MOBILE_NUMBER("mobile_number");

    private final String key;

    AccountFields(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static AccountFields fromKey(String key) {
        for (AccountFields field : values()) {
            if (field.getKey().equalsIgnoreCase(key)) {
                return field;
            }
        }
        throw new IllegalArgumentException("Unknown form field key: " + key);
    }

    public String generate(Faker faker) {
        switch (this) {
            case NAME:
                return faker.name().fullName();
            case FIRSTNAME:
                return faker.name().firstName();
            case LASTNAME:
                return faker.name().lastName();
            case EMAIL:
                return faker.internet().emailAddress();
            case PASSWORD:
                return faker.internet().password();
            case COMPANY:
                return faker.company().name();
            case ADDRESS1:
            case ADDRESS2:
                return faker.address().streetAddress();
            case CITY:
                return faker.address().city();
            case STATE:
                return faker.address().state();
            case COUNTRY:
                return faker.address().country();
            case ZIPCODE:
                return faker.address().zipCode();
            case MOBILE_NUMBER:
                return faker.phoneNumber().cellPhone();
            case TITLE:
                return faker.demographic().sex();
            case BIRTH_DATE:
                return String.valueOf(faker.number().numberBetween(1, 28));
            case BIRTH_MONTH:
                return String.valueOf(faker.number().numberBetween(1, 12));
            case BIRTH_YEAR:
                return String.valueOf(faker.number().numberBetween(1970, 2005));
            default:
                return "unknown";
        }
    }
}

