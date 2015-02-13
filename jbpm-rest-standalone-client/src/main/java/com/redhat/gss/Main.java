package com.redhat.gss;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.services.client.api.RemoteRestRuntimeFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

/**
 * This is a very simple Rest Client to test against a running
 *  instance of the KIE Workbench. 
 * You can parameterize
 *   - the Deployment Unit Id
 *   - the Application URL
 *   - the user/pass to execute operations
 */
public class Main {

	/*
		Set the parameters accoring your installation	 
	*/
	private static final String DEPLOYMENT_ID = "org.kie.example:project1:1.0";
	private static final String APP_URL = "http://localhost:8080/business-central/";
	private static final String USER = "bpmsAdmin";
	private static final String PASSWORD = "admin@123";

	public static void main( String[] args ) throws MalformedURLException {
		accessServerTasks();
	}
	
	public static void accessServerTasks() throws MalformedURLException {
		URL url = new URL(APP_URL);
		// create a factory using the installation parameters
		RemoteRestRuntimeFactory factory = new RemoteRestRuntimeFactory(DEPLOYMENT_ID, url, USER, PASSWORD);
		RemoteRuntimeEngine engine = factory.newRuntimeEngine();
		// The TaskService class allows we to access the server tasks
		TaskService taskService = engine.getTaskService();
		List<TaskSummary> tasks = taskService.getTasksOwned(USER, "en-UK");
		if(tasks.size() == 0){
			System.out.printf("No tasks for user \"%s\" as owner...\n", USER);
		} else {
			System.out.printf("Tasks where user \"%s\" is a an owner...\n", USER);
			for(TaskSummary t : tasks){
				System.out.printf("ID: %d\n", t.getId());
				System.out.printf("Name: %s\n", t.getName());
				System.out.printf("Actual Owner: %s\n", t.getActualOwner());
				System.out.printf("Created by: %s\n", t.getCreatedBy());
				System.out.printf("Created on: %s\n", t.getCreatedOn());
				System.out.printf("Status: %s\n", t.getStatus());
				System.out.printf("Description: %s\n", t.getDescription());
				System.out.println("---------------");
			}
		}
	}
	
}
