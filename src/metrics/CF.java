package metrics;

import java.util.List;
import java.util.Set;

import ast.ClassObject;
import ast.FieldInstructionObject;
import ast.FieldObject;
import ast.MethodInvocationObject;
import ast.MethodObject;
import ast.SystemObject;

public class CF {

	private int client_number;
	private double cf;
	
	public CF(SystemObject system) {
		Set<ClassObject> classes = system.getClassObjects();
		
		for(ClassObject classObject1 : classes) {
			
			for(ClassObject classObject2 : classes) {
				if(!classObject1.equals(classObject2)) {
					client_number += isClient(classObject1, classObject2);
				}
			}
		}
		cf = (double)client_number / (classes.size() * (classes.size() - 1));
		System.out.println("CF: " + cf + " client: " + client_number + " size: " + classes.size());
	}

	private int isClient(ClassObject classObject1, ClassObject classObject2) {
		if(callMethod(classObject1, classObject2)) {
			return 1;
		}
		else if(assessAttribute(classObject1, classObject2)) {
			return 1;
		}
		return 0;
	}

	private boolean assessAttribute(ClassObject classObject1, ClassObject classObject2) {
		List<MethodObject> methods1 = classObject1.getMethodList();
		for(MethodObject method1 : methods1) {
			List<FieldInstructionObject> instructions1 = method1.getFieldInstructions();
			for(FieldInstructionObject instruction1 : instructions1) {
				String assessed_name = instruction1.getName();
				String assessed_classname = instruction1.getOwnerClass();
				if(assessed_classname.equals(classObject2.getName()) && isIncluded(assessed_name, classObject2)) 
					return true;
			}
		}
		return false;
	}

	private boolean isIncluded(String assessed_name, ClassObject classObject2) {
		List<FieldObject> attributes2 = classObject2.getFieldList();
		for(FieldObject attribute2 : attributes2) {
			if(attribute2.getName().equals(assessed_name))
				return true;
		}
		return false;
	}

	private boolean callMethod(ClassObject classObject1, ClassObject classObject2) {
		
		List<MethodObject> methods1 = classObject1.getMethodList();
		for(MethodObject method1 : methods1) {
			List<MethodInvocationObject> invocations1 = method1.getMethodInvocations();
			for(MethodInvocationObject invocation1 : invocations1) {
				String invoked_name = invocation1.getMethodName();  
				String invoked_classname = invocation1.getOriginClassName();
				if(invoked_classname.equals(classObject2.getName()) && isContained(invoked_name, classObject2))
					return true;
			}
		}
		
		return false;
	}

	private boolean isContained(String invoked_name, ClassObject classObject2) {
		List<MethodObject> methods2 = classObject2.getMethodList();
		for(MethodObject method2 : methods2) {
			if(method2.getName().equals(invoked_name))
				return true;
		}
		return false;
	}
}
