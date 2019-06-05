package PMF_SVDpp_MFMPC_SGD;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Data 
{
	// === Configurations	
	// the number of latent dimensions
	public static int d = 20; 
	public static int num_rating_types = 5; // should be different for different data
	
	// tradeoff $\alpha_u$
	public static float alpha_u = 0.01f;
	// tradeoff $\alpha_v$
	public static float alpha_v = 0.01f;
	// tradeoff $\alpha_o$
	public static float alpha_o = 0.01f; //
	// tradeoff $\alpha_g$
	public static float alpha_g = 0.01f; // graded observation
	
	// tradeoff $\delta_o$
	public static float delta_o = 0f; // not graded observation, default is 1
	// tradeoff $\delta_g$
	public static float delta_g = 0f; // graded observation, default is 1

//	public static float rho = 1.0f;
	 
	// learning rate $\gamma$
	public static float gamma = 0.01f;

	 // === Input data files
	public static String fnTrainingUserData = "";
//	public static String fnTestUserSupportData = "";
	public static String fnTestUserQueryData = "";
	public static String fnOutputData = "";

	// 
	public static int n; // number of users
	public static int m; // number of items
	public static int num_train; // number of training users triples of (user,item,rating)
//	public static int num_test_user_support_data; // number of test user support triples of (user,item,rating)
	public static int num_test_user_query_data; // number of test user query triples of (user,item,rating)

	public static float MinRating = 1.0f; // minimum rating value (1 for ML100K)
	public static float MaxRating = 5.0f; // maximum rating value

	// scan number over the whole data
	public static int num_iterations = 0; 
//	public static int num_iterations_new = 0; //100

	// === training user data
	public static HashMap<Integer, HashMap<Integer, HashSet<Integer>>> TrainingUser_ExplicitFeedbacksGraded 
	= new HashMap<Integer, HashMap<Integer, HashSet<Integer>>>();
	public static HashMap<Integer, HashSet<Integer>> TrainingUser_ExplicitFeedbacks 
	= new HashMap<Integer, HashSet<Integer>>();

	//rated item of user 
	public static ArrayList<Integer> []I_u;

	
	// === training user data
	public static int[] indexUserTrain; // start from index "0"
	public static int[] indexItemTrain; 
	public static float[] ratingTrain;
	public static float[][] trainUserRatings;
	// === test user support data
	public static float[][] testUserSupportRatings;
	
	
	// === test user support data
//	public static int[] indexUserOfTestUserSupportData;
//	public static int[] indexItemTestOfTestUserSupportData;
//	public static float[] ratingTestUserSupportData;
	// === test user query data
	public static int[] indexUserOfTestUserQueryData;
	public static int[] indexItemTestOfTestUserQueryData;
	public static float[] ratingTestUserQueryData;

	// === some statistics, start from index "1"
	public static int[][] user_graded_rating_number;
	public static int[] user_rating_number;
	
	// === model parameters to learn, start from index "1"
	public static float[][] U;
	public static float[][] V;
	public static float[][] O;
	public static float[][][] G;  

	// === file operation
	public static FileWriter fw ;
	public static BufferedWriter bw;
}
