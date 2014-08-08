package com.cashman.physio.v1.android.alarm.activity.about;

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.R.drawable;

public   class TypeData {
	public   List<Data> personData = new ArrayList<TypeData.Data>();
	public List<Data> groupData = new ArrayList<TypeData.Data>();
	private static TypeData abouts;
	
	public static TypeData getIntance(){
		if(abouts == null){
			abouts  = new TypeData();
		}
		return abouts;
	}

	private TypeData(){
		/*
		 * DOCTOR INIT;
		 */
		
		Data group1 = new Data("Physiotherapy");
		group1.content ="Physiotherapists can treat just about everything to do with the musculo-skeletal system including sports injuries, back and neck problems, strains and sprains of muscles and complaints relating to joints of the whole body from neck to toe.  They treat all age levels from young children to the very elderly.<br/><br/>" +
				"Treatment at this clinic may include all or any of the following:<br/><br/>" +
				"1:Joint mobilisations<br/>" +
				"2:Deep tissue massage<br/>" +
				"3:Assisted stretching<br/>" +
				"4:Strengthening<br/>" +
				"5:Manipulative spinal therapy<br/>" +
				"6:Acupuncture<br/>" +
				"7:Postural advice and exercises<br/>" +
				"8:Home and gym exercise programmes<br/>" +
				"9:Workstation and ergonomic advice<br/>";
		group1.image = com.cashman.physio.v1.android.alarm.R.drawable.image_group;
		groupData.add(group1);
		
		Data group2 = new Data("Acupuncture");
		group2.content ="We use traditional Chinese and western acupuncture" +
				"techniques for a variety of conditions (if suitable for you)" +
				"to improve healing and reduce pain. This is used in " +
				"conjunction with other physiotherapy treatments to speed the healing of your injury<br/><br/>" + 
				"If you don¡¯t like needles acupressure can be used instead.<br/><br/>" +
				"Acupuncture has been around for about 6,000 yeas and is" +
				"becoming more and more popular as a medical treatment.";
		group2.image = com.cashman.physio.v1.android.alarm.R.drawable.image_group;
		groupData.add(group2);
		
		Data group3 = new Data("Workplace testing");
		group3.content ="We have training and facilities for testing whether you are physical fit and able to take on a particular job you might be applying for.  We are the preferred provider for such testing by JobFit in Australia as well as an Immigration Doctor, for jobs both here and in Australia.";
		group3.image = com.cashman.physio.v1.android.alarm.R.drawable.image_group;
		groupData.add(group3); 
		
		Data data1 = new Data("Physiotherapists");
		data1.titleName ="Physiotherapists";
		data1.content ="<p>No physio info yet<br/>";
		Person p1 = new Person();
		p1.id =1;
		p1.name = "Rob Cashman";
		p1.img = com.cashman.physio.v1.android.alarm.R.drawable.robcashman;
		p1.big_img = com.cashman.physio.v1.android.alarm.R.drawable.robcashman_big;
		p1.frist_content="Physiotherapist and Director";
		p1.content = "Having qualified in 1986 at the Auckland School of Physiotherapy, I moved to Wellington Hospital before moving into private practice a couple of years later.  Since qualifying I have become a Credentialed McKenzie Spinal therapist, have gained a Diploma in Manipulative Therapy and studied as a physiotherapy acupuncturist.  I am also an accredited provider for High Performance Sports NZ.<br/>My special interests and expertise include sports injuries, spinal problems and treating arthritic conditions.<br/>I have had extensive experience with many NZ sports teams which have include the All Whites, Maori All Blacks, NZ Sevens, All Black development sides, NZ and Wellington cricket teams and I am currently with the NZ Black Sox softball team.<br/>I am a keen sportsman particularly enjoying soccer and golf and am also a regular attendee at the gym. <br/>";
		
		
		Person p2 = new Person();
		p2.id = 2;
		p2.name = "Alistair Fergus";
		p2.img = com.cashman.physio.v1.android.alarm.R.drawable.alistairfergus;
		p2.big_img =com.cashman.physio.v1.android.alarm.R.drawable.alistairfergus_big;
		p2.frist_content="I am a highly experienced physiotherapist and in the many years I have been one I have worked with the army, police trainees, numerous sports people including the Paramata Rugby club and a wide variety of injuries ";
		p2.content="requiring various methods of rehabilitation.<br/>I am very much a hands-on therapist using manual techniques, manipulation and acupuncture, specialising in neck and back problems and like to think of myself as going that extra mile for my clients.<br/>I have numerous interests which include: running, cycling, surf swimming and jogging with my dog. <br/>";
		Person p3 = new Person();
		p3.id = 3;
		p3.name = "Liam MacLachlan";
		p3.img = com.cashman.physio.v1.android.alarm.R.drawable.liamm;
		p3.big_img =com.cashman.physio.v1.android.alarm.R.drawable.liamm_big;
		p3.frist_content="I studied at the Robert Gordon University in Aberdeen, Scotland.  As an undergraduate I won several academic achievement awards before graduating in 2008 with a 1st Class honours degree.";
		p3.content="<p>I moved to New Zealand on completing my degree and am now studying towards a Masters Degree in Musculoskeletal physiotherapy. </p><p>I have a specific interest in running injuries and flight analysis and enjoy developing comprehensive exercise/strengthening programmes for rehabilitation and performance enhancement. </p><p>I have numerous interests including Chinese martial arts and running.</p>";
		Person p4 = new Person();
		p4.id =4;
		p4.name = "Loralee Reid";
		p4.img = com.cashman.physio.v1.android.alarm.R.drawable.loraleereid;
		p4.big_img =com.cashman.physio.v1.android.alarm.R.drawable.loraleereid_big;
		p4.frist_content="I am an experienced physiotherapist who studied for the basic MSc in Physical Therapy in Houston, Texas.  I have worked in a variety of hospitals and clinics in the US and NZ but have spent the majority of my time working";
		p4.content =" with general musculo-skeletal injuries.<p>I have expertise in treating soft-tissue injuries.</p><p>I enjoy a variety of outdoor activities which includes a little recreational running but mainly walking trails and hills.</p>";
		data1.personList.add(p1);
	    data1.personList.add(p2);
	    data1.personList.add(p3);
	    data1.personList.add(p4);
	    
		
		this.personData.add(data1);
		
		 
	}
	public class Data {
		public Data(String name){
			this.name = name;
		}
		public String titleName;
	public	String name;
	public	String content;
	public int image;
	public    List<Person> personList = new ArrayList<TypeData.Person>();
	}
	
	public class Person{
	public	int id;
	public	String name;
	public	String remark;
	public	String frist_content;
	public	String content;
	public	int img;
	public int big_img;
	}
}
