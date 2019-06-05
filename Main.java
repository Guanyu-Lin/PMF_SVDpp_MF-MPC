package PMF_SVDpp_MFMPC_SGD;


import java.io.*;

//java -Xmx2048m Main -d 20 -alpha_u 0.01 -alpha_v 0.01 -alpha_g 0.01 -alpha_o 0.01 -beta_u 0.01 -beta_v 0.01 -delta_g 1 -delta_o 1 -gamma 1000 -fnTrainData ./ml-100k/copy1.target -fnTestData ./ml-100k/copy1.test -fnOutputData ./result_SVDpp_MFMPC -n 943 -m 1682 -num_iterations 2000 -MinRating 1 -MaxRating 5

//SVDpp
//java -Xmx2048m Main -d 20 -alpha_u 0.01 -alpha_v 0.01 -alpha_g 0.01 -alpha_o 0.01 -beta_u 0.01 -beta_v 0.01 -delta_g 0 -delta_o 1 -gamma 1000 -fnTrainData ./ml-100k/copy1.target -fnTestData ./ml-100k/copy1.test -fnOutputData ./result_SVDpp_MFMPC -n 943 -m 1682 -num_iterations 2000 -MinRating 1 -MaxRating 5
//java -Xmx2048m Main -alpha_v 0.01 -alpha_u 0.01 -alpha_o 0.01 -alpha_g 0.01 -gamma 0.01 -delta_o 1 -delta_g 0 -d 20 -fnTestUserSupportData ./ml-100k/copy1.target -fnTestUserQueryData ./ml-100k/copy1.test -fnOutputData ./result_SVDpp -n 943 -m 1682 -num_iterations_new 2500

//MFMPC
//java -Xmx2048m Main -d 20 -alpha_u 0.01 -alpha_v 0.01 -alpha_g 0.01 -alpha_o 0.01 -beta_u 0.01 -beta_v 0.01 -delta_g 1 -delta_o 0 -gamma 1000 -fnTrainData ./ml-100k/copy1.target -fnTestData ./ml-100k/copy1.test -fnOutputData ./result_SVDpp_MFMPC -n 943 -m 1682 -num_iterations 2000 -MinRating 1 -MaxRating 5

public class Main 
{
    public static void main(String[] args) throws IOException 
	{	
		// 1. read configurations		
		ReadConfigurations.readConfigurations(args);

		// 2. read training data and test data
        ReadData.readData();
        
		// 3. apply initialization
		Initialization.initialization();

		// 4. training
//		if(Data.fnTrainingUserData != "")
		{
			Train.train();
		}
		
		// 5. new user data training
//		Train.newTrain();
		
		// 6. test
		Test.test();		
    }
}
