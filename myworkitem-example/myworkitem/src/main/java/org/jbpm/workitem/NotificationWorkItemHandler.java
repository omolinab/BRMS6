package org.jbpm.workitem;

import org.jbpm.bpmn2.handler.ServiceTaskHandler;
import org.jbpm.bpmn2.handler.SignallingTaskHandlerDecorator;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

/**
 * Hello world!
 *
 */
public class NotificationWorkItemHandler implements WorkItemHandler {

	public NotificationWorkItemHandler() {
		super();
	}
	
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		throw new MyException();		
/*
		String from = (String) workItem.getParameter("From");
		String to = (String) workItem.getParameter("To");
		String message = (String) workItem.getParameter("Message");
		String priority = (String) workItem.getParameter("Priority");
*/
		/*SignallingTaskHandlerDecorator pp;
		new org.jbpm.process.workitem.rest.RESTWorkItemHandler();
		org.drools.compiler.kie.util.CDIHelper pp1;*/
		
		/*
		 * Send email.
		 * The ServiceRegistry class is an example class implementing the task business logic. 
		 */
		/*EmailService service = ServiceRegistry.getInstance().getEmailService();
		service.sendEmail(from, to, "Notification", message);*/
//		System.out.println("From: " + from + " To: " + to + " Message: " + message + " Priority: " + priority);

		/* 
		 * Notify manager that work item has been completed.
		 * The completeWorkItem() call completes the work item execution.
		 */
//		manager.completeWorkItem(workItem.getId(), null);
	}

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// Do nothing, notifications cannot be aborted
	}

}
