package it.eng.web.rest;

import com.codahale.metrics.annotation.Timed;

import it.eng.converter.Docx2PdfConversion;
import it.eng.domain.File;
import it.eng.security.SecurityUtils;
import it.eng.service.FileService;
import it.eng.web.rest.errors.BadRequestAlertException;
import it.eng.web.rest.util.HeaderUtil;
import it.eng.web.rest.util.PaginationUtil;
import it.eng.service.dto.FileDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing File.
 */
@RestController
@RequestMapping("/api")
public class FileResource {

    private static final int MAX_FILE_SIZE = 20971520;

	private final Logger log = LoggerFactory.getLogger(FileResource.class);

    private static final String ENTITY_NAME = "file";

    private final FileService fileService;

    public FileResource(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * POST  /files : Create a new file.
     *
     * @param fileDTO the fileDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fileDTO, or with status 400 (Bad Request) if the file has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/files")
    @Timed
    public ResponseEntity<FileDTO> createFile(@Valid @RequestBody FileDTO fileDTO) throws URISyntaxException {
    	if(fileDTO.getContent().length > MAX_FILE_SIZE) {
    		log.debug("File can't be biger than"+ MAX_FILE_SIZE + "bytes.");
    		return null;
    	}else {
        log.debug("REST request to save File : {}", fileDTO);
        if (fileDTO.getId() != null) {
            throw new BadRequestAlertException("A new file cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileDTO result = fileService.save(fileDTO);
        return ResponseEntity.created(new URI("/api/files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    	}
    }

    /**
     * PUT  /files : Updates an existing file.
     *
     * @param fileDTO the fileDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fileDTO,
     * or with status 400 (Bad Request) if the fileDTO is not valid,
     * or with status 500 (Internal Server Error) if the fileDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/files")
    @Timed
    public ResponseEntity<FileDTO> updateFile(@Valid @RequestBody FileDTO fileDTO) throws URISyntaxException {
        log.debug("REST request to update File : {}", fileDTO);
        if (fileDTO.getId() == null) {
            return createFile(fileDTO);
        }
        FileDTO result = fileService.save(fileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fileDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /files : get all the files which owner is logged user.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of files in body
     */
    @GetMapping("/files")
    @Timed
    public ResponseEntity<List<FileDTO>> getAllFiles(Pageable pageable) {
        log.debug("REST request to get a page of Files");
        String login = SecurityUtils.getCurrentUserLogin().get().toString();
        Page<FileDTO> page = fileService.findByLogin(pageable, login);
        System.out.println("BROJ ELEMENATA POSLATO" + page.getTotalElements());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/files");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /files/:id : get the "id" file.
     *
     * @param id the id of the fileDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fileDTO, or with status 404 (Not Found)
     */
    @GetMapping("/files/{id}")
    @Timed
    public ResponseEntity<FileDTO> getFile(@PathVariable Long id) {
        log.debug("REST request to get File : {}", id);
        FileDTO fileDTO = fileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fileDTO));
    }
    
    /**
     * GET  /files/view/:id : get the file by "id" and convert it for the PDF preview.
     *
     * @param id the id of the fileDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fileDTO, or with status 404 (Not Found)
     */
    @GetMapping(value = "/files/view/{id}")
    @Timed
    public ResponseEntity<FileDTO> viewFile(@PathVariable Long id) {
        log.debug("REST request to get File : {}", id);
        FileDTO fileDTO = fileService.findOne(id);
        if(fileDTO.getContentContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")){
        	fileDTO.setContent(Docx2PdfConversion.convertWord2PDF(fileDTO.getContent()));
        }
        else if (fileDTO.getContentContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
        	fileDTO.setContent(Docx2PdfConversion.convertExcel2PDF(fileDTO.getContent()));
        }
        fileDTO.setContentContentType("application/pdf");
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fileDTO));
    }

    /**
     * DELETE  /files/:id : delete the "id" file.
     *
     * @param id the id of the fileDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/files/{id}")
    @Timed
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        log.debug("REST request to delete File : {}", id);
        fileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
