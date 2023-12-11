package ru.digital.design;

public class User {
    private final Long id;
    private final String lastName;
    private final Long age;
    private final Double cost;
    private final Boolean active;

    public static class UserBulder
    {
        private Long id = null;
        private String lastName = null;
        private Long age = null;
        private Double cost = null;
        private Boolean active = null;

        public UserBulder id(long val)
        {
            this.id = val;
            return this;
        }

        public UserBulder lastName(String val) {
            this.lastName = val;
            return this;
        }

        public UserBulder age(long val) {
            this.age = val;
            return this;
        }

        public UserBulder cost(double val) {
            this.cost = val;
            return this;
        }

        public UserBulder active(boolean val) {
            this.active = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", cost=" + cost +
                ", active=" + active +
                '}';
    }

    private User() {
        throw new IllegalArgumentException("Wrong constructor call. Use Builder!");
    }
    private User(UserBulder builder) {
        id = builder.id;
        lastName = builder.lastName;
        age = builder.age;
        cost = builder.cost;
        active = builder.active;
    }
}
