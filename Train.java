package PMF_SVDpp_MFMPC_SGD;

import java.io.IOException;
import java.util.HashSet;



public class Train 
{
	public static void train() throws IOException 
	{    	
		// =================================================================

		for (int iter = 0; iter < Data.num_iterations; iter++)
		{	
			float loss = 0;
			// output each iteration result
			try {
				Data.bw.write("Iter:" + Integer.toString(iter) + "| ");
				Data.bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.print("Iter:" + Integer.toString(iter) + "| ");

			try {
				Test.test();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ====================================


			for (int iter_rand = 0; iter_rand < Data.num_train; iter_rand++) 
			{   	    		
				// ===========================================
				// --- random sampling one triple of (userID,itemID,rating), Math.random(): [0.0, 1.0)
//				int rand_case = (int) Math.floor( Math.random() * Data.num_train );
//				int userID = Data.indexUserTrain[rand_case];	    		
//				int itemID = Data.indexItemTrain[rand_case];
//				float rating = Data.ratingTrain[rand_case];
				int userID = (int) Math.floor( Math.random() * (Data.n - 1)) + 1 ;
				int itemID = Data.I_u[userID].get((int) (Math.random() * Data.I_u[userID].size()));
				float rating = Data.trainUserRatings[userID][itemID];

				// ===========================================
				float [] tilde_Uu_g = new float[Data.d];
				float [] tilde_Uu = new float[Data.d];

				if(Data.delta_g!=0)
				{
					//compute \bar{C}_{u\cdot}^\mathcal{G}
					for(int g=1; g<=Data.num_rating_types; g++)
					{
						if( Data.user_graded_rating_number[userID][g]>0 )
						{
							// ---
							HashSet<Integer> itemSet = Data.TrainingUser_ExplicitFeedbacksGraded.get(userID).get(g);

							// ---
							float explicit_feedback_num_u_sqrt = 0;
							if(itemSet.contains(itemID) )
							{
								if( itemSet.size()>1 )
								{
									explicit_feedback_num_u_sqrt 
									= (float) Math.sqrt( Data.user_graded_rating_number[userID][g] - 1 );
								}
							}
							else
							{
								explicit_feedback_num_u_sqrt 
								= (float) Math.sqrt( Data.user_graded_rating_number[userID][g] );
							}

							if (explicit_feedback_num_u_sqrt>0)
							{
								// --- aggregation
								for( int i2 : itemSet )
								{
									if(i2 != itemID)
									{
										for(int f=0; f<Data.d; f++)
										{
											tilde_Uu_g[f] += Data.G[i2][g][f];
										}
									}
								}

								// --- normalization
								for (int f=0; f<Data.d; f++)
								{
									tilde_Uu_g[f] = tilde_Uu_g[f] / explicit_feedback_num_u_sqrt;
									tilde_Uu[f] += tilde_Uu_g[f];
									tilde_Uu_g[f] = 0;
								}
							}
						}
					}
				}


				float [] Ou = new float[Data.d];				
				if( Data.delta_o!=0 && Data.user_rating_number[userID] > 0)
				{
					//compute \bar{C}_{u\cdot}^o
					float feedback_num_u_sqrt = 0;

					HashSet<Integer> itemSet = Data.TrainingUser_ExplicitFeedbacks.get(userID);		

					if(itemSet.contains(itemID) )
					{
						if( itemSet.size()>1 )
						{
							feedback_num_u_sqrt 
							= (float) Math.sqrt(Data.user_rating_number[userID]- 1);
						}
					}
					else
					{
						feedback_num_u_sqrt 
						= (float) Math.sqrt(Data.user_rating_number[userID]);
					}

					if (feedback_num_u_sqrt>0)
					{
						for( int i3 : itemSet )
						{
							if(i3 != itemID)
							{
								for (int f=0; f<Data.d; f++)
								{
									Ou[f] += Data.O[i3][f];
								}	    		
							}
						}

						// --- normalization
						for (int f=0; f<Data.d; f++)
						{
							Ou[f] = Ou[f] / feedback_num_u_sqrt;			
						}
					}

				}


				// prediction and error
				float pred = 0;
				float err = 0;
				for (int f=0; f<Data.d; f++)
				{	
					pred += ( Data.U[userID][f] + Data.delta_g * tilde_Uu[f] + Data.delta_o * Ou[f]) * Data.V[itemID][f];	    				
				}
				err = rating-pred;

				loss += err;

				// --- update U, V	    			
				float [] V_before_update = new float[Data.d];
				for(int f=0; f<Data.d; f++)
				{	
					V_before_update[f] = Data.V[itemID][f];

					float grad_U_f = -err * Data.V[itemID][f] + Data.alpha_u * Data.U[userID][f];
					float grad_V_f = -err * ( Data.U[userID][f] + Data.delta_g * tilde_Uu[f] 
							+ Data.delta_o * Ou[f]) + Data.alpha_v * Data.V[itemID][f];
					Data.U[userID][f] = Data.U[userID][f] - Data.gamma * grad_U_f;
					Data.V[itemID][f] = Data.V[itemID][f] - Data.gamma * grad_V_f;		    			
				}


				// --- update G
				if(Data.delta_g!=0)
				{

					for(int g=1; g<=Data.num_rating_types; g++)
					{
						if( Data.user_graded_rating_number[userID][g]>0 )
						{
							// ---
							HashSet<Integer> itemSet = Data.TrainingUser_ExplicitFeedbacksGraded.get(userID).get(g);

							// ---
							float explicit_feedback_num_u_sqrt = 0;
							if(itemSet.contains(itemID) )
							{
								if( itemSet.size()>1 )
								{
									explicit_feedback_num_u_sqrt 
									= (float) Math.sqrt( Data.user_graded_rating_number[userID][g] - 1 );
								}
							}
							else
							{
								explicit_feedback_num_u_sqrt 
								= (float) Math.sqrt( Data.user_graded_rating_number[userID][g] );
							}

							if(explicit_feedback_num_u_sqrt>0)
							{
								for( int i2 : itemSet )
								{
									if(i2 != itemID)
									{
										for (int f=0; f<Data.d; f++)
										{
											Data.G[i2][g][f] = (float) (Data.G[i2][g][f] - 
													Data.gamma * ( -err * V_before_update[f] / explicit_feedback_num_u_sqrt 
															+ Data.alpha_g * Data.G[i2][g][f] ) );
										}
										loss += err;
									}
								}


							}
						}
					}

				}



				// -----------------------	  
				// --- update O    			
				if( Data.delta_o!=0 && Data.user_rating_number[userID] > 0)
				{

					float feedback_num_u_sqrt = 0;

					HashSet<Integer> itemSet = Data.TrainingUser_ExplicitFeedbacks.get(userID);		

					if(itemSet.contains(itemID) )
					{
						if( itemSet.size()>1 )
						{
							feedback_num_u_sqrt 
							= (float) Math.sqrt(Data.user_rating_number[userID]- 1);
						}
					}
					else
					{
						feedback_num_u_sqrt 
						= (float) Math.sqrt(Data.user_rating_number[userID]);
					}

					if (feedback_num_u_sqrt>0)
					{
						for(int i3 : itemSet)
						{	    					
							for (int f=0; f<Data.d; f++)
							{
								Data.O[i3][f] = (float) (Data.O[i3][f] - 
										Data.gamma * ( -err * V_before_update[f] / feedback_num_u_sqrt + Data.alpha_o * Data.O[i3][f] ));
							}
							loss += err;
						}

					}

				}
				// -----------------------	  
			}

			Data.gamma = Data.gamma * 0.9f;

//			System.out.println("loss = " + loss + "  gamma = " + Data.gamma);
			try {
				Data.bw.write("loss =" + loss + "\n");
				Data.bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("loss =" + loss);
			// ===========================================
		}

	}



//	public static void newTrain() throws IOException 
//	{    	
//		// =================================================================
//
//		for (int iter = 0; iter < Data.num_iterations_new; iter++)
//		{	
//			float loss = 0;
//			// output each iteration result
//			try {
//				Data.bw.write("Iter:" + Integer.toString(iter) + "| ");
//				Data.bw.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			System.out.print("Iter:" + Integer.toString(iter) + "| ");
//
//			try {
//				Test.test();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			// ====================================
//
//
//			for (int iter_rand = 0; iter_rand < Data.num_test_user_support_data; iter_rand++) 
//			{   	    		
//				// ===========================================
//				// --- random sampling one triple of (userID,itemID,rating), Math.random(): [0.0, 1.0)
//				int rand_case = (int) Math.floor( Math.random() * Data.num_test_user_support_data );
//				int userID = Data.indexUserOfTestUserSupportData[rand_case];	    		
//				int itemID = Data.indexItemTestOfTestUserSupportData[rand_case];
//				float rating = Data.ratingTestUserSupportData[rand_case];
//
//
//				// ===========================================
//				float [] tilde_Uu_g = new float[Data.d];
//				float [] tilde_Uu = new float[Data.d];
//
//				if(Data.delta_g!=0)
//				{
//					//compute \bar{C}_{u\cdot}^\mathcal{G}
//					for(int g=1; g<=Data.num_rating_types; g++)
//					{
//						if( Data.test_user_support_graded_rating_number[userID][g]>0 )
//						{
//							// ---
//							HashSet<Integer> itemSet = Data.TestUserSupport_ExplicitFeedbacksGraded.get(userID).get(g);
//
//							// ---
//							float explicit_feedback_num_u_sqrt = 0;
//							if(itemSet.contains(itemID) )
//							{
//								if( itemSet.size()>1 )
//								{
//									explicit_feedback_num_u_sqrt 
//									= (float) Math.sqrt( Data.test_user_support_graded_rating_number[userID][g] - 1 );
//								}
//							}
//							else
//							{
//								explicit_feedback_num_u_sqrt 
//								= (float) Math.sqrt( Data.test_user_support_graded_rating_number[userID][g] );
//							}
//
//							if (explicit_feedback_num_u_sqrt>0)
//							{
//								// --- aggregation
//								for( int i2 : itemSet )
//								{
//									if(i2 != itemID)
//									{
//										for(int f=0; f<Data.d; f++)
//										{
//											tilde_Uu_g[f] += Data.G[i2][g][f];
//										}
//									}
//								}
//
//								// --- normalization
//								for (int f=0; f<Data.d; f++)
//								{
//									tilde_Uu_g[f] = tilde_Uu_g[f] / explicit_feedback_num_u_sqrt;
//									tilde_Uu[f] += tilde_Uu_g[f];
//									tilde_Uu_g[f] = 0;
//								}
//							}
//						}
//					}
//				}
//
//
//				float [] Ou = new float[Data.d];				
//				if( Data.delta_o!=0 && Data.test_user_support_rating_number[userID] > 0)
//				{
//					//compute \bar{C}_{u\cdot}^o
//					float feedback_num_u_sqrt = 0;
//
//					HashSet<Integer> itemSet = Data.TestUserSupport_ExplicitFeedbacks.get(userID);		
//
//					if(itemSet.contains(itemID) )
//					{
//						if( itemSet.size()>1 )
//						{
//							feedback_num_u_sqrt 
//							= (float) Math.sqrt(Data.test_user_support_rating_number[userID]- 1);
//						}
//					}
//					else
//					{
//						feedback_num_u_sqrt 
//						= (float) Math.sqrt(Data.test_user_support_rating_number[userID]);
//					}
//
//					if (feedback_num_u_sqrt>0)
//					{
//						for( int i3 : itemSet )
//						{
//							if(i3 != itemID)
//							{
//								for (int f=0; f<Data.d; f++)
//								{
//									Ou[f] += Data.O[i3][f];
//								}	    		
//							}
//						}
//
//						// --- normalization
//						for (int f=0; f<Data.d; f++)
//						{
//							Ou[f] = Ou[f] / feedback_num_u_sqrt;			
//						}
//					}
//
//				}
//
//
//				// prediction and error
//				float pred = 0;
//				float err = 0;
//				for (int f=0; f<Data.d; f++)
//				{	
//					pred += ( Data.U[userID][f] + Data.delta_g * tilde_Uu[f] + Data.delta_o * Ou[f]) * Data.V[itemID][f];	    				
//				}
//				err = rating-pred;
//
//				loss += err;
//
//				// --- update U, V	    			
//				float [] V_before_update = new float[Data.d];
//				for(int f=0; f<Data.d; f++)
//				{	
//					V_before_update[f] = Data.V[itemID][f];
//
//					float grad_U_f = -err * Data.V[itemID][f] + Data.alpha_u * Data.U[userID][f];
//					float grad_V_f = -err * ( Data.U[userID][f] + Data.delta_g * tilde_Uu[f] 
//							+ Data.delta_o * Ou[f]) + Data.alpha_v * Data.V[itemID][f];
//					Data.U[userID][f] = Data.U[userID][f] - Data.gamma * grad_U_f;
//					Data.V[itemID][f] = Data.V[itemID][f] - Data.gamma * grad_V_f;		    			
//				}
//
//
//				// --- update G
//				if(Data.delta_g!=0)
//				{
//
//					for(int g=1; g<=Data.num_rating_types; g++)
//					{
//						if( Data.test_user_support_graded_rating_number[userID][g]>0 )
//						{
//							// ---
//							HashSet<Integer> itemSet = Data.TestUserSupport_ExplicitFeedbacksGraded.get(userID).get(g);
//
//							// ---
//							float explicit_feedback_num_u_sqrt = 0;
//							if(itemSet.contains(itemID) )
//							{
//								if( itemSet.size()>1 )
//								{
//									explicit_feedback_num_u_sqrt 
//									= (float) Math.sqrt( Data.test_user_support_graded_rating_number[userID][g] - 1 );
//								}
//							}
//							else
//							{
//								explicit_feedback_num_u_sqrt 
//								= (float) Math.sqrt( Data.test_user_support_graded_rating_number[userID][g] );
//							}
//
//							if(explicit_feedback_num_u_sqrt>0)
//							{
//								for( int i2 : itemSet )
//								{
//									if(i2 != itemID)
//									{
//										for (int f=0; f<Data.d; f++)
//										{
//											Data.G[i2][g][f] = (float) (Data.G[i2][g][f] - 
//													Data.gamma * ( -err * V_before_update[f] / explicit_feedback_num_u_sqrt 
//															+ Data.alpha_g * Data.G[i2][g][f] ) );
//										}
//										loss += err;
//									}
//								}
//
//
//							}
//						}
//					}
//
//				}
//
//
//
//				// -----------------------	  
//				// --- update O    			
//				if( Data.delta_o!=0 && Data.test_user_support_rating_number[userID] > 0)
//				{
//
//					float feedback_num_u_sqrt = 0;
//
//					HashSet<Integer> itemSet = Data.TestUserSupport_ExplicitFeedbacks.get(userID);		
//
//					if(itemSet.contains(itemID) )
//					{
//						if( itemSet.size()>1 )
//						{
//							feedback_num_u_sqrt 
//							= (float) Math.sqrt(Data.test_user_support_rating_number[userID]- 1);
//						}
//					}
//					else
//					{
//						feedback_num_u_sqrt 
//						= (float) Math.sqrt(Data.test_user_support_rating_number[userID]);
//					}
//
//					if (feedback_num_u_sqrt>0)
//					{
//						for(int i3 : itemSet)
//						{	    					
//							for (int f=0; f<Data.d; f++)
//							{
//								Data.O[i3][f] = (float) (Data.O[i3][f] - 
//										Data.gamma * ( -err * V_before_update[f] / feedback_num_u_sqrt + Data.alpha_o * Data.O[i3][f] ));
//							}
//							loss += err;
//						}
//
//					}
//
//				}
//				// -----------------------	  
//			}
//
//			Data.gamma = Data.gamma * 0.9f;
//
//			System.out.println("loss = " + loss + "  gamma = " + Data.gamma);
//			// ===========================================
//		}
//
//	}


}
