package it.eng.service.impl;

import it.eng.service.FileService;
import it.eng.domain.File;
import it.eng.repository.FileRepository;
import it.eng.security.SecurityUtils;
import it.eng.service.dto.FileDTO;
import it.eng.service.mapper.FileMapper;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing File.
 */
@Service
@Transactional
public class FileServiceImpl implements FileService {

    private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    public FileServiceImpl(FileRepository fileRepository, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    /**
     * Save a file.
     *
     * @param fileDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FileDTO save(FileDTO fileDTO) {
    	log.debug("Request to save File : {}", fileDTO);  
        File file = fileMapper.toEntity(fileDTO);
        
        // Fill data for the database
        file.setLogin(SecurityUtils.getCurrentUserLogin().get().toString());
        file.setFileSize(fileDTO.getContent().length);
        if(file.getDateCreated() == null) {
        file.dateCreated(LocalDate.now());
        log.debug("Time of creation: " + file.getDateCreated().toString());
        }
        file.setLastModified(LocalDate.now());
        log.debug("Time of the last modification: " + file.getLastModified().toString());
        file.setCode(Integer.toString(file.getName().hashCode())
				+ Integer.toString(file.getDateCreated().toString().hashCode())
				+ Integer.toString(file.getLogin().hashCode()));
        log.debug("Generated code for uploaded file: " + file.getCode());
        
        file = fileRepository.save(file);
        return fileMapper.toDto(file);
    }

    /**
     * Get all the files.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Files");
        return fileRepository.findAll(pageable)
            .map(fileMapper::toDto);
    }

    /**
     * Get one file by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FileDTO findOne(Long id) {
        log.debug("Request to get File : {}", id);
        File file = fileRepository.findOne(id);
        return fileMapper.toDto(file);
    }

    /**
     * Delete the file by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete File : {}", id);
        fileRepository.delete(id);
    }
    
    @Override
    public Page<FileDTO> findByLogin(Pageable pageable, String login) {
    	Page<File> files = fileRepository.findByLogin(pageable, login);
    	return fileRepository.findByLogin(pageable, login).map(fileMapper::toDto);   	
    }
}
