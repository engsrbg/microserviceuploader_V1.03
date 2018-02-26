package it.eng.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;


public class Docx2PdfConversion {

    	//conversion of docx byte[] content to pdf byte[] content
	    public static byte[] convertWord2PDF(byte[] content)
	    {
	        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
	        InputStream is = new ByteArrayInputStream(content);
	        IConverter converter = LocalConverter.make();
	            converter.convert(is).as(DocumentType.DOCX)
	                     .to(outstream).as(DocumentType.PDF)
	                     .execute();
//	            converter.shutDown();

	            byte[] data = outstream.toByteArray();
	            return data;
	    }

}