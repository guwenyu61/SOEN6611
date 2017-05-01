package metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ast.ClassObject;
import ast.MethodObject;
import ast.SystemObject;
import ast.TypeObject;

public class PF {

	private double pf;
	private double overridden_total = 0.0;
	private double new_total = 0.0;
	
	public PF(SystemObject system) {
		Set<ClassObject> classes = system.getClassObjects();
		
		for(ClassObject classObject : classes) {
			int overridden_number = 0;
			System.out.println("class: " + classObject.getName() + "----------------------");
			
			List<ClassObject> superclasses = getSuperClasses(classObject, system);
			overridden_number = getOverridden(superclasses, classObject);
			
			int new_number = classObject.getMethodList().size() - overridden_number;
			int children_number = getChildrenNumber(classObject, system);
			
			System.out.println("new: " + new_number + " children: " + children_number);
			overridden_total += overridden_number;
			new_total += new_number * children_number;
		}
		
		pf = overridden_total / new_total;
		System.out.println("PF: " + pf + " overridden: " + overridden_total + " new: " + new_total);
	}
	
	private int getChildrenNumber(ClassObject c, SystemObject system) {
		Set<ClassObject> classes = system.getClassObjects();
		int children_number = 0;
		for(ClassObject classObject : classes) {
			TypeObject superclass = classObject.getSuperclass();
			if(superclass != null) {
				String name = superclass.toString();
				if(name != null && name.equals(c.getName())) {
					children_number++;
				}
			}

		}
		
		return children_number;
	}

	private int getOverridden(List<ClassObject> superclasses, ClassObject c) {
		int overridden_number;
		List<String> overriddenMethods = new ArrayList<String>();
		for(ClassObject classObject : superclasses) {
			List<MethodObject> super_methods = classObject.getMethodList();
			for(MethodObject super_method : super_methods) {
				String name = super_method.getName();
				if(isOverridden(super_method, c)) {
					if(!overriddenMethods.contains(name)) {
						overriddenMethods.add(name);
						System.out.println(name);
					}
				}
			}
		}
		
		overridden_number = overriddenMethods.size();
		return overridden_number;
	}
	
	private boolean isOverridden(MethodObject super_method, ClassObject c) {
		List<MethodObject> methods = c.getMethodList();
		for(MethodObject method : methods) {
			if(method.getName().equals(super_method.getName()))
				return true;
		}
		return false;
	}

	private List<ClassObject> getSuperClasses(ClassObject c, SystemObject system) {
		ClassObject current_class = c;
		List<ClassObject> classes = new ArrayList<ClassObject>();
		while(current_class != null && current_class.getSuperclass() != null) {
			String name = current_class.getSuperclass().toString();
			System.out.println("super: " + name);
			ClassObject s = getSuperClass(name, system);
			if(s != null) {
				classes.add(s);
				current_class = s;				
			}
			current_class = null;
		}
		
		return classes;
	}
	
	private ClassObject getSuperClass(String name, SystemObject system) {
		ClassObject c;
		for(ClassObject classObject : system.getClassObjects()) {
			if(classObject.getName().equals(name)) {
				c = classObject;
				return c;
			}
		}
		return null;
	}
}
