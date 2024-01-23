package models;

import lombok.Data;

@Data
public class DeleteRequestModel{
	private String isbn;
	private String userId;
}