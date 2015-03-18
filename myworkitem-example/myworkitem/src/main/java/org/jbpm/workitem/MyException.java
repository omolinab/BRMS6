package org.jbpm.workitem;

public class MyException extends RuntimeException {

    MyException(String message) {
    	super(message);
    }

    MyException() {
    	System.out.println("Hello Error!!!!");
    }
}
