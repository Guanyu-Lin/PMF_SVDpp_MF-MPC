package PMF_SVDpp_MFMPC_SGD;

public class Initialization
{
    public static void initialization()
	{
    	// --- model parameters to learn, start from index "1"
        Data.U = new float[Data.n+1][Data.d];
        Data.V = new float[Data.m+1][Data.d];
        Data.O = new float[Data.m+1][Data.d];
        Data.G = new float[Data.m+1][Data.num_rating_types+1][Data.d];
        
    	// ======================================================    	
    	// --- initialization of U, V, O, G
    	for (int u=1; u<Data.n+1; u++)
    	{
    		for (int f=0; f<Data.d; f++)
    		{
    			Data.U[u][f] = (float) ( (Math.random()-0.5)*0.01 );
    		}
    	}
    	//
    	for (int i=1; i<Data.m+1; i++)
    	{
    		for (int f=0; f<Data.d; f++)
    		{
    			Data.V[i][f] = (float) ( (Math.random()-0.5)*0.01 );
    			Data.O[i][f] = (float) ( (Math.random()-0.5)*0.01 );
    		}
    	}
    	//
    	for(int i=1; i<Data.m+1; i++)
		{
    		for(int g=1; g<=Data.num_rating_types; g++)	
        	{
        		for(int f=0; f<Data.d; f++)
        		{	
        			Data.G[i][g][f] = (float) ( (Math.random()-0.5)*0.01 );
        		}
        	}
		}
    	// ======================================================
    }
}
