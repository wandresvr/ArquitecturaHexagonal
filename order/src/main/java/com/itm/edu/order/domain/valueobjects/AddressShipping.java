package com.itm.edu.order.domain.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressShipping {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public static class AddressShippingBuilder {
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String country;

        public AddressShippingBuilder street(String street) {
            if (street == null || street.trim().isEmpty()) {
                throw new IllegalArgumentException("La calle no puede estar vacía");
            }
            this.street = street;
            return this;
        }

        public AddressShippingBuilder city(String city) {
            if (city == null || city.trim().isEmpty()) {
                throw new IllegalArgumentException("La ciudad no puede estar vacía");
            }
            this.city = city;
            return this;
        }

        public AddressShippingBuilder state(String state) {
            if (state == null || state.trim().isEmpty()) {
                throw new IllegalArgumentException("El estado no puede estar vacío");
            }
            this.state = state;
            return this;
        }

        public AddressShippingBuilder zipCode(String zipCode) {
            if (zipCode == null || zipCode.trim().isEmpty()) {
                throw new IllegalArgumentException("El código postal no puede estar vacío");
            }
            this.zipCode = zipCode;
            return this;
        }

        public AddressShippingBuilder country(String country) {
            if (country == null || country.trim().isEmpty()) {
                throw new IllegalArgumentException("El país no puede estar vacío");
            }
            this.country = country;
            return this;
        }

        public AddressShipping build() {
            return new AddressShipping(street, city, state, zipCode, country);
        }
    }
}
