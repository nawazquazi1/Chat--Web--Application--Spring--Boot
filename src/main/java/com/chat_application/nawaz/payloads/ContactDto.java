package com.chat_application.nawaz.payloads;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ContactDto {
	
	private String contactId;
	private Long userId;
	private Long contactUserId;
	private Date addedOn;

}
