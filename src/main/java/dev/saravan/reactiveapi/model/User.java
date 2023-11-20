package dev.saravan.reactiveapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("reactive_user")
@Data
@NoArgsConstructor
public class User {
    @Id
    private Integer id;
    private String name;
    private Integer score;

    @Column("address_id")
    private Long addressId;

    @Transient
    private Address address;

    public User(Integer id, String name, Integer score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public User( String name, Integer score, Long addressId) {
        this.name = name;
        this.score = score;
        this.addressId = addressId;
    }

    public User(Integer id, String name, Integer score, Long addressId, Address address) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.addressId = addressId;
        this.address = address;
    }

    public User( String name, Integer score, Address address) {
        this.name = name;
        this.score = score;
        this.address = address;
    }

    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", addressId=" + addressId +
                ", address=" + address +
                '}';
    }
}