import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by Sanket N on 05-12-2016.
 */






public class PerceptronTest {


    //Thresholds are used for normalization. I have used 2 bins; 0 and 1, i.e., population can be low(0) or high(1)
    //The below values are calculated using my K-means algorithm using k=2
    public static double thresholds[][]=
            {
            {34946.4, 645187},
            {2.61,5},
            {12.53, 16.5},
            {0.07,0.4},
            {0.02,0.1},
            {13.98, 27.77},
            {37434.5, 56443},
            {12.25, 22.7},
            {121.73, 1269.75}
            };

    public static final int attr_no = 10, clusters=6;
    public static double[][] nearest, test, train;
    public static double[] neighbour_distance;
    public static int k, attr_considered = 1;
    public static int[] votetracker,decision;
    public static double adjacency_matrix[][];



    public static void main(String[] args)
    {
        //Change the path according to ur project and file location
        String train_file = "D:\\Documents\\NYU- documents\\AI\\Assignment 2\\Perceptron temp\\src\\votes-train.csv";
        String test_file = "D:\\Documents\\NYU- documents\\AI\\Assignment 2\\Perceptron temp\\src\\votes-test.csv";

        DecimalFormat df = new DecimalFormat("#.##");
        String line = "";
        String splitby = ",";

        //Reading no of lines in train file
        int count_train = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(train_file))) {
            while ((br.readLine()) != null) {
                count_train++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initialising train array
        String[][] train_string = new String[count_train][attr_no];


        try (BufferedReader br = new BufferedReader(new FileReader(train_file))) {
            int i = 0;
            while ((line = br.readLine()) != null) {
                train_string[i] = line.split(splitby);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //converting training set into double array for calculation
        train = new double[count_train-1][attr_no-1];
        decision= new int[count_train-1];




        for (int i = 1; i < count_train; i++) {
            decision[i-1] = Integer.parseInt(train_string[i][0]);
            for (int j = 1; j < attr_no; j++)
            {
                train[i - 1][j - 1] = Double.parseDouble(train_string[i][j]);
            }
        }


      /*  System.out.println("********** Before normalisation **********");
        for(int i=0;i<train.length;i++)
        {
            for(int j=0;j<train[0].length;j++)
            {
                System.out.print(train[i][j]+"\t");
            }

            System.out.println();
        } */



        //normalizing training samples
        normalize(train);






        //Reading no of lines in test file
        int count_test = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(test_file))) {
            while ((br.readLine()) != null) {
                count_test++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initialising test array
        String[][] test_string = new String[count_test][attr_no];


        try (BufferedReader br = new BufferedReader(new FileReader(test_file))) {
            int i = 0;
            while ((line = br.readLine()) != null) {
                test_string[i] = line.split(splitby);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //converting testing set into double array for calculation
        test = new double[count_test-1][attr_no-1];
        for (int i = 1; i < count_test; i++)
            for (int j = 1; j < attr_no; j++)
                test[i-1][j-1] = Double.parseDouble(test_string[i][j]);



      /*  System.out.println("********** Before normalisation **********");
        for(int i=0;i<test.length;i++)
        {
            for(int j=0;j<test[0].length;j++)
            {
                System.out.print(test[i][j]+"\t");
            }

            System.out.println();
        } */

        //Normalising testing samples
        normalize(test);


        //The main Perceptron algorithm starts here......

        Perceptron p = new Perceptron();

        p.Train(train, decision,0.5, 0.1, 200);

        int[] result= new int[test.length];
        int correct=0;

        for(int i=0;i<test.length;i++) {
            result[i]= p.Result(test[i]);
            if(result[i]==test[i][0])
                correct++;
            System.out.println("Line no: " + i + ": " + result[i]);
        }

        System.out.println("Correct anss: "+correct+" / "+test.length);
    }



    public static void normalize(double[][] sample)
    {

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
                    sample[i][j]= 1.0;
            }
        }


      /*  System.out.println("********** After normalisation **********");

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
        System.out.println(); */


    }



}
