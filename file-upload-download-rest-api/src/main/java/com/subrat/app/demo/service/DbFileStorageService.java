package com.subrat.app.demo.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.subrat.app.demo.entities.DbFile;
import com.subrat.app.demo.exception.FileNotFoundException;
import com.subrat.app.demo.exception.FileStorageException;
import com.subrat.app.demo.repositories.DbFileRepository;



@Service
public class DbFileStorageService {

	@Autowired
	private DbFileRepository dbFileRepository;

	public DbFile storeFile(MultipartFile file) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			DbFile dbFile = new DbFile(fileName, file.getContentType(), file.getBytes());

			return dbFileRepository.save(dbFile);
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	public DbFile getFile(Long fileId) {
		return dbFileRepository.findById(fileId)
				.orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
	}
	
	public DbFile updateFile(Long fileId,MultipartFile file) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			DbFile dbFile = new DbFile(fileId,fileName, file.getContentType(), file.getBytes());

			return dbFileRepository.save(dbFile);
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}
	
	public void deleteFile(Long fileId) {
		if (!dbFileRepository.existsById(fileId))
			throw new FileNotFoundException( "File with fileId : " + fileId + "notFound");
		dbFileRepository.deleteById(fileId);
	}

}
