package com.chat_application.nawaz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts")
public class Contact {
	
	@Id
	private String contactId;
	
	private String contactName;
	
	@ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
	private User userId;
	
	@ManyToOne
    @JoinColumn(name = "contact_user_id", referencedColumnName = "id")
	private User contactUserId;
	
	@Column(nullable = false)
	private Date addedOn;
}
