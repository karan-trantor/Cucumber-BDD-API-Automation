package outlook.main;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.service.TaskStatus;
import microsoft.exchange.webservices.data.core.service.item.Task;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

public class TaskHandler {
	private static final String OUTLOOK_EMAIL = "Harpreet.Singh@hbgusa.com";
	private static final String OUTLOOK_PASS = "NDsd2653";
	private static final String LIFERAY_EMAIL = "email";
	private static final String LIFERAY_PASS = "pass";
	private static final String EWS_URL = "https://outlook.office365.com/EWS/Exchange.asmx";
	protected static ExchangeService service;

	// Create Open Task
	public static boolean createOpenTask(String subject, String body, String startdate, String enddate) {
		try {
			service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
			ExchangeCredentials credentials = new WebCredentials(OUTLOOK_EMAIL, OUTLOOK_PASS);
			service.setCredentials(credentials);
			service.setUrl(new URI(EWS_URL));

			Task task = new Task(service);
			task.setSubject(subject);
			task.setBody(MessageBody.getMessageBodyFromText(body));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = formatter.parse(startdate);
			Date endDate = formatter.parse(enddate);
			task.setStartDate(startDate);
			task.setDueDate(endDate);
			task.setStatus(TaskStatus.InProgress);
			task.save(WellKnownFolderName.Tasks);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		}
		return true;
	}
	
	// Create Completed Task
	public static boolean createCompletedTask(String subject, String body, String startdate, String enddate) {
		try {
			service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
			ExchangeCredentials credentials = new WebCredentials(OUTLOOK_EMAIL, OUTLOOK_PASS);
			service.setCredentials(credentials);
			service.setUrl(new URI(EWS_URL));

			Task task = new Task(service);
			task.setSubject(subject);
			task.setBody(MessageBody.getMessageBodyFromText(body));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = formatter.parse(startdate);
			Date endDate = formatter.parse(enddate);
			task.setStartDate(startDate);
			task.setDueDate(endDate);
			task.setStatus(TaskStatus.Completed);
			task.save(WellKnownFolderName.Tasks);
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		}
		return true;
	}
	
	
	
	
	
}
