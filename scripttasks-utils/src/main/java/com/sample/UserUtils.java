package com.sample;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.workflow.instance.node.WorkItemNodeInstance;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.NodeInstance;
import org.kie.api.runtime.process.ProcessContext;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.User;
import org.kie.internal.task.api.InternalTaskService;
import org.kie.internal.task.api.TaskModelProvider;
import org.kie.internal.task.api.model.InternalOrganizationalEntity;
import org.kie.internal.task.api.model.InternalPeopleAssignments;

public class UserUtils {

	public static void main(String[] args) throws Exception {

		System.out.println("UsersUtils main() method..."); 
/*        
        InternalTaskService taskService = (InternalTaskService) runtime.getTaskService();

        long taskId0 = 0;
        TaskImpl task = (TaskImpl)taskService.getTaskById(taskId0);//retrieving task

        PeopleAssignmentsImpl ppl = new PeopleAssignmentsImpl();
        UserImpl ent = new UserImpl();
        ent.setId("john");// new user adding to a task

        List<OrganizationalEntity> lle = new ArrayList<OrganizationalEntity>();
        lle.add(ent);
        ppl.setBusinessAdministrators(lle);        
*/
		
/*
		NodeInstance ni = null;
		ProcessContext kcontext = null;
		WorkItemNodeInstance WINI = null;
		RuntimeEngine re = null;
		KieSession ks = null;
		
        InternalTaskService taskService = (InternalTaskService) re.getTaskService();
		long wini = ((WorkItemNodeInstance)kcontext.getNodeInstance()).getWorkItemId();
		Task task = taskService.getTaskByWorkItemId(wini);

		taskService.getTasksByProcessInstanceId(kcontext.getProcessInstance().getId());
*/        

	}
	
	public static void helloWorld() {
		System.out.println("Hello world !!!"); 
	}
	
	public static boolean setBussinessAdministrator(Task task, String userId) {

		// userId = "john"
		List<OrganizationalEntity> businessAdmins = new ArrayList<OrganizationalEntity>();
        User user = TaskModelProvider.getFactory().newUser();
        ((InternalOrganizationalEntity) user).setId(userId);
        businessAdmins.add(user);
        businessAdmins.addAll(task.getPeopleAssignments().getBusinessAdministrators());
        ((InternalPeopleAssignments) task.getPeopleAssignments()).setBusinessAdministrators(businessAdmins);
		
		return true;
	}
}
