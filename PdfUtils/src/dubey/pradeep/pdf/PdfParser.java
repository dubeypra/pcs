/**
 * 
 */
package dubey.pradeep.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author prdubey
 *
 */
public class PdfParser {
    /**
     * XLS Report Related
     */

    private XSSFWorkbook workbook;

    /**
     * Path where file has to be generated
     */
    public static String DIRECTORY_OUTPUT_FILE = "data/";

    /**
     * Path where file has to be generated
     */
    public static String DIRECTORY_REPORT_FILE = "report/";

    /**
     * @param args
     */
    public static void main(String[] args) {
	List<String> fileList = listFilesForFolder(new File(DIRECTORY_OUTPUT_FILE));

	System.out.println("Total No of Files : " + fileList.size());
	System.out.println(" Files : " + fileList.size());

	PdfParser parser = new PdfParser();

	parser.createReport(fileList);

    }

    /**
     * Provide list of all the files which are present on the provided location
     * 
     * @param folder
     *            : Target Folder
     * @return: List of Files
     */
    public static List<String> listFilesForFolder(final File folder) {
	List<String> fileList = new ArrayList<String>();
	for (final File fileEntry : folder.listFiles()) {
	    if (fileEntry.isDirectory()) {
		listFilesForFolder(fileEntry);
	    } else {
		System.out.println(fileEntry.getName());
		fileList.add(fileEntry.getAbsolutePath());
	    }
	}
	return fileList;
    }

    /**
     * Creates Report for a TestSuiteRunResult object passed Filename includes
     * the path of the file
     */
    public boolean createReport(List<String> fileList) {
	try {
	    workbook = new XSSFWorkbook();

	    String filename = DIRECTORY_REPORT_FILE + "Survey_FeedBack_" + System.currentTimeMillis() + ".xls";

	    XSSFSheet sheet = workbook.createSheet("Feed Back");

	    // write data to sheer
	    writeDatatoSheet(sheet, fileList);

	    // write the excel workbook to file now
	    FileOutputStream fos = new FileOutputStream(filename);
	    workbook.write(fos);
	    fos.close();

	    return true;

	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
    }

    /**
     * Writes data to Sheet
     * 
     * @param sheet
     * @param listofusecase
     */
    private void writeDatatoSheet(XSSFSheet sheet, List<String> fileList) {

	popualateHeader(sheet, FormDataBean.getAllCoumns());

	for (int i = 0, j = 1; i < fileList.size(); i++, j++) {

	    insertSurveyRow(fileList.get(i), j, sheet, FormDataBean.getAllCoumns());
	}

    }

    /**
     * Read File and convert the user input data into to Java Entity
     * 
     * @param filePath
     * @return
     */
    public static String getFieldValue(PDAcroForm acroForm, String fieldKey) {
	String fetchedValue = "";

	PDField field = acroForm.getField(fieldKey);
	fetchedValue = field.getValueAsString();

	System.out.println("Data fetched is  : " + fetchedValue);

	return fetchedValue;
    }

    /**
     * This method will insert one row corresponding to one file
     * 
     * @param formDataBean
     * @param row
     * @param surveyReportHeaders
     */
    private void insertSurveyRow(String filePath, int rowIndex, XSSFSheet sheet, String[] surveyReportHeaders) {
	System.out.println("Creating Row for File  : " + filePath);

	PDDocument document = null;
	try {
	    XSSFRow row = sheet.createRow(rowIndex);
	    document = PDDocument.load(new File(filePath));
	    PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

	    String headerValue = "";
	    Cell dataCell = null;

	    for (String cell : surveyReportHeaders) {
		headerValue = cell;

		if (headerValue.equalsIgnoreCase(FormDataBean.USER_Id_KEY)) {
		    dataCell = row.createCell(0);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.USER_Id_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.WORK_LOCATION_KEY)) {
		    dataCell = row.createCell(1);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.WORK_LOCATION_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.DATE_KEY)) {
		    dataCell = row.createCell(2);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.DATE_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.FORM_FILLING_FREQUENCY_KEY)) {
		    dataCell = row.createCell(3);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.FORM_FILLING_FREQUENCY_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.FORM1_KEY)) {
		    dataCell = row.createCell(4);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.FORM1_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.FORM2_KEY)) {
		    dataCell = row.createCell(5);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.FORM2_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.FORM3_KEY)) {
		    dataCell = row.createCell(6);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.FORM3_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.FORM4_KEY)) {
		    dataCell = row.createCell(7);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.FORM4_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.FORM5_KEY)) {
		    dataCell = row.createCell(8);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.FORM5_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.PRIMARY_ROLE_KEY)) {
		    dataCell = row.createCell(9);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.PRIMARY_ROLE_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.NAME_RESERVATION_KEY)) {
		    dataCell = row.createCell(10);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.NAME_RESERVATION_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.DIRECTOR_APPOINTMENT_KEY)) {
		    dataCell = row.createCell(11);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.DIRECTOR_APPOINTMENT_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.COMPANY_INCORPORATION_KEY)) {
		    dataCell = row.createCell(12);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.COMPANY_INCORPORATION_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.ANNUAL_FILLINGS_KEY)) {
		    dataCell = row.createCell(13);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.ANNUAL_FILLINGS_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.CHARGES_RELATED_KEY)) {
		    dataCell = row.createCell(14);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.CHARGES_RELATED_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.SERVICES_KEY)) {
		    dataCell = row.createCell(15);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.SERVICES_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.PRE_SCRUTINY_KEY)) {
		    dataCell = row.createCell(16);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.PRE_SCRUTINY_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.E_FORM_UPLOAD_KEY)) {
		    dataCell = row.createCell(17);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.E_FORM_UPLOAD_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.ONLINE_PAYEMENT_KEY)) {
		    dataCell = row.createCell(18);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.ONLINE_PAYEMENT_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.VIEW_MASTER_DATA_KEY)) {
		    dataCell = row.createCell(19);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.VIEW_MASTER_DATA_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.VIEW_PUBLIC_DOCUMENTS_KEY)) {
		    dataCell = row.createCell(20);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.VIEW_PUBLIC_DOCUMENTS_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.KENDRA_COMBO_KEY)) {
		    dataCell = row.createCell(21);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.KENDRA_COMBO_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.KENDRA_KEY)) {
		    dataCell = row.createCell(22);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.KENDRA_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.NAME_RESERVATION_1_KEY)) {
		    dataCell = row.createCell(23);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.NAME_RESERVATION_1_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.DIRECTOR_APPOINTMENT_1_KEY)) {
		    dataCell = row.createCell(24);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.DIRECTOR_APPOINTMENT_1_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.COMPANY_INCORPORATION_1_KEY)) {
		    dataCell = row.createCell(25);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.COMPANY_INCORPORATION_1_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.ADDITIONAL_DETAILS_KEY)) {
		    dataCell = row.createCell(26);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.ADDITIONAL_DETAILS_KEY));
		}

		if (headerValue.equalsIgnoreCase(FormDataBean.TEST_RADIO_BUTTON)) {
		    dataCell = row.createCell(27);
		    dataCell.setCellValue(getFieldValue(acroForm, FormDataBean.TEST_RADIO_BUTTON));
		}

	    }
	} catch (Exception e) {

	    e.printStackTrace();
	} finally {
	    try {
		document.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * This method will populate the headers of the required sheet
     * 
     * @param sheet
     *            Sheet for which headers has to be populated
     * @param headerDetails
     *            Header Information which is added in the Report header
     *            constants
     */
    private List<Cell> popualateHeader(XSSFSheet sheet, String[] headerDetails) {
	Row row = sheet.createRow(0);
	String[] headers = headerDetails;
	List<Cell> headersForSheet = new ArrayList<Cell>();

	Font font = workbook.createFont();
	CellStyle style = workbook.createCellStyle();

	font.setBoldweight(Font.BOLDWEIGHT_BOLD);
	style.setFont(font);

	for (int i = 0; i < headers.length; i++) {
	    Cell cell = row.createCell(i);
	    cell.setCellValue(headers[i]);
	    cell.setCellStyle(style);
	    headersForSheet.add(cell);
	}

	return headersForSheet;
    }

}
