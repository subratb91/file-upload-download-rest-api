package com.subrat.app.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.subrat.app.demo.entities.DbFile;
import com.subrat.app.demo.payload.UploadFileResponse;
import com.subrat.app.demo.service.DbFileStorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("files")
public class FileController {

	@Autowired
	private DbFileStorageService dbFileStorageService;

	@PostMapping(value = "uploadFile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
		log.info("--------Entering - uploadFile------");

		DbFile dbFile = dbFileStorageService.storeFile(file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("files/downloadFile/")
				.path(dbFile.getFileId().toString()).toUriString();

		log.info("--------Exiting - uploadFile------");
		return new UploadFileResponse(dbFile.getFileName(), fileDownloadUri, file.getContentType(), file.getSize());
	}

	@PostMapping(value = "uploadMultipleFiles")
	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
		return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
	}

	@GetMapping(value = "downloadFile/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
		// Load file from database
		DbFile dbFile = dbFileStorageService.getFile(fileId);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(dbFile.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
				.body(new ByteArrayResource(dbFile.getData()));
	}
	
	@PutMapping(value = "updateFile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }) 
	public UploadFileResponse updateFile(@RequestParam("file") MultipartFile file,@RequestParam("fileId") Long fileId) {
		log.info("--------Entering - updateFile------");

		DbFile dbFile = dbFileStorageService.updateFile(fileId, file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("files/downloadFile/")
				.path(fileId.toString()).toUriString();

		log.info("--------Exiting - updateFile------");
		return new UploadFileResponse(dbFile.getFileName(), fileDownloadUri, file.getContentType(), file.getSize());
	}
	
	@DeleteMapping(value = "deleteFile/{fileId}") 
	public ResponseEntity<Boolean> deleteFile(@PathVariable("fileId") Long fileId) {
		log.info("--------Entering - deleteFile------");

		dbFileStorageService.deleteFile(fileId);

		log.info("--------Exiting - deleteFile------");
		return new ResponseEntity<>(true,HttpStatus.OK);
	}
	

}
