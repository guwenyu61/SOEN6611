package metrics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ast.ClassObject;
import ast.FieldInstructionObject;
import ast.FieldObject;
import ast.MethodInvocationObject;
import ast.MethodObject;
import ast.SystemObject;

public class AHF {
	private double v = 0.0;
	private double ahf;
	private double attribute_number;
	
	public AHF(SystemObject system) {
		
		Set<ClassObject> classes = system.getClassObjects();
		
		for(ClassObject classObject : classes) {
			System.out.println("class: " + classObject.getName() + "----------------------");
			List<FieldObject> attributes = classObject.getFieldList();
			for(FieldObject attribute : attributes) {
				double visible = getVisible(attribute, classes);
				v += (1 - visible/(classes.size() - 1));
				
				System.out.println("attribute: " + attribute.getName() + " visible: " + visible + " current v: " + v);
			}
			attribute_number += attributes.size();
		}
		ahf = v / attribute_number;
		System.out.println("AHF: " + ahf + " v: " + v + " att: " + attribute_number + " class size: " + classes.size());
	}

	private int getVisible(FieldObject a, Set<ClassObject> classes) {
		int visible = 0;
		boolean isVisible = false;
		for(ClassObject classObject : classes) {

			isVisible = false;
			List<MethodObject> methods = classObject.getMethodList();
			for(MethodObject method : methods) {

				List<FieldInstructionObject> instructions = method.getFieldInstructions();
				for(FieldInstructionObject instruction : instructions) { 

					if(instruction.getName().equals(a.getName()) && instruction.getOwnerClass().equals(a.getClassName())) {
						System.out.println(method.getClassName() + " " + method.getName() + "  use  " + a.getClassName() + " " + a.getName());
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