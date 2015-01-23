package com.sample;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.services.task.utils.ContentMarshallerHelper;import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Content;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.services.client.api.RemoteRestRuntimeEngineFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

/**
 * This is a very simple Rest Client to test against a running instance of the business-central. You can parameterize - the
 * Deployment Unit Id - the Application URL - the user/pass to execute operations
 */
public class Main {
	
	//set according to your environment
	private static final String APP_URL="http://localhost:8080/business-central/";
	private static final String USER = "bpmsAdmin";
	private static final String PASSWORD = "admin@123";

	public static void main(String[] args) throws MalformedURLException, InterruptedException {


		URL url = new URL(APP_URL);
		
		// create a factory for a given deploymentId
		RemoteRestRuntimeEngineFactory factory = RemoteRestRuntimeEngineFactory.newBuilder()
				.addDeploymentId("org.kie.example:project1:1.0").addUrl(url).addUserName(USER)
				.addPassword(PASSWORD).build();

		//startProcessWithCustomParam(factory, "project1.myprocess");
		//humanTaskOutputWithCustomParam(factory, "project1.myprocess");
		humanTaskInputWithCustomParam(factory, "project1.myprocess");

	}

	public static void startProcessWithCustomParam(RemoteRestRuntimeEngineFactory factory, String PROCESS_ID)
			throws InterruptedException {

		System.out.println("###########################################");
		System.out.println("..testing Starting the Process with a Custom Param...");
		System.out.println("###########################################");

		RemoteRuntimeEngine engine = factory.newRuntimeEngine();

		KieSession kieSession = engine.getKieSession();

		Map<String, Object> params = new HashMap<String, Object>();
		Person person = new Person();
		person.setName(Main.USER);
		params.put("po", person);

		ProcessInstance pi = kieSession.startProcess(PROCESS_ID, params);
		System.out.println("please check server log");
		Thread.sleep(2000);
		System.out.println("\n");

	}

	public static void humanTaskOutputWithCustomParam(RemoteRestRuntimeEngineFactory factory, String PROCESS_ID)
			throws InterruptedException {

		System.out.println("###########################################");
		System.out.println("..testing Human Task Output with Custom Param...");
		System.out.println("###########################################");

		HashMap<String, Object> params = new HashMap<String, Object>();
		Person person = new Person();
		person.setName("Oscar");
		params.put("tVar", person);
		//
		RemoteRuntimeEngine engine = factory.newRuntimeEngine();

		KieSession kieSession = engine.getKieSession();
		//PROCESS_ID = "remoteIssuesProject.humanTaskOutput";
		ProcessInstance pi = kieSession.startProcess(PROCESS_ID);

		TaskService taskService = engine.getTaskService();
		List<Long> taskList = taskService.getTasksByProcessInstanceId(pi.getId());

		taskService.start(taskList.get(0), Main.USER);

		taskService.complete(taskList.get(0), Main.USER, params);
		System.out
				.println("Ye should check the server log and see the value of the process variable, which we just filled"
						+ " by completing human task");
		System.out.println("\n");

		Thread.sleep(2000);

	}

	public static void humanTaskInputWithCustomParam(RemoteRestRuntimeEngineFactory factory, String PROCESS_ID) {

		System.out.println("###########################################");
		System.out.println("...testing Human Task Input with Custom Param...");
		System.out.println("###########################################");
		System.out.println("we have set process variable with the object Person, which has name "+Main.USER+" set.\n"
				+ "Then it was mapped as Task Input and it should be correctly retrieved.");

		RemoteRuntimeEngine engine = factory.newRuntimeEngine();

		KieSession kieSession = engine.getKieSession();
		TaskService taskService = engine.getTaskService();

		/*HashMap<String, Object> params = new HashMap<String, Object>();
		Person person = new Person();
		person.setName("Oscar");
		params.put("po", person);

		ProcessInstance pi = kieSession.startProcess(PROCESS_ID, params);

		long procId = pi.getId();

		List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(Main.USER, "en-UK");

        long taskId = -1;
        for (TaskSummary task : tasks) {
          if (task.getProcessInstanceId() == procId) {
            taskId = task.getId();
          }
        }

        if (taskId == -1) {
          throw new IllegalStateException("Unable to find task for " + Main.USER + " in process instance " + procId);
        }
        
		System.out.println("Task Data-0:" + taskService.getTaskContent(taskId));

		taskService.start(taskId, Main.USER);
		taskService.complete(taskId, Main.USER, null);*/

		long taskId = 19;
		System.out.println("Task start with id:" + taskId);
		//Task task = taskService.getTaskById(taskId);
		//System.out.println("Task Data-1:" + task.getTaskData().getDocumentType());
		System.out.println("Task Data-2:" + taskService.getTaskContent(taskId));
		Person input = (Person)taskService.getTaskContent(taskId).get("po_in");
		System.out.println("task input Name:" + input.getName());
		System.out.println("task input Surname:" + input.getSurname());

		/*Task task = taskService.getTaskById(taskService.getTasksByProcessInstanceId(pi.getId()).get(0));
		Content content = taskService.getContentById(task.getTaskData().getDocumentContentId());
		System.out.println("Task:" + task);
		System.out.println("Task Data:" + task.getTaskData().getDocumentType());
		System.out.println("Task Data-2:" + taskService.getTaskContent(task.getId()));
		System.out.println("Content:" + content.getContent());

		Object unmarshalledObject = ContentMarshallerHelper.unmarshall(content.getContent(), null);
		System.out.println("unmarshalledObject:" + unmarshalledObject);
		if (!(unmarshalledObject instanceof Map)) {
			System.err.println("well, this shouldn't really happen");

		}
		Map<String, Object> result = (Map<String, Object>) unmarshalledObject;
		System.out.println("Result:" + result);
		//Person input = (Person) result.get("po");
		Person input = (Person)taskService.getTaskContent(task.getId()).get("po_in");
		System.out.println("task input:" + input.getName());*/

		//System.out.println("Task Data-3:" + taskService.getTaskContent(task.getId()));
		
		/*taskService.start(taskService.getTasksByProcessInstanceId(pi.getId()).get(0), Main.USER);
		taskService.complete(taskService.getTasksByProcessInstanceId(pi.getId()).get(0), Main.USER, null);*/
	}
}
