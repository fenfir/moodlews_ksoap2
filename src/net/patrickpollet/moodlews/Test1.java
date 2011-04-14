package net.patrickpollet.moodlews;

import java.util.Arrays;

//use the new WSDL 
import net.patrickpollet.moodlews.core.*;

// definitions of Moodle server, login, password ...


public class Test1 {
	// WE now use the simplified WSDL that is now generated from the server's
	// php classes !!!
	// instead of the one created by hand ...
	// major difference is that integer type are now mapped to java int and not
	// Integer
	// and in the case of returned arrays there is no need to extract first a
	// property xxxReturn
	// the array is right now available
	
	

	
	
	//DO NOT CHANGE we are talking to Ws using the NEW simplified WSDL
	private static final String MOODLE_SERVICE=Constantes.MOODLE_URL+"wspp/service_pp2.php";
	private static final String MOODLE_NAMESPACE=Constantes.MOODLE_URL+"wspp/wsdl2/";



	public static void main(String args[]) throws Exception {
		new Test1();
	}

	public Test1() throws Exception {

		Mdl_soapserverBindingStub moodle = new Mdl_soapserverBindingStub(MOODLE_SERVICE,MOODLE_NAMESPACE, Constantes.WS_DEBUG);
		LoginReturn lr = moodle.login(Constantes.LOGIN, Constantes.PWD);

		// moodle.get_my_id(client, sesskey);
		if (lr != null) {
			int me = moodle.get_my_id(lr.getClient(),lr.getSessionkey());
			System.out.println("me " + me);
			CourseRecord[] ret = moodle.get_my_courses(lr.getClient(), lr
					.getSessionkey(), "" + me, null);
		
			if (ret != null) {
			
				System.out.println(Arrays.toString(ret));
				String[] coursesIds = new String[ret.length];
				
				int n=0;
				for (CourseRecord c : ret) {
					System.out.println(c.getShortname());
					coursesIds[n++]="" + c.getId();
				}
				System.out.println(Arrays.toString(coursesIds));
				

				CourseRecord[] bis = moodle
						.get_courses(lr.getClient(), lr.getSessionkey(),
								coursesIds, "id");
				for (CourseRecord c : bis)
					System.out.println(c);

				
				 // get forums of some of my courses 
				 for (int i = 0; i < ret.length; i++) {
					 CourseRecord course = ret[i];
				    ResourceRecord[] forums=moodle.get_instances_bytype(lr.getClient(),
				    		 lr.getSessionkey(),new String[]{""+course.getId()},"id", "forum");
				    System.out.println(Arrays.toString(forums));
				    if (i>2) break;
				  }
				 
				RoleRecord[] roles = moodle.get_roles(lr.getClient(), lr
						.getSessionkey(), null, null);
				System.out.println(Arrays.toString(roles));

				UserRecord[] users = moodle.get_user(lr.getClient(), lr
						.getSessionkey(), "ppollet", "username");
				System.out.println(Arrays.toString(users));

				String[] userids = { "ppollet", "pguy", "astrid", "inconnu" };
				UserRecord[] thems = moodle.get_users(lr.getClient(), lr
						.getSessionkey(), userids, "username");
				System.out.println(Arrays.toString(thems));
				for (UserRecord u : thems)
					System.out.println(u);
				for (int i=0; i<thems.length;i++)
					System.out.println(i+" "+thems[i]);

			 
				// create a new user 
			
				UserDatum newU=new UserDatum(moodle.getNAMESPACE());
				newU.setUsername("inconnu005");
				newU.setFirstname("inconnu");
				newU.setLastname("inconnu");
				newU.setEmail("inconnu005@patrickpollet.net");
				newU.setIdnumber("inconnu005");
				newU.setPassword("inconnu");
				System.out.println(newU);
				
				UserRecord[] res=moodle.add_user(lr.getClient(),lr.getSessionkey(),newU);
				System.out.println (Arrays.toString(res));
				
				UserDatum[] users2= new UserDatum[10];
				for (int i=0; i<10; i++) {
					users2[i]=new UserDatum(moodle.getNAMESPACE());
					users2[i].setId(i);
					users2[i].setAction("get");
				}
				UserRecord[] users3=moodle.edit_users(lr.getClient(), lr.getSessionkey(), users2);
				for (UserRecord u : users3)
					System.out.println (u);
				
				//System.out.println (Arrays.toString(users3));
			
				GradeRecord[] grs=moodle.get_course_grades(lr.getClient(), lr.getSessionkey(),"116","id");
				System.out.println (Arrays.toString(grs));
				
				moodle.logout(lr.getClient(),lr.getSessionkey());
				System.out.println ("bye");
			} else
				System.out.println("echec");
		}
	}

}