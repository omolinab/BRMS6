package com.sample;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.transform.stream.StreamSource;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.util.Base64.InputStream;
import org.jbpm.services.task.utils.ContentMarshallerHelper;import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Content;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.services.client.api.RemoteRestRuntimeEngineFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;
import org.kie.services.client.serialization.JaxbSerializationProvider;
import org.kie.services.client.serialization.jaxb.impl.JaxbCommandResponse;
import org.kie.services.client.serialization.jaxb.impl.JaxbCommandsRequest;
import org.kie.services.client.serialization.jaxb.impl.JaxbCommandsResponse;
import org.kie.services.client.serialization.jaxb.impl.task.JaxbTaskResponse;

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
		//humanTaskInputWithCustomParam(factory, "project1.myprocess");
		//humanTaskInputWithCustomParamTest(factory, "project1.myprocess");
		humanTaskInputWithCustomParamTest2(factory, "project1.myprocess");

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
		System.out.println("Ye should check the server log and see the value of the process variable, which we just filled by completing human task");
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

		HashMap<String, Object> params = new HashMap<String, Object>();
		Person person = new Person();
		person.setName(Main.USER);
		params.put("po", person);

		ProcessInstance pi = kieSession.startProcess(PROCESS_ID, params);

		Task task = taskService.getTaskById(taskService.getTasksByProcessInstanceId(pi.getId()).get(0));
		Content content = taskService.getContentById(task.getTaskData().getDocumentContentId());

		Object unmarshalledObject = ContentMarshallerHelper.unmarshall(content.getContent(), null);
		if (!(unmarshalledObject instanceof Map)) {
			System.err.println("well, this shouldn't really happen");

		}
		Map<String, Object> result = (Map<String, Object>) unmarshalledObject;
		Person input = (Person) result.get("po_in");
		System.out.println("task input:" + input.getName());

		taskService.start(taskService.getTasksByProcessInstanceId(pi.getId()).get(0), Main.USER);
		taskService.complete(taskService.getTasksByProcessInstanceId(pi.getId()).get(0), Main.USER, null);
	}

	public static void humanTaskInputWithCustomParamTest(RemoteRestRuntimeEngineFactory factory, String PROCESS_ID) {

		System.out.println("###########################################");
		System.out.println("...testing Human Task Input with Custom Param...");
		System.out.println("###########################################");
		System.out.println("we have set process variable with the object Person, which has name "+Main.USER+" set.\n"
				+ "Then it was mapped as Task Input and it should be correctly retrieved.");

		RemoteRuntimeEngine engine = factory.newRuntimeEngine();

		KieSession kieSession = engine.getKieSession();
		TaskService taskService = engine.getTaskService();

		HashMap<String, Object> params = new HashMap<String, Object>();
		PersonImpl person = new PersonImpl();
		person.setName("Oscar");
		person.setSurname("Molina");
		person.setAddress("Carretera Castellar");
		params.put("po", person);

		ProcessInstance pi = kieSession.startProcess(PROCESS_ID, params);
		//ProcessInstance pi = kieSession.startProcess(PROCESS_ID);

		Task task = taskService.getTaskById(taskService.getTasksByProcessInstanceId(pi.getId()).get(0));
		
		System.out.println("Task:" + task.getId());
		System.out.println("Task Data:" + taskService.getTaskContent(task.getId()));

		/*Person input = (Person)taskService.getTaskContent(task.getId()).get("po_in");
		System.out.println("task input:" + input.getName());*/

		taskService.start(taskService.getTasksByProcessInstanceId(pi.getId()).get(0), Main.USER);
		
		Content content = taskService.getContentById(task.getTaskData().getDocumentContentId());
		Object unmarshalledObject = ContentMarshallerHelper.unmarshall(content.getContent(), null);
		if (!(unmarshalledObject instanceof Map)) {
			System.err.println("well, this shouldn't really happen");

		}
		Map<String, Object> result = (Map<String, Object>) unmarshalledObject;
		//Person input = (Person) result.get("po_in");

		
		System.out.println("Task Data-1:" + taskService.getTaskContent(task.getId()));
		
		taskService.complete(taskService.getTasksByProcessInstanceId(pi.getId()).get(0), Main.USER, null);
		
	}

	public static void humanTaskInputWithCustomParamTest2(RemoteRestRuntimeEngineFactory factory, String PROCESS_ID) {

		System.out.println("###########################################");
		System.out.println("...testing Human Task Input with Custom Param...");
		System.out.println("###########################################");

		RemoteRuntimeEngine engine = factory.newRuntimeEngine();

		KieSession kieSession = engine.getKieSession();
		TaskService taskService = engine.getTaskService();

		//ProcessInstance pi = kieSession.startProcess(PROCESS_ID);

		ProcessInstance pi = kieSession.getProcessInstance(1);
		Task task = taskService.getTaskById(taskService.getTasksByProcessInstanceId(pi.getId()).get(0));
		
		System.out.println("Task:" + task.getId());
		System.out.println("Task Data:" + taskService.getTaskContent(task.getId()));

		Person input = (Person)taskService.getTaskContent(task.getId()).get("po_in");
		System.out.println("task input - Name:" + input.getName());
		System.out.println("task input - Surname:" + input.getSurname());
		System.out.println("task input - Address:" + input.getAddress());
		
		try {
			kieSession.getKieBase().getFactType("com.redhat.bpms.examples.mortgage", "Applicant").newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		//taskService.start(taskService.getTasksByProcessInstanceId(pi.getId()).get(0), Main.USER);
		
		Content content = taskService.getContentById(task.getTaskData().getDocumentContentId());
		System.out.println("ContentId:" + content.getId());
		System.out.println("Content:" + content.getContent());
		System.out.println("ContentObj:" + content);
		
		/*Object unmarshalledObject = ContentMarshallerHelper.unmarshall(content.getContent(), null);
		if (!(unmarshalledObject instanceof Map)) {
			System.err.println("well, this shouldn't really happen");

		}
		Map<String, Object> result = (Map<String, Object>) unmarshalledObject;*/
		//Person input = (Person) result.get("po_in");
	}

	/*
	public List<JaxbCommandResponse<?>> executeCommand(String deploymentId, List<Command<?>> commands) throws Exception {
		URL address = new URL(url + "/runtime/" + deploymentId + "/execute");
		ClientRequest restRequest = createRequest(address);

		JaxbCommandsRequest commandMessage = new JaxbCommandsRequest(deploymentId, commands);
		String body = JaxbSerializationProvider.convertJaxbObjectToString(commandMessage);
		restRequest.body(MediaType.APPLICATION_XML, body);

		ClientResponse<JaxbCommandsResponse> responseObj = restRequest.post(JaxbCommandsResponse.class);
		checkResponse(responseObj);
		JaxbCommandsResponse cmdsResp = responseObj.getEntity();
		return cmdsResp.getResponses();
	}

	private ClientRequest createRequest(URL address) {
		return getClientRequestFactory().createRequest(address.toExternalForm());
	}

	private ClientRequestFactory getClientRequestFactory() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST,
				AuthScope.ANY_PORT, AuthScope.ANY_REALM), new UsernamePasswordCredentials(USER, PASSWORD));
		ClientExecutor clientExecutor = new ApacheHttpClient4Executor(httpClient);
		return new ClientRequestFactory(clientExecutor, ResteasyProviderFactory.getInstance());
	}

	public Task getTaskInstanceInfo(long taskId) throws Exception {
		URL address = new URL(url + "/task/" + taskId);
		ClientRequest restRequest = createRequest(address);

		ClientResponse<JaxbTaskResponse> responseObj = restRequest.get(JaxbTaskResponse.class);
		ClientResponse<InputStream> taskResponse = responseObj.get(InputStream.class);
		JAXBContext jaxbTaskContext = JAXBContext.newInstance(JaxbTaskResponse.class);
		StreamSource source  = new StreamSource(taskResponse.getEntity());
		return jaxbTaskContext.createUnmarshaller().unmarshal(source, JaxbTaskResponse.class).getValue();

	}

	*/
}
