package com.lomoussw.androidstarter.generator;

import java.io.IOException;

import com.lomoussw.androidstarter.AppDetails;
import com.lomoussw.androidstarter.util.RefHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

public class ApplicationGenerator implements Generator {

	private Logger logger;
	private JDefinedClass jClass;
	private AppDetails appDetails;

	public ApplicationGenerator(AppDetails appDetails) {
		this.appDetails = appDetails;
	}

	public JCodeModel generate(JCodeModel jCodeModel, RefHelper ref) throws IOException {
		logger = LoggerFactory.getLogger(getClass());
		try {
			jClass = jCodeModel._class(appDetails.getApplicationPackage());

			jClass._extends(ref.application());

            if (this.appDetails.isCustomApp()) jClass.annotate(ref.reportsCrashes()).param("formKey", "YOUR_FORM_KEY");

			JMethod onCreateMethod = jClass.method(JMod.PUBLIC, jCodeModel.VOID, "onCreate");
			onCreateMethod.annotate(ref.override());

			JBlock onCreateMethodBody = onCreateMethod.body();

            if (this.appDetails.isCustomApp()) onCreateMethodBody.staticInvoke(ref.acra(), "init").arg(JExpr._this());

			onCreateMethodBody.invoke(JExpr._super(), "onCreate");

		} catch (JClassAlreadyExistsException e1) {
			logger.error("Classname already exists", e1);
		}
		return jCodeModel;

	}

}
