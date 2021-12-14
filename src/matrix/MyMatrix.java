package matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyMatrix {
    public List<List<Double>> field;

    public MyMatrix() {
        this.field = new ArrayList<>();
    }

    public MyMatrix(int n) {//generates square matrix
        this.field = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Double> line = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                line.add(0.);
            }
            this.field.add(line);
        }
    }

    public MyMatrix(List<List<Double>> nField) {
        this.field = nField;
    }

    public MyMatrix Add(MyMatrix a, MyMatrix b) {
        int n = this.field.size();
        List<List<Double>> nField = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(a.field.get(i).get(j) + b.field.get(i).get(j));
            }
            nField.add(row);
        }
        MyMatrix c = new MyMatrix(nField);
        return c;
    }

    public static MyMatrix Mult(MyMatrix a, MyMatrix b) {
        int n = a.field.size();
        int m = a.field.get(0).size();
        int n2 = b.field.size();
        int m2 = b.field.get(0).size();
        MyMatrix c ;
        if(m == n2){
        List<List<Double>> nField = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < m2; j++) {
                Double sum = 0.;
                for (int k = 0; k < m; k++) {
                    sum += a.field.get(i).get(k) * b.field.get(k).get(j);
                }
                row.add(sum);
            }
            nField.add(row);
        }
        c = new MyMatrix(nField);
        } else {
            c = new MyMatrix();
        }
        return c;
    }

    public MyMatrix T() {
        List<List<Double>> tField = new ArrayList<>();
        int n = this.field.size();
        for (int i = 0; i < n; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(this.field.get(j).get(i));
            }
            tField.add(row);
        }
        MyMatrix tMatrix = new MyMatrix(tField);
        return tMatrix;
    }

    public static Double det(MyMatrix matrix){
        if(matrix.field.size() > 1){
            Double sum = 0.;
            for(int i = 0; i < matrix.field.size(); i++){
                MyMatrix smallMatrix = new MyMatrix();
                for (int j = 1; j < matrix.field.size(); j++) {
                    List<Double> line = new ArrayList<>();
                    for (int k = 0; k < matrix.field.size(); k++) {
                        if(k != i) {
                            line.add(matrix.field.get(j).get(k));
                        }
                    }
                    smallMatrix.field.add(line);
                }
                double sign = (i % 2 == 0) ? 1 : -1;
                sum += matrix.field.get(0).get(i) * sign * det(smallMatrix);
            }
            return sum;
        } else if(matrix.field.size() == 1)  {
            return matrix.field.get(0).get(0);
        } else {
            return 1.;
        }
    }

    public MyMatrix MinusOnePower(){
        MyMatrix matrix = new MyMatrix();
        Double detMatrix = MyMatrix.det(this);
        for (int i = 0; i < this.field.size(); i++) {
            List<Double> line = new ArrayList<>();
            for (int j = 0; j < this.field.size(); j++) {
                MyMatrix smallMatrix = new MyMatrix();
                for (int k = 0; k < this.field.size(); k++) {
                    if(k != i) {
                        List<Double> sLine = new ArrayList<>();
                        for (int l = 0; l < this.field.size(); l++) {
                            if (l != j){
                                sLine.add(this.field.get(k).get(l));
                            }
                        }
                        smallMatrix.field.add(sLine);
                    }
                }
                int sign = (i+j) % 2 == 0 ? 1 : -1;
                line.add(sign * MyMatrix.det(smallMatrix)/detMatrix);
            }
            matrix.field.add(line);
        }
        return matrix.T();
    }

    public void SwapLines(int n1, int n2) {
        int n = this.field.size();
        for (int i = 0; i < n; i++) {
            Double tmp = this.field.get(n1).get(i);
            this.field.get(n1).set(i, this.field.get(n2).get(i));
            this.field.get(n2).set(i, tmp);
        }
    }

    public String ToLine(){
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < this.field.size(); i++) {
            for (int j = 0; j < this.field.get(0).size(); j++) {
                line.append(String.format("%11.7f ", this.field.get(i).get(j)));
            }
        }
        return line.toString();
    }

    public List<String> ToLines(){
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < this.field.size(); i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < this.field.get(0).size(); j++) {
                line.append(String.format("%11.7f ", this.field.get(i).get(j)));
            }
            lines.add(line.toString());
        }
        return lines;
    }

    public List<String> ToLinesPresicion(){
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < this.field.size(); i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < this.field.get(0).size(); j++) {
                line.append(String.format("%22.13e ", this.field.get(i).get(j)));
            }
            lines.add(line.toString());
        }
        return lines;
    }

    public static MyMatrix Copy(MyMatrix matrix){
        List<List<Double>> newField = new ArrayList<>();
        for (int i = 0; i < matrix.field.size(); i++) {
            List<Double> line = new ArrayList<>();
            for (int j = 0; j < matrix.field.get(0).size(); j++) {
                line.add(matrix.field.get(i).get(j));
            }
            newField.add(line);
        }
        MyMatrix newMatrix = new MyMatrix(newField);
        return newMatrix;
    }
}
