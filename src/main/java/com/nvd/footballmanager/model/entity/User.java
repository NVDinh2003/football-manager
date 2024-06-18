package com.nvd.footballmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nvd.footballmanager.model.BaseModel;
import com.nvd.footballmanager.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User extends BaseModel {

    @Column(nullable = false, length = 30)
    private String username;
    @Column(nullable = false, length = 75)
    @JsonIgnore
    private String password;
    @Column(nullable = false, length = 30)
    private String name;
    private Date dob;
    @Column(nullable = false, unique = true, length = 50)
    private String email;
    @Column(unique = true, length = 15)
    private String phoneNumber;
    private String avatar;
    @Enumerated(EnumType.STRING)    // giá trị của enum sẽ được lưu dưới dạng string trong db
    @Column(length = 9)
    private UserRole role;
    @Column(columnDefinition = "boolean default false")
    private Boolean enabled = false;
    @JsonIgnore
    private Long verificationCode;

    // các entity con sẽ tự động bị xóa khỏi db nếu không còn được tham chiếu bởi entity cha.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Member> members;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MembershipRequest> membershipRequests;

    public User() {
        this.enabled = false;
    }
}
