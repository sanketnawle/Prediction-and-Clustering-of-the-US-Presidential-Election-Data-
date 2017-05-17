import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * Created by Sanket N on 06-12-2016.
 */
public class AgglomerativeClustering {

    public static final int attr_no=10;
    public static double[][] nearest,test,train;
    public static double[] attr_array;
    public static double[] neighbour_distance;
    public static int k, attr_considered=1,iteration=0;
    public static int[][] votetracker;
    public static ArrayList v[], adjacency_matrix[];


    public static void main(String args[]) {
        String train_file = "D:\\Documents\\NYU- documents\\AI\\Assignment 2\\Agglomerative Clustering\\src\\sample-train.csv";

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
        for (int i = 1; i < attr_no; i++) {
            System.out.print(i + ": " + train_string[0][i] + "\t");
        }
        System.out.println();

        Scanner src = new Scanner(System.in);

        attr_considered = src.nextInt();


        System.out.println("You selected: " + train_string[0][attr_considered]);

        //converting training set into double array for calculation
        train = new double[train_string.length-1][train_string[0].length-1];
        attr_array = new double[train_string.length-1];


        for (int i = 1; i < train_string.length; i++) {
            for (int j = 1; j < train_string[0].length; j++) {
                train[i - 1][j - 1] = Math.abs(Double.parseDouble(train_string[i][j]));
                if(j==attr_considered)
                    attr_array[i-1]= train[i-1][j-1];
            }
        }

        adjacency_matrix= new ArrayList[train.length];

        for(int i=0;i<adjacency_matrix.length;i++)
            adjacency_matrix[i]= new ArrayList();


       // System.out.println("Select the no of clusters you want: ");
       // k = src.nextInt();


        k= (int)Math.sqrt(attr_array.length);

        adjacency_matrix = constructAdjacencyMatrix(attr_array);


        v = new ArrayList[adjacency_matrix.length];

        for (int i = 0; i < adjacency_matrix.length; i++) {
            v[i] = new ArrayList();
        }

        for (int i = 0; i < adjacency_matrix.length; i++) {
            v[i].add(attr_array[i]);

        }

        int index[][]= new int[attr_array.length][attr_array.length];

        for(int i=0;i<index.length;i++)
            index[i][0]=i;

        int cluster_count= attr_array.length;

       for(int m=0;m<(attr_array.length-k-1);m++)
        {

            cluster_count=0;

            for(int i=0;i<v.length;i++)
                if((double)v[i].get(0)<Double.POSITIVE_INFINITY)
                    cluster_count++;

            System.out.println("The cluster count is: "+cluster_count);

           /* if(cluster_count==k)
                break; */




            System.out.println("The adjacency matrix is: ");

           /* for (int i = 0; i < adjacency_matrix.length; i++) {
                for (int j = 0; j < adjacency_matrix[0].size(); j++) {

                    if(i==j) {
                        System.out.print("0.00" + "\t");
                        continue;
                    }
                    if ((double) adjacency_matrix[i].get(j) == Double.POSITIVE_INFINITY)
                             System.out.print("0.00"+"\t");
                       else System.out.print(df.format(adjacency_matrix[i].get(j)) + "\t");
                }
                System.out.println();
            } */


          /*  for (int i = 0; i < v.length; i++) {
                System.out.print("Cluster: " + i+": ");
                for (int j = 0; j < v[i].size(); j++)
                    System.out.print(v[i].get(j)+"\t");
                System.out.println();
            } */


            int smallest_i = 0, smallest_j = 0;
            double smallest_distance = Double.MAX_VALUE;

            for (int i = 0; i < (adjacency_matrix.length-iteration); i++) {
                for (int j = 0; j < adjacency_matrix[0].size(); j++) {
                    if (smallest_distance > (double) adjacency_matrix[i].get(j)) {
                        smallest_distance = (double) adjacency_matrix[i].get(j);
                        smallest_i = i;
                        smallest_j = j;
                    }
                }
            }


            System.out.println("Old cluster: ");

            int c=0;

            for (int i = 0; i < v.length; i++) {

                if ((double) v[i].get(0) < Double.POSITIVE_INFINITY) {
                    c++;
                    System.out.print("Cluster: " + i + " ");
                    for (int j = 0; j < v[i].size(); j++)
                        System.out.print(v[i].get(j) + "\t \t \t");
                    //System.out.println("Voted for: "+votedfor[i]);
                    System.out.println();
                }

            }

            System.out.println("The Adjacency matrix is: ");

            for(int i=0;i<(adjacency_matrix.length-iteration-1);i++) {
                for (int j = 0; j < adjacency_matrix[0].size(); j++)
                {
                    if((double) adjacency_matrix[i].get(j)== Double.POSITIVE_INFINITY)
                        System.out.print("0.00"+"\t");
                    else System.out.format((df.format(adjacency_matrix[i].get(j))+"\t"));
                }

                System.out.println();
            }




            for (int j = 0; j < v[smallest_i].size(); j++) {
                v[smallest_j].add(v[smallest_i].get(j));
                index[smallest_j][v[smallest_j].size()-1]= index[smallest_i][j];
            }


            for (int j = 0; j < v[smallest_i].size(); j++) {
                v[smallest_i].set(j, Double.POSITIVE_INFINITY);
                index[smallest_i][j]=-1;

            }


        /* Removed
            System.out.println("Index is: ");

            for(int i=0;i<index.length;i++) {
                for (int j=0;j<index[0].length;j++)
                    System.out.print(index[i][j]+"\t");
                System.out.println();
            }
        */

            int zerocount=0, onecount=0;
            String votedfor[]= new String[index.length];

            for(int i=0; i<index.length;i++)
            {
                for(int j=0;j<index[0].length;j++)
                {
                    if(index[i][j]==-1)
                       break;

                    if(train_string[index[i][j]][0].equals("0"))
                          zerocount++;
                    else  onecount++;

                }

                if(zerocount>onecount)
                     votedfor[i]="Trumph";
                else votedfor[i]="Hillary";

            }


            System.out.println("Smallest distance: " + smallest_distance + " found at: " + "(" + smallest_i + " , " + smallest_j + " )");

            newestMatrix(smallest_i, smallest_j);


            System.out.println("New cluster: ");




            for (int i = 0; i < v.length; i++) {

                if ((double) v[i].get(0) < Double.POSITIVE_INFINITY) {
                    c++;
                    System.out.print("Cluster: " + i + " ");
                    for (int j = 0; j < v[i].size(); j++)
                        System.out.print(v[i].get(j) + "\t \t \t");
                    System.out.println("Voted for: "+votedfor[i]);
                }

            }

        }

    }


    public static ArrayList[] constructAdjacencyMatrix(double[] attr_array)
    {

        ArrayList matrix[] = new ArrayList[attr_array.length];

        for(int i=0;i<matrix.length;i++)
            matrix[i]= new ArrayList();

        for(int i=0;i<matrix.length;i++)
        {
            for(int j=0;j<attr_array.length;j++)
            {
              if(i<=j)
                   matrix[i].add(j,Double.POSITIVE_INFINITY);

              else matrix[i].add(j,Math.sqrt(Math.pow((attr_array[i]-attr_array[j]),2)));
            }
        }


        return matrix;
    }



   /* public static void newMatrix(int x,int y)
    {

        DecimalFormat df = new DecimalFormat("#.##");

        for(int i=0;i<adjacency_matrix.length;i++)
        {
            for(int j=0;j<adjacency_matrix[i].size();j++)
            {
                if(i==j)
                    adjacency_matrix[i].set(j,Double.POSITIVE_INFINITY);

                if(i==x || y==j)
                {
                    adjacency_matrix[i].set(j,Double.POSITIVE_INFINITY);
                }

            }

        }


        System.out.println("The Modifiedmatrix is: ");

        for(int i=0;i<adjacency_matrix.length;i++) {
            for (int j = 0; j < adjacency_matrix[0].size(); j++)
            {
                if((double) adjacency_matrix[i].get(j)== Double.POSITIVE_INFINITY)
                     System.out.print("0.00"+"\t");
                else System.out.format((df.format(adjacency_matrix[i].get(j))+"\t"));
            }

            System.out.println();
        }


        System.out.println();
        System.out.println();
        System.out.println();

    } */

    public static void newestMatrix(int x,int y)
    {

        iteration++;

        DecimalFormat df = new DecimalFormat("#.##");


        if(x==adjacency_matrix.length)
        {
            for(int j=0;j<adjacency_matrix[x].size();j++)
                         adjacency_matrix[x].remove(j);
            return;
        }

        int i;
        for(i=x;i<(adjacency_matrix.length-iteration-1);i++)
        {
            for(int j=0;j<adjacency_matrix[i].size();j++)
                adjacency_matrix[i].set(j,adjacency_matrix[i+1].get(j));
        }

        adjacency_matrix[adjacency_matrix.length-1].clear();


  /*      for(int j=0;j<adjacency_matrix.length;j++)
            adjacency_matrix[i].remove(j); */

       /* for(i=0;i<adjacency_matrix.length;i++)
            adjacency_matrix[i].trimToSize(); */


        for(int j=0;j<(adjacency_matrix.length);j++)
        {
            for(int k=y;k<adjacency_matrix[j].size()-1;k++)
            {
                adjacency_matrix[j].set(k,adjacency_matrix[j].get(k+1));
            }

        }


        System.out.println("The Modifiedmatrix is: ");

        for(int l =0;l<(adjacency_matrix.length-iteration-1);l++) {
            for (int j = 0; j < adjacency_matrix[0].size(); j++)
            {
                if((double) adjacency_matrix[l].get(j)== Double.POSITIVE_INFINITY)
                    System.out.print("0.00"+"\t");
                else System.out.format((df.format(adjacency_matrix[l].get(j))+"\t"));
            }

            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.println();

    }

}
