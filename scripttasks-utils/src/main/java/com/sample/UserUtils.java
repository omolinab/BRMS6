package com.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jbpm.services.task.impl.model.TaskImpl;
import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;
import org.jbpm.workflow.instance.node.WorkItemNodeInstance;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.NodeInstance;
import org.kie.api.runtime.process.ProcessContext;
import org.kie.api.runtime.rule.Variable;
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
		
/**/
		NodeInstance ni = null;
		ProcessContext kcontext = null;
		WorkItemNodeInstance WINI = null;
		long wini = 0;
		RuntimeEngine re = null;
		
		TaskService taskService = re.getTaskService();
		Task task = taskService.getTaskByWorkItemId(wini);

		taskService.getTasksByProcessInstanceId(kcontext.getProcessInstance().getId());
		
		
        List<OrganizationalEntity> businessAdmins = new ArrayList<OrganizationalEntity>();
        User user = TaskModelProvider.getFactory().newUser();
        ((InternalOrganizationalEntity) user).setId("john");
        businessAdmins.add(user);
        businessAdmins.addAll(task.getPeopleAssignments().getBusinessAdministrators());
        ((InternalPeopleAssignments) task.getPeopleAssignments()).setBusinessAdministrators(businessAdmins);
/**/        

	}
}
