package com.tarot.codegen;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by Martin on 2016/8/1.
 */
public final class Context {
    private final ProcessingEnvironment pe;
    private final boolean logDebug = true;

    public Context(ProcessingEnvironment pe) {
        this.pe = pe;
    }

    public TypeElement getTypeElementForFullyQualifiedName(String fqcn) {
        Elements elementUtils = pe.getElementUtils();
        return elementUtils.getTypeElement( fqcn );
    }

    public void logMessage(Diagnostic.Kind type, String message) {
        if (!logDebug && type.equals(Diagnostic.Kind.OTHER)) {
            return;
        }
        pe.getMessager().printMessage(type, message);
    }

    public Elements getElementUtils() {
        return pe.getElementUtils();
    }

    public Types getTypeUtils() {
        return pe.getTypeUtils();
    }
}
