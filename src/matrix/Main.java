package matrix;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static List<List<Double>> GetMatrix(){
        try {
            File myFile = new File("input.txt");
            Scanner myScanner = new Scanner(myFile);
            List<List<Double>> field = new ArrayList<>();
            while(myScanner.hasNextLine()){
                String sLine = myScanner.nextLine();
                List<Double> line = new ArrayList<>();
                String[] wordLine = sLine.split(" ");
                for (String s :
                     wordLine) {
                    line.add(Double.parseDouble(s));
                }
                field.add(line);
            }
            return field;
        } catch (Exception e) {
            System.out.println("Error in input from file!");
            return null;
        }
    }

    static void MatrixIterations(MyMatrix taskMatrix){
        String fileName = "output.txt";
        List<List<Double>> rootXArray = new ArrayList<>();

        for (int i = 0; i < taskMatrix.field.size(); i++) {
            List<Double>  rootLine = new ArrayList<>();
            rootLine.add(i+1.);
            rootXArray.add(rootLine);
        }

        MyMatrix rootX = new MyMatrix(rootXArray);
        PrintLine(rootX.ToLine(), fileName);
        PrintMatrix(taskMatrix, fileName);
        MyMatrix b = MyMatrix.Mult(taskMatrix, rootX);
        PrintLine(b.ToLine(), fileName);


        MyMatrix U = MyMatrix.Copy(taskMatrix);
        MyMatrix L = new MyMatrix(taskMatrix.field.size());

        List<Integer> lineChange = new ArrayList<>();//P
        for (int i = 0; i < U.field.size(); i++) {
            lineChange.add(i);
        }

        for(int i = 0; i < U.field.size(); i++){
            Double maxElem = U.field.get(i).get(i);
            int maxNum = i;
            for(int j = i + 1; j < U.field.size(); j++){
                if(Math.abs(maxElem) < Math.abs(U.field.get(j).get(i))){
                    maxElem = U.field.get(j).get(i);
                    maxNum = j;
                }
            }
            U.SwapLines(i, maxNum);
            int tmp = lineChange.get(i);
            lineChange.set(i, lineChange.get(maxNum));
            lineChange.set(maxNum, tmp);

            for (int j = 0; j < U.field.size(); j++) {
                U.field.get(i).set(j, U.field.get(i).get(j)/maxElem);
            }
            for (int j = i + 1; j < U.field.size(); j++) {
                Double tmpFirst = U.field.get(j).get(i);
                for (int k = i; k < U.field.size(); k++) {
                    U.field.get(j).set(k, U.field.get(j).get(k) - U.field.get(i).get(k)*tmpFirst);
                }
            }
            PrintLine(lineChange.toString(), fileName);
            PrintLine("U:", fileName);
            PrintMatrix(U, fileName);

            if(i > 0) {
                //U^-1
                MyMatrix tmpU = new MyMatrix();
                for (int j = 0; j < i; j++) {
                    List<Double> line = new ArrayList<>();
                    for (int k = 0; k < i; k++) {
                        line.add(U.field.get(j).get(k));
                    }
                    tmpU.field.add(line);
                }
                MyMatrix smallMinusU = tmpU.MinusOnePower();

                //d^T
                MyMatrix d = new MyMatrix();
                d.field.add(new ArrayList<>());
                for (int j = 0; j < i; j++) {
                    d.field.get(0).add(taskMatrix.field.get(lineChange.get(i)).get(j));
                }

                //l^T(k-1)
                MyMatrix lMinus = MyMatrix.Mult(d, smallMinusU);

                //u(k-1)
                MyMatrix u = new MyMatrix();
                for (int j = 0; j < i; j++) {
                    List<Double> line = new ArrayList<>();
                    line.add(U.field.get(j).get(i));
                    u.field.add(line);
                }
                //tmp lxu
                Double lxu = (MyMatrix.Mult(lMinus, u)).field.get(0).get(0);
                //l (k,k)
                Double lkk = taskMatrix.field.get(lineChange.get(i)).get(i) - lxu;
                for (int j = 0; j < i; j++) {
                    L.field.get(i).set(j, lMinus.field.get(0).get(j));
                }
                L.field.get(i).set(i, lkk);
            } else {
                L.field.get(0).set(0, taskMatrix.field.get(lineChange.get(0)).get(0));
            }
            PrintLine("L:", fileName);
            PrintMatrix(L, fileName);
        }

        //LU
        MyMatrix LU = MyMatrix.Mult(L, U);
        MyMatrix LUminusPA = new MyMatrix(taskMatrix.field.size());//LU-PA
        for (int i = 0; i < taskMatrix.field.size(); i++) {
            for (int j = 0; j < taskMatrix.field.size(); j++) {
                LUminusPA.field.get(i).set(j, LU.field.get(i).get(j)-taskMatrix.field.get(lineChange.get(i)).get(j));
            }
        }
        PrintLine("LU - PA", fileName);
        PrintMatrixPrecision(LUminusPA, fileName);
        PrintLine("Determinant A", fileName);
        PrintLine(String.format("%.5f",MyMatrix.det(taskMatrix)), fileName);

        //get x from LU
        MyMatrix vb = new MyMatrix();
        for (int i = 0; i < b.field.size(); i++) {
            List<Double> line = new ArrayList<>();
            line.add(b.field.get(lineChange.get(i)).get(0));
            vb.field.add(line);
        }

        MyMatrix y = MyMatrix.Mult(L.MinusOnePower(), vb);
        MyMatrix x = new MyMatrix();
        Double[] tmpX = new Double[U.field.size()];
        for (int i = U.field.size() -1; i >= 0; i--) {
            tmpX[i] = y.field.get(i).get(0);
            for (int j = U.field.size()-1; j > i; j--) {
                tmpX[i] -= tmpX[j]*U.field.get(i).get(j);
            }

        }
        x.field.add(new ArrayList<>());
        for (int i = 0; i < U.field.size(); i++) {
            x.field.get(0).add(tmpX[i]);
        }

        PrintLine("x", fileName);
        PrintLine(x.ToLine(), fileName);

        MyMatrix newA = new MyMatrix();
        for (int i = 0; i < LU.field.size(); i++) {
            List<Double> line = new ArrayList<>();
            for (int j = 0; j < LU.field.size(); j++) {
                line.add(LU.field.get(lineChange.indexOf(i)).get(j));
            }
            newA.field.add(line);
        }
        PrintLine("A", fileName);
        PrintMatrix(newA, fileName);
        PrintLine("A^-1", fileName);
        MyMatrix minusA = newA.MinusOnePower();
        PrintMatrix(minusA, fileName);
        PrintLine("A*A^-1", fileName);
        PrintMatrixPrecision(MyMatrix.Mult(newA, minusA), fileName);

    }

    public static void PrintMatrixPrecision(MyMatrix matrix, String fileName){
        try{
            FileWriter myWriter = new FileWriter(fileName, true);
            List<String> matrixLines = matrix.ToLinesPresicion();
            for (String s:
                    matrixLines) {
                myWriter.write(s);
                myWriter.write("\n");
            }
            myWriter.write("\n");
            myWriter.close();
        } catch (Exception e){
            System.out.println("Error in printing matrix to file!");
        }
    }

    public static void PrintMatrix(MyMatrix matrix, String fileName){
        try{
            FileWriter myWriter = new FileWriter(fileName, true);
            List<String> matrixLines = matrix.ToLines();
            for (String s:
                 matrixLines) {
                myWriter.write(s);
                myWriter.write("\n");
            }
            myWriter.write("\n");
            myWriter.close();
        } catch (Exception e){
            System.out.println("Error in printing matrix to file!");
        }
    }

    public static void PrintLine(String line, String fileName){
        try{
            FileWriter myWriter = new FileWriter(fileName, true);
            myWriter.write(line);
            myWriter.write("\n\n");
            myWriter.close();
        } catch (Exception e){
            System.out.println("Error in printing line to file!");
        }
    }

    public static void main(String[] args) {
        String fileName = "output.txt";
        try{
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.close();
        } catch (Exception e){
            System.out.println("Error in opening output file!");
        }
        MyMatrix taskMatrix = new MyMatrix(GetMatrix());


        MatrixIterations(taskMatrix);
    }
}
