package com.lomoussw.androidstarter.generator;

import java.io.IOException;

import com.lomoussw.androidstarter.util.RefHelper;
import com.sun.codemodel.JCodeModel;

public interface Generator {

	public JCodeModel generate(JCodeModel jCodeModel, RefHelper ref) throws IOException;

}
