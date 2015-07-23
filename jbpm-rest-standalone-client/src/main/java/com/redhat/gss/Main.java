package com.redhat.gss;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.jbpm.process.core.context.variable.Variable;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.jbpm.services.task.identity.JBossUserGroupCallbackImpl;
import org.jbpm.workflow.core.node.DataAssociation;
import org.jbpm.workflow.core.node.HumanTaskNode;
import org.jbpm.runtime.manager.impl.RuntimeEnvironmentBuilder;
import org.kie.api.KieBase;
import org.kie.api.definition.process.Node;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.UserGroupCallback;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.InternalRuntimeManager;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.services.client.api.RemoteRestRuntimeFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

/*import com.sample.MyPojo;
import com.sample.MyResultPojo;*/

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

    private static EntityManagerFactory emf;

    public static void main( String[] args ) throws MalformedURLException {
		//accessServerTasks();
		accessingProcessVars();
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

    public static void accessingProcessVars() throws MalformedURLException {

        try {

    		URL url = new URL(APP_URL);
    		// create a factory using the installation parameters
    		RemoteRestRuntimeFactory factory = new RemoteRestRuntimeFactory(DEPLOYMENT_ID, url, USER, PASSWORD);
    		RemoteRuntimeEngine manager = factory.newRuntimeEngine();

            //RuntimeManager manager = getRuntimeManager("myprocess.bpmn2");

            // process variables info
            KieBase kbase = ((InternalRuntimeManager) manager).getEnvironment().getKieBase();
            //KieBase kbase = manager.getKieSession().getKieBase();
            RuleFlowProcess process = (RuleFlowProcess) kbase.getProcess("project1.myprocess");
            VariableScope variableScope = process.getVariableScope();
            List<Variable> variables = variableScope.getVariables();
            System.out.println("=== process variables defined in this process ===");
            for (Variable variable : variables) {
                System.out.println("" + variable.getName() + " -> class : " + variable.getType().getStringType());
            }
            System.out.println();

            // task parameters info
            Node[] nodes = process.getNodes();
            System.out.println("=== task out parameters defined in this process ===");
            for (Node node : nodes) {
                if (node instanceof HumanTaskNode) {
                    System.out.println(" [" + node.getName() + "]");
                    List<DataAssociation> outAssociations = ((HumanTaskNode) node).getOutAssociations();
                    for (DataAssociation dataAssociation : outAssociations) {
                        System.out.println("   " + dataAssociation.getSources().get(0) + " will be mapped to "
                                + dataAssociation.getTarget());
                        System.out.println("     -> class : "
                                + variableScope.findVariable(dataAssociation.getTarget()).getType().getStringType());
                    }
                }
            }
            System.out.println();
/*
            RuntimeEngine runtime = manager.getRuntimeEngine(EmptyContext.get());
            KieSession ksession = runtime.getKieSession();

            // start a new process instance
            {
                Map<String, Object> params = new HashMap<String, Object>();
                MyPojo myPojo = new MyPojo();
                myPojo.setName("John");
                params.put("myData", myPojo);
                ProcessInstance pi = ksession.startProcess("com.sample.bpmn.hello", params);
                long processInstanceId = pi.getId();
                System.out.println("A process instance started : processInstanceId = " + processInstanceId);
            }

            // start and complete a task
            {
                TaskService taskService = runtime.getTaskService();
                List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner("john", "en-UK");
                for (TaskSummary taskSummary : list) {
                    System.out.println("john starts a task : taskId = " + taskSummary.getId() + " taskNodeName = " + taskSummary.getName());
                    taskService.start(taskSummary.getId(), "john");

                    // dynamically find a HumanTaskNode of the task
                    String taskNodeName = taskSummary.getName();
                    for (Node node : nodes) {
                        if (node instanceof HumanTaskNode && node.getName().equals(taskNodeName)) {
                            System.out.println(" [" + node.getName() + "]");
                            List<DataAssociation> outAssociations = ((HumanTaskNode) node).getOutAssociations();
                            for (DataAssociation dataAssociation : outAssociations) {
                                System.out.println("   " + dataAssociation.getSources().get(0) + " will be mapped to " + dataAssociation.getTarget());
                                System.out.println("     -> class : "
                                        + variableScope.findVariable(dataAssociation.getTarget()).getType().getStringType());
                            }
                        }
                    }

                    Map<String, Object> resultParams = new HashMap<String, Object>();
                    MyResultPojo myResultPojo = new MyResultPojo();
                    myResultPojo.setName("Result!");
                    resultParams.put("myOutputParam1", myResultPojo);
                    taskService.complete(taskSummary.getId(), "john", resultParams);
                }
            }

            // -----------
            manager.disposeRuntimeEngine(runtime);
*/
        } catch (Throwable th) {
            th.printStackTrace();
        }

        System.exit(0);
    }

    private static RuntimeManager getRuntimeManager(String process) {

        Properties properties = new Properties();
        properties.setProperty("krisv", "");
        properties.setProperty("mary", "");
        properties.setProperty("john", "");
        UserGroupCallback userGroupCallback = new JBossUserGroupCallbackImpl(properties);

        RuntimeEnvironment environment = RuntimeEnvironmentBuilder.getDefault().persistence(true)
                .entityManagerFactory(emf).userGroupCallback(userGroupCallback)
                .addAsset(ResourceFactory.newClassPathResource(process), ResourceType.BPMN2).get();
        return RuntimeManagerFactory.Factory.get().newPerProcessInstanceRuntimeManager(environment);

    }
    
}
