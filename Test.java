package PMF_SVDpp_MFMPC_SGD;

import java.io.IOException;
import java.util.*;

public class Test 
{
    public static void test() throws IOException 
	{       	
    	// --- number of test cases
    	float mae=0;
    	float rmse=0;    	
    	
    	// ====================================================
    	float [][] Osum = new float[Data.n+1][Data.d];

    	float[] tilde_Uu_g = new float[Data.d];
		float[][] tilde_Uu = new float[Data.n+1][Data.d];
		
    	for( int userID = 1; userID<=Data.n; userID++ )
    	{	
    		// ---
    		if(Data.delta_g!=0)
    		{
	    		for(int g=1; g<=Data.num_rating_types; g++)
	        	{
	        		if( Data.user_graded_rating_number[userID][g]>0 )
	        		{
	        			float explicit_feedback_num_u_sqrt 
	        				= (float) Math.sqrt( Data.user_graded_rating_number[userID][g] );
	    	    		
	        			// --- aggregation
	        			HashSet<Integer> itemSet = Data.TrainingUser_ExplicitFeedbacksGraded.get(userID).get(g);
	    	    		for( int i2 : itemSet )
	    	    		{
	    	    			for (int f=0; f<Data.d; f++)
	    	    	    	{
	    	    				tilde_Uu_g[f] += Data.G[i2][g][f];
	    	    	    	}
	    	    		}
	    	    		
	    	    		// --- normalization
	    	    		for(int f=0; f<Data.d; f++)
	        		    {	
	    	    			tilde_Uu[userID][f] += tilde_Uu_g[f] / explicit_feedback_num_u_sqrt;
	    	    			tilde_Uu_g[f] = 0;
	        		    }
	        		}
	        	}
    		}
    		
    		
    		if( Data.delta_o!=0 && Data.user_rating_number[userID] > 0)
    		{
    			float feedback_num_u_sqrt = (float) Math.sqrt(Data.user_rating_number[userID]);
    			
    			HashSet<Integer> itemSet = Data.TrainingUser_ExplicitFeedbacks.get(userID);			    			
    			for( int i3 : itemSet )
    			{
    				for (int f=0; f<Data.d; f++)
    				{
    					Osum[userID][f] += Data.O[i3][f];
    				}	    		
    			}

    			// --- normalization
    			for (int f=0; f<Data.d; f++)
    			{
    				Osum[userID][f] = Osum[userID][f] / feedback_num_u_sqrt;			
    			}
	    		// ----------------------	
    		}

    		

    	}
    	// ====================================================
    	    	
    	for(int t=0; t<Data.num_test_user_query_data; t++)
    	{
    		int userID = Data.indexUserOfTestUserQueryData[t];
    		int itemID = Data.indexItemTestOfTestUserQueryData[t];
    		float rating = Data.ratingTestUserQueryData[t];
    		
    		// ===========================================    		
    		// --- prediction via inner product
    		float pred = 0;
    		for (int f=0; f<Data.d; f++)
    		{
    			pred += ( Data.U[userID][f] + Data.delta_g * tilde_Uu[userID][f] + Data.delta_o * Osum[userID][f]  )*Data.V[itemID][f];	
    		}
    		// ===========================================
    		
    		// ===========================================
    		// --- post processing predicted rating
    		// if(pred < 1) pred = 1;
    		// if(pred < 0.5) pred = 0.5f;
			// if(pred > 5) pred = 5;
    		if(pred < Data.MinRating) pred = Data.MinRating;
    		if(pred > Data.MaxRating) pred = Data.MaxRating;
    		
			float err = pred-rating;
			mae += Math.abs(err);
			rmse += err*err;
    		// ===========================================    		
    	}
    	float MAE = mae/Data.num_test_user_query_data;
    	float RMSE = (float) Math.sqrt(rmse/Data.num_test_user_query_data);
    	
    	//output result
    	String result = "MAE:" + Float.toString(MAE) +  "| RMSE:" + Float.toString(RMSE);
    	System.out.println(result);
    	try {
			Data.bw.write(result +"\r\n"); 
			Data.bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    // =============================================================
}