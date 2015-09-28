package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.Collections.Each;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.sealionsoftware.Collections.both;
import static java.util.Collections.emptyList;

public class ClasspathClassFactory {

    private Map<String, Class> classLibrary;

    public ClasspathClassFactory(Map<String, Class> classLibrary) {
        this.classLibrary = classLibrary;
    }

    public void addToLibrary(java.lang.Class clazz){
        addOrGet(clazz);
    }

    private Class addOrGet(java.lang.Class clazz){
        String name = clazz.getName();
        Class ret = classLibrary.get(name);
        if (ret != null){
            return ret;
        }

        ret = new Class(name);
        classLibrary.put(name, ret);

        ret.initialise(
                convert(clazz.getTypeParameters()),
                convert(clazz.getGenericSuperclass()),
                convert(clazz.getGenericInterfaces())
        );
        return ret;
    }

    private List<Parameter> convert(java.lang.reflect.TypeVariable[] input){
        List<Parameter> ret = new ArrayList<>();
        for (java.lang.reflect.TypeVariable type : input){
            ret.add(new Parameter(type.getName(), convert(type.getBounds()[0])));
        }
        return ret;
    }

    private List<Type> convert(java.lang.reflect.Type[] input){
        List<Type> ret = new ArrayList<>();
        for (java.lang.reflect.Type type : input){
            ret.add(convert(type));
        }
        return ret;
    }

    private Type convert(java.lang.reflect.Type input){

        if (input == null || Object.class.equals(input)){
            return null;
        }

        if (input instanceof java.lang.Class){
            return new ClassBasedType(addOrGet((java.lang.Class) input), emptyList());
        }

        if (input instanceof ParameterizedType){
            ParameterizedType parameterisedType = (ParameterizedType) input;
            java.lang.Class raw = (java.lang.Class) parameterisedType.getRawType();
            Class clazz = addOrGet(raw);
            List<Parameter> arguments = new ArrayList<>();
            for (Each<TypeVariable, java.lang.reflect.Type> each : both(raw.getTypeParameters(), parameterisedType.getActualTypeArguments())){
                arguments.add(new Parameter(each.i.getName(), convert(each.j)));
            }
            return new ClassBasedType(clazz, arguments);
        }

        if (input instanceof TypeVariable){
            return null;
        }

        throw new RuntimeException("Cannot handle input of type " + input.getClass().getName());
    }
}
