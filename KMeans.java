import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Sanket N on 04-12-2016.
 */
public class KMeans {

    public static final int attr_no=10;
    public static double[][] nearest,test,train;
    public static double[] neighbour_distance;
    public static int k, attr_considered=1;
    public static int[] votetracker;
    public static double adjacency_matrix[][];

    public static void main(String args[]) {
        String train_file = "D:\\Documents\\NYU- documents\\AI\\Assignment 2\\KMeans\\src\\votes-train.csv";

        DecimalFormat df = new DecimalFormat("#.##");
        String line = "";
        String splitby = ",";

        //Reading no of lines in train file
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(train_file))) {
            while ((br.readLine()) != null) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initialising train array
        String[][] train_string = new String[count][attr_no];


        try (BufferedReader br = new BufferedReader(new FileReader(train_file))) {
            int i = 0;
            while ((line = br.readLine()) != null) {
                train_string[i] = line.split(splitby);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("Choose which parameter interests you");
        for(int i=1;i<attr_no;i++)
        {
            System.out.print(i+": "+ train_string[0][i]+"\t");
        }
        System.out.println();

        Scanner src = new Scanner(System.in);

        attr_considered = src.nextInt();

        System.out.println("You selected: "+train_string[0][attr_considered]);

        //value of k
        System.out.println("Select a value of k"+"(select 0 for appropriate value)");
        k=src.nextInt();
        //k=2;


        //converting training set into double array for calculation
        train = new double[count][attr_no];
        for (int i = 1; i < count; i++)
            for (int j = 0; j < attr_no; j++)
                train[i-1][j] = Math.abs(Double.parseDouble(train_string[i][j]));

        if(k==0)
            k = (int) Math.sqrt(train.length);


        ArrayList[] v = new ArrayList[k];
        ArrayList[] v_copy = new ArrayList[k];

        for(int i=0;i<k;i++)
            v[i]= new ArrayList();

        for(int i=0;i<k;i++)
           v_copy[i]= new ArrayList();

        int votetracker[][]= new int[k][10000];

      //  System.out.println("Size of arraylist: "+k);


        double centroid[] = new double[k];

        for(int i=0;i<k;i++) {
           // v[i].add(0,train[i][attr_considered]);
           // v_copy[i].add(0,train[i][attr_considered]);
            centroid[i]= train[i][attr_considered];
            votetracker[i][0]= (int) train[i][0];
        }

        double min_dist,temp;


        do
        {

            boolean change=false;

            for (int i = 0; i < train.length - 1; i++)
            {
                min_dist = Double.MAX_VALUE;
                int pos = 0;
                for (int j = 0; j < k; j++)
                {
                    temp = Math.abs(train[i][attr_considered] - centroid[j]);
                    if (min_dist > temp) {
                        change=true;
                        min_dist = temp;
                        pos = j;
                    }
                }

                v[pos].add((train[i][attr_considered]));
               /* if(v[pos].size()>= v_copy[pos].size())
                     v_copy[pos].add(new Object()); */

                votetracker[pos][v[pos].size() - 1] = (int) train[i][0];
            }


            for (int l = 0; l < k; l++)
            {
                int vsize = v[l].size();
                centroid[l] = 0;
                for (int p = 0; p < vsize; p++)
                    centroid[l] += Math.abs(((double) v[l].get(p)));
                centroid[l] = centroid[l] / vsize;
            }


            for(int i=0;i<v.length;i++)
            {
                for (int j = 0; j < v[i].size(); j++)
                {
                    if (v_copy[i].size() != 0 && !(v[i].get(j).equals(v_copy[i].get(j))))
                    {
                        change = true;
                        break;
                    }
                }
            }


            if(change)
                break;


           //making a copy of vector
            for(int i=0;i<v.length;i++)
            {


                for(int j=0; j<v[i].size();j++) {

                    if(v[i].size()>= v_copy[i].size())
                            v_copy[i].add(new Object());

                    v_copy[i].set(j, v[i].get(j));
                }
            }



           /* System.out.println("The Clusters are: ");
            for(int i=0;i<k;i++)
            {
                int vsize = v[i].size();

                for(int j=0;j<vsize;j++) {
                    System.out.print(v[i].get(j) + "\t");
                }

                System.out.println();
            } */


          /*  System.out.println("*******Here V copy  go: *****");
            for(int i=0;i<k;i++)
            {
                int vsize = v_copy[i].size();

                for(int j=0;j<vsize;j++)
                    System.out.print(v_copy[i].get(j)+"\t");

                System.out.println();
            } */



            for(int i=0;i<v.length;i++)
                v[i].clear();


        }while(true);


      /*  System.out.println("The Clusters are: ");
        for(int i=0;i<k;i++) {
            int vsize = v[i].size();

            for (int j = 0; j < vsize; j++) {
                System.out.print(v[i].get(j) + "\t");
            }

            System.out.println();
        } */

        String votedfor[]= new String[k];
        int zerocount,onecount;


        for(int i=0;i<k;i++)
        {
            zerocount=0;
            onecount=0;

            int vsize = v[i].size();

           for(int j=0;j<vsize;j++) {
                //System.out.print(votetracker[i][j] + "\t");


                if(votetracker[i][j]==0)
                      zerocount++;
                else  onecount++;
            }

           if(zerocount>onecount)
                 votedfor[i]= "Trump";
            else votedfor[i]="Hillary";

          //  System.out.println();
        }


        System.out.println("****Clustering result****");





        for(int i=0;i<k;i++)
        {
            System.out.print("Centroid of cluster "+(i+1)+" is "+ df.format(centroid[i])+" "+"and it voted for: "+votedfor[i]);
            System.out.println();
        }


     /*   double[] s,a,b;

        a= new double[v.length];
        b= new double[v.length];

        double distance;

        for(int i=0;i<v.length;i++)
        {
            a[i]=0;

            for(int j=0;j<v[i].size()-1;j++)
            {
                a[i]+= Math.abs((double) v[i].get(j)- (double)v[i].get(j+1));
            }
            a[i]= a[i]/v[i].size();

            System.out.println("a["+i+"]: "+a[i]);
        }

        double closest_distance= Double.MAX_VALUE;
        int closest_cluster[]= new int[centroid.length];

        for(int i=0;i<centroid.length-1;i++)
        {
           // closest_distance= Double.MAX_VALUE;
            if(Math.abs(centroid[i]-centroid[i+1])<closest_distance)
            {
                closest_distance= Math.abs(centroid[i]-centroid[i+1]);
                closest_cluster[i] = i+1;
                System.out.println("Closest cluster of "+i+": "+closest_cluster[i]);
            }

        }

        for(int i=0;i<v.length;i++)
        {
            b[i]=0;

            for(int j=0;j<v[closest_cluster[i]].size()-1;j++)
            {
                b[i]+= Math.abs((double) v[i].get(j)- (double)v[closest_cluster[i]].get(j+1));
            }
            b[i]= b[i]/v[closest_cluster[i]].size();

            System.out.println("b["+i+"]: "+b[i]);

        }
        */



    }

}
