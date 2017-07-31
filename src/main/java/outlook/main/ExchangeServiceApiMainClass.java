package outlook.main;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.DefaultExtendedPropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.MapiPropertyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.enumeration.service.TaskStatus;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.item.Task;
import microsoft.exchange.webservices.data.core.service.schema.AppointmentSchema;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.core.service.schema.TaskSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.StringList;
import microsoft.exchange.webservices.data.property.definition.ExtendedPropertyDefinition;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;
//import org.testng.Reporter;

public class ExchangeServiceApiMainClass {

	private static final String OUTLOOK_EMAIL = Utils.readConfig("outlook_user_email");
	private static final String OUTLOOK_PASS = Utils.readConfig("outlook_user_password");
	private static final String LIFERAY_EMAIL = "email";
	private static final String LIFERAY_PASS = "pass";
	private static final String EWS_URL = Utils.readConfig("ews_url");
	protected static ExchangeService service;

	public ExchangeServiceApiMainClass() {
		try{
			service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
			ExchangeCredentials credentials = new WebCredentials(OUTLOOK_EMAIL, OUTLOOK_PASS);
			service.setCredentials(credentials);
			service.setUrl(new URI(EWS_URL));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	public static void main(String... arg) throws Exception {
		service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		ExchangeCredentials credentials = new WebCredentials(OUTLOOK_EMAIL, OUTLOOK_PASS);
		service.setCredentials(credentials);
		service.setUrl(new URI(EWS_URL));
		parseDate("");
		
		
		//findAllTasks();
		//deleteAllAppoinitments(service);
		//deleteAllTasks(service);
		//System.out.println(createOpenTask());
		//System.out.println(createAppointment());
		
	}

	public static void deleteAllAppoinitments(ExchangeService service) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = formatter.parse("2017-01-01 12:00:00");
			Date endDate = formatter.parse("2018-01-01 13:00:00");

			CalendarFolder calendar = CalendarFolder.bind(service, WellKnownFolderName.Calendar, new PropertySet());
			CalendarView cView = new CalendarView(startDate, endDate, 5);
			cView.setPropertySet(
					new PropertySet(AppointmentSchema.Subject, AppointmentSchema.Start, AppointmentSchema.End));
			FindItemsResults<Appointment> appointments = calendar.findAppointments(cView);
			System.out.println("appointments: " + appointments.getItems().size());

			for (int i = 0; i < appointments.getItems().size(); i++) {

				Appointment appointment = Appointment.bind(service, appointments.getItems().get(i).getRootItemId());
				appointment.delete(DeleteMode.MoveToDeletedItems);
				System.out.println("Appointment deleted successfully:"+appointment.getSubject());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void deleteAllTasks(ExchangeService service){
		try {
			Folder folder = Folder.bind(service, WellKnownFolderName.Tasks);
			 folder.empty(DeleteMode.HardDelete, true);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	// Event
	public static boolean createAppointment() {
		try {
			Appointment appointment = new Appointment(service);
			// appointment.delete(DeleteMode.MoveToDeletedItems);
			appointment.setSubject("YOYO AUTOMATION Event");
			appointment.setBody(MessageBody.getMessageBodyFromText("Recurrence Test Body Msg"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = formatter.parse("2017-07-26 12:00:00");
			Date endDate = formatter.parse("2017-07-27 13:00:00");
			appointment.setStart(startDate);
			appointment.setEnd(endDate);
			StringList sl = new StringList();
			sl.add("Publicity");
			sl.add("Marketing");
			appointment.setCategories(sl);
			appointment.save();
			// Reporter.log("Event with Subject created successfully", true);
			System.out.println("Appointment created successfully:"+appointment.getSubject());
			return true;
		} catch (Exception e) {
			// Reporter.log("Error on creating Event", true);
			return false;
		}
	}

	// Create Completed Task
	public static boolean createCompletedTask() throws Exception {
		Task task = new Task(service);
		task.setSubject("zzzzzzzzzz");
		task.setBody(MessageBody.getMessageBodyFromText("QA Automation Task for Testing Purpose"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = formatter.parse("2017-07-22 12:00:00");
		Date endDate = formatter.parse("2017-07-24 13:00:00");
		task.setStartDate(startDate);
		task.setCompleteDate(endDate);
		task.setStatus(TaskStatus.Completed);
		task.save(WellKnownFolderName.Tasks);
		return true;
	}

	// Create Open Task
	public static boolean createOpenTask() throws Exception {
		Task task = new Task(service);
		task.setSubject("YOYO  Automation Task");
		task.setBody(MessageBody.getMessageBodyFromText("QA Automation Task for Testing Purpose"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = formatter.parse("2017-07-22 12:00:00");
		Date endDate = formatter.parse("2017-08-27 13:00:00");
		task.setStartDate(startDate);
		task.setDueDate(endDate);
		task.setStatus(TaskStatus.InProgress);
		task.save(WellKnownFolderName.Tasks);
		return true;
	}

	public static Map<String, String> getCreds() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(LIFERAY_EMAIL, OUTLOOK_EMAIL);
		map.put(LIFERAY_PASS, OUTLOOK_PASS);
		return map;
	}
	
	
public static void findAllTasks(){
	try{service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
	ExchangeCredentials credentials = new WebCredentials(OUTLOOK_EMAIL, OUTLOOK_PASS);
	service.setCredentials(credentials);
	service.setUrl(new URI(EWS_URL));
	
	SearchFilter greaterthanfilter = new SearchFilter.IsGreaterThanOrEqualTo(ItemSchema.LastModifiedTime, "2016-07-29" );
	SearchFilter filter = new SearchFilter.SearchFilterCollection(LogicalOperator.And, greaterthanfilter);
	Folder folder = Folder.bind(service, WellKnownFolderName.Tasks); //Or the folder you want to search in
	FindItemsResults<Item> results = folder.findItems(filter, new ItemView(1000));
	System.out.println("RESULT: "+results.getTotalCount());
	for(Item ii: results){
		System.out.println(ii.getSubject());
	}
	
	}catch(Exception e){
		e.printStackTrace();
	}
}

public static String parseDate(String date){
	
	LocalDate todayDate = LocalDate.now();
	if(date.equals("${today}")){
		return todayDate.toString();
	}else 
	if(date.contains("+") ){
			String days[]=date.split("\\+");
			return todayDate.plus(Long.parseLong(days[1]), ChronoUnit.DAYS).toString();
	}else if(date.contains("-")){
			String days[]=date.split("-");
			return todayDate.minus(Long.parseLong(days[1]), ChronoUnit.DAYS).toString();
		}
		
	else{
		System.out.println("PLEASE ENTER CORRECT DATE FORMAT e.g ${today}, ${today}-1, ${today}+1");
	}
	return "PLEASE ENTER CORRECT DATE FORMAT e.g ${today}, ${today}-1, ${today}+1";
}

}