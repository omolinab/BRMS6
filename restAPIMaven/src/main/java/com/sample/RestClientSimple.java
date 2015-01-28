package com.sample;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * Starting an instance of the process, without submitting any variables in a map.
 *
 */
public class RestClientSimple {
	private static final String BASE_URL = "http://localhost:8080/business-central/rest/";
	private static final String AUTH_URL = "http://localhost:8080/business-central/org.kie.workbench.KIEWebapp/j_security_check";
	private static final String DEPLOYMENT_ID = "org.kie.example:project1:1.0";
	private static final String PROCESS_DEF_ID = "project1.myprocess";
	private static String username = "bpmsAdmin";
	private static String password = "admin@123";
	private static AuthenticationType type = AuthenticationType.FORM_BASED;

	public static void main(String[] args) throws Exception {
		System.out.println("Starting process instance: " + DEPLOYMENT_ID);
		System.out.println();
		// start a process instance with no variables.
		//startProcess();
		getProcessInstanceDetails("2");
		System.out.println();
		System.out.println("Completed process instance: " + DEPLOYMENT_ID);
	}

	/**
	 * Start a process using the rest api start call, no map variables passed.
	 *
	 * @throws Exception
	 */
	public static void startProcess() throws Exception {
		String newInstanceUrl = BASE_URL + "runtime/" + DEPLOYMENT_ID + "/process/" + PROCESS_DEF_ID + "/start";
		String dataFromService = getDataFromService(newInstanceUrl, "POST");
		System.out.println("newInstanceUrl:["+newInstanceUrl+"]");
		System.out.println("--------");
		System.out.println(dataFromService);
		System.out.println("--------");
	}

	/**
	 * Start a process using the rest api start call, no map variables passed.
	 *
	 * @throws Exception
	 */
	public static void getProcessInstanceDetails(String processId) throws Exception {
		String newInstanceUrl = BASE_URL + "runtime/" + DEPLOYMENT_ID + "/process/instance/" + processId;
		String dataFromService = getDataFromService(newInstanceUrl, "GET");
		System.out.println("newInstanceUrl:["+newInstanceUrl+"]");
		System.out.println("--------");
		System.out.println(dataFromService);
		System.out.println("--------");
	}

	/**
	 * Call service (GET) with no params.
	 *
	 * @param urlpath Rest API call.
	 * @param method Post or Get call.
	 * @return
	 * @throws Exception
	 */
	private static String getDataFromService(String urlpath, String method) throws Exception {
		// no params
		return getDataFromService(urlpath, method, null, false);
	}
	
	private static void doAuthorization2( String url, HttpClient httpclient, HttpMethodBase method) {
		httpclient.getState().setCredentials(
				new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
				new UsernamePasswordCredentials(username, password)
				);
		method.setDoAuthentication(true);
		httpclient.getParams().setAuthenticationPreemptive(true);
	}
	
	public static String getDataFromService(String urlpath, String method, Map<String, String> params, boolean multipart){
		boolean handleException = false;
		// extract required parameters
		String urlStr = urlpath;
		String result = "";
		if (urlStr == null) {
			throw new IllegalArgumentException("Url is a required parameter");
		}
		if (method == null || method.trim().length() == 0) {
			method = "GET";
		}
		HttpClient httpclient = new HttpClient();
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(3000);
		httpclient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		
		HttpMethodBase theMethod = null;
		if (method.equals("GET")) {
			theMethod = new GetMethod(urlpath); 
		} else {
			theMethod = new PostMethod(urlpath);
		}
		/*GetMethod getMethod = null;
		PostMethod theMethod1 = null;*/
		theMethod.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		doAuthorization2(urlpath,httpclient, theMethod);
		try {
			Header[] headers = theMethod.getRequestHeaders();
			for(Header header:headers){
				System.out.println(header.getName()+":["+header.getValue()+"]");
			}
			int responseCode = httpclient.executeMethod(theMethod);
			System.out.println("Call Restful API again authMethod responseCode:["+responseCode+"] ");
			Map<String, Object> results = new HashMap<String, Object>();
			if (responseCode >= 200 && responseCode < 300) {
				result = theMethod.getResponseBodyAsString();
				System.out.println("Successfully completed :[" + result+"]");
			} else {
				if (handleException) {
					System.out.println("handleException responseCode:["+responseCode+"] theMethod.getResponseBodyAsString():["+theMethod.getResponseBodyAsString()+"] urlStr:["+urlStr+"]");
				} else {
					System.out.println("Unsuccessful responseCode:["+responseCode+"] theMethod.getResponseBodyAsString():["+theMethod.getResponseBodyAsString()+"] urlStr:["+urlStr+"]");
					results.put("StatusMsg", "endpoint " + urlStr + " could not be reached: " + theMethod.getResponseBodyAsString());
				}
			}
			results.put("Status", responseCode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			theMethod.releaseConnection();
		}
		return result;
	}
	
	public enum AuthenticationType {
		BASIC,
		FORM_BASED
	}
}
