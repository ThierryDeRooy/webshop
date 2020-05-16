package com.webshop.utils;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public abstract class VelocityEngineUtils {


    public static void mergeTemplate(
            VelocityEngine velocityEngine, String templateLocation, String encoding,
            Map model, Writer writer) throws VelocityException {

        VelocityContext velocityContext = new VelocityContext(model);
        velocityEngine.mergeTemplate(templateLocation, encoding, velocityContext, writer);
    }



    public static String mergeTemplateIntoString(VelocityEngine velocityEngine, String templateLocation,
                                                 String encoding, Map model) throws VelocityException {

        StringWriter result = new StringWriter();
        mergeTemplate(velocityEngine, templateLocation, encoding, model, result);
        return result.toString();
    }

}
