package matrix;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class MyMatrix {
    public List<List<Double>> field;
    public MyMatrix(){
        this.field = new ArrayList<>();
    }
    public MyMatrix(List<List<Double>> nField){
        this.field = nField;
    }
    public MyMatrix Add(MyMatrix a, MyMatrix b) {
        int n = this.field.size();
        List<List<Double>> nField = new ArrayList<>();
        for(int i = 0; i < n; i++){
            List<Double> row = new ArrayList<>();
            for(int j = 0; j < n; j++){
                row.add(a.field.get(i).get(j)+b.field.get(i).get(j));
            }
            nField.add(row);
        }
        MyMatrix c = new MyMatrix(nField);
        return c;
    }

    public MyMatrix Mult(MyMatrix a, MyMatrix b){
        int n = this.field.size();
        List<List<Double>> nField = new ArrayList<>();
        for(int i = 0; i < n; i++){
            List<Double> row = new ArrayList<>();
            for(int j = 0; j < n; j++){
                Double sum = 0.;
                for(int k = 0; k < n; k++){
                    sum += a.field.get(i).get(k)*a.field.get(k).get(j);
                }
                row.add(sum);
            }
            nField.add(row);
        }
        MyMatrix c = new MyMatrix(nField);
        return c;
    }

    public MyMatrix T(){
        List<List<Double>> tField = new ArrayList<>();
        int n = this.field.size();
        for(int i = 0; i < n; i++){
            List<Double> row = new ArrayList<>();
            for(int j = 0; j < n; j++){
                row.add(this.field.get(j).get(i));
            }
            tField.add(row);
        }
        MyMatrix tMatrix = new MyMatrix(tField);
        return tMatrix;
    }
}

public class Main {

    static List<List<Double>> GetMatrix(){
        File myFile =  new File("input.txt");
        return null;
    }
    static void MatrixIterations(MyMatrix matrix){

    }
    public static void main(String[] args) {
        MyMatrix taskMatrix = new MyMatrix(GetMatrix());
        MatrixIterations(taskMatrix);
    }
}
