// Austin Martinez
// CS 3010
// Assignment 3

import java.io.*;
import java.util.*;

public class polRoot


{
   
    public static void main(String[] args) throws IOException
    {
        String filename = "Placeholder"; // This will hold the user-provided filename
        int maxIterations = 10000;       // In case the user specifies Max Iterations
                                         // Default is 10,000
        double initialPoint = 0;    // Initial point inputted by user
        double extraPoint = 0;      // Extra point for Bisection and Secant
        boolean methodSelect;  // To determine if a method has been selected yet

        boolean newtDefault = false;
        boolean newtMaxIt = false;

        boolean secDefault = false;
        boolean secMaxIt = false;

        boolean hybridDefault = false;
        boolean hybridMaxIt = false;

        boolean bisecDefault = false;
        boolean bisecMaxIt = false;

        methodSelect = false;

        double eps = Math.pow(2, -23);



        /////////////////////////////////////////////////////////////////////////



        // Newton's Method Parsing

        // Ex: polRoot -newt -maxIter 1000 5.5 fun1.pol

        if ( (args[0].equals("-newt")) || (args[0].equals("--newt")) )
        {
            System.out.println("\nNewton's Method selected!\n");

            if (args.length == 5) //This means that Max Iterations was specified
            {
                maxIterations = Integer.parseInt(args[2]);

                initialPoint = Double.parseDouble(args[3]);

                filename = args[4];

                methodSelect = true;

                newtMaxIt = true;
            }
            else if (args.length == 3) //Max Iterations NOT specified
            {
                initialPoint = Double.parseDouble(args[1]);

                filename = args[2];

                methodSelect = true;

                newtDefault = true;
            }

        }



        // Secant Method Parsing (Initial Point + Extra Point)

        // Ex: polRoot -sec -maxIter 1000 0 1 fun.pol


        if ( (args[0].equals("-sec")) || (args[0].equals("--sec")) )
        {
            System.out.println("\nSecant Method selected!\n");

            if (args.length == 6) //This means that Max Iterations was specified
            {
                maxIterations = Integer.parseInt(args[2]);

                initialPoint = Double.parseDouble(args[3]);

                extraPoint = Double.parseDouble(args[4]);

                filename = args[5];

                methodSelect = true;

                secMaxIt = true;
            }
            else if (args.length == 4) // Max Iterations NOT specified
            {
                initialPoint = Double.parseDouble(args[1]);

                extraPoint = Double.parseDouble(args[2]);

                filename = args[3];

                methodSelect = true;

                secDefault = true;
            }

        }



        // Hybrid Method Parsing

        // Ex: polRoot -hybrid -maxIter 10000 0 1 fun1.pol

        if ( (args[0].equals("-hybrid")) || (args[0].equals("--hybrid")) )
        {
            System.out.println("\nHybrid Method selected!\n");

            if ( args.length == 6 )     // Max iterations was specified
            {
                maxIterations = Integer.parseInt(args[2]);

                initialPoint = Double.parseDouble(args[3]);

                extraPoint = Double.parseDouble(args[4]);

                filename = args[5];

                methodSelect = true;

                hybridMaxIt = true;
            }
            else if ( args.length == 4) // Max iterations NOT specified
            {
                initialPoint = Double.parseDouble(args[1]);

                extraPoint = Double.parseDouble(args[2]);

                filename = args[3];

                methodSelect = true;

                hybridDefault = true;


            }

        }




        // Bisection Method Parsing (Initial Point + Extra Point) (Default)
        
        // Ex: polRoot -maxIter 1000 0 1 fun1.pol

        if ( (args.length == 5) && (methodSelect == false) ) // Max iterations WAS specified
        {
            System.out.println("\nBisection Method selected!\n");

            maxIterations = Integer.parseInt(args[1]);

            initialPoint = Double.parseDouble(args[2]);
            extraPoint = Double.parseDouble(args[3]);
            
            filename = args[4];

            methodSelect = true;

            bisecMaxIt = true;


        }
        else if ( (args.length == 3) && (methodSelect == false) )   // Max iterations NOT specified
        {
            System.out.println("\nBisection Method selected!\n");

            initialPoint = Double.parseDouble(args[0]);
            extraPoint = Double.parseDouble(args[1]);

            filename = args[2];

            methodSelect = true;

            bisecDefault = true;
        }

       //Set a scanner to file
       Scanner scanSys = new Scanner(new File(filename));


        
       //To get the degree of the polynomial
       int polyDegree = scanSys.nextInt();




       List<Double> polyCoeff = new ArrayList<>();

       // Scans doubles from file into our polynomial arraylist.
       scanSys.nextLine();

       while ( scanSys.hasNextDouble() )
       {
           polyCoeff.add(scanSys.nextDouble());
       }
       scanSys.close();



       //It will be easier to work with an array over an arraylist, so I copy the values to an array
       double[] polyArray = new double[polyCoeff.size()];

       
       for(int i = 0; i < polyCoeff.size(); i++)
       {
           polyArray[i] = polyCoeff.get(i);
       }



       //Print the polynomial array to the screen to verify that it matches the polynomial in the provided file
       System.out.println("The provided polynomial is...\n");

       for(int j = 0; j < polyArray.length; j++)
       {
           System.out.print(polyArray[j] +" ");
       }

        /////////////////////////////////////////////////////////////////////////


        

        
        // Here we call the appropriate function
        if (bisecDefault == true)
        {
            boolean hybridVer = false;
            Bisection(polyArray, polyDegree, initialPoint, extraPoint, maxIterations, eps, filename, hybridVer);
            
        }

        if (bisecMaxIt == true)
        {
            boolean hybridVer = false;
            Bisection(polyArray, polyDegree, initialPoint, extraPoint, maxIterations, eps, filename, hybridVer);
        }


        if (newtDefault == true)
        {
            boolean hybridVer = false;
            Newton(polyArray, polyDegree, initialPoint, maxIterations, eps, filename, hybridVer);
        }

        if (newtMaxIt == true)
        {
            boolean hybridVer = false;
            Newton(polyArray, polyDegree, initialPoint, maxIterations, eps, filename, hybridVer);
        }


        if (secDefault == true)
        {
            Secant(polyArray, polyDegree, initialPoint, extraPoint, maxIterations, eps, filename);
        }

        if (secMaxIt == true)
        {
            Secant(polyArray, polyDegree, initialPoint, extraPoint, maxIterations, eps, filename);
        }


        if (hybridDefault == true)
        {
            boolean hybridVer = true;
            Hybrid(polyArray, polyDegree, initialPoint, extraPoint, maxIterations, eps, filename, hybridVer);
        }

        if (hybridMaxIt == true)
        {
            boolean hybridVer = true;
            Hybrid(polyArray, polyDegree, initialPoint, extraPoint, maxIterations, eps, filename, hybridVer);
        }

    }



///////////////////////////////////////////////////////////////////////



public static double Bisection(double[] polynom, int maxDegree, double a, double b, int maxIteration, double eps, String filename, boolean hybridVer) throws IOException
{
    double fa = polyFunc(a, polynom, maxDegree);
    double fb = polyFunc(b, polynom, maxDegree);

    if (fa * fb >= 0.0)
    {
        System.out.println("\n\nInadequate values for a and b.\n");
        if (hybridVer == true)
        {
            System.out.println("\nIf max iterations is > 5, moving on to Newton method...\n");
            return a;
        }
        else if (hybridVer == false)
        {
            SolutionToFileFail(0, 0, filename);
            return -1.0;
        }
    }

    double error = b - a;

    double c = 0.0;
    for (int i = 0; i < maxIteration; i++)
    {
        error = error/2.0;
        c = a + error;
        double fc = polyFunc(c, polynom, maxDegree);

        if ( (Math.abs(error) < eps) || (fc == 0) )
        {
            System.out.println("\n\nAlgorithm has converged after #" + i + " iterations!\n");
            SolutionToFileSuccess(c, i, filename);
            System.out.println(c + " " + i + " success");
            System.out.println("\nYour results have also been printed to a file of the same name with a .sol extension.\n");
            return c;
        }

        if (fa * fc < 0.0)
        {
            b = c;
            fb = fc;
        }
        else
        {
            a = c;
            fa = fc;
        }

    }
    if(hybridVer == false)
    {
        System.out.println("\n\nMax iterations reached without convergence...\n");
        SolutionToFileFail(c, maxIteration, filename);
        System.out.println(c + " " + maxIteration + " fail");
        System.out.println("\nYour results have also been printed to a file of the same name with a .sol extension.\n");
    }
    return c;
}



public static double Newton(double[] polynom, int maxDegree, double x, int maxIteration, double eps, String filename, boolean hybridVer) throws IOException
{
    double fx = polyFunc(x, polynom, maxDegree);

    for(int it = 0; it < maxIteration; it++)
    {
        double fd = polyDeri(x, polynom, maxDegree);

        if(Math.abs(fd) < 0.00001)
        {
            System.out.println("\n\nSmall slope!\n");
            if(hybridVer == false) //Standard Version
            {
                SolutionToFileFail(x, it, filename);
                System.out.println(x + " " + it + " fail");
            }
            else //Hybrid Version
            {
                SolutionToFileFail(x, it+5, filename);
                System.out.println(x + " " + it + " fail");
            }
            System.out.println("\nYour results have also been printed to a file of the same name with a .sol extension.");
            return x;
        }

        double d = (fx/fd);
        x = x - d;
        fx = polyFunc(x, polynom, maxDegree);

        if(Math.abs(d) < eps)
        {
            if(hybridVer == false) //Standard Version
            {
                System.out.println("\n\nAlgorithm has converged after #" + it + " iterations!\n");
                SolutionToFileSuccess(x, it, filename);
                System.out.println(x + " " + it + " success");
                System.out.println("\nYour results have also been printed to a file of the same name with a .sol extension.");
            }
            else //Hybrid Version
            {
                it += 5;
                System.out.println("\nAlgorithm has converged after #" + it + " iterations!");
                SolutionToFileSuccess(x, it, filename);
                System.out.println(x + " " + it + " success");
                System.out.println("\nYour results have also been printed to a file of the same name with a .sol extension.");
            }
            return x;
        }

    }

    if( hybridVer == false)
    {
        System.out.println("\nMax iterations reached without convergence...");
        SolutionToFileFail(x, maxIteration, filename);
        System.out.println(x + " " + maxIteration + " fail");
        System.out.println("\nYour results have also been printed to a file of the same name with a .sol extension.");
    }
    else //Hybrid Version
    {
        maxIteration += 5;
        System.out.println("\nMax iterations reached without convergence...");
        SolutionToFileFail(x, maxIteration, filename);
        System.out.println(x + " " + maxIteration + " fail");
        System.out.println("\nYour results have also been printed to a file of the same name with a .sol extension.");
    }
    return x;

}


public static double Secant(double[] polynom, int maxDegree, double a, double b, int maxIteration, double eps, String filename) throws IOException
{
    double fa = polyFunc(a, polynom, maxDegree);
    double fb = polyFunc(b, polynom, maxDegree);

    if( Math.abs(fa) > Math.abs(fb) )
    {
        //Swap (a, b)
        double temp = a;
        a = b;
        b = temp; //Setting b to temp, which is a

        //Swap (fa, fb)
        double newtemp = fa;
        fa = fb;
        fb = newtemp; //Setting fb to newtemp, which is fa
    }

    for(int it = 0; it < maxIteration; it++)
    {
        if( Math.abs(fa) > Math.abs(fb) )
        {
            //Swap (a, b)
            double temp = a;
            a = b;
            b = temp; //Setting b to temp, which is a

            //Swap (fa, fb)
            double newtemp = fa;
            fa = fb;
            fb = newtemp; //Setting fb to newtemp, which is fa
        }

        double d = ( (b-a)/(fb-fa) );
        b = a;
        fb = fa;
        d = (d*fa);

        if(Math.abs(d) < eps)
        {
            System.out.println("\n\nAlgorithm has converged after #" + it + " iterations!\n");
            SolutionToFileSuccess(a, it, filename);
            System.out.println(a + " " + it + " success");
            System.out.println("\nYour results have also been printed to a file of the same name with a .sol extension.");
            return a;
        }

        a = a - d;
        fa = polyFunc(a, polynom, maxDegree);
    }

    System.out.println("\n\nMaximum number of iterations reached!\n");
    SolutionToFileFail(a, maxIteration, filename);
    System.out.println(a + " " + maxIteration + " fail");
    System.out.println("\nYour results have also been printed to a file of the same name with a .sol extension.");
    return a;
}


public static void Hybrid(double[] polynom, int maxDegree, double a, double b, int maxIteration, double eps, String filename, boolean hybridVer) throws IOException
{
    System.out.println("\n\nFor the Hybrid method, the first five iterations will be done with the Bisection method. "
    + "The remaining iterations will be done with Newton's method.");

    if ( (maxIteration - 5) <= 0)
    {
        hybridVer = false;
        Bisection(polynom, maxDegree, a, b, maxIteration, eps, filename, hybridVer);
    }
    else
    {

        int BisecIterations = 5;

        hybridVer = true;

        double halfwayVal = Bisection(polynom, maxDegree, a, b, BisecIterations, eps, filename, hybridVer);

        int RemainingIterations = maxIteration - 5;

        Newton(polynom, maxDegree, halfwayVal, RemainingIterations, eps, filename, hybridVer);
    }

}



///////////////////////////////////////////////////////////////////////

// This function calculates f(x) for an input x

    public static double polyFunc(Double input, double[] polynom, int n)
    {
        float fx = 0;

        for(int i = 0; i < polynom.length && n >= 0; i++)
        {
            fx += polynom[i] * Math.pow(input, n);
            n--;
        }

        return fx;
    }


    public static double polyDeri(Double input, double[] polynom, int n)
    {
        double fx = 0;
        int NewN = n;

        double[] deriFunc = new double[polynom.length];

        for(int i = 0; i < polynom.length; i++)
        {
            deriFunc[i] = NewN * polynom[i];
            NewN--;
        }

        fx = polyFunc(input, deriFunc, n-1);
        return fx;
    }
/////////////////////////////////////////////////////////////




// Prints answer to file

    public static void SolutionToFileSuccess(double solution, int iterations, String filename1) throws IOException
    {
        //We will first need to remove the ".lin" extension from the filename
        String filename2 = ( filename1.substring(0, filename1.length() - 3) ) + "sol";

        BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter(filename2));

        
            writer.write(solution + " " + iterations + " success");
            writer.write(" ");
        

        writer.close();;
    }

    public static void SolutionToFileFail(double solution, int iterations, String filename1) throws IOException
    {
        //We will first need to remove the ".lin" extension from the filename
        String filename2 = ( filename1.substring(0, filename1.length() - 3) ) + "sol";

        BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter(filename2));

        
            writer.write(solution + " " + iterations + " fail");
            writer.write(" ");
        

        writer.close();;
    }
    

}

