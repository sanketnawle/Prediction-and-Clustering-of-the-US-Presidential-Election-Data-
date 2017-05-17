import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class KNN
{
    public static final int attr_no=10;
    public static double[][] nearest,test,train;
    public static double[] neighbour_distance;
    public static int k,correct=0;
    public static int[] votetracker;


   /* public static double thresholds[][]= {
            {34946.4, 645187},
            {2.61,5},
            {12.53, 16.5},
            {0.07,0.4},
            {0.02,0.1},
            {13.98, 27.77},
            {37434.5, 56443},
            {12.25, 22.7},
            {121.73, 1269.75}
    }; */


    //Thresholds are calculated using my K-means algorithm using k=5
    public static double thresholds[][]= {
            {13097, 29317,59610,435286, 750137},
            {0.7, 1.1, 1.5, 3, 5},
            {11.9, 13.8, 14.6, 15.65, 18.05},
            {0.02,  0.04, 0.07, 0.19, 0.4},
            {0.02,0.03, 0.07, 0.1, 0.12},
            {10.1, 14.4, 20.9, 23.4,29.95},
            {32149, 39267, 44445, 53682, 59204},
            {12.1,12.4, 18.45, 20.6, 26.23},
            {19.9, 26.7, 93.8 ,476.3, 1269.75}
    };



 public static void main(String args[])
 {
     String train_file = "D:\\Documents\\NYU- documents\\AI\\Assignment 2\\KNN New\\src\\sample-train.csv";
     String test_file= "D:\\Documents\\NYU- documents\\AI\\Assignment 2\\KNN New\\src\\sample-test.csv";

     String line="";
     String splitby=",";

    //Reading no of lines in train file
    int count=0;
     try(BufferedReader br= new BufferedReader(new FileReader(train_file))){
         while((br.readLine())!=null)
         {
            count++;
         }
     }
     catch(IOException e)
     {
         e.printStackTrace();
     }

     //Initialising train array
     String[][] train_string = new String [count][attr_no];


     try(BufferedReader br= new BufferedReader(new FileReader(train_file)))
     {
         int i = 0;
         while((line=br.readLine())!=null)
         {
             train_string[i]= line.split(splitby);
             i++;
         }
     }
     catch(IOException e)
     {
         e.printStackTrace();
     }


     //converting training set into double array for calculation
     train= new double[train_string.length-1][train_string[0].length-1];

     for(int i=1;i<train_string.length;i++)
         for(int j=1;j<train_string[0].length;j++)
             train[i-1][j-1]= Double.parseDouble(train_string[i][j]);

     System.out.println("Train columns: "+train[0].length);


     System.out.println("********** Before normalisation **********");
     for(int i=0;i<train.length;i++)
     {
         for(int j=0;j<train[0].length;j++)
         {
             System.out.print(train[i][j]+"\t");
         }

         System.out.println();
     }



     //normalizing training samples
     normalize(train);







     //Reading no of lines in test file
    count=0;
     try(BufferedReader br= new BufferedReader(new FileReader(test_file))){
         while((br.readLine())!=null)
         {
             count++;
         }
     }
     catch(IOException e)
     {
         e.printStackTrace();
     }


     //Initialising test array
     String[][] test_string = new String [count][attr_no];

     try(BufferedReader br= new BufferedReader(new FileReader(test_file)))
     {
         int i = 0;
         while((line=br.readLine())!=null)
         {
             test_string[i]= line.split(splitby);
             i++;
         }
     }
     catch(IOException e)
     {
         e.printStackTrace();
     }

     test= new double[test_string.length-1][test_string[0].length-1];
     for(int i=1;i<test_string.length;i++)
         for(int j=1;j<test_string[0].length;j++)
             test[i-1][j-1]= Double.parseDouble(test_string[i][j]);

     System.out.println("Test columns: "+test[0].length);


     System.out.println("********** Before normalisation **********");
     for(int i=0;i<test.length;i++)
     {
         for(int j=0;j<test[0].length;j++)
         {
             System.out.print(test[i][j]+"\t");
         }

         System.out.println();
     }

     //Normalising testing samples
     normalize(test);











    //value of k
     k= (int) Math.sqrt(train.length);

     nearest= new double[k][train[0].length]; //nearest neighbours
     votetracker = new int[k]; //To keep a track of the value of democratic field of the nearest neighbours



    /* System.out.println("********************Nearest*****************");
     System.out.println();
     System.out.println();
     System.out.println();
     System.out.println();
     System.out.println(); */

     //initialising nearest neighbours

     for(int i=0;i<k;i++) {
         for (int j = 0; j < train[0].length; j++) {
             nearest[i][j] = train[i][j];
            // System.out.print(nearest[i][j] + "\t");
         }

         votetracker[i]=i;
       //  System.out.println();
     }

    for(int m=0;m<test.length;m++)
    {
        neighbour_distance = initialCalculation(test[m]);

        //sorting the neighbouring distances in ascending order
        for (int i = 0; i < neighbour_distance.length; i++)
            for (int j = 0; j < neighbour_distance.length - i - 1; j++) {
                if (neighbour_distance[j] > neighbour_distance[j + 1]) {
                    double temp = neighbour_distance[j];
                    neighbour_distance[j] = neighbour_distance[j + 1];
                    neighbour_distance[j + 1] = temp;
                }
            }

        trainingCalculation(m);

        int zero_count = 0, one_count = 0;

        for (int i = 0; i < votetracker.length; i++) {
            int temp = votetracker[i];

            if (train_string[temp+1][0].equals("0"))
                zero_count++;

            if (train_string[temp+1][0].equals("1"))
                one_count++;
        }

        System.out.println("Vote tracker: nearest neighbours for test: " + (m+2));
        for(int i=0;i<votetracker.length;i++)
            System.out.println(votetracker[i]+1);


        System.out.println("Counties supporting Trumph:"+zero_count);
        System.out.println("Counties supporting Hillary:"+one_count);

        if (zero_count >= one_count) {
            System.out.println("For test= "+(m+2)+": Trumph won");
            if(test_string[m+1][0].equals("0")) {
                System.out.println("Correct!");
                correct++;
            }
            else System.out.println("Wrong!");
            System.out.println();
        }
        else {
            System.out.println("For test= "+(m+2)+": Hillary won");
            if(test_string[m+1][0].equals("1")) {
                System.out.println("Correct!");
                correct++;
            }
            else System.out.println("Wrong!");
            System.out.println();
        }

    }

     System.out.println("No. of correct results: "+correct+" / "+test.length);

 }


    public static void normalize(double[][] sample)
    {

        System.out.println("Sample length: "+sample[0].length);
        System.out.println("Threshold length: "+thresholds.length);

        for(int i =0;i<sample.length;i++)
        {
            for (int j = 0; j < sample[0].length; j++)
            {
                boolean updated=false;

                for (int k = 0; k < thresholds[0].length; k++)
                {
                    if (Math.abs(sample[i][j]) < Math.abs(thresholds[j][k]))
                    {
                        sample[i][j] = k;
                        updated = true;
                        break;
                    }
                }

                if(!updated)
                    sample[i][j]= 5.0;
            }
        }


        System.out.println("********** After normalisation **********");

        for(int i=0;i<sample.length;i++)
        {
            for(int j=0;j<sample[0].length;j++)
            {
                System.out.print(sample[i][j]+"\t");
            }

            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();


    }

 //calculate the initial values of the neighbouring distances
 public static double[] initialCalculation(double[] county)
 {
     double distance[]= new double[nearest.length];


   //  System.out.println("County: "+county.length);
    // System.out.println("Distance: "+distance.length);
    // System.out.println("Nearest: "+nearest[0].length);

     //calculating the distance between current test sample with all the neighbours (Euclidean distance)
     for(int i=0;i<nearest.length;i++)
     {
         for (int j = 0; j < nearest[0].length; j++)
              distance[i] += Math.pow((county[j] - nearest[i][j]), 2);

         distance[i] = Math.sqrt(distance[i]);
     }

     return distance;

 }

// calculate the distances between the test sample & the training sample and modify the nearest neighbours
 public static void trainingCalculation(int m)
 {
     double temp_distance=0;

    for(int i=k;i<train.length;i++)
    {
        for(int j=0;j<train[0].length;j++)
        {
            temp_distance +=(Math.pow(test[m][j]-train[i][j],2));
        }

        temp_distance = Math.sqrt(temp_distance);

        for(int k=0;k<nearest.length;k++)
        {
            if(temp_distance<neighbour_distance[k])
            {
                votetracker[k]=i;
                double temp = temp_distance;
                temp_distance= neighbour_distance[k];
                neighbour_distance[k]= temp;
                break;
            }
        }
    }
 }


}
