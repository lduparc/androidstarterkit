package com.lomoussw.androidstarter.model;

import org.junit.Test;

import com.lomoussw.androidstarter.AppDetails;
import com.lomoussw.androidstarter.AppDetails.Builder;

public class ApplicationTest {

	@Test
	public void packageName() {
		Builder builder = new AppDetails.Builder();

		builder.packageName("com.lomoussw.androidstarter");
		builder.packageName("com.lomoussw.AndroidStarterR");
		builder.packageName("com.lomoussw1.androidstarterr2");
		builder.packageName("com.lomoussw1._2androidstarterr2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void packageName_invalid1() {
		new AppDetails.Builder().packageName("com.lomoussw.androidstarter.");
	}

	@Test(expected = IllegalArgumentException.class)
	public void packageName_invalid2() {
		new AppDetails.Builder().packageName(".com.athomas.androidstarterr");
	}

	@Test(expected = IllegalArgumentException.class)
	public void packageName_invalid3() {
		new AppDetails.Builder().packageName(".com.athomas.androidstarterr..");
	}

	@Test(expected = IllegalArgumentException.class)
	public void packageName_invalid4() {
		new AppDetails.Builder().packageName(".com.athomas..androidstarterr");
	}

	@Test
	public void name() throws Exception {
		Builder builder = new AppDetails.Builder();

		builder.name("AndroidStarterR");
		builder.name("androidstarterr");
		builder.name("androidstarter1");
		builder.name("androidstarterr_");
	}

	@Test(expected = IllegalArgumentException.class)
	public void name_invalid1() {
		new AppDetails.Builder().name("AndroidKickstartR-");
	}

	@Test(expected = IllegalArgumentException.class)
	public void name_invalid2() {
		new AppDetails.Builder().name("AndroidKickstartR_éà");
	}

	@Test(expected = IllegalArgumentException.class)
	public void name_invalid3() {
		new AppDetails.Builder().name("AndroidKickstartR@");
	}

	@Test
	public void activity() throws Exception {
		Builder builder = new AppDetails.Builder();

		builder.activity("AndroidKickstartR");
		builder.activity("androidkickstartr");
		builder.activity("androidkickstartr1");
		builder.activity("androidkickstartr_");
	}

	@Test(expected = IllegalArgumentException.class)
	public void activity_invalid1() {
		new AppDetails.Builder().activity("AndroidKickstartR-");
	}

	@Test(expected = IllegalArgumentException.class)
	public void activity_invalid2() {
		new AppDetails.Builder().activity("AndroidKickstartR_éà");
	}

	@Test(expected = IllegalArgumentException.class)
	public void activity_invalid3() {
		new AppDetails.Builder().activity("AndroidKickstartR@");
	}

}
