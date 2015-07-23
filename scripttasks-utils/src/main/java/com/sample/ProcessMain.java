package com.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jbpm.services.task.identity.JBossUserGroupCallbackImpl;
import org.jbpm.test.JBPMHelper;
import org.jbpm.workflow.core.node.Assignment;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
//import org.jbpm.runtime.manager.impl.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.task.TaskService;
import org.kie.api.task.UserGroupCallback;
import org.kie.api.task.model.Group;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.api.task.model.PeopleAssignments;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.api.task.model.User;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.task.api.TaskModelProvider;
import org.kie.internal.task.api.model.InternalOrganizationalEntity;
import org.kie.internal.task.api.model.InternalPeopleAssignments;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class ProcessMain {

	private static EntityManagerFactory emf;

	public static void main(String[] args) {
        setup();

        KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieBase kbase = kContainer.getKieBase("kbase");

		RuntimeManager manager = createRuntimeManager(kbase);
		RuntimeEngine engine = manager.getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();
		TaskService taskService = engine.getTaskService();

		ksession.startProcess("com.sample.bpmn.hello");

		// let john execute Task 1
		List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner("john", "en-UK");
		TaskSummary task = list.get(0);
		System.out.println("John is executing task " + task.getName());
		taskService.start(task.getId(), "john");
		taskService.complete(task.getId(), "john", null);

		Task myTask = taskService.getTaskById(task.getId());
		System.out.println("Taskid: "+ myTask.getId());
		PeopleAssignments pa = myTask.getPeopleAssignments();
		System.out.println("BA: "+ pa.getBusinessAdministrators());
		System.out.println("PO: "+ pa.getPotentialOwners());
		
		setBussinessAdministrator(myTask, "john", "johns");

//		myTask = taskService.getTaskById(task.getId());
		System.out.println("Taskid: "+ myTask.getId());
		pa = myTask.getPeopleAssignments();
		System.out.println("BA: "+ pa.getBusinessAdministrators());
		System.out.println("PO: "+ pa.getPotentialOwners());
		
		// let mary execute Task 2
		list = taskService.getTasksAssignedAsPotentialOwner("mary", "en-UK");
		task = list.get(0);
		System.out.println("Mary is executing task " + task.getName());
		taskService.start(task.getId(), "mary");
		taskService.complete(task.getId(), "mary", null);

		manager.disposeRuntimeEngine(engine);
		System.exit(0);
	}

	public static boolean setBussinessAdministrator(Task task, String userId, String groupId) {

		// userId = "john"
		List<OrganizationalEntity> businessAdmins = new ArrayList<OrganizationalEntity>();
        User user = TaskModelProvider.getFactory().newUser();
        Group group = TaskModelProvider.getFactory().newGroup();
        ((InternalOrganizationalEntity) user).setId(userId);
        ((InternalOrganizationalEntity) group).setId(groupId);
        businessAdmins.add(user);
        businessAdmins.add(group);
        businessAdmins.addAll(task.getPeopleAssignments().getBusinessAdministrators());
        ((InternalPeopleAssignments) task.getPeopleAssignments()).setBusinessAdministrators(businessAdmins);
		
		return true;
	}
	
	public static boolean updateBussinessAdministratorUser(Task task, String userIdOld, String userIdNew) {

		// userId = "john"
		List<OrganizationalEntity> businessAdmins = task.getPeopleAssignments().getBusinessAdministrators();
		if (businessAdmins != null && !businessAdmins.isEmpty()) {
			for (Iterator<OrganizationalEntity> it = businessAdmins.iterator(); it.hasNext(); ) {
				OrganizationalEntity oe = (OrganizationalEntity)it.next();
				if (oe.getId().equals(userIdOld)) {
					businessAdmins.remove(oe);
					break;
				}
	        }
		}
        User user = TaskModelProvider.getFactory().newUser();
        ((InternalOrganizationalEntity) user).setId(userIdNew);
        businessAdmins.add(user);
        ((InternalPeopleAssignments) task.getPeopleAssignments()).setBusinessAdministrators(businessAdmins);
		
		return true;
	}

	public static boolean updateBussinessAdministratorGroup(Task task, String groupIdOld, String groupIdNew) {

		// userId = "john"
		List<OrganizationalEntity> businessAdmins = task.getPeopleAssignments().getBusinessAdministrators();
		if (businessAdmins != null && !businessAdmins.isEmpty()) {
			for (Iterator<OrganizationalEntity> it = businessAdmins.iterator(); it.hasNext(); ) {
				OrganizationalEntity oe = (OrganizationalEntity)it.next();
				if (oe.getId().equals(groupIdOld)) {
					businessAdmins.remove(oe);
					break;
				}
	        }
		}
        Group group = TaskModelProvider.getFactory().newGroup();
        ((InternalOrganizationalEntity) group).setId(groupIdNew);
        businessAdmins.add(group);
        ((InternalPeopleAssignments) task.getPeopleAssignments()).setBusinessAdministrators(businessAdmins);
		
		return true;
	}


	
	
	
	
	//---------------------------------------------------------------	

	
    private static void setup() {
        // for H2 datasource
        /*JBPMHelper.startH2Server();
        JBPMHelper.setupDataSource();*/

        // for external database datasource
        setupMYSQLDataSource();
        //setupPSGDataSource();
        //setupORADataSource();

        Map configOverrides = new HashMap();
        //configOverrides.put("hibernate.hbm2ddl.auto", "create"); // Uncomment if
                                                                 // you don't
                                                                 // want to
                                                                 // clean up
                                                                 // tables
        // Edit for other databases
        configOverrides.put("hibernate.dialect",
        		"org.hibernate.dialect.MySQL5InnoDBDialect");
//        		"org.hibernate.dialect.H2Dialect");
//            	"org.hibernate.dialect.MySQLDialect");

        emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa", configOverrides);

    }

    private static RuntimeManager getRuntimeManager(String process) {

        Properties properties = new Properties();
        properties.setProperty("krisv", "");
        properties.setProperty("mary", "");
        properties.setProperty("john", "");
        UserGroupCallback userGroupCallback = new JBossUserGroupCallbackImpl(properties);

        RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder()
                .entityManagerFactory(emf).userGroupCallback(userGroupCallback)
                .addAsset(ResourceFactory.newClassPathResource(process), ResourceType.BPMN2).get();
        return RuntimeManagerFactory.Factory.get().newPerProcessInstanceRuntimeManager(environment);

    }

    public static PoolingDataSource setupMYSQLDataSource() {
        // Please edit here when you want to use your database
        PoolingDataSource pds = new PoolingDataSource();
        pds.setUniqueName("jdbc/jbpm-ds");
        pds.setClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
        pds.setMaxPoolSize(5);
        pds.setAllowLocalTransactions(true);
        pds.getDriverProperties().put("user", "mysql");
        pds.getDriverProperties().put("password", "mysql");
        pds.getDriverProperties().put("url", "jdbc:mysql://localhost:3306/bpms603");
        //pds.getDriverProperties().put("driverClassName", "com.mysql.jdbc.Driver");
        pds.init();
        return pds;
    }
	
    public static PoolingDataSource setupPSGDataSource() {
        // Please edit here when you want to use your database
        PoolingDataSource pds = new PoolingDataSource();
        pds.setUniqueName("jdbc/jbpm-ds");
        pds.setClassName("org.postgresql.xa.PGXADataSource");
        pds.setMaxPoolSize(5);
        pds.setAllowLocalTransactions(true);
        pds.getDriverProperties().put("user", "postgres");
        pds.getDriverProperties().put("password", "postgres");
        pds.getDriverProperties().put("url", "jdbc:postgresql://192.168.100.221:5432/bpm6003");
        pds.getDriverProperties().put("driverClassName", "org.postgresql.Driver");
        pds.init();
        return pds;
    }
	
    public static PoolingDataSource setupORADataSource() {
        // Please edit here when you want to use your database
        PoolingDataSource pds = new PoolingDataSource();
        pds.setUniqueName("jdbc/jbpm-ds");
        pds.setClassName("oracle.jdbc.xa.OracleXADataSource");
        pds.setMaxPoolSize(5);
        pds.setAllowLocalTransactions(true);
        pds.getDriverProperties().put("user", "magic_bpms603_ORA");
        pds.getDriverProperties().put("password", "magic_bpms603_ORA");
        pds.getDriverProperties().put("url", "jdbc:oracle:thin:@192.168.100.134:1521:orcl");
        pds.getDriverProperties().put("driverClassName", "oracle.jdbc.driver.Driver");
        pds.init();
        return pds;
    }
	
	
//---------------------------------------------------------------	
	
	
	public static void main_OLD(String[] args) {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieBase kbase = kContainer.getKieBase("kbase");

		RuntimeManager manager = createRuntimeManager(kbase);
		RuntimeEngine engine = manager.getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();
		TaskService taskService = engine.getTaskService();

		ksession.startProcess("com.sample.bpmn.hello");

		// let john execute Task 1
		List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner("john", "en-UK");
		TaskSummary task = list.get(0);
		System.out.println("John is executing task " + task.getName());
		taskService.start(task.getId(), "john");
		taskService.complete(task.getId(), "john", null);

		// let mary execute Task 2
		list = taskService.getTasksAssignedAsPotentialOwner("mary", "en-UK");
		task = list.get(0);
		System.out.println("Mary is executing task " + task.getName());
		taskService.start(task.getId(), "mary");
		taskService.complete(task.getId(), "mary", null);

		manager.disposeRuntimeEngine(engine);
		System.exit(0);
	}

	private static RuntimeManager createRuntimeManager(KieBase kbase) {
		JBPMHelper.startH2Server();
		JBPMHelper.setupDataSource();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
		RuntimeEnvironmentBuilder builder = RuntimeEnvironmentBuilder.Factory.get()
			.newDefaultBuilder().entityManagerFactory(emf)
			.knowledgeBase(kbase);
		return RuntimeManagerFactory.Factory.get()
			.newSingletonRuntimeManager(builder.get(), "com.sample:example:1.0");
	}

}