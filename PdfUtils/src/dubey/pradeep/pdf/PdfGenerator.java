package dubey.pradeep.pdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDComboBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

/**
 * This class is created to generate the PDF which will have the interactive
 * from to capture the User responses
 * 
 * @author prdubey
 *
 */
public class PdfGenerator {

    /**
     * Path where file has to be generated
     */
    public static String OUTPUT_DIRECTORY = "data/";

    public static int OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD = 300;
    public static int OFFSET_HORIZONTAL_CHILD_LABLE_FEILD = 20;
    public static int OFFSET_VERTICAL_BETWEEN_LABLE_FEILD = 5;
    public static int OFFSET_X = 100;
    public static int OFFSET_Y = 700; // IT Decrease in Negative direction
    public static int verticalGap = 25;

    public static final String defaultAppearanceString = "/Helv 12 Tf 0 0 0 rg";
    public static final int defaultFontSize = 10;

    /**
     * Root Element which holds complete PDF
     */
    private PDDocument document;

    private PDPageContentStream contentStream;

    /**
     * Page that contains the form. All the element created must be added in
     * this page to visible
     */
    private PDPage page;

    private PDPage page1;

    /**
     * In order to process Interactivity of the form all the fields must be
     * added in this form too
     */
    private PDAcroForm acroForm;

    public PDPageContentStream getContentStream() {
	return contentStream;
    }

    public void setContentStream(PDPageContentStream contentStream) {
	this.contentStream = contentStream;
    }

    public PDDocument getDocument() {
	return document;
    }

    public void setDocument(PDDocument document) {
	this.document = document;
    }

    public PDPage getPage() {
	return page;
    }

    public void setPage(PDPage page) {
	this.page = page;
    }

    public PDAcroForm getAcroForm() {
	return acroForm;
    }

    public void setAcroForm(PDAcroForm acroForm) {
	this.acroForm = acroForm;
    }

    /**
     * Initialize the Docuement , Page, and Form
     * 
     * @throws IOException
     */
    private void init() throws IOException {

	document = new PDDocument();

	page = new PDPage();

	document.addPage(page);

	PDRectangle pdRectangle = new PDRectangle(1, 1, 745, 745);
	page.setArtBox(pdRectangle);

	page1 = new PDPage();
	document.addPage(page1);
	page1.setArtBox(pdRectangle);

	// Adobe Acrobat uses Helvetica as a default font and
	// stores that under the name '/Helv' in the resources dictionary
	PDFont font = PDType1Font.HELVETICA;
	PDResources resources = new PDResources();
	resources.put(COSName.getPDFName("Helv"), font);

	acroForm = new PDAcroForm(document);
	document.getDocumentCatalog().setAcroForm(acroForm);

	// Add and set the resources and default appearance at the form level
	acroForm.setDefaultResources(resources);
	acroForm.setNeedAppearances(true);

	// Acrobat sets the font size on the form level to be
	// auto sized as default. This is done by setting the font size to '0'
	String defaultAppearanceString = "/Helv 0 Tf 0 g";
	acroForm.setDefaultAppearance(defaultAppearanceString);

	contentStream = new PDPageContentStream(document, page);

    }

    /**
     * Close and save the generated document
     * 
     * @throws IOException
     */
    private void close() throws IOException {
	contentStream.close();
	OUTPUT_DIRECTORY = OUTPUT_DIRECTORY + "MCA_SURVEY_" + System.currentTimeMillis() + ".pdf";
	document.save(OUTPUT_DIRECTORY);
	document.close();
    }

    /**
     * Workflow to addition of various elements in the
     * 
     * @throws IOException
     */
    private void draftForm() throws IOException {
	// addHeaderInPage();
	addLabel("1* User Id", OFFSET_X, OFFSET_Y, defaultFontSize, contentStream);
	addTextFied(FormDataBean.USER_Id_KEY, OFFSET_X + OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y
		- OFFSET_VERTICAL_BETWEEN_LABLE_FEILD, 75, 20, 7, defaultFontSize, page);

	addLabel("2* Work Location", OFFSET_X, OFFSET_Y - verticalGap, defaultFontSize, contentStream);
	addTextFied(FormDataBean.WORK_LOCATION_KEY, OFFSET_X + OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y
		- OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap, 75, 20, 20, defaultFontSize, page);

	addLabel("3* Date (DD/MM/YYYY)", OFFSET_X, OFFSET_Y - verticalGap * 2, defaultFontSize, contentStream);
	addTextFied(FormDataBean.DATE_KEY, OFFSET_X + OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y
		- OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap * 2, 75, 20, 10, defaultFontSize, page);

	addLabel("4* How many E-forms do you generally file within a month?", OFFSET_X, OFFSET_Y - verticalGap * 3,
		defaultFontSize, contentStream);

	List<String> optionList = new ArrayList<String>();

	optionList.add("Do not file");
	optionList.add("1 filing per month");
	optionList.add("2 to 5 filings per month ");
	optionList.add("6 to 10 filings per month");
	optionList.add("11 to 30 filings per month");
	optionList.add("More than 30 filings per month");

	addComboBox(FormDataBean.FORM_FILLING_FREQUENCY_KEY, optionList, "Do not file", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 3, 100, 20, defaultFontSize, page);

	addLabel("5* Forms frequently filed during the last 30 days", OFFSET_X, OFFSET_Y - verticalGap * 4,
		defaultFontSize, contentStream);
	addLabel("Form 1", OFFSET_X, OFFSET_Y - verticalGap * 5, defaultFontSize, contentStream);
	addLabel("Form 2", OFFSET_X + 80, OFFSET_Y - verticalGap * 5, defaultFontSize, contentStream);
	addLabel("Form 3", OFFSET_X + 160, OFFSET_Y - verticalGap * 5, defaultFontSize, contentStream);
	addLabel("Form 4", OFFSET_X + 240, OFFSET_Y - verticalGap * 5, defaultFontSize, contentStream);
	addLabel("Form 5", OFFSET_X + 320, OFFSET_Y - verticalGap * 5, defaultFontSize, contentStream);
	addTextFied(FormDataBean.FORM1_KEY, OFFSET_X, OFFSET_Y - verticalGap * 6, 75, 20, 5, defaultFontSize, page);
	addTextFied(FormDataBean.FORM2_KEY, OFFSET_X + 80, OFFSET_Y - verticalGap * 6, 75, 20, 5, defaultFontSize, page);
	addTextFied(FormDataBean.FORM3_KEY, OFFSET_X + 160, OFFSET_Y - verticalGap * 6, 75, 20, 5, defaultFontSize,
		page);
	addTextFied(FormDataBean.FORM4_KEY, OFFSET_X + 240, OFFSET_Y - verticalGap * 6, 75, 20, 5, defaultFontSize,
		page);
	addTextFied(FormDataBean.FORM5_KEY, OFFSET_X + 320, OFFSET_Y - verticalGap * 6, 75, 20, 5, defaultFontSize,
		page);

	addLabel("6* What is your primary role?", OFFSET_X, OFFSET_Y - verticalGap * 7, defaultFontSize, contentStream);
	optionList = new ArrayList<String>();

	optionList.add("Director or Company Representative");
	optionList.add("Professional (CA / CS / CWA");
	optionList.add("External Agency User (Banks / Govt.) ");
	optionList.add("Researcher or Academician");
	optionList.add("MCA Employee (RD / RoC / OL)");

	addComboBox(FormDataBean.PRIMARY_ROLE_KEY, optionList, "Do not file", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 7, 100, 20, defaultFontSize, page);

	addLabel("7* How do you rate Company Law rules governing the following?", OFFSET_X, OFFSET_Y - verticalGap * 8,
		defaultFontSize, contentStream);

	optionList = new ArrayList<String>();

	optionList.add("No information");
	optionList.add("Law is clear and unambiguous");
	optionList.add("Few clarifications are needed");
	optionList.add("Minor changes are needed in law");
	optionList.add("Major changes are needed in law");

	addLabel("i) Name Reservation", OFFSET_X + OFFSET_HORIZONTAL_CHILD_LABLE_FEILD, OFFSET_Y - verticalGap * 9,
		defaultFontSize - 2, contentStream);
	addComboBox(FormDataBean.NAME_RESERVATION_KEY, optionList, "No information", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 9, 100, 20, defaultFontSize - 2, page);

	addLabel("ii) Director Appointment", OFFSET_X + OFFSET_HORIZONTAL_CHILD_LABLE_FEILD, OFFSET_Y - verticalGap
		* 10, defaultFontSize - 2, contentStream);
	addComboBox(FormDataBean.DIRECTOR_APPOINTMENT_KEY, optionList, "No information", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 10, 100, 20, defaultFontSize - 2, page);

	addLabel("iii) Company Incorporation", OFFSET_X + OFFSET_HORIZONTAL_CHILD_LABLE_FEILD, OFFSET_Y - verticalGap
		* 11, defaultFontSize - 2, contentStream);
	addComboBox(FormDataBean.COMPANY_INCORPORATION_KEY, optionList, "No information", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 11, 100, 20, defaultFontSize - 2, page);

	addLabel("iv) Annual Filings", OFFSET_X + OFFSET_HORIZONTAL_CHILD_LABLE_FEILD, OFFSET_Y - verticalGap * 12,
		defaultFontSize - 2, contentStream);
	addComboBox(FormDataBean.ANNUAL_FILLINGS_KEY, optionList, "No information", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 12, 100, 20, defaultFontSize - 2, page);

	addLabel("v) Charges related", OFFSET_X + OFFSET_HORIZONTAL_CHILD_LABLE_FEILD, OFFSET_Y - verticalGap * 13,
		defaultFontSize - 2, contentStream);
	addComboBox(FormDataBean.CHARGES_RELATED_KEY, optionList, "No information", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 13, 100, 20, defaultFontSize - 2, page);

	addLabel("8* How do you rate the following services in MCA21 system?", OFFSET_X, OFFSET_Y - verticalGap * 14,
		defaultFontSize, contentStream);
	addTextFied(FormDataBean.SERVICES_KEY, OFFSET_X + OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y
		- OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap * 14, 75, 20, 10, defaultFontSize, page);

	optionList = new ArrayList<String>();

	optionList.add("No information");
	optionList.add("Very Dissatisfied");
	optionList.add("Somewhat Dissatisfied");
	optionList.add("Somewhat Dissatisfied");
	optionList.add("Very Satisfactory");

	addLabel("i) Pre-Scrutiny", OFFSET_X + OFFSET_HORIZONTAL_CHILD_LABLE_FEILD, OFFSET_Y - verticalGap * 15,
		defaultFontSize - 2, contentStream);
	addComboBox(FormDataBean.PRE_SCRUTINY_KEY, optionList, "No information", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 15, 100, 20, defaultFontSize - 2, page);

	addLabel("ii) E-Form Upload", OFFSET_X + OFFSET_HORIZONTAL_CHILD_LABLE_FEILD, OFFSET_Y - verticalGap * 16,
		defaultFontSize - 2, contentStream);
	addComboBox(FormDataBean.E_FORM_UPLOAD_KEY, optionList, "No information", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 16, 100, 20, defaultFontSize - 2, page);

	addLabel("iii) Online Payment ", OFFSET_X + OFFSET_HORIZONTAL_CHILD_LABLE_FEILD, OFFSET_Y - verticalGap * 17,
		defaultFontSize - 2, contentStream);
	addComboBox(FormDataBean.ONLINE_PAYEMENT_KEY, optionList, "No information", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 17, 100, 20, defaultFontSize - 2, page);

	addLabel("iv) View Master Data", OFFSET_X + OFFSET_HORIZONTAL_CHILD_LABLE_FEILD, OFFSET_Y - verticalGap * 18,
		defaultFontSize - 2, contentStream);
	addComboBox(FormDataBean.VIEW_MASTER_DATA_KEY, optionList, "No information", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 18, 100, 20, defaultFontSize - 2, page);

	addLabel("v) View Public Documents", OFFSET_X + OFFSET_HORIZONTAL_CHILD_LABLE_FEILD, OFFSET_Y - verticalGap
		* 19, defaultFontSize - 2, contentStream);
	addComboBox(FormDataBean.VIEW_PUBLIC_DOCUMENTS_KEY, optionList, "No information", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 19, 100, 20, defaultFontSize - 2, page);

	addLabel("9* How do you rate Seva Kendra and Helpdesk support?", OFFSET_X, OFFSET_Y - verticalGap * 20,
		defaultFontSize, contentStream);

	optionList = new ArrayList<String>();

	optionList.add("No information");
	optionList.add("Very Dissatisfied");
	optionList.add("Somewhat Dissatisfied");
	optionList.add("Somewhat Dissatisfied");
	optionList.add("Very Satisfactory");

	addComboBox(FormDataBean.KENDRA_COMBO_KEY, optionList, "No information", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 20, 100, 20, defaultFontSize, page);
	addLabel("     Ticket No. #", OFFSET_X, OFFSET_Y - verticalGap * 21, defaultFontSize - 2, contentStream);
	addTextFied(FormDataBean.KENDRA_KEY, OFFSET_X + OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y
		- OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap * 21, 75, 20, 10, defaultFontSize - 2, page);

	addLabel("10.* How many days is typically taken to completely process these applications? ", OFFSET_X, OFFSET_Y
		- verticalGap * 22, defaultFontSize, contentStream);

	optionList = new ArrayList<String>();

	optionList.add("No information");
	optionList.add("Less than 2 days");
	optionList.add("Less than 1 week");
	optionList.add("Less than 2 weeks");
	optionList.add("Less than 4 weeks");

	addLabel("i) Name Reservation", OFFSET_X + OFFSET_HORIZONTAL_CHILD_LABLE_FEILD, OFFSET_Y - verticalGap * 23,
		defaultFontSize - 2, contentStream);
	addComboBox(FormDataBean.NAME_RESERVATION_1_KEY, optionList, "No information", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 23, 100, 20, defaultFontSize - 2, page);

	addLabel("ii) DIN Allotment", OFFSET_X + OFFSET_HORIZONTAL_CHILD_LABLE_FEILD, OFFSET_Y - verticalGap * 24,
		defaultFontSize - 2, contentStream);
	addComboBox(FormDataBean.DIRECTOR_APPOINTMENT_1_KEY, optionList, "No information", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 24, 100, 20, defaultFontSize - 2, page);

	addLabel("iii) Company Incorporation", OFFSET_X + OFFSET_HORIZONTAL_CHILD_LABLE_FEILD, OFFSET_Y - verticalGap
		* 25, defaultFontSize - 2, contentStream);
	addComboBox(FormDataBean.COMPANY_INCORPORATION_1_KEY, optionList, "No information", OFFSET_X
		+ OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD - verticalGap
		* 25, 100, 20, defaultFontSize - 2, page);

	addLabel("11. Please provide any additional details or specific suggestions.", OFFSET_X, OFFSET_Y - verticalGap
		* 26, defaultFontSize, contentStream);
	addTextFied(FormDataBean.ADDITIONAL_DETAILS_KEY, OFFSET_X, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD
		- verticalGap * 27, 400, 20, 100, defaultFontSize, page);

	addLabel("Note: Questions marked with asterisk(*) are mandatory to Answer", OFFSET_X, OFFSET_Y - verticalGap
		* 28, defaultFontSize - 2, contentStream);

	close();

	// Start of for Second page
	File file = new File(OUTPUT_DIRECTORY);
	document = PDDocument.load(file);

	// Retrieving the pages of the document
	page1 = document.getPage(1);
	contentStream = new PDPageContentStream(document, page1);

	List optionsList = new ArrayList();
	optionsList.add("Dubey");
	optionsList.add("Pradeep");
	optionsList.add("kakau");

	addRadioButtonGroup(optionsList, FormDataBean.TEST_RADIO_BUTTON, OFFSET_X, OFFSET_Y + verticalGap * 3, 10, 10,
		page1, contentStream);

	addTextArea(FormDataBean.ADDITIONAL_DETAILS_KEY, OFFSET_X, OFFSET_Y - OFFSET_VERTICAL_BETWEEN_LABLE_FEILD
		- verticalGap * 8, 400, 20, 1000, defaultFontSize, page1);

	// addFooterInPage();
    }

    /**
     * Common method to add the Form Field element on the Page+Form
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     * @param field
     * @throws IOException
     */
    private void addFieldToPage(float x, float y, float width, float height, PDField field, PDPage page)
	    throws IOException {
	// add the field to the acroform
	acroForm.getFields().add(field);

	// Specify the annotation associated with the field
	PDAnnotationWidget widget = field.getWidgets().get(0);

	PDRectangle rect = new PDRectangle(x, y, width, height);
	widget.setRectangle(rect);
	widget.setPage(page);

	// set green border and yellow background
	// if you prefer defaults, just delete this code block
	PDAppearanceCharacteristicsDictionary fieldAppearance = new PDAppearanceCharacteristicsDictionary(
		new COSDictionary());
	fieldAppearance.setBorderColour(new PDColor(new float[] { 0, 0, 0 }, PDDeviceRGB.INSTANCE));
	// fieldAppearance.setBackground(new PDColor(new float[] { 0, 0, 1 },
	// PDDeviceRGB.INSTANCE));
	widget.setAppearanceCharacteristics(fieldAppearance);

	// make sure the annotation is visible on screen and paper
	widget.setPrinted(true);

	// Add the annotation to the page
	page.getAnnotations().add(widget);
    }

    /**
     * Add Label
     * 
     * @param text
     * @param x
     * @param y
     * @throws IOException
     */
    private void addLabel(String text, float x, float y, float fontSize, PDPageContentStream contentStream)
	    throws IOException {
	contentStream.beginText();
	contentStream.setNonStrokingColor(0, 0, 0);
	contentStream.setFont(PDType1Font.HELVETICA, fontSize); // 12
	contentStream.newLineAtOffset(x, y);

	contentStream.showText(text);

	contentStream.endText();

	System.out.println("Content added");
    }

    /**
     * Add Text Field
     * 
     * @param name
     *            : Name of the Field through which Identification can be done
     * @param x
     * @param y
     * @param width
     * @param height
     * @throws IOException
     */
    private void addTextFied(String name, float x, float y, float width, float height, int maxLength, float fontSize,
	    PDPage page) throws IOException {
	PDTextField textBox = new PDTextField(acroForm);
	textBox.setPartialName(name);
	textBox.setAlternateFieldName(name);

	String defaultAppearanceString1 = defaultAppearanceString.replace("12", String.valueOf(fontSize));
	textBox.setDefaultAppearance(defaultAppearanceString1);
	textBox.setMaxLen(maxLength);

	addFieldToPage(x, y, width, height, textBox, page);

	// set the field value
	// textBox.setValue(name);
    }

    /**
     * Add Text Field
     * 
     * @param name
     *            : Name of the Field through which Identification can be done
     * @param x
     * @param y
     * @param width
     * @param height
     * @throws IOException
     */
    private void addTextArea(String name, float x, float y, float width, float height, int maxLength, float fontSize,
	    PDPage page) throws IOException {
	PDTextField textBox = new PDTextField(acroForm);
	textBox.setPartialName(name);
	textBox.setAlternateFieldName(name);
	textBox.setMultiline(true);
	textBox.setDoNotScroll(false);

	String defaultAppearanceString1 = defaultAppearanceString.replace("12", String.valueOf(fontSize));
	textBox.setDefaultAppearance(defaultAppearanceString1);
	textBox.setMaxLen(maxLength);

	addFieldToPage(x, y, width, height, textBox, page);

	// set the field value
	// textBox.setValue(name);
    }

    /**
     * Add check Box in the Form
     * 
     * @param name
     *            Name of the Field through which Identification can be done
     * @param x
     * @param y
     * @param width
     * @param height
     * @throws IOException
     */
    private void addCheckBox(String name, float x, float y, float width, float height, PDPage page) throws IOException {
	PDCheckBox checkBox = new PDCheckBox(acroForm);
	checkBox.setAlternateFieldName(name);
	checkBox.setPartialName(name);

	addFieldToPage(x, y, width, height, checkBox, page);

    }

    /**
     * Add Radio Button in the Form
     * 
     * @param name
     *            Name of the Field through which Identification can be done
     * @param x
     * @param y
     * @param width
     * @param height
     * @throws IOException
     */
    private void addCheckBoxAsRadioButton(String name, float x, float y, float width, float height, PDPage page)
	    throws IOException {
	PDCheckBox radioButton = new PDCheckBox(acroForm);
	radioButton.setAlternateFieldName(name);
	radioButton.setPartialName(name);
	radioButton.setRadioButton(true);

	addFieldToPage(x, y, width, height, radioButton, page);

    }

    private void addRadioButtonGroup(List<String> options, String baseRadioButtonName, float x, float y, float width,
	    float height, PDPage page, PDPageContentStream contentStream) throws IOException {

	PDRadioButton button = new PDRadioButton(acroForm);
	button.setPartialName(baseRadioButtonName);
	button.setExportValues(options);
	button.getCOSObject().setName(COSName.DV, options.get(1));

	List<PDAnnotationWidget> widgets = new ArrayList<>();

	for (int i = 0; i < options.size(); i++) {

	    PDAppearanceCharacteristicsDictionary fieldAppearance = new PDAppearanceCharacteristicsDictionary(
		    new COSDictionary());
	    fieldAppearance.setBorderColour(new PDColor(new float[] { 0, 0, 0 }, PDDeviceRGB.INSTANCE));

	    PDAnnotationWidget widget = new PDAnnotationWidget();
	    addLabel(options.get(i), x, y - i * (21), defaultFontSize, contentStream);
	    widget.setRectangle(new PDRectangle(x + OFFSET_HORIZONTAL_BETWEEN_LABLE_FEILD, y - i * (21), width, height));
	    widget.setAppearanceCharacteristics(fieldAppearance);

	    widgets.add(widget);
	    page.getAnnotations().add(widget);

	    // added by Tilman on 13.1.2017, without it Adobe does not set the
	    // values properly
	    PDAppearanceDictionary appearance = new PDAppearanceDictionary();
	    COSDictionary dict = new COSDictionary();
	    dict.setItem(COSName.getPDFName("Off"), new COSDictionary());
	    dict.setItem(COSName.getPDFName(options.get(i)), new COSDictionary());
	    PDAppearanceEntry appearanceEntry = new PDAppearanceEntry(dict);
	    appearance.setNormalAppearance(appearanceEntry);
	    widget.setAppearance(appearance);
	}

	button.setWidgets(widgets);
	acroForm.getFields().add(button);

    }

    private void addComboBox(String name, List<String> options, String defaultValue, float x, float y, float width,
	    float height, float fontSize, PDPage page) throws IOException {
	PDComboBox comboBox = new PDComboBox(acroForm);
	comboBox.setAlternateFieldName(name);
	comboBox.setPartialName(name);

	String defaultAppearanceString1 = defaultAppearanceString.replace("12", String.valueOf(fontSize));
	comboBox.setDefaultAppearance(defaultAppearanceString1);

	comboBox.setOptions(options);
	comboBox.setDefaultValue(defaultValue);

	addFieldToPage(x, y, width, height, comboBox, page);
    }

    private void addHeaderInPage(PDPage page) {
	try {
	    PDFont font = PDType1Font.HELVETICA_BOLD;
	    String message = "I am Header";
	    PDRectangle pageSize = page.getArtBox();
	    float stringWidth = 5;

	    stringWidth = font.getStringWidth(message) * defaultFontSize / 1000f;

	    // calculate to center of the page
	    int rotation = page.getRotation();
	    boolean rotate = rotation == 90 || rotation == 270;

	    float pageWidth = rotate ? pageSize.getLowerLeftY() : pageSize.getLowerLeftX();
	    float pageHeight = rotate ? pageSize.getLowerLeftX() : pageSize.getLowerLeftY();
	    double centeredXPosition = rotate ? pageHeight / 2f : (pageWidth + stringWidth) / 2f;
	    double centeredYPosition = rotate ? (pageWidth + stringWidth) / 2f : pageHeight / 2f;

	    contentStream.beginText();
	    // set font and font size
	    contentStream.setFont(PDType1Font.HELVETICA_BOLD, defaultFontSize); // 12

	    // set text color to red
	    contentStream.setNonStrokingColor(255, 0, 0);
	    if (rotate) {
		// rotate the text according to the page rotation
		contentStream.setTextRotation(Math.PI / 2, centeredXPosition, centeredYPosition);
	    } else {
		contentStream.setTextTranslation(centeredXPosition, centeredYPosition);
	    }
	    contentStream.drawString(message);
	    contentStream.endText();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void addFooterInPage(PDPage page) {
	try {
	    PDFont font = PDType1Font.HELVETICA_BOLD;
	    String message = "I am Footer";
	    PDRectangle pageSize = page.getArtBox();
	    float stringWidth = 5;

	    stringWidth = font.getStringWidth(message) * defaultFontSize / 1000f;

	    // calculate to center of the page
	    int rotation = page.getRotation();
	    boolean rotate = rotation == 90 || rotation == 270;
	    float pageWidth = rotate ? pageSize.getHeight() : pageSize.getWidth();
	    float pageHeight = rotate ? pageSize.getWidth() : pageSize.getHeight();
	    double centeredXPosition = rotate ? pageHeight / 2f : (pageWidth - stringWidth) / 2f;
	    double centeredYPosition = rotate ? (pageWidth - stringWidth) / 2f : pageHeight / 2f;

	    contentStream.beginText();
	    // set font and font size
	    contentStream.setFont(PDType1Font.HELVETICA_BOLD, defaultFontSize); // 12

	    // set text color to red
	    contentStream.setNonStrokingColor(255, 0, 0);
	    if (rotate) {
		// rotate the text according to the page rotation
		contentStream.setTextRotation(Math.PI / 2, centeredXPosition, centeredYPosition);
	    } else {
		contentStream.setTextTranslation(centeredXPosition, centeredYPosition);
	    }
	    contentStream.drawString(message);
	    contentStream.endText();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String args[]) {
	try {

	    PdfGenerator pdfGenerator = new PdfGenerator();
	    pdfGenerator.init();

	    pdfGenerator.draftForm();
	    pdfGenerator.close();

	} catch (IOException e) {

	    e.printStackTrace();
	}
    }

}
