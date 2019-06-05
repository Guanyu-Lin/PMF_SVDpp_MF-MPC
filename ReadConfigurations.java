package PMF_SVDpp_MFMPC_SGD;

import java.io.*;
import java.util.Arrays;

public class ReadConfigurations
{
    public static void readConfigurations(String[]args) throws IOException 
	{
    	// read the configurations
        for (int k=0; k < args.length; k++)
        {
        	if (args[k].equals("-d")) Data.d = Integer.parseInt(args[++k]);
    		else if (args[k].equals("-alpha_u")) Data.alpha_u = Float.parseFloat(args[++k]);
    		else if (args[k].equals("-alpha_v")) Data.alpha_v = Float.parseFloat(args[++k]);        		
    		else if (args[k].equals("-alpha_o")) Data.alpha_o = Float.parseFloat(args[++k]);
    		else if (args[k].equals("-alpha_g")) Data.alpha_g = Float.parseFloat(args[++k]);    		   		
    		else if (args[k].equals("-delta_g")) Data.delta_g = Float.parseFloat(args[++k]);    	
    		else if (args[k].equals("-delta_o")) Data.delta_o = Float.parseFloat(args[++k]);    		
    		else if (args[k].equals("-gamma")) Data.gamma = Float.parseFloat(args[++k]);
			else if (args[k].equals("-fnTrainingUserSupportData")) Data.fnTrainingUserData = args[++k];
//			else if (args[k].equals("-fnTrainingUserQueryData")) Data.fnTrainingUserQueryData = args[++k];
//			else if (args[k].equals("-fnTestUserSupportData")) Data.fnTestUserSupportData = args[++k];
			else if (args[k].equals("-fnTestUserQueryData")) Data.fnTestUserQueryData = args[++k];
			else if (args[k].equals("-fnOutputData")) Data.fnOutputData = args[++k];
    		else if (args[k].equals("-fnTestData")) Data.fnTestUserQueryData = args[++k];
    		else if (args[k].equals("-MinRating")) Data.MinRating = Float.parseFloat(args[++k]);
    		else if (args[k].equals("-MaxRating")) Data.MaxRating = Float.parseFloat(args[++k]);    		
    		else if (args[k].equals("-n")) Data.n = Integer.parseInt(args[++k]);
    		else if (args[k].equals("-m")) Data.m = Integer.parseInt(args[++k]);
//    		else if (args[k].equals("-rho")) Data.rho = Float.parseFloat(args[++k]);
    		else if (args[k].equals("-num_iterations")) Data.num_iterations = Integer.parseInt(args[++k]);
//			else if (args[k].equals("-num_iterations_new")) Data.num_iterations_new = Integer.parseInt(args[++k]);
        }
			
        // print the configurations
        System.out.println(Arrays.toString(args));
        
        System.out.println("d: " + Integer.toString(Data.d));    	
    	System.out.println("alpha_u: " + Float.toString(Data.alpha_u));
    	System.out.println("alpha_v: " + Float.toString(Data.alpha_v));
    	System.out.println("alpha_o: " + Float.toString(Data.alpha_o));
    	System.out.println("alpha_g: " + Float.toString(Data.alpha_g));    	   	
    	System.out.println("delta_g: " + Float.toString(Data.delta_g));
    	System.out.println("delta_o: " + Float.toString(Data.delta_o));
    	System.out.println("gamma: " + Float.toString(Data.gamma));    	
    		
		System.out.println("fnTrainingUserSupportData: " + Data.fnTrainingUserData);
//		System.out.println("fnTrainingUserQueryData: " + Data.fnTrainingUserQueryData);
//		System.out.println("fnTestUserSupportData: " + Data.fnTestUserSupportData);
		System.out.println("fnTestUserQueryData: " + Data.fnTestUserQueryData);
    	System.out.println("fnOutputData: " + Data.fnOutputData);    	
    	System.out.println("MinRating: " + Float.toString(Data.MinRating));
    	System.out.println("MaxRating: " + Float.toString(Data.MaxRating));
    	System.out.println("n: " + Integer.toString(Data.n));
    	System.out.println("m: " + Integer.toString(Data.m));    	    	

        System.out.println("num_iterations: " + Integer.toString(Data.num_iterations));
//		System.out.println("num_iterations_new: " + Integer.toString(Data.num_iterations_new));

        try {
			// --- initialization of file operation
			File file = new File(Data.fnOutputData);
			if(!file.exists())
			{
				file.createNewFile();
			}
			Data.fw = new FileWriter(Data.fnOutputData);
			Data.bw = new BufferedWriter(Data.fw);
			
			// --- output the value of parameters to file
			Data.bw.write("d: " + Integer.toString(Data.d) + "\r\n");
			Data.bw.write("alpha_u: " + Float.toString(Data.alpha_u) + "\r\n");
			Data.bw.write("alpha_v: " + Float.toString(Data.alpha_v) + "\r\n");
			Data.bw.write("alpha_o: " + Float.toString(Data.alpha_o) + "\r\n");
			Data.bw.write("alpha_g: " + Float.toString(Data.alpha_g) + "\r\n");
			Data.bw.write("delta_g: " + Float.toString(Data.delta_g) + "\r\n");
			Data.bw.write("delta_o: " + Float.toString(Data.delta_o) + "\r\n");
			Data.bw.write("gamma: " + Float.toString(Data.gamma) + "\r\n");
			Data.bw.write("fnTrainingUserSupportData: " + Data.fnTrainingUserData + "\r\n");
//			Data.bw.write("fnTrainingUserQueryData: " + Data.fnTrainingUserQueryData + "\r\n");
//			Data.bw.write("fnTestUserSupportData: " + Data.fnTestUserSupportData + "\r\n");
			Data.bw.write("fnTestUserQueryData: " + Data.fnTestUserQueryData + "\r\n");
			Data.bw.write("fnOutputData: " + Data.fnOutputData + "\r\n");
			Data.bw.write("MinRating: " + Float.toString(Data.MinRating) + "\r\n");
			Data.bw.write("MaxRating: " + Float.toString(Data.MaxRating) + "\r\n");
			Data.bw.write("n: " + Integer.toString(Data.n) + "\r\n");
			Data.bw.write("m: " + Integer.toString(Data.m) + "\r\n");
			Data.bw.write("num_iterations: " + Integer.toString(Data.num_iterations) + "\r\n");
//			Data.bw.write("num_iterations_new: " + Integer.toString(Data.num_iterations_new) + "\r\n");
			Data.bw.flush();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
