package it.eng.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
	        System.out.println("OVO JE KONTENT PRIMLJEN OD DOCX-a");
	        IConverter converter = LocalConverter.make();
	            converter.convert(is).as(DocumentType.DOCX)
	                     .to(outstream).as(DocumentType.PDF)
	                     .execute();
	            System.out.println("Konvertovanje u PDF zavrseno");
	            System.out.println("OVO JE OUTSTREAM" + outstream);
//	            converter.shutDown();

	            byte[] data = outstream.toByteArray();
	            System.out.println("OVO SU BYTE ARRAY PODACI" + data);
	            return data;
	    }
	    
    	//conversion of xlsx byte[] content to pdf byte[] content
	    public static byte[] convertExcel2PDF(byte[] content)
	    {
	        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
	        InputStream is = new ByteArrayInputStream(content);
	        System.out.println("OVO JE KONTENT PRIMLJEN OD DOCX-a");
	        IConverter converter = LocalConverter.make();
	            converter.convert(is).as(DocumentType.XLSX)
	                     .to(outstream).as(DocumentType.PDF)
	                     .execute();
	            System.out.println("Konvertovanje u PDF zavrseno");
	            System.out.println("OVO JE OUTSTREAM" + outstream);
//	            converter.shutDown();

	            byte[] data = outstream.toByteArray();
	            System.out.println("OVO SU BYTE ARRAY PODACI" + data);
	            return data;
	    }

	    public static void convert2Excel()
	    {
	        File excelFile = new File("C:\\Users\\divanovic\\Downloads\\EB_K0031_timesheet_201801.xlsx")
	        , target = new File("C:\\Users\\divanovic\\Downloads\\EB_K0031_timesheet_201801.pdf");
	        IConverter converter = LocalConverter.make();
	            converter.convert(excelFile).as(DocumentType.XLSX)
	                     .to(target).as(DocumentType.PDF)
	                     .prioritizeWith(1000) // optional
	                     .schedule();
	        //converter.shutDown();
	    }

}