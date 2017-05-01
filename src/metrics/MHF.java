package metrics;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ast.ClassObject;
import ast.MethodInvocationObject;
import ast.MethodObject;
import ast.SystemObject;
import ast.decomposition.cfg.AbstractVariable;

public class MHF {
	private double v = 0.0;
	private double mhf;
	private double method_number;
	
	public MHF(SystemObject system) {
		
		Set<ClassObject> classes = system.getClassObjects();
		
		for(ClassObject classObject : classes) {
			System.out.println("class: " + classObject.getName() + "----------------------");
			List<MethodObject> methods = classObject.getMethodList();
			for(MethodObject method : methods) {
				double visible = getVisible(method, classes);
				v += (1 - visible/(classes.size() - 1));
				
				System.out.println("method: " + method.getName() + " visible: " + visible);
			}
			method_number += methods.size();
		}
		mhf = v / method_number;
		System.out.println("MHF: " + mhf);
	}

	private int getVisible(MethodObject m, Set<ClassObject> classes) {
		int visible = 0;
		boolean isVisible = false;
		for(ClassObject classObject : classes) {

			isVisible = false;
			List<MethodObject> methods = classObject.getMethodList();
			for(MethodObject method : methods) {

				List<MethodInvocationObject> invocations = method.getMethodInvocations();
				for(MethodInvocationObject invocation : invocations) { 

					if(invocation.getMethodName().equals(m.getName()) && invocation.getOriginClassName().equals(m.getClassName())) {
						System.out.println(classObject.getName() + " " + method.getName() + "  invoke  " + m.getClassName() + " " + m.getName());
						visible += 1;
						isVisible = true;
						break;
					}
				}
				if(isVisible)
					break;
			}
		}
		
		
		return visible;
	}
	

}
