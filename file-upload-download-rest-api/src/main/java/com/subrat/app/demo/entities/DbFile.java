package com.subrat.app.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "files")
public class DbFile {
	@Id
	@GeneratedValue
	private Long fileId;

	private String fileName;

	private String fileType;

	@Lob
	private byte[] data;

	public DbFile(String fileName, String fileType, byte[] data) {
		super();
		this.fileName = fileName;
		this.fileType = fileType;
		this.data = data;
	}

}
