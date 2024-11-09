package br.com.sirio.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(columnDefinition = "serial")
	private Long id;

	@NotNull
	@Column(name = "cpf", unique = true)
	private String cpf;

	@NotNull
	@Column(name = "name")
	private String name;

	@NotNull
	@Column(name = "password")
	private String password;

	@Column(name = "address")
	private String address;

	@Column(name = "number_address")
	private Integer numberAddress;

	@Column(name = "additional_address")
	private String additionalAddress;

	@Column(name = "district")
	private String district;

	@Column(name = "state")
	private String state;

	@Column(name = "zip_code")
	private Integer zipCode;

	@Enumerated(EnumType.STRING)
	private StatusType status;

	@Column(name = "created_at")
	private Instant createdAt;

	@Column(name = "upadated_at")
	private Instant upadatedAt;

	@Column(name = "deleted_at")
	private Instant deletedAt;

	@Column(name = "created_user_id")
	private Long createdUser;

	@Column(name = "upadated_user_id")
	private Long upadatedUser;

	@Column(name = "deleted_user_id")
	private Long deletedUser;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name="role_id"))
	private List<Role> roles;
}
