package PMF_SVDpp_MFMPC_SGD;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ReadData
{
	public static void readData() throws IOException 
	{
		// --- some statistics, start from index "1"
		Data.user_graded_rating_number = new int[Data.n+1][Data.num_rating_types+1];
		Data.user_rating_number = new int[Data.n+1];
		Data.I_u = new ArrayList[Data.n + 1];

		// ----------------------------------------------------     
		// --- number of training user records
		Data.num_train = 0;	
		String line = null;
		BufferedReader brTrain = null;
//		if(Data.fnTrainingUserData != "")
		{
			brTrain = new BufferedReader(new FileReader(Data.fnTrainingUserData));    	
			while ((line = brTrain.readLine())!=null)
			{
				Data.num_train += 1;
			}
			System.out.println("num_train: " + Data.num_train);
		}

		// --- number of test user support records
//		Data.num_test_user_support_data = 0;
//		BufferedReader brTest = new BufferedReader(new FileReader(Data.fnTestUserSupportData));
//		line = null;
//		while ((line = brTest.readLine())!=null)
//		{
//			Data.num_test_user_support_data += 1;
//		}
//		System.out.println("num_test_user_support_data: " + Data.num_test_user_support_data);


		// --- number of test user query records
		Data.num_test_user_query_data = 0;
		BufferedReader brTest = new BufferedReader(new FileReader(Data.fnTestUserQueryData));
		line = null;
		while ((line = brTest.readLine())!=null)
		{
			Data.num_test_user_query_data += 1;
		}
		System.out.println("num_test_user_query_data: " + Data.num_test_user_query_data);
		// ----------------------------------------------------


		int id_case=0;
		// ----------------------------------------------------
		// --- Locate memory for the data structure    	
		// --- train data
		Data.trainUserRatings = new float [Data.n + 1][Data.m + 1];
		Data.indexUserTrain = new int[Data.num_train]; // start from index "0"
		Data.indexItemTrain = new int[Data.num_train];
		Data.ratingTrain = new float[Data.num_train];

		// ----------------------------------------------------
		// Training data: (userID,itemID,rating)
//		if(Data.fnTrainingUserData != "")
		{
			brTrain = new BufferedReader(new FileReader(Data.fnTrainingUserData));    	
			line = null;
			while ((line = brTrain.readLine())!=null)
			{	
				String[] terms = line.split("\\s+|,|;");
				int userID = Integer.parseInt(terms[0]);
				int itemID = Integer.parseInt(terms[1]);
				float rating = Float.parseFloat(terms[2]);
				Data.indexUserTrain[id_case] = userID;
				Data.indexItemTrain[id_case] = itemID;
				Data.ratingTrain[id_case] = rating;
				id_case+=1;
				Data.trainUserRatings[userID][itemID] = rating;
				if (Data.I_u[userID] == null) Data.I_u[userID] = new ArrayList<Integer>();
	    		Data.I_u[userID].add(itemID);
	    		
				//
				int g = 5; //(int) (rating*2); // convert grade index to 1,2,...,10
				if(Data.TrainingUser_ExplicitFeedbacksGraded.containsKey(userID))
				{
					HashMap<Integer, HashSet<Integer>> g2itemSet 
					= Data.TrainingUser_ExplicitFeedbacksGraded.get(userID);
					if(g2itemSet.containsKey(g))
					{
						HashSet<Integer> itemSet = g2itemSet.get(g);
						itemSet.add(itemID);
						g2itemSet.put(g, itemSet);
					}
					else
					{
						HashSet<Integer> itemSet = new HashSet<Integer>();
						itemSet.add(itemID);
						g2itemSet.put(g, itemSet);
					}
					Data.TrainingUser_ExplicitFeedbacksGraded.put(userID, g2itemSet);
				}
				else
				{
					HashMap<Integer,HashSet<Integer>> g2itemSet 
					= new HashMap<Integer, HashSet<Integer>>();
					HashSet<Integer> itemSet = new HashSet<Integer>();
					itemSet.add(itemID);
					g2itemSet.put(g, itemSet);
					Data.TrainingUser_ExplicitFeedbacksGraded.put(userID, g2itemSet);
				}



				if(Data.TrainingUser_ExplicitFeedbacks.containsKey(userID))
				{
					HashSet<Integer> itemSet = Data.TrainingUser_ExplicitFeedbacks.get(userID);
					itemSet.add(itemID);
					Data.TrainingUser_ExplicitFeedbacks.put(userID, itemSet);
				}
				else
				{
					HashSet<Integer> itemSet = new HashSet<Integer>();
					itemSet.add(itemID);
					Data.TrainingUser_ExplicitFeedbacks.put(userID, itemSet);
				}

				// ---
				Data.user_graded_rating_number[userID][g] += 1;
				Data.user_rating_number[userID] += 1;
			}
			brTrain.close();
			System.out.println("Finished reading the train user data");
		}
		// ----------------------------------------------------    	


		// Test user support data: (userID,itemID,rating)
//		Data.testUserSupportRatings = new float [Data.n + 1][Data.m + 1];
//		Data.indexUserOfTestUserSupportData = new int[Data.num_test_user_support_data];
//		Data.indexItemTestOfTestUserSupportData = new int[Data.num_test_user_support_data];
//		Data.ratingTestUserSupportData = new float[Data.num_test_user_support_data];
//		id_case = 0;
//		brTrain = new BufferedReader(new FileReader(Data.fnTestUserSupportData));    	
//		line = null;
//		while ((line = brTrain.readLine())!=null)
//		{	
//			String[] terms = line.split("\\s+|,|;");
//			int userID = Integer.parseInt(terms[0]);
//			int itemID = Integer.parseInt(terms[1]);
//			float rating = Float.parseFloat(terms[2]);
//			Data.indexUserOfTestUserSupportData[id_case] = userID;
//			Data.indexItemTestOfTestUserSupportData[id_case] = itemID;
//			Data.ratingTestUserSupportData[id_case] = rating;
//			id_case+=1;
//
//			Data.testUserSupportRatings[userID][itemID] = rating;
//
//			//
//			int g = 5;//(int) (rating*2); // convert grade index to 1,2,...,10
//			if(Data.TestUserSupport_ExplicitFeedbacksGraded.containsKey(userID))
//			{
//				HashMap<Integer, HashSet<Integer>> g2itemSet 
//				= Data.TestUserSupport_ExplicitFeedbacksGraded.get(userID);
//				if(g2itemSet.containsKey(g))
//				{
//					HashSet<Integer> itemSet = g2itemSet.get(g);
//					itemSet.add(itemID);
//					g2itemSet.put(g, itemSet);
//				}
//				else
//				{
//					HashSet<Integer> itemSet = new HashSet<Integer>();
//					itemSet.add(itemID);
//					g2itemSet.put(g, itemSet);
//				}
//				Data.TestUserSupport_ExplicitFeedbacksGraded.put(userID, g2itemSet);
//			}
//			else
//			{
//				HashMap<Integer,HashSet<Integer>> g2itemSet 
//				= new HashMap<Integer, HashSet<Integer>>();
//				HashSet<Integer> itemSet = new HashSet<Integer>();
//				itemSet.add(itemID);
//				g2itemSet.put(g, itemSet);
//				Data.TestUserSupport_ExplicitFeedbacksGraded.put(userID, g2itemSet);
//			}
//
//
//
//			if(Data.TestUserSupport_ExplicitFeedbacks.containsKey(userID))
//			{
//				HashSet<Integer> itemSet = Data.TestUserSupport_ExplicitFeedbacks.get(userID);
//				itemSet.add(itemID);
//				Data.TestUserSupport_ExplicitFeedbacks.put(userID, itemSet);
//			}
//			else
//			{
//				HashSet<Integer> itemSet = new HashSet<Integer>();
//				itemSet.add(itemID);
//				Data.TestUserSupport_ExplicitFeedbacks.put(userID, itemSet);
//			}
//
//			// ---
//			Data.test_user_support_graded_rating_number[userID][g] += 1;
//			Data.test_user_support_rating_number[userID] += 1;
//		}
//		brTrain.close();
//		System.out.println("Finished reading the test user support data");
		// ----------------------------------------------------

		// --- test user query data
		Data.indexUserOfTestUserQueryData = new int[Data.num_test_user_query_data];
		Data.indexItemTestOfTestUserQueryData = new int[Data.num_test_user_query_data];
		Data.ratingTestUserQueryData = new float[Data.num_test_user_query_data];
		// ----------------------------------------------------
		// Test data: (userID,itemID,rating)   	
		id_case = 0; // initialize it to zero
		brTest = new BufferedReader(new FileReader(Data.fnTestUserQueryData));
		line = null;
		while ((line = brTest.readLine())!=null)
		{	
			String[] terms = line.split("\\s+|,|;");
			int userID = Integer.parseInt(terms[0]);    		
			int itemID = Integer.parseInt(terms[1]);
			float rating = Float.parseFloat(terms[2]);
			Data.indexUserOfTestUserQueryData[id_case] = userID;
			Data.indexItemTestOfTestUserQueryData[id_case] = itemID;
			Data.ratingTestUserQueryData[id_case] = rating;
			id_case+=1;
		}
		brTest.close();
		System.out.println("Finished reading the test user query data");
		// ----------------------------------------------------


	}    
}
