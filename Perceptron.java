/**
 * Created by Sanket N on 05-12-2016.
 */

import java.util.Random;

public class Perceptron
{

    double[] weight_array;
    double threshold;

    public void Train(double[][] ip, int[] op, double threshold, double learning_rate, int epoch)
    {

        this.threshold = threshold;
        int input_length = ip[0].length;
        int output_length = op.length;
        weight_array = new double[input_length];
        Random rand = new Random();

        //initialize weight_array
        for(int i=0;i<input_length;i++)
        {
            weight_array[i] = rand.nextDouble();
        }

        for(int i=0;i<epoch;i++)
        {
            int error_total = 0;
            for(int j =0;j<output_length;j++)
            {
                int output = Result(ip[j]);
                int error = op[j] - output;

                error_total +=error;

                for(int k = 0;k<input_length;k++)
                {
                    double d = learning_rate * ip[j][k] * error;
                    weight_array[k] += d;
                }
            }
            if(error_total == 0)
                break;
        }

    }

    public int Result(double[] ip)
    {
        double avg = 0.0;
        for(int i=0;i<ip.length;i++)
        {
            avg += weight_array[i]*ip[i];
        }

        avg= avg/ip.length;

        if(avg>threshold)
            return 1;
        else
            return 0;
    }

}
