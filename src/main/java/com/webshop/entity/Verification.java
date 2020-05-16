package com.webshop.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class Verification {

    @Id
    @Column
    private String id;

    @Column
    private String username;

    public Verification(String id, String username) {
        setId(id);
        setUsername(username);
    }

}
