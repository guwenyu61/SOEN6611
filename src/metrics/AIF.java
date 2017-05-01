package metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ast.ClassObject;
import ast.FieldObject;
import ast.MethodObject;
import ast.SystemObject;
import ast.TypeObject;

public class AIF {
	
	private double aif;
	private double inherited_total = 0.0;
	private double total = 0.0;
	
	public AIF(SystemObject system) {
		Set<ClassObject> classes = system.getClassObjects();
		
		for(ClassObject classObject : classes) {
			int inherited_number = 0;
			System.out.println("class: " + classObject.getName() + "----------------------");
			
			List<ClassObject> superclasses = getSuperClasses(classObject, system);
			inherited_number = getInherited(superclasses, classObject);
			
			int declared_number = classObject.getFieldList().size();
			System.out.println("total: " + declared_number);
			inherited_total += inherited_number;
			total += inherited_number + declared_number;
		}
		
		aif = inherited_total / total;
		System.out.println("AIF: " + aif + " inherited: " + inherited_total + " total: " + total);
	}
	
	private int getInherited(List<ClassObject> superclasses, ClassObject c) {
		int inherited_number;
		List<String> inheritedAttributes = new ArrayList<String>();
		for(ClassObject classObject : superclasses) {
			List<FieldObject> super_attributes = classObject.getFieldList();
			for(FieldObject super_attribute : super_attributes) {
				String name = super_attribute.getName();
				if(!isOverridden(super_attribute, c)) {
					if(!inheritedAttributes.contains(name)) {
						inheritedAttributes.add(name);
						System.out.println(name);
					}
				}
			}
		}
		
		inherited_number = inheritedAttributes.size();
		return inherited_number;
	}
	
	private boolean isOverridden(FieldObject super_attribute, ClassObject c) {
		List<FieldObject> attributes = c.getFieldList();
		for(FieldObject attribute : attributes) {
			if(attribute.getName().equals(super_attribute.getName()))
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
