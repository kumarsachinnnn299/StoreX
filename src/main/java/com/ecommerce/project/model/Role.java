package com.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Integer roleId;
    @Enumerated(EnumType.STRING)//This is don because enum by default are persisted in db as Integer and we
                                    //want to persist it as string
    @Column(length = 20, name="role_name")
    @ToString.Exclude
    private AppRole roleName;

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
