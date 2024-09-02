package com.giansiccardi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String accountNumber;

    private String accountHolderName;

    private String cvu;

    private String bankName;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//No se incluirá en la serialización, lo que significa que no aparecerá en la respuesta JSON cuando el objeto se convierta en JSON.
    private Customer customer;
}
